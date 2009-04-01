package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.pentaho.commons.metadata.mqleditor.IConnection;

public class ConnectionServiceDelegate {

  private String locale = Locale.getDefault().toString();

  private List<IConnection> connectionList = new ArrayList<IConnection>();
  
  public ConnectionServiceDelegate() {
  }
  
  public List<IConnection> getConnections() {
    return connectionList;
  }
  public IConnection getConnectionByName(String name) {
    for(IConnection connection:connectionList) {
      if(connection.getName().equals(name)) {
        return connection;
      }
    }
    return null;
  }
  public Boolean addConnection(IConnection connection) {
    connectionList.add(connection);
    return true;
  }
  public Boolean updateConnection(IConnection connection) {
    IConnection conn = getConnectionByName(connection.getName());
    conn.setDriverClass(connection.getDriverClass());
    conn.setPassword(connection.getPassword());
    conn.setUrl(connection.getUrl());
    conn.setUsername(connection.getUsername());
    return true;
  }
  public Boolean deleteConnection(IConnection connection) {
    connectionList.remove(connectionList.indexOf(connection));
    return true;
  }
  public Boolean deleteConnection(String name) {
    for(IConnection connection:connectionList) {
      if(connection.getName().equals(name)) {
        return deleteConnection(connection);
      }
    }
    return false;
  }
}
