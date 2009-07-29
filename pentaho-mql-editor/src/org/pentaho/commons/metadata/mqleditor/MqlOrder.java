package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

public interface MqlOrder extends Serializable{

  public enum Type{ASC, DESC}
  
  public MqlColumn getColumn();
  
  public Type getOrderType();
  
  public AggType getSelectedAggType();

}
