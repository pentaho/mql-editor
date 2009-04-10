package org.pentaho.commons.metadata.mqleditor.beans;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class Connection extends XulEventSourceAdapter implements IConnection{

  private String name;
  private String driverClass;
  private String username;
  private String password;
  private String url;

  public Connection(){
    
  }

  public Connection(IConnection connection){
    setName(connection.getName());
    setDriverClass(connection.getDriverClass());
    setPassword(connection.getPassword());
    setUrl(connection.getUrl());
    setUsername(connection.getUsername());
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setDriverClass(String driverClass) {
    this.driverClass = driverClass;
  }

  public String getDriverClass() {
    return driverClass;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return password;
  }
  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

}
