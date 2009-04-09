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
  
  public String formatCondition(String objectName, String value, boolean forceString){
    String retVal = "";
    switch(this){
      case EXACTLY_MATCHES:
        retVal += objectName+" = \"" + value + "\"";
        break;
      case CONTAINS:
        retVal += "LIKE("+objectName+"; \"%" + value + "%\")";
        break;
      case DOES_NOT_CONTAIN:
        retVal += "NOT(LIKE("+objectName+"; \"%" + value + "%\"))";
        break;
      case BEGINS_WITH:
        retVal += "LIKE("+objectName+"; \"" + value + "%\")";
        break;
      case ENDS_WITH:
        retVal += "LIKE("+objectName+"; \"%" + value + "\")";
        break;
      case IS_EMPTY:
      case IS_NULL:
        retVal += "ISNA("+objectName+"; NULL())";
        break;
      case IS_NOT_NULL:
      case IS_NOT_EMPTY:
        retVal += "NOT(ISNA("+objectName+"; NULL()))";
        break;
      default:
        retVal = objectName + " " + this.toString();
        if(this.requiresValue){
          if(this.stringType || forceString){
            retVal += "\""+value+"\"";
          } else {
            retVal += value;
          }
        }
        break;
    }
    return retVal;
    
  }
}
