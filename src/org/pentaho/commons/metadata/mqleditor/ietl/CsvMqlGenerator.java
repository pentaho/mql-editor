package org.pentaho.commons.metadata.mqleditor.ietl;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.connection.IPentahoResultSet;
import org.pentaho.commons.connection.memory.MemoryMetaData;
import org.pentaho.commons.connection.memory.MemoryResultSet;
import org.pentaho.commons.metadata.mqleditor.MqlOrder.Type;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleValueException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.RowListener;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaDataCombi;
import org.pentaho.di.trans.steps.groupby.GroupByMeta;
import org.pentaho.di.trans.steps.selectvalues.SelectValuesMeta;
import org.pentaho.di.trans.steps.sort.SortRowsMeta;

public class CsvMqlGenerator {
  
  public List<Column> getAllColumns(Query query) {
    return query.getColumns();
  }
  
  public IPentahoResultSet generate(CsvPhysicalModel model, Query query) throws Exception {
    // step one, execute a transformation into a result set

    // group by?
    int groupBys = 0;
    List<Column> allColumns = getAllColumns(query);
    for (Column col : allColumns) {
      if (col.agg > 0) {
        groupBys++;
      }
    }

    String fileAddress = "file://home/gorman/platform_workspace/pentaho-mql-editor-dataaccess-sprint-1/resources/inlinecsv.ktr";
    if (groupBys > 0) {
      fileAddress = "file://home/gorman/platform_workspace/pentaho-mql-editor-dataaccess-sprint-1/resources/inlinecsv_groupby.ktr";  
    }
    TransMeta transMeta = new TransMeta(fileAddress, null, true);
    transMeta.setFilename(fileAddress);
    
    //
    // SELECT
    //
    
    StepMeta selections = getStepMeta(transMeta, "Select values");

    SelectValuesMeta selectVals = (SelectValuesMeta)selections.getStepMetaInterface();
    selectVals.allocate(query.getColumns().size(), 0, 0);
    for (int i = 0; i < query.getColumns().size(); i++) {
      selectVals.getSelectName()[i] = query.getColumns().get(i).getName();
    }

    // 
    // SORT
    //
    
    // TODO: group by impacts sorting
    
    StepMeta sort = getStepMeta(transMeta, "Sort rows");
    
    SortRowsMeta sortRows = (SortRowsMeta)sort.getStepMetaInterface();
    
    sortRows.allocate(query.getColumns().size());
    
    List<Column> unordered = new ArrayList<Column>();
    for (Column col : query.getColumns()) {
      boolean found = false;
      for (Order order : query.getOrders()) {
        if (col == order.getColumn()) {
          found = true;
        }
      }
      if (!found) {
        unordered.add(col);
      }
      
    }
    
    int c = 0;
    for (Order order : query.getOrders()) {
      sortRows.getFieldName()[c] = order.getColumn().getName();
      sortRows.getAscending()[c] = (order.getOrderType() == Type.ASC);
      sortRows.getCaseSensitive()[c] = false;
      c++;
    }
    for (Column col : unordered) {
      sortRows.getFieldName()[c] = col.getName();
      sortRows.getAscending()[c] = true;
      sortRows.getCaseSensitive()[c] = false;
      c++;
    }
    
    //
    // GROUP BY
    //
    
    if (groupBys > 0) {
      StepMeta group = getStepMeta(transMeta, "Group by");
      GroupByMeta groupStep = (GroupByMeta)group.getStepMetaInterface();
      groupStep.allocate(allColumns.size() - groupBys, groupBys);
      
      c = 0;
      for (Column col : allColumns) {
        if (col.agg == 0) {
          groupStep.getGroupField()[c] = col.getName();
          c++;
        }
      }
      c = 0;
      for (Column col : allColumns) {
        if (col.agg != 0) {
          groupStep.getAggregateField()[c] = col.getName();    
          groupStep.getSubjectField()[c] = col.getName();    
          groupStep.getAggregateType()[c] = col.agg;
          groupStep.getValueField()[c] = null;
          c++;
        }
      }
    }
    
    InlineEtlRowListener listener = new InlineEtlRowListener();
    Trans trans = new Trans(transMeta);
    trans.prepareExecution(transMeta.getArguments());
    listener.registerAsStepListener(trans);
    
    trans.startThreads();
    trans.waitUntilFinished();
    trans.endProcessing("end"); //$NON-NLS-1$
    
    return listener.results;
  }
  
