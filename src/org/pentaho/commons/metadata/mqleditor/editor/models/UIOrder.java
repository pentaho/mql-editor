package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class UIOrder extends XulEventSourceAdapter implements MqlOrder {
  
  
  private UIColumn column;
  private Type orderType = Type.DESC;

  
  public UIOrder(){
  }
  
  public UIOrder(MqlOrder order){
    this.column = new UIColumn(order.getColumn());
    this.orderType = order.getOrderType();
  }

  public UIOrder(UIColumn column, Type type){
    this.column = column;
    this.orderType = type;
  }
  
  public UIColumn getColumn() {
  
    return column;
  }

  public void setColumn(UIColumn column) {
  
    this.column = (UIColumn) column;
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
    // ignored
  }
  
  public void setColumnName(String str){
    // ignored
  }
  
  public String getColumnName(){
    return this.column.getName();
  }

  public Vector getOrderTypes(){
    Vector v = new Vector();
    v.addAll(Arrays.asList(MqlOrder.Type.values()));
    return v;
  }
  
  
}
