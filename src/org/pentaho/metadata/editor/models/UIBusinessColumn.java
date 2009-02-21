package org.pentaho.metadata.editor.models;

import java.util.HashMap;
import java.util.Map;

import org.pentaho.metadata.ColumnType;
import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.beans.BusinessColumn;

public class UIBusinessColumn extends AbstractModelNode<UIBusinessColumn> implements IBusinessColumn<UIBusinessTable> {

  private UIBusinessTable table;

  private ColumnType type;

  private String name, id;
  
  private BusinessColumn bean;
  
  // The supplied Beans are a Graph of objects. In order to maintain those relationships, we track
  // previously created objects in order to serve the same objects when needed.
  private static Map<BusinessColumn, UIBusinessColumn> wrappedCols = new HashMap<BusinessColumn, UIBusinessColumn>();
  
  public static UIBusinessColumn wrap(BusinessColumn col){
    if(wrappedCols.containsKey(col)){
      return wrappedCols.get(col);
    }
    UIBusinessColumn c = new UIBusinessColumn(col);
    wrappedCols.put(col, c);
    return c;
  }

  public UIBusinessColumn() {

  }

  private UIBusinessColumn(BusinessColumn col) {
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
  
  public BusinessColumn getBean(){
    return bean;
  }
  
}
