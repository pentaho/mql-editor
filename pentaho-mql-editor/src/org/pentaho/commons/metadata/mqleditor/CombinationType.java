package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

/**
 * Defines the logical relationship between two {@see MqlCondition}s
 *
 */
public enum CombinationType implements Serializable {
  AND("AND"), OR("OR"), AND_NOT("AND NOT"), OR_NOT("OR NOT");
  
  private String toStringVal;
  private CombinationType(String val){
    toStringVal = val;
  }
  
  @Override
  public String toString() {
    return toStringVal;
  }
  
}

  