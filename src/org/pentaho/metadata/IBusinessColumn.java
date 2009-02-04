package org.pentaho.metadata;

import java.io.Serializable;

import org.pentaho.metadata.editor.models.UIBusinessTable;

public interface IBusinessColumn<T extends IBusinessTable> extends Serializable {

  public T getTable();

  public void setTable(T table);

  public ColumnType getType();

  public void setType(ColumnType type);

  public String getId();

  public void setId(String id);

  public String getName();

  public void setName(String name);

}
