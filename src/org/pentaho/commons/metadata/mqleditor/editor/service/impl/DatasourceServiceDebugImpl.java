package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;
import org.pentaho.metadata.model.Domain;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;

public class DatasourceServiceDebugImpl implements DatasourceService{

  DatasourceServiceInMemoryDelegate SERVICE;
  public DatasourceServiceDebugImpl(){
    SERVICE = new DatasourceServiceInMemoryDelegate();
  }
 
  public void getDatasources(XulServiceCallback<List<IDatasource>> callback) {
    callback.success(SERVICE.getDatasources());
  }
  public void getDatasourceByName(String name, XulServiceCallback<IDatasource> callback) {
    callback.success(SERVICE.getDatasourceByName(name));
  }
  public void addDatasource(IDatasource datasource, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.addDatasource(datasource));
  }
  
  public void updateDatasource(IDatasource datasource, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.updateDatasource(datasource));
  }
  public void deleteDatasource(IDatasource datasource, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.deleteDatasource(datasource));
  }
  public void deleteDatasource(String name, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.deleteDatasource(name));
  }

  public void doPreview(IConnection connection, String query, String previewLimit, XulServiceCallback<SerializedResultSet> callback)
      throws DatasourceServiceException {
    callback.success(SERVICE.doPreview(connection, query, previewLimit));
  }

  public void doPreview(IDatasource datasource, XulServiceCallback<SerializedResultSet> callback)
      throws DatasourceServiceException {
    callback.success(SERVICE.doPreview(datasource));
  }

  public void generateModel(String modelName, IConnection connection, String query, String previewLimit,
      XulServiceCallback<BusinessData> callback) throws DatasourceServiceException {
   callback.success(SERVICE.generateModel(modelName, connection, query, previewLimit));
  }
  public void saveModel(String modelName, IConnection connection, String query, Boolean overwrite, String previewLimit,
      XulServiceCallback<BusinessData> callback) throws DatasourceServiceException {
   callback.success(SERVICE.saveModel(modelName, connection, query, overwrite, previewLimit));
  }

  public void saveModel(BusinessData businessData, Boolean overwrite, XulServiceCallback<Boolean> callback)
      throws DatasourceServiceException {
    callback.success(SERVICE.saveModel(businessData, overwrite));
  }

  public void isAdministrator(XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.isAdministrator());
  }

  public void uploadFile(FormPanel uploadForm, final XulServiceCallback<String> callback)  throws DatasourceServiceException {
    uploadForm.setAction(GWT.getModuleBaseURL() + "UploadService");
    uploadForm.submit();
    uploadForm.addFormHandler(new FormHandler() {
      public void onSubmit(FormSubmitEvent event) {
      }

      public void onSubmitComplete(FormSubmitCompleteEvent event) {
        Window.alert(event.getResults());
        callback.success(event.getResults());
      }
    });
}

  public void generateInlineEtlModel(String modelName, String relativeFilePath, boolean headersPresent,
      String delimeter, String enclosure, XulServiceCallback<Domain> callback) throws DatasourceServiceException {
    callback.success(SERVICE.generateInlineEtlModel(modelName, relativeFilePath, headersPresent, delimeter, enclosure));
  }

  public void saveInlineEtlModel(Domain modelName, Boolean overwrite, XulServiceCallback<Boolean> callback)
      throws DatasourceServiceException {
    callback.success(SERVICE.saveInlineEtlModel(modelName, overwrite));
  }
}

  