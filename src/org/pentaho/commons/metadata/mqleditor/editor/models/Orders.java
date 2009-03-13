package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.Order;


public class Orders extends AbstractModelNode<UIOrder>{
  
  public Orders(){
  }
  
  public Orders(List<UIOrder> orders){
    super(orders);
  }
  
  public List<Order> getBeanCollection(){
    List<Order> orders = new ArrayList<Order>();
    
    for(UIOrder o : this.getChildren()){
      orders.add(o.getBean());
    }
    return orders;
  }
  
}

  