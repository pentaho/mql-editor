package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class UICondition extends XulEventSourceAdapter implements MqlCondition<UIColumn> {
  
  private Condition bean;
  
  public UICondition(){
    bean = new Condition();
  }
  
  
  //The supplied Beans are a Graph of objects. In order to maintain those relationships, we track
  // previously created objects in order to serve the same objects when needed.
  private static Map<Condition, UICondition> wrappedConditions = new HashMap<Condition, UICondition>();
  
  public static UICondition wrap(Condition condition){
    if(wrappedConditions.containsKey(condition)){
      return wrappedConditions.get(condition);
    }
    UICondition c = new UICondition(condition);
    wrappedConditions.put(condition, c);
    return c;
  }

  public UICondition(UIColumn column, Operator operator, String value){
    this();
    bean.setColumn(column.getBean());
    bean.setOperator(operator);
    bean.setValue(value);
    
  }
  
  private UICondition(Condition bean){
    this.bean = bean;
  }
  
  public boolean validate(){
    //add type checking by column type
    return true;
  }


  public UIColumn getColumn() {
    return UIColumn.wrap(bean.getColumn());
  }

  

  public void setColumn(UIColumn column) {
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

  