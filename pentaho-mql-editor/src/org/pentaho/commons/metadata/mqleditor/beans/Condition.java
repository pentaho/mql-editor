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
    String val = this.value;
    
    // Date is a special case where we craft a formula function.
    if(this.column.getType() == ColumnType.DATE){
      if(this.isParameterized()){
        // Due to the fact that the value of a Date is a forumula function, the tokenizing of
        // the value needs to happen here instead of letting the Operator class handle it.
        val = "DATEVALUE("+"[param:"+value.replaceAll("[\\{\\}]","")+"]"+")";
        return this.operator.formatCondition(objName, val, false);
      } else {  
        val = "DATEVALUE(\""+val+"\")";
      }
    }
    
    return this.operator.formatCondition(objName, val, this.isParameterized());
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
