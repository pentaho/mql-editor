/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;
import org.pentaho.ui.xul.XulEventSourceAdapter;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UIOrder extends XulEventSourceAdapter implements MqlOrder {

  private UIColumn column;
  private Type orderType = Type.DESC;
  private AggType selectedAggType;

  public UIOrder() {
  }

  public UIOrder( MqlOrder order ) {
    if ( order.getColumn() != null ) {
      this.column = new UIColumn( order.getColumn() );
    }
    this.orderType = order.getOrderType();
    this.selectedAggType = order.getSelectedAggType();
  }

  public UIOrder( UIColumn column, Type type ) {
    this.column = column;
    this.orderType = type;
  }

  @Bindable
  public UIColumn getColumn() {

    return column;
  }

  @Bindable
  public void setColumn( UIColumn column ) {

    this.column = (UIColumn) column;
  }

  @Bindable
  public Type getOrderType() {

    return orderType;
  }

  @Bindable
  public void setOrderType( Type orderType ) {

    this.orderType = orderType;
  }

  @Bindable
  public void setOrderType( Object orderType ) {

    this.orderType = (Type) orderType;
  }

  @Bindable
  public void setTableName( String str ) {
    // ignored
  }

  @Bindable
  public void setColumnName( String str ) {
    // ignored
  }

  @Bindable
  public String getColumnName() {
    return this.column.getName();
  }

  @Bindable
  public Vector getOrderTypes() {
    Vector v = new Vector();
    v.addAll( Arrays.asList( MqlOrder.Type.values() ) );
    return v;
  }

  @Bindable
  public void setSelectedAggType( Object aggType ) {
    setSelectedAggType( (AggType) aggType );
  }

  @Bindable
  public void setSelectedAggType( AggType aggType ) {
    this.selectedAggType = aggType;
  }

  @Bindable
  public AggType getSelectedAggType() {
    return this.selectedAggType;
  }

  @Bindable
  public List<AggType> getAggTypes() {
    return this.column.getAggTypes();
  }

  @Bindable
  public void setAggTypes( List<AggType> ignored ) {

  }

  @Bindable
  public Vector getBindingAggTypes() {
    return column.getBindingAggTypes();
  }
}
