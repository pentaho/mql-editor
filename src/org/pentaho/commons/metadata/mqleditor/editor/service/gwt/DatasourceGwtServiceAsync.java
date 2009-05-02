package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DatasourceGwtServiceAsync {
  void getDatasources(AsyncCallback<List<IDatasource>> callback);
  void getDatasourceByName(String name, AsyncCallback<IDatasource> callback);
  void addDatasource(IDatasource datasource, AsyncCallback<Boolean> callback);
  void deleteDatasource(IDatasource datasource, AsyncCallback<Boolean> callback);
  void updateDatasource(IDatasource datasource, AsyncCallback<Boolean> callback);
  void deleteDatasource(String name, AsyncCallback<Boolean> callback);
  void doPreview(IConnection connection, String query, String previewLimit, AsyncCallback<SerializedResultSet> callback);
  void doPreview(IDatasource datasource, AsyncCallback<SerializedResultSet> callback);
  void generateModel(String modelName, IConnection connection, String query, String previewLimit, AsyncCallback<BusinessData> callback) throws DatasourceServiceException;
  void saveModel(BusinessData businessData, Boolean overwrite, AsyncCallback<Boolean> callback) throws DatasourceServiceException ;
}

  