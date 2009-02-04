package org.pentaho.metadata;

import java.io.Serializable;
import java.sql.Date;

public enum ColumnType implements Serializable{
  NUMERIC(Integer.class), TEXT(String.class), FLOAT(Float.class), BOOLEAN(Boolean.class), DATE(Date.class);

  private final Class clazz;

  ColumnType(Class cls) {
    this.clazz = cls;
  }

  boolean validate(Object val) {
    return clazz.getClass().equals(clazz);
  }
}