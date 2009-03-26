package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

public interface MqlCondition<T extends MqlColumn> extends Serializable{

  public boolean validate();

  public T getColumn();

  public void setColumn(T column);

  public Operator getOperator();

  public void setOperator(Operator operator);

  public String getValue();

  public void setValue(String value);

  public CombinationType getCombinationType() ;

  public void setCombinationType(CombinationType combinationType);
  
  /**
   * Returns the comparision plus value, i.e "= 'Atlanta'"
   */
  public String getCondition(String objName);

}
