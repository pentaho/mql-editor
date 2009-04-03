package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.sql.Date;

/**
 * ColumnType represents the data type of a {@see MqlColumn}
 *
 */
public enum ColumnType implements Serializable{
  NUMERIC(Integer.class, "Integer"), TEXT(String.class, "Text"), FLOAT(Float.class,"Float"), BOOLEAN(Boolean.class, "Boolean"), DATE(Date.class, "Date");

  private final Class clazz;

  private final String name;
  ColumnType(Class cls, String name) {
    this.clazz = cls;
    this.name = name;
  }

  boolean validate(Object val) {
    return clazz.getClass().equals(clazz);
  }
  
  public String toString(){
    return name;
  }
}