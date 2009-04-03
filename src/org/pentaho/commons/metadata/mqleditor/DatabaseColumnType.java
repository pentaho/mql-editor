package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.sql.Types;

/**
 * DatabaseColumnType represents the data type
 *
 */
public enum DatabaseColumnType implements Serializable{
  BOOLEAN(Types.BOOLEAN, "BOOLEAN"), DATE(Types.DATE, "DATE"), DECIMAL(Types.DECIMAL, "DECIMAL"), DOUBLE(Types.DOUBLE, "DOUBLE"), INTEGER(Types.INTEGER, "INTEGER"), NUMERIC(Types.NUMERIC, "NUMERIC"), TIMESTAMP(Types.TIMESTAMP, "TIMESTAMP") , VARCHAR(Types.VARCHAR, "VARCHAR") ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

  private final int colType;
  private String name;
  DatabaseColumnType(int colType, String name) {
    this.colType = colType;
    this.name = name;
  }

  public String toString() {
    return name;
  }
  
  public int getType() {
    return colType;
  }
}