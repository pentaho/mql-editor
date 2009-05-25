package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.model.Category;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.ui.xul.XulEventSourceAdapter;


public class CsvModelData extends XulEventSourceAdapter{

  private List<CsvModelDataRow> csvDataRows = new ArrayList<CsvModelDataRow>();

  public CsvModelData(Domain domain) {
    List<LogicalModel> logicalModels = domain.getLogicalModels();
    int i=0;
    for (LogicalModel logicalModel : logicalModels) {
      List<Category> categories = logicalModel.getCategories();
      for (Category category : categories) {
        List<LogicalColumn> logicalColumns = category.getLogicalColumns();
        for (LogicalColumn logicalColumn : logicalColumns) {
          addCsvModelDataRow(logicalColumn);
        }
      }
    }
  }

  public void addCsvModelDataRow(LogicalColumn column) {
  /*  this.dataRows.add(new ModelDataRow(column, data));*/
    firePropertyChange("csvDataRows", null, csvDataRows);
  }

  public List<CsvModelDataRow> getModelData() {
    return csvDataRows;
  }


  public void setModelData(List<CsvModelDataRow> csvDataRows) {
    this.csvDataRows = csvDataRows;
    firePropertyChange("csvDataRows", null, csvDataRows);
  }


}
