package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.ConnectionServiceDelegate;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.DatasourceServiceDelegate;
import org.pentaho.commons.metadata.mqleditor.utils.ResultSetObject;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DatasourceGwtServlet extends RemoteServiceServlet implements DatasourceGwtService {

  DatasourceServiceDelegate SERVICE;

  public DatasourceGwtServlet() {
    SERVICE = new DatasourceServiceDelegate();
  }
  public Boolean addDatasource(IDatasource datasource) {
    return SERVICE.addDatasource(datasource);
  }

  public Boolean createCategory(String categoryName, IConnection connection, String query, BusinessData businessData)
      throws DatasourceServiceException {
    return SERVICE.createCategory(categoryName, connection, query, businessData);
  }

  public Boolean deleteDatasource(IDatasource datasource) {
    return SERVICE.deleteDatasource(datasource);
  }

  public Boolean deleteDatasource(String name) {
    return SERVICE.deleteDatasource(name);
  }


  public ResultSetObject doPreview(IConnection connection, String query, String previewLimit)
      throws DatasourceServiceException {
    return SERVICE.doPreview(connection, query, previewLimit);
  }

  public ResultSetObject doPreview(IDatasource datasource) throws DatasourceServiceException {
    return SERVICE.doPreview(datasource);
  }

  public BusinessData getBusinessData(IConnection connection, String query, String previewLimit)
      throws DatasourceServiceException {
    return SERVICE.getBusinessData(connection, query, previewLimit);
  }


  public BusinessData getBusinessData(IDatasource datasource) throws DatasourceServiceException {
    return SERVICE.getBusinessData(datasource);
  }

  public IDatasource getDatasourceByName(String name) {
    return SERVICE.getDatasourceByName(name);
  }

  public List<IDatasource> getDatasources() {
    return SERVICE.getDatasources();
  }

  public Boolean updateDatasource(IDatasource datasource) {
    return SERVICE.updateDatasource(datasource);
  }
}