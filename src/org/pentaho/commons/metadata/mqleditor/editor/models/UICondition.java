package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.*;

import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class UICondition extends XulEventSourceAdapter implements MqlCondition<UIColumn> {
  
  private String defaultValue;
  private UIColumn column;
  private Operator operator = Operator.EQUAL;
  private String value;
  private CombinationType combinationType = CombinationType.AND;
  private boolean parameterized = false;
  
  public UICondition(){
  }
  
  public UICondition(UIColumn column, Operator operator, String value){
    this();
    this.column = column;
    this.operator = operator;
    this.value = value;
    
  }
  
  public UICondition(Condition bean){
    this.column = new UIColumn(bean.getColumn());
    this.defaultValue = bean.getDefaultValue();
    this.operator = bean.getOperator();
    this.value = bean.getValue();
    this.combinationType = bean.getCombinationType();
    column = new UIColumn(bean.getColumn());
  }
  
  public boolean validate(){
    //add type checking by column type
    return true;
  }


  public UIColumn getColumn() {
    return column;
  }

  

  public void setColumn(UIColumn column) {
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
    this.setParameterized(value != null && value.contains("{") && value.contains("}"));
  }


  public CombinationType getCombinationType() {
    return combinationType;
  }


  public void setCombinationType(CombinationType combinationType) {
    this.combinationType = combinationType;
  }
  
  //Binding value comes in as Object unfortunately.
  public void setCombinationType(Object combinationType) {
    setCombinationType((CombinationType) combinationType);
  }

  public String getTableName(){
    return this.column.getTable().getName();
  }

  public void setTableName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  public String getColumnName(){
    return column.getName();
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
    return this.operator.formatCondition(objName, this.value, (this.isParameterized()));
  }

  public boolean isParameterized() {
    return parameterized;
  }

  public void setParameterized(boolean parameterized) {
    boolean prevVal = isParameterized();
    this.firePropertyChange("parameterized", prevVal, parameterized);
    this.parameterized = parameterized;
    this.firePropertyChange("defaultDisabled", null, isDefaultDisabled());
    if(!parameterized){
      setDefaultValue("");
    }
  }

  public boolean isDefaultDisabled(){
    return ! parameterized;
  }

  public void setDefaultValue(String val){
    String prevVal = this.defaultValue;
    this.defaultValue = val;
    this.firePropertyChange("defaultValue", prevVal, val);
  }
  
  public String getDefaultValue(){
    return defaultValue;
  }

  private List<String> availableFilters = new ArrayList<String>();
  public Vector getAvailableFilters(){
    return new Vector(availableFilters);
  }

  public void setAvailableFilters(List<String> filters){
    this.firePropertyChange("availableFilters", this.availableFilters, filters);
    this.availableFilters = filters;

  }

}

  