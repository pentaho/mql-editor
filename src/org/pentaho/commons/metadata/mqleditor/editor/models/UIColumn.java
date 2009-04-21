package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.HashMap;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.beans.Column;

public class UIColumn extends AbstractModelNode<UIColumn> implements MqlColumn<UIBusinessTable> {

  private UIBusinessTable table;

  private ColumnType type;
  
  public UIColumn() {

  }

  public UIColumn(MqlColumn col) {
    this.type = col.getType();
    this.id = col.getId();
    this.name = col.getName();
    this.table = new UIBusinessTable(col.getTable());
  }
  
  public String getTableName() {
    return table.getName();
  }

  public void setTableName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  public UIBusinessTable getTable() {
    return table;
  }

  public ColumnType getType() {

    return type;
  }
  
  public String toString(){
    if(getTable() != null){
      return getTable().getName() + "." + getId(); //$NON-NLS-1$
    }
    return id;
  }
  
}
