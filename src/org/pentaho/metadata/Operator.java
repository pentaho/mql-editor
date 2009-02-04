package org.pentaho.metadata;

import java.io.Serializable;

public enum Operator implements Serializable{
  GREATER_THAN(">"), LESS_THAN("<"), EQUAL("="), GREATOR_OR_EQUAL(">="), LESS_OR_EQUAL("<=");

  private String strVal;

  Operator(String str) {
    this.strVal = str;
  }

  public String toString() {
    return strVal;
  }
}
