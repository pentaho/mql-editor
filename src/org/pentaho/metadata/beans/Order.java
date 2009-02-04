package org.pentaho.metadata.beans;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.IOrder;

public class Order implements IOrder {

  private IBusinessColumn column;

  private Type orderType;

  public IBusinessColumn getColumn() {
    return column;
  }

  public Type getOrderType() {
    return orderType;
  }

  public void setOrderType(Type orderType) {
    this.orderType = orderType;
  }

  public void setColumn(IBusinessColumn column) {
    this.column = column;
  }

}
