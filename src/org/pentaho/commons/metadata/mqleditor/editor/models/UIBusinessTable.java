package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlBusinessTable;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessTable;
import org.pentaho.commons.metadata.mqleditor.beans.Column;

public class UIBusinessTable extends AbstractModelNode<UIColumn> implements MqlBusinessTable<UIColumn> {

  private BusinessTable bean;

  // The supplied Beans are a Graph of objects. In order to maintain those relationships, we track
  // previously created objects in order to serve the same objects when needed.
  private static Map<BusinessTable, UIBusinessTable> wrappedTables = new HashMap<BusinessTable, UIBusinessTable>();
  
  public static UIBusinessTable wrap(BusinessTable table){
    if(wrappedTables.containsKey(table)){
      return wrappedTables.get(table);
    }
    UIBusinessTable t = new UIBusinessTable(table);
    wrappedTables.put(table, t);
    return t;
  }
  
  public UIBusinessTable(){
    
  }
  
  private UIBusinessTable(BusinessTable table){
    this.bean = table;

    for(Column col : table.getBusinessColumns()){
      UIColumn c = UIColumn.wrap(col);
      this.children.add(c);
    }
  }
 
  public List<UIColumn> getBusinessColumns() {
    return this.getChildren();  
  }

  public String getId() {
    return bean.getId();
  }

  public String getName() {
   return bean.getName();   
  }
  
  public BusinessTable getBean(){
    return bean;
  }
  
}

  