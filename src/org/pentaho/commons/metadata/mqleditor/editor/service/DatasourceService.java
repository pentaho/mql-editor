package org.pentaho.commons.metadata.mqleditor.editor.service;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;
import org.pentaho.ui.xul.XulServiceCallback;

public interface DatasourceService {
  void getDatasources(XulServiceCallback<List<IDatasource>> callback);
  void getDatasourceByName(String name, XulServiceCallback<IDatasource> callback);
  void addDatasource(IDatasource datasource, XulServiceCallback<Boolean> callback);
  void deleteDatasource(IDatasource datasource, XulServiceCallback<Boolean> callback);
  void updateDatasource(IDatasource datasource, XulServiceCallback<Boolean> callback);
  void deleteDatasource(String name, XulServiceCallback<Boolean> callback);
  void doPreview(IConnection connection, String query, String previewLimit, XulServiceCallback<SerializedResultSet> callback) throws DatasourceServiceException;
  void doPreview(IDatasource datasource, XulServiceCallback<SerializedResultSet> callback) throws DatasourceServiceException;
  void generateModel(String modelName, IConnection connection, String query, String previewLimit, XulServiceCallback<BusinessData> callback) throws DatasourceServiceException;
  void saveModel(BusinessData businessData, Boolean overwrite, XulServiceCallback<Boolean> callback) throws DatasourceServiceException ;
}

  