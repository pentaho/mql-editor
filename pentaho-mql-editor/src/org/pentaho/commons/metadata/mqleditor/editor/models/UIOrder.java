/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.ui.xul.XulEventSourceAdapter;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UIOrder extends XulEventSourceAdapter implements MqlOrder {
  
  
  private UIColumn column;
  private Type orderType = Type.DESC;
  private AggType selectedAggType;

  
  public UIOrder(){
  }
  
  public UIOrder(MqlOrder order){
    if(order.getColumn() != null){
      this.column = new UIColumn(order.getColumn());
    }
    this.orderType = order.getOrderType();
    this.selectedAggType = order.getSelectedAggType();
  }

  public UIOrder(UIColumn column, Type type){
    this.column = column;
    this.orderType = type;
  }
  
  @Bindable
  public UIColumn getColumn() {
  
    return column;
  }

  @Bindable
  public void setColumn(UIColumn column) {
  
    this.column = (UIColumn) column;
  }

  @Bindable
  public Type getOrderType() {
  
    return orderType;
  }

  @Bindable
  public void setOrderType(Type orderType) {
  
    this.orderType = orderType;
  }
  
  @Bindable
  public void setOrderType(Object orderType) {
  
    this.orderType = (Type) orderType;
  }
  
  @Bindable
  public void setTableName(String str){
    // ignored
  }
  
  @Bindable
  public void setColumnName(String str){
    // ignored
  }
  
  @Bindable
  public String getColumnName(){
    return this.column.getName();
  }

  @Bindable
  public Vector getOrderTypes(){
    Vector v = new Vector();
    v.addAll(Arrays.asList(MqlOrder.Type.values()));
    return v;
  }
  

  @Bindable
  public void setSelectedAggType(Object aggType){
    setSelectedAggType((AggType) aggType);
  }
  

  @Bindable
  public void setSelectedAggType(AggType aggType){
    this.selectedAggType = aggType;
  }
  
  @Bindable
  public AggType getSelectedAggType(){
    return this.selectedAggType;
  }
  
  @Bindable
  public List<AggType> getAggTypes() {
    return this.column.getAggTypes();
  }
  
  @Bindable
  public void setAggTypes(List<AggType> ignored){
    
  }

  @Bindable
  public Vector getBindingAggTypes(){
    return column.getBindingAggTypes();
  }
}
