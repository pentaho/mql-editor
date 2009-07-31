package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

public interface MqlCondition extends Serializable{

  public boolean validate();

  public MqlColumn getColumn();

  public Operator getOperator();

  /**
   * @return if isParameterized() then the name of the parameter whose value will be substituted before query execution, else the literal value
   */
  public String getValue();

  public CombinationType getCombinationType() ;
  
  /**
   * Returns the comparision plus value, i.e "= 'Atlanta'" 
   * 
   * @return a string formatted to support parameters
   */
  public String getCondition(String objName);

  /**
   * Returns the comparision plus value, i.e "= 'Atlanta'" 
   * 
   * @param enforceParam flag to format the Condition for  parameters
   * @return a string formatted to support parameters based on the enforceParams flag
   */
  public String getCondition(String objName, boolean enforceParams);
  
  /**
   * Value in this condition is not static, but rather supplied for each execution of this query.
   * 
   * @return true if value denotes parameter name rather than a literal value
   */
  public boolean isParameterized();
  
  public String getDefaultValue();

  public AggType getSelectedAggType();
}
