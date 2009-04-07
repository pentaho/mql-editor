package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

/**
 * DatabaseColumnType represents the data type
 *
 */
public enum DatabaseColumnType implements Serializable{
  BOOLEAN(16, "BOOLEAN"), DATE(91, "DATE"), DECIMAL(3, "DECIMAL"), DOUBLE(8, "DOUBLE"), INTEGER(4, "INTEGER"), NUMERIC(2, "NUMERIC"), TIMESTAMP(93, "TIMESTAMP") , VARCHAR(12, "VARCHAR") ; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$

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