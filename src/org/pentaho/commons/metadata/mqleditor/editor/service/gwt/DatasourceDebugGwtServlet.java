package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BogoPojo;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.DatasourceServiceInMemoryDelegate;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;
import org.pentaho.metadata.model.Domain;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DatasourceDebugGwtServlet extends RemoteServiceServlet implements DatasourceGwtService {

  DatasourceServiceInMemoryDelegate SERVICE;

  public DatasourceDebugGwtServlet() {
    SERVICE = new DatasourceServiceInMemoryDelegate();
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

  public SerializedResultSet doPreview(IConnection connection, String query, String previewLimit) throws DatasourceServiceException{
    return SERVICE.doPreview(connection, query, previewLimit);
  }

  public SerializedResultSet doPreview(IDatasource datasource) throws DatasourceServiceException{
    return SERVICE.doPreview(datasource);
  }

  public BusinessData generateModel(String modelName, IConnection connection, String query, String previewLimit) throws DatasourceServiceException {
    return SERVICE.generateModel(modelName, connection, query, previewLimit);
   }
  public BusinessData saveModel(String modelName, IConnection connection, String query, Boolean overwrite, String previewLimit) throws DatasourceServiceException {
    return SERVICE.saveModel(modelName, connection, query, overwrite, previewLimit);
  }
  public Boolean saveModel(BusinessData businessData, Boolean overwrite) throws DatasourceServiceException {
    return SERVICE.saveModel(businessData, overwrite);
  }
  public Boolean isAdministrator() {
    return SERVICE.isAdministrator();
  }
  
  public BogoPojo gwtWorkaround(BogoPojo pojo) {
    return pojo;
  }

  public Domain generateInlineEtlModel(String modelName, String relativeFilePath, boolean headersPresent,
      String delimeter, String enclosure) throws DatasourceServiceException {
    return SERVICE.generateInlineEtlModel(modelName, relativeFilePath, headersPresent, delimeter, enclosure);
  }

  public Boolean saveInlineEtlModel(Domain modelName, Boolean overwrite) throws DatasourceServiceException {
    return SERVICE.saveInlineEtlModel(modelName, overwrite);
  } 

}