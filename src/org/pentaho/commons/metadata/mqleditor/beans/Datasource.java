package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;

public class Datasource implements IDatasource{

  private String datasourceName;
  private String query;
  private List<IConnection> connections;
  private IConnection selectedConnection;
  private DatasourceType type;
  private String previewLimit;
  private ResultSetObject object;
  public Datasource(){
    
  }

  public Datasource(IDatasource datasource){
    setDatasourceName(datasource.getDatasourceName());
    setQuery(datasource.getQuery());
    setConnections(datasource.getConnections());
  }

  public void setDatasourceName(String datasourceName) {
    this.datasourceName = datasourceName;
  }

  public String getDatasourceName() {
    return datasourceName;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }

  public void setPreviewLimit(String previewLimit) {
    this.previewLimit = previewLimit;
  }

  public String getPreviewLimit() {
    return previewLimit;
  }
  
  public IConnection getConnection(String name) {
    for(IConnection connection:connections) {
      if(connection.getName().equals(name)) {
        return connection;
      }
    }
    return null;
  }

  public List<IConnection> getConnections() {
    return connections;
  }

  public void setConnection(IConnection connection) {
    connections.add(connection);
  }

  public void setConnections(List<IConnection> connections) {
   connections.clear();
   connections.addAll(connections);
  }

  public DatasourceType getDatasourceType() {
    return type;
  }
  public void setDatasourceType(DatasourceType type) {
    this.type = type; 
  }

  public IConnection getSelectedConnection() {
    return selectedConnection;
  }

  public void setSelectedConnection(IConnection connection) {
    selectedConnection = connection;
  }

  @Override
  public ResultSetObject getResultSetObject() {
    // TODO Auto-generated method stub
    return object;
  }

  @Override
  public void setResultSetObject(ResultSetObject object) {
    this.object = object;
  }

}
