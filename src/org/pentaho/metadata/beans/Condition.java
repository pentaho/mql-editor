package org.pentaho.metadata.beans;

import org.pentaho.metadata.CombinationType;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.Operator;

public class Condition implements ICondition<BusinessColumn> {

  private BusinessColumn column;
  private Operator operator;
  private String value;
  private CombinationType comboType;
  
  public BusinessColumn getColumn() {
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

  public void setColumn(BusinessColumn column) {
    this.column = column;  
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

}
