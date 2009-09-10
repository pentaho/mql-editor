/*
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
 * Copyright (c) 2009 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.ui.xul.XulEventSourceAdapter;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UICondition extends XulEventSourceAdapter implements MqlCondition {
  
  private String defaultValue;
  private UIColumn column;
  private Operator operator = Operator.EQUAL;
  private String value;
  private CombinationType combinationType = CombinationType.AND;
  private boolean parameterized;
  private boolean valueDisabled;
  private AggType selectedAggType;
  
  private boolean topMost;
  
  public UICondition(){
  }
  
  public UICondition(UIColumn column, Operator operator, String value){
    this();
    this.column = column;
    this.operator = operator;
    this.value = value;
    
  }
  
  public UICondition(Condition bean){
    if(bean.getColumn() != null){
      this.column = new UIColumn(bean.getColumn());
    }
    this.defaultValue = bean.getDefaultValue();
    this.operator = bean.getOperator();
    this.value = bean.getValue();
    this.combinationType = bean.getCombinationType();
    column = new UIColumn(bean.getColumn());
    this.selectedAggType = bean.getSelectedAggType();
  }
  
  public boolean validate(){
    //add type checking by column type
    return true;
  }


  @Bindable
  public UIColumn getColumn() {
    return column;
  }

  

  @Bindable
  public void setColumn(UIColumn column) {
    this.column = column;
    
    //only do this if it's default, otherwise you may override a user specified value
    if(this.operator == Operator.EQUAL){ 
      this.setOperator( (column.getType() == ColumnType.TEXT) ?  Operator.EXACTLY_MATCHES :Operator.EQUAL );
    }
  }


  @Bindable
  public Operator getOperator() {
    return operator;
  }


  @Bindable
  public void setOperator(Operator operator) {
    this.operator = operator;
    switch(this.operator){
      case IS_NULL:
      case IS_NOT_NULL:
      case IS_EMPTY:
      case IS_NOT_EMPTY:
        this.setValueDisabled(true);
        break;
      default:
        this.setValueDisabled(false);
    }
  }

  @Bindable
  public void setOperator(Object operator) {
    setOperator((Operator) operator);
  }

  @Bindable
  public String getValue() {
    return value;
  }


  @Bindable
  public void setValue(String value) {
    String prevVal = this.value;
    this.value = value;
    this.firePropertyChange("value", prevVal, this.value); //$NON-NLS-1$
    
    this.setParameterized(value != null && value.contains("{") && value.contains("}"));  //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  
  @Bindable
  public String getComboTypeStr() {
    if (isTopMost()) {
      if (combinationType == CombinationType.AND_NOT) {
        return "NOT"; //$NON-NLS-1$
      } else {
        return ""; //$NON-NLS-1$
      }
    } else {
      return combinationType.name();
    }
  }

  @Bindable
  public void setComboTypeStr(Object name) {
    if (isTopMost()) {
      if (name.equals("NOT")) { //$NON-NLS-1$
        combinationType = CombinationType.AND_NOT; 
      } else {
        combinationType = CombinationType.AND;
      }
    } else {
      combinationType = CombinationType.valueOf((String)name);
    }
  }

  @Bindable
  public CombinationType getCombinationType() {
    return combinationType;
  }


  @Bindable
  public void setCombinationType(CombinationType combinationType) {
    this.combinationType = combinationType;
  }
  
  //Binding value comes in as Object unfortunately.
  public void setCombinationType(Object combinationType) {
    if(combinationType != null){
      setCombinationType((CombinationType) combinationType);
    }
  }

  public void setTableName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  @Bindable
  public String getColumnName(){
    return column.getName();
  }

  @Bindable
  public void setColumnName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  @Bindable
  public Vector getComparisons(){
    Vector v = new Vector();

    v.addAll(
        Arrays.asList(
            Operator.values( this.getColumn().getType() == ColumnType.TEXT )
        )
    );
    
    return v;
  }
  
  @Bindable
  public void setComparisons(String str){}
  
  @Bindable
  public Vector getCombinations(){
    if (isTopMost()) {
      Vector v = new Vector();
      v.add(""); //$NON-NLS-1$
      v.add("NOT"); //$NON-NLS-1$
      return v;
    } else {
      Vector v = new Vector();
      for (CombinationType type : CombinationType.values()) {
        v.add(type.name());
      }
      return v;
    }
  }
  
  @Bindable
  public void setCombinations(String str){}
  
  @Bindable
  public String getCellType(){
    return "COMBOBOX"; //$NON-NLS-1$
  }

  
  @Bindable
  public String getCondition(String objName) {
    throw new RuntimeException("UI does not implement this method");
  }
  
  @Bindable
  public String getCondition(String objName, boolean enforceParameters){
    throw new RuntimeException("UI does not implement this method");
  }

  @Bindable
  public boolean isParameterized() {
    return parameterized;
  }

  @Bindable
  public void setParameterized(boolean parameterized) {
    boolean prevVal = isParameterized();
    this.firePropertyChange("parameterized", prevVal, parameterized);
    this.parameterized = parameterized;
    this.firePropertyChange("defaultDisabled", null, isDefaultDisabled());
    if(!parameterized){
      setDefaultValue("");
    }
  }

  @Bindable
  public boolean isDefaultDisabled(){
    return ! parameterized;
  }

  @Bindable
  public void setDefaultValue(String val){
    String prevVal = this.defaultValue;
    this.defaultValue = val;
    this.firePropertyChange("defaultValue", prevVal, val);
  }
  
  @Bindable
  public String getDefaultValue(){
    return defaultValue;
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
  public void setSelectedAggType(Object o){
    setSelectedAggType((AggType)  o);
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

  @Bindable
  public void setTopMost(boolean topMost) {
    this.topMost = topMost;
  }

  @Bindable
  public boolean isTopMost() {
    return topMost;
  }
  
  @Bindable
  public boolean isValueDisabled(){
    return valueDisabled;
  }
  
  @Bindable
  public void setValueDisabled(boolean disabled){
    boolean prevVal = this.valueDisabled;
    this.valueDisabled = disabled;
    this.firePropertyChange("valueDisabled", prevVal, this.valueDisabled);
    if(disabled){
      setValue(null);
    }
  }
}

