package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Operator is used in the definition of a @see MqlCondition 
 */
public enum Operator implements Serializable{
  GREATER_THAN(">", false, true), 
  LESS_THAN("<", false, true), 
  EQUAL("=", false, true), 
  GREATOR_OR_EQUAL(">=", false, true), 
  LESS_OR_EQUAL("<=", false, true), 
  IS_NULL("is null", false, false), 
  IS_NOT_NULL("is not null", false, false),
  
  EXACTLY_MATCHES("exactly matches", true, true), 
  CONTAINS("contains", true, true), 
  DOES_NOT_CONTAIN("does not contain", true, true), 
  BEGINS_WITH("begins with", true, true), 
  ENDS_WITH("ends with", true, true), 
  IS_EMPTY("is null", true, false), 
  IS_NOT_EMPTY("is not null", true, false);

  private String strVal;
  private boolean stringType;
  private boolean requiresValue;

  Operator(String str, boolean stringType, boolean requiresValue) {
    this.strVal = str;
    this.stringType = stringType;
    this.requiresValue = requiresValue;
  }

  public String toString() {
    return strVal;
  }
  
  public static Operator parse(String val){
    if(val.equals(">")){
      return Operator.GREATER_THAN;
    } else if(val.equals(">=")){
      return Operator.GREATOR_OR_EQUAL;
    } else if(val.equals("=")){
      return Operator.EQUAL;
    } else if(val.equals("<")){
      return Operator.LESS_THAN;
    } else if(val.equals("<=")){
      return Operator.LESS_OR_EQUAL;
    } else if(val.equals("exactly matches")){
      return Operator.EXACTLY_MATCHES;
    } else if(val.equals("contains")){
      return Operator.CONTAINS;
    } else if(val.equals("does not contain")){
      return Operator.DOES_NOT_CONTAIN;
    } else if(val.equals("begins with")){
      return Operator.BEGINS_WITH;
    } else if(val.equals("ends with")){
      return Operator.ENDS_WITH;
    } else if(val.equals("is null")){
      return Operator.IS_NULL;
    }  else if(val.equals("is not null")){
      return Operator.IS_NOT_NULL;
    } 
    
    
    return Operator.EQUAL;
  }
  
  public boolean requiresValue(){
    return requiresValue;
  }
  
  public boolean isStringType(){
    return this.stringType;
  }
  
  /**
   * Returns an array of types separated by whether or not they're string types
   * @param stringType
   * @return array of Operators
   */
  public static Operator[] values(boolean stringType){
    Operator[] vals = Operator.values();
    List<Operator> ops = new ArrayList<Operator>();
    for(int i=0; i < vals.length; i++){
      if(vals[i].isStringType() == stringType){
        ops.add(vals[i]); 
      }
    }
    return ops.toArray(new Operator[]{});
  }
  
  public String formatCondition(String objectName, String value, boolean parameterized){
    
    if(parameterized){
     value = "[param:"+value.replaceAll("[\\{\\}]","")+"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    } else if (this.stringType) {
      value = "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$ 
    }
    String retVal = ""; //$NON-NLS-1$
    
    switch(this){
      case EXACTLY_MATCHES:
        retVal += objectName+" = " + value; //$NON-NLS-1$
        break;
      case CONTAINS:
        retVal += "CONTAINS("+objectName+";"+value+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case DOES_NOT_CONTAIN:
        retVal += "NOT(CONTAINS("+objectName+";"+value+"))"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case BEGINS_WITH:
        retVal += "BEGINSWITH("+objectName+";"+value+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case ENDS_WITH:
        retVal += "ENDSWITH("+objectName+";"+value+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case IS_EMPTY:
      case IS_NULL:
        retVal += "ISNA("+objectName+")"; //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case IS_NOT_NULL:
      case IS_NOT_EMPTY:
        retVal += "NOT(ISNA("+objectName+"))"; //$NON-NLS-1$ //$NON-NLS-2$
        break;
      default:
        retVal = objectName + " " + this.toString(); //$NON-NLS-1$
        if(this.requiresValue){
          retVal += value;
        }
        break;
    }
    return retVal;
    
  }
}
