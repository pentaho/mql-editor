package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.metadata.model.Category;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.ui.xul.XulEventSourceAdapter;


public class ModelData extends XulEventSourceAdapter{

  private List<ModelDataRow> dataRows = new ArrayList<ModelDataRow>();

  
  
  public ModelData(BusinessData businessData) {
    Domain domain = businessData.getDomain();
    List<List<String>> data = businessData.getData();
    List<LogicalModel> logicalModels = domain.getLogicalModels();
    int i=0;
    for (LogicalModel logicalModel : logicalModels) {
      List<Category> categories = logicalModel.getCategories();
      for (Category category : categories) {
        List<LogicalColumn> logicalColumns = category.getLogicalColumns();
        for (LogicalColumn logicalColumn : logicalColumns) {
          addModelDataRow(logicalColumn, data.get(i++));
        }
      }
    }
  }

  public void addModelDataRow(LogicalColumn column, List<String> data) {
  /*  this.dataRows.add(new ModelDataRow(column, data));*/
    firePropertyChange("dataRows", null, dataRows);
  }

  public List<ModelDataRow> getModelData() {
    return dataRows;
  }


  public void setModelData(List<ModelDataRow> dataRows) {
    this.dataRows = dataRows;
    firePropertyChange("dataRows", null, dataRows);
  }


}
