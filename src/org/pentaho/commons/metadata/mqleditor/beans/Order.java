package org.pentaho.commons.metadata.mqleditor.beans;

import org.pentaho.commons.metadata.mqleditor.IBusinessColumn;
import org.pentaho.commons.metadata.mqleditor.IOrder;

public class Order implements IOrder {

  private BusinessColumn column;

  private Type orderType;

  public BusinessColumn getColumn() {
    return column;
  }

  public Type getOrderType() {
    return orderType;
  }

  public void setOrderType(Type orderType) {
    this.orderType = orderType;
  }

  public void setColumn(BusinessColumn column) {
    this.column = column;
  }

}
