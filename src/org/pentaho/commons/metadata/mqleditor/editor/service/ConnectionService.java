package org.pentaho.commons.metadata.mqleditor.editor.service;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.ui.xul.XulServiceCallback;

public interface ConnectionService {
  void getConnections(XulServiceCallback<List<IConnection>> callback);
  void getConnectionByName(String name, XulServiceCallback<IConnection> callback);
  void addConnection(IConnection connection, XulServiceCallback<Boolean> callback);
  void updateConnection(IConnection connection, XulServiceCallback<Boolean> callback);
  void deleteConnection(IConnection connection, XulServiceCallback<Boolean> callback);
  void deleteConnection(String name, XulServiceCallback<Boolean> callback);
}

  