  public StepMeta getStepMeta(TransMeta meta, String name) {
    for (StepMeta step : meta.getSteps()) {
      if (name.equals(step.getName())) {
        return step;
      }
    }
    return null;
  }
  
  public static class InlineEtlRowListener implements RowListener {
    
    private MemoryResultSet results;
    
    private MemoryResultSet errorResults;

    private boolean registerAsStepListener(Trans trans) throws Exception {
      boolean success = false;
//      try{
        if(trans != null){
          List<StepMetaDataCombi> stepList = trans.getSteps();
          // assume the last step
          for (StepMetaDataCombi step : stepList) {
            if (!"Unique rows".equals(step.stepname)) {
              continue;
            }
            System.out.println("STEP NAME: " + step.stepname);
            RowMetaInterface row = trans.getTransMeta().getStepFields(step.stepMeta); // step.stepname?
            // create the metadata that the Pentaho result sets need
            String fieldNames[] = row.getFieldNames();
            String columns[][] = new String[1][fieldNames.length];
            for (int column = 0; column < fieldNames.length; column++) {
              columns[0][column] = fieldNames[column];
            }
            MemoryMetaData metaData = new MemoryMetaData(columns, null);
            results = new MemoryResultSet(metaData);
            errorResults = new MemoryResultSet(metaData);
            // add ourself as a row listener
            step.step.addRowListener(this);
            success = true;
          }
        }
//      } catch (Exception e){
//        throw new KettleComponentException(Messages.getString("Kettle.ERROR_0027_ERROR_INIT_STEP",stepName), e); //$NON-NLS-1$
//      }
      
      return success;
    }
    
    
    public void rowReadEvent(final RowMetaInterface row, final Object[] values) {
    }

    public void rowWrittenEvent(final RowMetaInterface rowMeta, final Object[] row) throws KettleStepException {
      processRow(results, rowMeta, row);
    }

    public void errorRowWrittenEvent(final RowMetaInterface rowMeta, final Object[] row) throws KettleStepException {
      processRow(errorResults, rowMeta, row);
    }
    
    public void processRow(MemoryResultSet memResults, final RowMetaInterface rowMeta, final Object[] row) throws KettleStepException {
      if (memResults == null) {
        return;
      }
      try {
        Object pentahoRow[] = new Object[memResults.getColumnCount()];
        for (int columnNo = 0; columnNo < memResults.getColumnCount(); columnNo++) {
          ValueMetaInterface valueMeta = rowMeta.getValueMeta(columnNo);

          switch (valueMeta.getType()) {
            case ValueMetaInterface.TYPE_BIGNUMBER:
              pentahoRow[columnNo] = rowMeta.getBigNumber(row, columnNo);
              break;
            case ValueMetaInterface.TYPE_BOOLEAN:
              pentahoRow[columnNo] = rowMeta.getBoolean(row, columnNo);
              break;
            case ValueMetaInterface.TYPE_DATE:
              pentahoRow[columnNo] = rowMeta.getDate(row, columnNo);
              break;
            case ValueMetaInterface.TYPE_INTEGER:
              pentahoRow[columnNo] = rowMeta.getInteger(row, columnNo);
              break;
            case ValueMetaInterface.TYPE_NONE:
              pentahoRow[columnNo] = rowMeta.getString(row, columnNo);
              break;
            case ValueMetaInterface.TYPE_NUMBER:
              pentahoRow[columnNo] = rowMeta.getNumber(row, columnNo);
              break;
            case ValueMetaInterface.TYPE_STRING:
              pentahoRow[columnNo] = rowMeta.getString(row, columnNo);
              break;
            default:
              pentahoRow[columnNo] = rowMeta.getString(row, columnNo);
          }
        }
        memResults.addRow(pentahoRow);
      } catch (KettleValueException e) {
        throw new KettleStepException(e);
      }
    }

    
  }
}
