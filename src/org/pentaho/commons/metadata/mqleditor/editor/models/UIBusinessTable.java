package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlBusinessTable;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessTable;

public class UIBusinessTable extends AbstractModelNode<UIColumn> implements MqlBusinessTable<UIColumn> {
  
  private String id, name;
  
  public UIBusinessTable(){
    
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id){
    this.id = id;
  }

  public String getName() {
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }

  public UIBusinessTable(MqlBusinessTable<Column> table){
    this.id = table.getId();
    this.name = table.getName();
    
    for(Column col : table.getBusinessColumns()){
      UIColumn c = new UIColumn(col);
      this.children.add(c);
    }
  }
 
  public List<UIColumn> getBusinessColumns() {
    return this.getChildren();  
  }

}

  