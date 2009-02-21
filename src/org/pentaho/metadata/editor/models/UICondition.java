package org.pentaho.metadata.editor.models;

import java.util.Arrays;
import java.util.Vector;

import org.pentaho.metadata.ColumnType;
import org.pentaho.metadata.CombinationType;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.Operator;
import org.pentaho.metadata.beans.Condition;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class UICondition extends XulEventSourceAdapter implements ICondition<UIBusinessColumn> {
  
  private Condition bean;
  
  public UICondition(){
    bean = new Condition();
  }
  

  public UICondition(UIBusinessColumn column, Operator operator, String value){
    this();
    bean.setColumn(column.getBean());
    bean.setOperator(operator);
    bean.setValue(value);
    
  }
  
  
  public boolean validate(){
    //add type checking by column type
    return true;
  }


  public UIBusinessColumn getColumn() {
    return UIBusinessColumn.wrap(bean.getColumn());
  }

  

  public void setColumn(UIBusinessColumn column) {
    bean.setColumn(column.getBean());
  }


  public Operator getOperator() {
    return bean.getOperator();
  }


  public void setOperator(Operator operator) {
    bean.setOperator(operator);
  }

  public void setOperator(Object operator) {
    setOperator((Operator) operator);
  }

  public String getValue() {
    return bean.getValue();
  }


  public void setValue(String value) {
    bean.setValue(value);
  }


  public CombinationType getCombinationType() {
    return bean.getCombinationType();
  }


  public void setCombinationType(CombinationType combinationType) {
    bean.setCombinationType(combinationType);
  }
  
  //Binding value comes in as Object unfortunately.
  public void setCombinationType(Object combinationType) {
    setCombinationType((CombinationType) combinationType);
  }

  public String getTableName(){
    return bean.getColumn().getTable().getName();
  }

  public void setTableName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  public String getColumnName(){
    return bean.getColumn().getName();
  }

  public void setColumnName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
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
    return bean.getCondition(objName);
  }
  
  public Condition getBean(){
    return bean;
  }
  
  
}

  