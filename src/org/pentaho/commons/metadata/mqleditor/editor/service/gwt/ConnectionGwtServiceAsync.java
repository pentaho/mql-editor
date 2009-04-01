package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConnectionGwtServiceAsync {
  void getConnections(AsyncCallback<List<IConnection>> callback);
  void getConnectionByName(String name, AsyncCallback<IConnection> callback);
  void addConnection(IConnection connection, AsyncCallback<Boolean> callback);
  void updateConnection(IConnection connection, AsyncCallback<Boolean> callback);
  void deleteConnection(IConnection connection, AsyncCallback<Boolean> callback);
  void deleteConnection(String name, AsyncCallback<Boolean> callback);
}

  