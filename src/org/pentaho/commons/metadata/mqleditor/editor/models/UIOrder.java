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
  
  //The supplied Beans are a Graph of objects. In order to maintain those relationships, we track
  // previously created objects in order to serve the same objects when needed.
  private static Map<Order, UIOrder> wrappedOrders = new HashMap<Order, UIOrder>();
  

  public static UIOrder wrap(Order order){
    if(wrappedOrders.containsKey(order)){
      return wrappedOrders.get(order);
    }
    UIOrder c = new UIOrder(order);
    wrappedOrders.put(order, c);
    return c;
  }
  
  public UIOrder(){
  }
  
  private UIOrder(Order order){
    this.column = UIColumn.wrap(order.getColumn());
    this.orderType = order.getOrderType();
  }

  public UIOrder(UIColumn column, Type type){
    this.column = column;
    this.orderType = type;
  }
  
  public UIColumn getColumn() {
  
    return column;
  }

  public void setColumn(MqlColumn column) {
  
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
    
  }
  
  public void setColumnName(String str){
    
  }
  
  public String getColumnName(){
    return this.column.getName();
  }

  public Vector getOrderTypes(){
    Vector v = new Vector();
    v.addAll(Arrays.asList(MqlOrder.Type.values()));
    return v;
  }
  
  public Order getBean() {
    Order bean = new Order();
    bean.setColumn(column.getBean());
    bean.setOrderType(orderType);
    return bean;
  }
  
  
}
