package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.metadata.model.Category;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.IPhysicalColumn;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.ui.xul.XulEventSourceAdapter;


public class DatasourceModel extends XulEventSourceAdapter implements IDatasource{
  private boolean isValid;
  private IConnection selectedConnection;
  private List<IConnection> connections = new ArrayList<IConnection>();
  private List<ModelDataRow> dataRows = new ArrayList<ModelDataRow>();
  private String query;
  private String datasourceName;
  private String previewLimit;
  private EditType editType = EditType.ADD;
  private DatasourceType datasourceType = DatasourceType.SQL;
  private BusinessData object;
  
  public DatasourceModel() {
    previewLimit="10";
  }
  
  public EditType getEditType() {
    return editType;
  }
  
  public void setEditType(EditType editType) {
    this.editType = editType;
  }

  public String getDatasourceName() {
    return datasourceName;
  }
  
  public void setDatasourceName(String datasourceName) {
   String previousVal = this.datasourceName;
   this.datasourceName = datasourceName;
   this.firePropertyChange("datasourcename", previousVal, datasourceName); //$NON-NLS-1$
   validate();
  }
  public IConnection getSelectedConnection() {
    return selectedConnection;
  }
  
  public void setSelectedConnection(IConnection selectedConnection){
    IConnection previousValue = this.selectedConnection;
    this.selectedConnection = selectedConnection;
    this.firePropertyChange("selectedConnection", previousValue, selectedConnection);
    validate();
  }
  
  public List<IConnection> getConnections() {
    return connections;
  }

  public void addConnection(IConnection connection) {
    List<IConnection> previousValue = getPreviousValue();
    connections.add(connection);
    this.firePropertyChange("connections", previousValue, connections); //$NON-NLS-1$
  }
  public void updateConnection(IConnection connection) {
    List<IConnection> previousValue = getPreviousValue();
    IConnection conn = getConnectionByName(connection.getName());
    conn.setDriverClass(connection.getDriverClass());
    conn.setPassword(connection.getPassword());
    conn.setUrl(connection.getUrl());
    conn.setUsername(connection.getUsername());
    this.firePropertyChange("connections", previousValue, connections); //$NON-NLS-1$
  }
  private List<IConnection> getPreviousValue() {
    List<IConnection> previousValue = new ArrayList<IConnection>();
    for(IConnection conn:connections) {
      previousValue.add(conn);
    }
    return previousValue;
  }
  public void deleteConnection(IConnection connection) {
    List<IConnection> previousValue = getPreviousValue();
    connections.remove(connections.indexOf(connection));
    this.firePropertyChange("connections", previousValue, connections); //$NON-NLS-1$
  }
  public void deleteConnection(String name) {
    for(IConnection connection:connections) {
      if(connection.getName().equals(name)) {
        deleteConnection(connection);
        break;
      }
    }
  }
  
  public void setConnections(List<IConnection> connections) {
    List<IConnection> previousValue = getPreviousValue();
    this.connections = connections;
    this.firePropertyChange("connections", previousValue, connections); //$NON-NLS-1$
  }
  
  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    String previousVal = this.query;
    this.query = query;
    this.firePropertyChange("query", previousVal, query); //$NON-NLS-1$
    validate();
  }

  public DatasourceType getDatasourceType() {
    return this.datasourceType;
  }

  public void setDatasourceType(DatasourceType type) {
    DatasourceType previousVal = this.datasourceType;
    this.datasourceType = type;
    this.firePropertyChange("query", previousVal, type); //$NON-NLS-1$
    validate();
  }
  
  public String getPreviewLimit() {
    return previewLimit;
  }

  public void setPreviewLimit(String previewLimit) {
    String previousVal = this.previewLimit;
    this.previewLimit = previewLimit;
    this.firePropertyChange("previewLimit", previousVal, previewLimit); //$NON-NLS-1$
  }
  
  public IConnection getConnectionByName(String name) {
    for(IConnection connection:connections) {
      if(connection.getName().equals(name)) {
        return connection;
      }
    }
    return null;
  }

  public Integer getConnectionIndex(IConnection conn) {
    IConnection connection = getConnectionByName(conn.getName());
    return connections.indexOf(connection);
  }
  
  public boolean isValidated(){
    return isValid;
  }
  
  private void setValidated(boolean valid){
    boolean prevVal = isValid;
    this.isValid = valid;
    this.firePropertyChange("validated", prevVal, isValid);
  }
  
  public void validate() {
    if((getDatasourceType() != null) 
          && (getQuery() != null && getQuery().length() > 0)
            && (getSelectedConnection() != null) ) {
      this.setValidated(true);
    } else {
      this.setValidated(false);
    }
  }

  public BusinessData getBusinessData() {
    return object;
  }

  public void setBusinessData(BusinessData object) {
    this.object = object;
    setModelData(object);
  }
  
    public void setModelData(BusinessData businessData) {
    if(businessData != null) {
      Domain domain = businessData.getDomain();
      List<List<String>> data = businessData.getData();
      List<IPhysicalColumn> physicalColumns = new ArrayList<IPhysicalColumn>();
      List<LogicalModel> logicalModels = domain.getLogicalModels();
      int i=0;
      for (LogicalModel logicalModel : logicalModels) {
        List<Category> categories = logicalModel.getCategories();
        for (Category category : categories) {
          List<LogicalColumn> logicalColumns = category.getLogicalColumns();
          for (LogicalColumn logicalColumn : logicalColumns) {
            addModelDataRow(logicalColumn.getPhysicalColumn(), data.get(i++));
          }
        }
      }
      firePropertyChange("dataRows", null, dataRows);
    } else {
      this.dataRows.removeAll(dataRows);
      List<ModelDataRow> previousValue = this.dataRows;
      firePropertyChange("dataRows", previousValue, null);
    }
  }

  public void addModelDataRow(IPhysicalColumn column, List<String> data) {
    this.dataRows.add(new ModelDataRow(column, data));
  }

  public List<ModelDataRow> getDataRows() {
    return dataRows;
  }


  public void setDataRows(List<ModelDataRow> modelData) {
    this.dataRows = dataRows;
    firePropertyChange("dataRows", null, dataRows);
  }
  
}
