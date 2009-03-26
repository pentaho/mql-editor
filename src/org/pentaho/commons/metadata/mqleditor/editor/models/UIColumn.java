package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.HashMap;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.beans.Column;

public class UIColumn extends AbstractModelNode<UIColumn> implements MqlColumn<UIBusinessTable> {

  private UIBusinessTable table;

  private ColumnType type;

  private String name, id;
  
  private Column bean;
  
  // The supplied Beans are a Graph of objects. In order to maintain those relationships, we track
  // previously created objects in order to serve the same objects when needed.
  private static Map<Column, UIColumn> wrappedCols = new HashMap<Column, UIColumn>();
  
  public static UIColumn wrap(Column col){
    if(wrappedCols.containsKey(col)){
      return wrappedCols.get(col);
    }
    UIColumn c = new UIColumn(col);
    wrappedCols.put(col, c);
    return c;
  }

  public UIColumn() {

  }

  private UIColumn(Column col) {
    this.bean = col;
    this.table = UIBusinessTable.wrap(col.getTable());
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

    return bean.getType();
  }

  public String getId() {
    return bean.getId();
  }

  public String getName() {
   return bean.getName();   
  }

  public void setName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  public String toString(){
    if(bean.getTable() != null){
      return bean.getTable().getName() + "." + bean.getId(); //$NON-NLS-1$
    }
    
    return id;
  }
  
  public Column getBean(){
    return bean;
  }
  
}
