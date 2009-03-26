package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

/**
 * Represents a Metadata Column object
 *
 * @param <T> implementation of a MqlBusinessTable
 */
public interface MqlColumn<T extends MqlBusinessTable> extends Serializable {

  public T getTable();

  public ColumnType getType();

  public String getId();

  public String getName();

}
