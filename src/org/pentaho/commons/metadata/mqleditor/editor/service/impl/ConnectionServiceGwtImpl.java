package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionService;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.ConnectionGwtService;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.ConnectionGwtServiceAsync;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ConnectionServiceGwtImpl implements ConnectionService {

  static ConnectionGwtServiceAsync SERVICE;

  static {

    SERVICE = (org.pentaho.commons.metadata.mqleditor.editor.service.gwt.ConnectionGwtServiceAsync) GWT.create(org.pentaho.commons.metadata.mqleditor.editor.service.gwt.ConnectionGwtService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    String moduleRelativeURL = GWT.getModuleBaseURL() + "ConnectionService"; //$NON-NLS-1$
    endpoint.setServiceEntryPoint(moduleRelativeURL);

  }

  public ConnectionServiceGwtImpl() {

  }

  
  public void getConnections(final XulServiceCallback<List<IConnection>> callback) {
    SERVICE.getConnections(new AsyncCallback<List<IConnection>>() {

      public void onFailure(Throwable arg0) {
        callback.error("error getting connections: ", arg0);
      }

      public void onSuccess(List<IConnection> arg0) {
        callback.success(arg0);
      }

    });
  }
  public void getConnectionByName(String name, final XulServiceCallback<IConnection> callback) {
    SERVICE.getConnectionByName(name, new AsyncCallback<IConnection>() {

      public void onFailure(Throwable arg0) {
        callback.error("error getting connections: ", arg0);
      }

      public void onSuccess(IConnection arg0) {
        callback.success(arg0);
      }

    });
  }
  public void addConnection(IConnection connection, final XulServiceCallback<Boolean> callback) {
    SERVICE.addConnection(connection, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error adding connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }
  
  public void updateConnection(IConnection connection, final XulServiceCallback<Boolean> callback) {
    SERVICE.updateConnection(connection, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error updating connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }
  public void deleteConnection(IConnection connection, final XulServiceCallback<Boolean> callback) {
    SERVICE.deleteConnection(connection, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error deleting connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }
  public void deleteConnection(String name, final XulServiceCallback<Boolean> callback) {
    SERVICE.deleteConnection(name, new AsyncCallback<Boolean>() {

      public void onFailure(Throwable arg0) {
        callback.error("error deleting connection: ", arg0);
      }

      public void onSuccess(Boolean arg0) {
        callback.success(arg0);
      }

    }); 
  }
}
