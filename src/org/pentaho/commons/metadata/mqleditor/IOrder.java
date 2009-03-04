package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

public interface IOrder extends Serializable{

  public enum Type{ASC, DESC}
  
  public IBusinessColumn getColumn();
  
  public Type getOrderType();

}
