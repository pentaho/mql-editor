package org.pentaho.metadata.editor.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.IBusinessTable;

public class UIBusinessTable extends AbstractModelNode<UIBusinessColumn> implements IBusinessTable<UIBusinessColumn>{

  private String name, id;
  
  public UIBusinessTable(){
    
  }
  
  public UIBusinessTable(IBusinessTable<IBusinessColumn> table){
    this.id = table.getId();
    this.name = table.getName();


    for(IBusinessColumn col : table.getBusinessColumns()){
      UIBusinessColumn c = new UIBusinessColumn(col);
      this.children.add(c);
    }
  }
  
  public UIBusinessTable(String name){
    this.name = name;
  }
  
  public UIBusinessTable(String name, List<UIBusinessColumn> columns){
    super(columns);
    this.name = name;
  }

  public List<UIBusinessColumn> getBusinessColumns() {
    return this.getChildren();  
  }

  public void setBusinessColumns(List<UIBusinessColumn> columns) {
    this.children = columns;
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
  
  
  
}

  