package org.pentaho.metadata;

import java.io.Serializable;

import org.pentaho.metadata.editor.models.UIBusinessTable;

public interface IBusinessColumn<T extends IBusinessTable> extends Serializable {

  public T getTable();

  public ColumnType getType();

  public String getId();

  public String getName();

}
