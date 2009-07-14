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
    
    //only do this if it's default, otherwise you may override a user specified value
    if(this.operator == Operator.EQUAL){ 
      this.setOperator( (column.getType() == ColumnType.TEXT) ?  Operator.EXACTLY_MATCHES :Operator.EQUAL );
    }
  }


  public Operator getOperator() {
    return operator;
  }


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

  public void setOperator(Object operator) {
    setOperator((Operator) operator);
  }

  public String getValue() {
    return value;
  }


  public void setValue(String value) {
    String prevVal = this.value;
    this.value = value;
    this.firePropertyChange("value", prevVal, this.value);
    
    this.setParameterized(value != null && value.contains("{") && value.contains("}"));
  }


  public CombinationType getCombinationType() {
    if(isTopMost()){
      return null;
    }
    return combinationType;
  }


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
  
  public String getCellType(){
    if(isTopMost()){
      return "LABEL"; //$NON-NLS-1$
    }
    return "COMBOBOX"; //$NON-NLS-1$
  }


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


  public void setSelectedAggType(AggType aggType){
    this.selectedAggType = aggType;
  }
  
  public AggType getSelectedAggType(){
    return this.selectedAggType;
  }

  public void setSelectedAggType(Object o){
    setSelectedAggType((AggType)  o);
  }

  public List<AggType> getAggTypes() {
    return this.column.getAggTypes();
  }
  public void setAggTypes(List<AggType> ignored){
    
  }

  public Vector getBindingAggTypes(){
    return column.getBindingAggTypes();
  }

  public void setTopMost(boolean topMost) {
    this.topMost = topMost;
  }

  public boolean isTopMost() {
    return topMost;
  }
  
  public boolean isValueDisabled(){
    return valueDisabled;
  }
  
  public void setValueDisabled(boolean disabled){
    boolean prevVal = this.valueDisabled;
    this.valueDisabled = disabled;
    this.firePropertyChange("valueDisabled", prevVal, this.valueDisabled);
    if(disabled){
      setValue(null);
    }
  }
}

  