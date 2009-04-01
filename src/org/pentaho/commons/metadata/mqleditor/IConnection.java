package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

public interface IConnection extends Serializable{
  public String getName();
  public void setName(String name);
  public String getDriverClass();
  public void setDriverClass(String driverClass);
  public String getUsername();
  public void setUsername(String username);
  public String getPassword();
  public void setPassword(String password);
  public String getUrl();
  public void setUrl(String url);

}
