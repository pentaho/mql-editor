package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.ResultSetObject;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.DatasourceGwtServiceAsync;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class DatasourceServiceGwtImpl implements DatasourceService {

  static DatasourceGwtServiceAsync SERVICE;

  static {

    SERVICE = (org.pentaho.commons.metadata.mqleditor.editor.service.gwt.DatasourceGwtServiceAsync) GWT.create(org.pentaho.commons.metadata.mqleditor.editor.service.gwt.DatasourceGwtService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    String moduleRelativeURL = GWT.getModuleBaseURL() + "ConnectionService"; //$NON-NLS-1$
    endpoint.setServiceEntryPoint(moduleRelativeURL);

  }

  public DatasourceServiceGwtImpl() {

  }

  
  public void getDatasources(final XulServiceCallback<List<IDatasource>> callback) {
    SERVICE.getDatasources(new AsyncCallback<List<IDatasource>>() {

      public void onFailure(Throwable arg0) {
        callback.error("error getting connections: ", arg0);
      }

      public void onSuccess(List<IDatasource> arg0) {
        callback.success(arg0);
      }

    });
  }
  public void getDatasourceByName(String name, final XulServiceCallback<IDatasource> callback) {
    SERVICE.getDatasourceByName(name, new AsyncCallback<IDatasource>() {

      public void onFailure(Throwable arg0) {
        callback.error("error getting connections: ", arg0);
      }

      public void onSuccess(IDatasource arg0) {
        callback.success(arg0);
      }

    });
  }
  public void addDatasource(IDatasource datasource, final XulServiceCallback<Boolean> callback) {
    SERVICE.addDatasource(datasource, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error adding connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }
  
  public void updateDatasource(IDatasource datasource, final XulServiceCallback<Boolean> callback) {
    SERVICE.updateDatasource(datasource, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error updating connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }
  public void deleteDatasource(IDatasource datasource, final XulServiceCallback<Boolean> callback) {
    SERVICE.deleteDatasource(datasource, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error deleting connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }
  public void deleteDatasource(String name, final XulServiceCallback<Boolean> callback) {
    SERVICE.deleteDatasource(name, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error deleting connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }


  public void doPreview(IConnection connection, String query, String previewLimit, final XulServiceCallback<ResultSetObject> callback)
      throws DatasourceServiceException {
    SERVICE.doPreview(connection, query, previewLimit, new AsyncCallback<ResultSetObject>() {

      public void onFailure(Throwable arg0) {
        callback.error("error doing preview: ", arg0);
      }

      public void onSuccess(ResultSetObject arg0) {
        callback.success(arg0);
      }

    }); 
   
  }

  public void doPreview(IDatasource datasource, final XulServiceCallback<ResultSetObject> callback)
      throws DatasourceServiceException {
    SERVICE.doPreview(datasource, new AsyncCallback<ResultSetObject>() {

      public void onFailure(Throwable arg0) {
        callback.error("error doing preview: ", arg0);
      }

      public void onSuccess(ResultSetObject arg0) {
        callback.success(arg0);
      }

    });
    
  }
  

  public void getBusinessData(IConnection connection, String query, String previewLimit, final XulServiceCallback<ResultSetObject> callback)
      throws DatasourceServiceException {
    SERVICE.doPreview(connection, query, previewLimit, new AsyncCallback<ResultSetObject>() {

      public void onFailure(Throwable arg0) {
        callback.error("error doing preview: ", arg0);
      }

      public void onSuccess(ResultSetObject arg0) {
        callback.success(arg0);
      }

    }); 
   
  }

  public void getBusinessData(IDatasource datasource, final XulServiceCallback<ResultSetObject> callback)
      throws DatasourceServiceException {
    SERVICE.doPreview(datasource, new AsyncCallback<ResultSetObject>() {

      public void onFailure(Throwable arg0) {
        callback.error("error doing preview: ", arg0);
      }

      public void onSuccess(ResultSetObject arg0) {
        callback.success(arg0);
      }

    });
    
  }

  public void createCategory(String categoryName, IConnection connection, String query, ResultSetObject rso,
      final XulServiceCallback<Boolean> callback) {
    SERVICE.createCategory(categoryName, connection, query, rso, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error doing preview: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    });
    
  }

  
}
