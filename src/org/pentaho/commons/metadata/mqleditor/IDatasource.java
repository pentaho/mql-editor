package org.pentaho.commons.metadata.mqleditor;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;

public interface IDatasource {
  public enum DatasourceType {SQL, OLAP, CSV, XML}; 
  public enum EditType {ADD, EDIT};
  public String getDatasourceName();
  public void setDatasourceName(String name);
  public DatasourceType getDatasourceType();
  public void setDatasourceType(DatasourceType type);
  public List<IConnection> getConnections();
  public void setConnections(List<IConnection> connections);
  public IConnection getSelectedConnection();
  public void setSelectedConnection(IConnection connection);
  public String getQuery();
  public void setQuery(String query);
  public String getPreviewLimit();
  public void setPreviewLimit(String limit);
  public BusinessData getBusinessData();
  public void setBusinessData(BusinessData object);
}
