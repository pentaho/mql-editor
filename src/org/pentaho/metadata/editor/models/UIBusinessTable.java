package org.pentaho.metadata.editor.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.metadata.IBusinessTable;
import org.pentaho.metadata.beans.BusinessColumn;
import org.pentaho.metadata.beans.BusinessTable;

public class UIBusinessTable extends AbstractModelNode<UIBusinessColumn> implements IBusinessTable<UIBusinessColumn>{

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

    for(BusinessColumn col : table.getBusinessColumns()){
      UIBusinessColumn c = UIBusinessColumn.wrap(col);
      this.children.add(c);
    }
  }
 
  public List<UIBusinessColumn> getBusinessColumns() {
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

  