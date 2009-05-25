package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.DatasourceGwtServiceAsync;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;
import org.pentaho.metadata.model.Domain;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;

public class DatasourceServiceGwtImpl implements DatasourceService {
  final static String ERROR = "ERROR:";
  static DatasourceGwtServiceAsync SERVICE;

  static {

    SERVICE = (org.pentaho.commons.metadata.mqleditor.editor.service.gwt.DatasourceGwtServiceAsync) GWT
        .create(org.pentaho.commons.metadata.mqleditor.editor.service.gwt.DatasourceGwtService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    String moduleRelativeURL = GWT.getModuleBaseURL() + "DatasourceService"; //$NON-NLS-1$
    endpoint.setServiceEntryPoint(moduleRelativeURL);

  }

  public DatasourceServiceGwtImpl() {

  }

  public void getDatasources(final XulServiceCallback<List<IDatasource>> callback) {
    SERVICE.getDatasources(new AsyncCallback<List<IDatasource>>() {

      public void onFailure(Throwable arg0) {
        callback.error("error getting connections: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(List<IDatasource> arg0) {
        callback.success(arg0);
      }

    });
  }

  public void getDatasourceByName(String name, final XulServiceCallback<IDatasource> callback) {
    SERVICE.getDatasourceByName(name, new AsyncCallback<IDatasource>() {

      public void onFailure(Throwable arg0) {
        callback.error("error getting connections: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(IDatasource arg0) {
        callback.success(arg0);
      }

    });
  }

  public void addDatasource(IDatasource datasource, final XulServiceCallback<Boolean> callback) {
    SERVICE.addDatasource(datasource, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error adding connection: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
  }

  public void updateDatasource(IDatasource datasource, final XulServiceCallback<Boolean> callback) {
    SERVICE.updateDatasource(datasource, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error updating connection: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
  }

  public void deleteDatasource(IDatasource datasource, final XulServiceCallback<Boolean> callback) {
    SERVICE.deleteDatasource(datasource, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error deleting connection: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
  }

  public void deleteDatasource(String name, final XulServiceCallback<Boolean> callback) {
    SERVICE.deleteDatasource(name, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error deleting connection: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
  }

  public void doPreview(IConnection connection, String query, String previewLimit,
      final XulServiceCallback<SerializedResultSet> callback) throws DatasourceServiceException {
    SERVICE.doPreview(connection, query, previewLimit, new AsyncCallback<SerializedResultSet>() {

      public void onFailure(Throwable arg0) {
        callback.error("error doing preview: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(SerializedResultSet arg0) {
        callback.success(arg0);
      }

    });

  }

  public void doPreview(IDatasource datasource, final XulServiceCallback<SerializedResultSet> callback)
      throws DatasourceServiceException {
    SERVICE.doPreview(datasource, new AsyncCallback<SerializedResultSet>() {

      public void onFailure(Throwable arg0) {
        callback.error("error doing preview: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(SerializedResultSet arg0) {
        callback.success(arg0);
      }

    });

  }

  public void generateModel(String modelName, IConnection connection, String query, String previewLimit,
      final XulServiceCallback<BusinessData> callback) throws DatasourceServiceException {
    SERVICE.generateModel(modelName, connection, query, previewLimit, new AsyncCallback<BusinessData>() {

      public void onFailure(Throwable arg0) {
        callback.error("error generating the mode: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(BusinessData arg0) {
        callback.success(arg0);
      }

    });
  }

  public void saveModel(String modelName, IConnection connection, String query, Boolean overwrite, String previewLimit,
      final XulServiceCallback<BusinessData> callback) throws DatasourceServiceException {
    SERVICE.saveModel(modelName, connection, query, overwrite, previewLimit, new AsyncCallback<BusinessData>() {

      public void onFailure(Throwable arg0) {
        callback.error("error saving the mode: ", arg0); //$NON-NLS-1$
      }

      public void onSuccess(BusinessData arg0) {
        callback.success(arg0);
      }

    });
  }

  public void saveModel(BusinessData businessData, Boolean overwrite, final XulServiceCallback<Boolean> callback)
      throws DatasourceServiceException {
    SERVICE.saveModel(businessData, overwrite, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error saving the mode: ", arg0); //$NON-NLS-1$
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
  }

  public void isAdministrator(final XulServiceCallback<Boolean> callback) {
    SERVICE.isAdministrator(new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error checking if the user is the administrator: ", arg0); //$NON-NLS-1$
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
  }

  @SuppressWarnings("deprecation")
  public void uploadFile(FormPanel uploadForm, final XulServiceCallback<String> callback) throws DatasourceServiceException {
    uploadForm.setAction("UploadService"); //$NON-NLS-1$
    uploadForm.submit();
    uploadForm.addFormHandler(new FormHandler() {
      public void onSubmit(FormSubmitEvent event) {
      }

      public void onSubmitComplete(FormSubmitCompleteEvent event) {
        String results = event.getResults();
        if(results != null && results.indexOf(ERROR) >= 0) {
         if(results.indexOf(ERROR) + ERROR.length() < results.length()) {
           callback.error(results.substring(results.indexOf(ERROR) + ERROR.length())
               , new DatasourceServiceException(results.substring(results.indexOf(ERROR) + ERROR.length())));  
         } else {
           callback.error(results, new DatasourceServiceException(results));
         }
        } else {
          String result = event.getResults().replaceAll("\\<.*?>","");
          callback.success(result);          
        }
      }
    });
  }

  public void generateInlineEtlModel(String modelName, String relativeFilePath, boolean headersPresent,
      String delimeter, String enclosure, final XulServiceCallback<Domain> callback) throws DatasourceServiceException {
    SERVICE.generateInlineEtlModel(modelName, relativeFilePath, headersPresent, delimeter, enclosure, new AsyncCallback<Domain>() {

      public void onFailure(Throwable arg0) {
        callback.error("error generating the inline etl model: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(Domain arg0) {
        callback.success(arg0);
      }

    });
    
  }

  public void saveInlineEtlModel(Domain modelName, Boolean overwrite, final XulServiceCallback<Boolean> callback)
      throws DatasourceServiceException {
    SERVICE.saveInlineEtlModel(modelName, overwrite, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error generating the inline etl model: ", arg0);//$NON-NLS-1$
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
  }

}
