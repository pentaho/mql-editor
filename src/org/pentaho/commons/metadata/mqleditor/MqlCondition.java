package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

public interface MqlCondition<T extends MqlColumn> extends Serializable{

  public boolean validate();

  public T getColumn();

  public void setColumn(T column);

  public Operator getOperator();

  public void setOperator(Operator operator);

  /**
   * @return if isParameterized() then the name of the parameter whose value will be substituted before query execution, else the literal value
   */
  public String getValue();

  public void setValue(String value);

  public CombinationType getCombinationType() ;

  public void setCombinationType(CombinationType combinationType);
  
  /**
   * Returns the comparision plus value, i.e "= 'Atlanta'"
   */
  public String getCondition(String objName);

  /**
   * Value in this condition is not static, but rather supplied for each execution of this query.
   * 
   * @return true if value denotes parameter name rather than a literal value
   */
  public boolean isParameterized();

  public void setParameterized(boolean parameterized);
}
