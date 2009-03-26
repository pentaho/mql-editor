package org.pentaho.commons.metadata.mqleditor.beans;

import org.pentaho.commons.metadata.mqleditor.MqlOrder;

public class Order implements MqlOrder {

  private Column column;

  private Type orderType;

  public Column getColumn() {
    return column;
  }

  public Type getOrderType() {
    return orderType;
  }

  public void setOrderType(Type orderType) {
    this.orderType = orderType;
  }

  public void setColumn(Column column) {
    this.column = column;
  }

}
