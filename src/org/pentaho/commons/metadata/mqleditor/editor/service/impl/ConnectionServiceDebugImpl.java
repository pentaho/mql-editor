package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionService;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionServiceException;
import org.pentaho.ui.xul.XulServiceCallback;

public class ConnectionServiceDebugImpl implements ConnectionService{

  ConnectionServiceDelegate SERVICE;
  public ConnectionServiceDebugImpl(){
    SERVICE = new ConnectionServiceDelegate();
  }
 
  public void getConnections(XulServiceCallback<List<IConnection>> callback) {
    callback.success(SERVICE.getConnections());
  }
  public void getConnectionByName(String name, XulServiceCallback<IConnection> callback) {
    callback.success(SERVICE.getConnectionByName(name));
  }
  public void addConnection(IConnection connection, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.addConnection(connection));
  }
  
  public void updateConnection(IConnection connection, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.updateConnection(connection));
  }
  public void deleteConnection(IConnection connection, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.deleteConnection(connection));
  }
  public void deleteConnection(String name, XulServiceCallback<Boolean> callback) {
    callback.success(SERVICE.deleteConnection(name));
  }
  public void testConnection(IConnection connection, XulServiceCallback<Boolean> callback)throws ConnectionServiceException  {
    callback.success(SERVICE.testConnection(connection));
  }  
  
}

  