package org.pentaho.metadata.editor.models;

import java.util.Arrays;
import java.util.Vector;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.IOrder;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class UIOrder extends XulEventSourceAdapter implements IOrder {
  
  
  private UIBusinessColumn column;
  private Type orderType;
  
  public UIOrder(){
    
  }

  public UIOrder(UIBusinessColumn column, Type type){
    this.column = column;
    this.orderType = type;
  }
  
  public UIBusinessColumn getColumn() {
  
    return column;
  }

  public void setColumn(IBusinessColumn column) {
  
    this.column = (UIBusinessColumn) column;
  }

  public Type getOrderType() {
  
    return orderType;
  }

  public void setOrderType(Type orderType) {
  
    this.orderType = orderType;
  }
  
  public void setOrderType(Object orderType) {
  
    this.orderType = (Type) orderType;
  }
  

  public String getTableName(){
    return this.column.getTableName();
  }
  
  public void setTableName(String str){
    
  }
  
  public void setColumnName(String str){
    
  }
  
  public String getColumnName(){
    return this.column.getName();
  }

  public Vector getOrderTypes(){
    Vector v = new Vector();
    v.addAll(Arrays.asList(IOrder.Type.values()));
    return v;
  }
  
  
}
