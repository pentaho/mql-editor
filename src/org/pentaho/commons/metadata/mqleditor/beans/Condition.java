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
package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.Operator;

public class Condition implements MqlCondition {

  private Column column;
  private Operator operator = Operator.EQUAL;
  private String value;
  private CombinationType comboType = CombinationType.AND;
  private boolean parameterized;
  private String defaultValue;
  private AggType selectedAggType;

  public Condition(){
    
  }
  
  public Column getColumn() {
    return this.column;    
  }

  public CombinationType getCombinationType() {
    return this.comboType;  
  }

  public Operator getOperator() {
    return this.operator;
  }

  public String getValue() {
    return this.value;
  }

  public void setColumn(Column column) {
    this.column = column;  

    if(column.getType() == ColumnType.TEXT && operator == Operator.EQUAL){
      operator = Operator.EXACTLY_MATCHES;
    }
  }

  public void setCombinationType(CombinationType combinationType) {
    this.comboType = combinationType;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }

  public void setValue(String value) {
   this.value = value;   
  }

  public boolean validate() {
    return true;   
  }

  public String getCondition(String objName) {
    return getCondition(objName, true);
  }

  public String getCondition(String objName, boolean enforceParameters){
    String val = this.value;
    
    // Date is a special case where we craft a formula function.
    if(this.column.getType() == ColumnType.DATE){
      if(this.isParameterized() && enforceParameters){
        // Due to the fact that the value of a Date is a forumula function, the tokenizing of
        // the value needs to happen here instead of letting the Operator class handle it.
        val = "DATEVALUE("+"[param:"+value.replaceAll("[\\{\\}]","")+"]"+")";
        return this.operator.formatCondition(objName, val, false);
      } else {  
        val = "DATEVALUE(\""+val+"\")";
      }
    }
    return this.operator.formatCondition(objName, val, this.isParameterized() && enforceParameters);
  }

  public boolean isParameterized() {
    return parameterized;
  }

  public void setParameterized(boolean parameterized) {
   this.parameterized = parameterized;   
  }

  public void setDefaultValue(String val){
    this.defaultValue = val;
  }
  
  public String getDefaultValue(){
    return this.defaultValue;
  }

  public void setSelectedAggType(AggType aggType){
    this.selectedAggType = aggType;
  }
  
  public AggType getSelectedAggType(){
    return this.selectedAggType;
  }

}
