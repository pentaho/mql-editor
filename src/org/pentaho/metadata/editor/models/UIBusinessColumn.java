package org.pentaho.metadata.editor.models;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.IBusinessTable;
import org.pentaho.metadata.ColumnType;

public class UIBusinessColumn extends AbstractModelNode<UIBusinessColumn> implements IBusinessColumn<UIBusinessTable> {

  private UIBusinessTable table;

  private ColumnType type;

  private String name, id;
  
  public UIBusinessColumn() {

  }

  public UIBusinessColumn(IBusinessColumn col) {
    this.id = col.getId();
    this.name = col.getName();
    this.type = col.getType();
    this.table = new UIBusinessTable(col.getTable());
  }

  public UIBusinessColumn(String name, UIBusinessTable table, ColumnType type) {
    this.type = type;
    this.name = name;
    this.table = table;
  }

  public String getTableName() {
    return table.getName();
  }

  public void setTableName(String name) {
  }

  public UIBusinessTable getTable() {

    return table;
  }

  public void setTable(UIBusinessTable table) {

    this.table = table;
  }

  public ColumnType getType() {

    return type;
  }

  public void setType(ColumnType type) {

    this.type = type;
  }

  public String getId() {
    return id;
  }

  public String getName() {
   return name;   
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String toString(){
    if(table != null){
      return table.getName() + "." + id; //$NON-NLS-1$
    }
    
    return id;
  }
  
}
