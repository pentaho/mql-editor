package org.pentaho.metadata;

import java.io.Serializable;

import org.pentaho.metadata.editor.models.UIBusinessColumn;
import org.pentaho.metadata.editor.models.UIOrder;

public interface IOrder extends Serializable{

  public enum Type{ASC, DESC}
  
  public IBusinessColumn getColumn();
  
  public Type getOrderType();

}
