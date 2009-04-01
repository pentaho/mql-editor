package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.DatasourceServiceDelegate;
import org.pentaho.commons.metadata.mqleditor.utils.ResultSetObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DatasourceDebugGwtServlet extends RemoteServiceServlet implements DatasourceGwtService {

  DatasourceServiceDelegate SERVICE;

  public DatasourceDebugGwtServlet() {
    SERVICE = new DatasourceServiceDelegate();
  }

  public List<IDatasource> getDatasources() {
    return SERVICE.getDatasources();
  }
  public IDatasource getDatasourceByName(String name) {
    return SERVICE.getDatasourceByName(name);
  }
  public Boolean addDatasource(IDatasource datasource) {
    return SERVICE.addDatasource(datasource);
  }

  public Boolean updateDatasource(IDatasource datasource) {
    return SERVICE.updateDatasource(datasource);
  }

  public Boolean deleteDatasource(IDatasource datasource) {
    return SERVICE.deleteDatasource(datasource);
  }
    
  public Boolean deleteDatasource(String name) {
    return SERVICE.deleteDatasource(name);    
  }

  public ResultSetObject doPreview(IConnection connection, String query, String previewLimit) throws DatasourceServiceException{
    return SERVICE.doPreview(connection, query, previewLimit);
  }

  public ResultSetObject doPreview(IDatasource datasource) throws DatasourceServiceException{
    return SERVICE.doPreview(datasource);
  }
}