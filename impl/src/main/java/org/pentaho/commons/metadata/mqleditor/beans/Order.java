/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.commons.metadata.mqleditor.beans;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;

public class Order implements MqlOrder {

  private Column column;

  private Type orderType;

  private AggType selectedAggType;

  public Column getColumn() {
    return column;
  }

  public Type getOrderType() {
    return orderType;
  }

  public void setOrderType( Type orderType ) {
    this.orderType = orderType;
  }

  public void setColumn( Column column ) {
    this.column = column;
  }

  public void setSelectedAggType( AggType aggType ) {
    this.selectedAggType = aggType;
  }

  public AggType getSelectedAggType() {
    return this.selectedAggType;
  }
}
