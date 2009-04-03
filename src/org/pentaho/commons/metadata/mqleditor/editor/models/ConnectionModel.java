package org.pentaho.commons.metadata.mqleditor.editor.models;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.beans.Connection;
import org.pentaho.ui.xul.XulEventSourceAdapter;



public class ConnectionModel extends XulEventSourceAdapter implements IConnection {
  
  private boolean isValid;
  private String name;
  private String driverClass;
  private String username;
  private String password;
  private String url;
  public ConnectionModel(){
    
  }

  public void setName(String name) {
    String previousVal = this.name;
    this.name = name;
    this.firePropertyChange("name", previousVal, name); //$NON-NLS-1$    
    validate();
  }

  public String getName() {
    return name;

  }

  public void setDriverClass(String driverClass) {
    String previousVal = this.driverClass;
    this.driverClass = driverClass;
    this.firePropertyChange("driverClass", previousVal, driverClass); //$NON-NLS-1$
    validate();
  }

  public String getDriverClass() {
    return driverClass;
  }
  
  public void setUsername(String username) {
    String previousVal = this.username;    
    this.username = username;
    this.firePropertyChange("username", previousVal, username); //$NON-NLS-1$
    validate();
  }

  public String getUsername() {
    return username;
  }
  public void setPassword(String password) {
    String previousVal = this.password;    
    this.password = password;
    this.firePropertyChange("password", previousVal, password); //$NON-NLS-1$
    validate();
  }

  public String getPassword() {
    return password;
  }
  public void setUrl(String url) {
    String previousVal = this.url;     
    this.url = url;
    this.firePropertyChange("url",previousVal, url); //$NON-NLS-1$
    validate();
  }

  public String getUrl() {
    return url;
  }
  
  public IConnection getConnection() {
    IConnection connection = new Connection();
    connection.setDriverClass(driverClass);
    connection.setName(name);
    connection.setPassword(password);
    connection.setUrl(url);
    connection.setUsername(username);
    return connection;
  }

  public void setConnection(IConnection connection) {
    if(connection != null) {
      setDriverClass(connection.getDriverClass());
      setName(connection.getName());
      setPassword(connection.getPassword());
      setUrl(connection.getUrl());
      setUsername(connection.getUsername());      
    } else {
      setDriverClass(null);
      setName(null);
      setPassword(null);
      setUrl(null);
      setUsername(null);      
    }
  }


  public boolean isValidated(){
    return isValid;
  }
  
  private void setValidated(boolean valid){
    boolean prevVal = isValid;
    this.isValid = valid;
    this.firePropertyChange("validated", prevVal, isValid);
  }
  
  public void validate() {
    if((getDriverClass() != null && getDriverClass().length() > 0)
        && (getName() != null && getName().length() > 0) 
          && (getUrl() != null && getUrl().length() > 0)
            && (getUsername() != null && getUsername().length() > 0) ) {
      this.setValidated(true);
    } else {
      this.setValidated(false);
    }
  }

}

