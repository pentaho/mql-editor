package org.pentaho.metadata.editor.models;

import java.util.Arrays;
import java.util.Vector;

import org.pentaho.metadata.ColumnType;
import org.pentaho.metadata.CombinationType;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.Operator;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class UICondition extends XulEventSourceAdapter implements ICondition<UIBusinessColumn> {
  
  
  
  private UIBusinessColumn column;
  private Operator operator = Operator.EQUAL;
  private String value;
  private CombinationType combinationType = CombinationType.AND;
  
  public UICondition(){
    
  }
  

  public UICondition(UIBusinessColumn column, Operator operator, String value){
    this.column = column;
    this.operator = operator;
    this.value = value;
  }
  
  
  public boolean validate(){
    //add type checking by column type
    return true;
  }


  public UIBusinessColumn getColumn() {
    return column;
  }


  public void setColumn(UIBusinessColumn column) {
    this.column = column;
  }


  public Operator getOperator() {
    return operator;
  }


  public void setOperator(Operator operator) {
    this.operator = operator;
  }



  public void setOperator(Object operator) {
    this.operator = (Operator) operator;
  }

  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    this.value = value;
  }


  public CombinationType getCombinationType() {
    return combinationType;
  }


  public void setCombinationType(CombinationType combinationType) {
    this.combinationType = combinationType;
  }
  
  //Binding value comes in as Object unfortunately.
  public void setCombinationType(Object combinationType) {
    this.combinationType = (CombinationType) combinationType;
  }
  

  public String getTableName(){
    return this.column.getTableName();
  }
  
  public void setTableName(String str){
    
  }
  
  public void setColumnName(String str){
    
  }
  
  public String getColumnName(){
    return this.column.getName();
  }
  
  public Vector getComparisons(){
    Vector v = new Vector();

    v.addAll(
        Arrays.asList(
            Operator.values( this.getColumn().getType() == ColumnType.TEXT )
        )
    );
    
    return v;
  }
  public void setComparisons(String str){}
  
  public Vector getCombinations(){
    Vector v = new Vector();
    v.addAll(Arrays.asList(CombinationType.values()));
    return v;
  }
  
  public void setCombinations(String str){}


  public String getCondition(String objName) {
    return this.operator.formatCondition(objName, this.value);
  }
  
  
  
}

  