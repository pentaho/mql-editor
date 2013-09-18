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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UIColumn extends AbstractModelNode<UIColumn> implements MqlColumn {


  private ColumnType type;
  private String id;
  private String name;

  private List<AggType> aggTypes = new ArrayList<AggType>();
  private AggType defaultAggType;

  private AggType selectedAggType;
  
  public UIColumn() {

  }
  
  public Object clone() {
    UIColumn col = new UIColumn();
    col.type = type;
    col.id = id;
    col.name = name;
    col.aggTypes = aggTypes;
    col.defaultAggType = defaultAggType;
    col.selectedAggType = selectedAggType;
    return col;
  }

  public UIColumn(MqlColumn col) {
    
    this.type = col.getType();
    this.id = col.getId();
    this.name = col.getName();
    this.aggTypes = col.getAggTypes();
    this.defaultAggType = col.getDefaultAggType();
    this.selectedAggType = col.getSelectedAggType();
    
  }
  public String getId() {
    return id;
  }
  
  public void setId(String id){
    this.id = id;
  }

  @Bindable
  public String getName() {
    return this.name;
  }
  
  public String getPreviewName() {
    if (selectedAggType != AggType.NONE) {
			return name + " (" + selectedAggType.toString() + ")";   
    } else {
	    return name;
	  }
  }
  
  @Bindable
  public void setName(String name){
    this.name = name;
  }
  
  @Bindable
  public void setTableName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  @Bindable
  public ColumnType getType() {

    return type;
  }
  
  @Bindable
  public void setType(ColumnType type){
    this.type = type;
  }
  
  public String toString(){
    return id;
  }

  @Bindable
  public AggType getDefaultAggType() {
    return defaultAggType;
  }

  @Bindable
  public List<AggType> getAggTypes() {
    return aggTypes;
  }

  @Bindable
  public void setAggTypes(List<AggType> aggTypes) {
    this.aggTypes = aggTypes;
  }

  @Bindable
  public void setDefaultAggType(AggType defaultAggType) {
    this.defaultAggType = defaultAggType;
  }
  
  @Bindable
  public void setSelectedAggType(Object o){
    setSelectedAggType((AggType)  o);
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
  public Vector getBindingAggTypes(){
    Vector v = new Vector();
    for(AggType t : this.aggTypes){
      v.add(t);
    }
    return v;
  }

  @Override
  // We clone this object so default equality from Object is not valid.
  public boolean equals(Object o) {
    if(o instanceof UIColumn == false){
      return false;
    }
    
    UIColumn other = (UIColumn) o;
    if(type != other.getType()){
      return false;
    } else if(id.equals(other.getId()) == false){
      return false;
    } else if(name.equals(other.getName()) == false){
      return false;
    } else if(selectedAggType != other.getSelectedAggType()){
      return false;
    }
    
    return true;
  }
  
  
}
