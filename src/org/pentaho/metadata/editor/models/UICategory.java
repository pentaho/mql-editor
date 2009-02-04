package org.pentaho.metadata.editor.models;

import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICategory;

public class UICategory extends AbstractModelNode<UIBusinessColumn> implements ICategory<UIBusinessColumn>{
  
  private String name, id;
  
  public UICategory(ICategory<IBusinessColumn> category){
    this.name = category.getName();
    this.id = category.getId();
    
    for(IBusinessColumn col : category.getBusinessColumns()){
      UIBusinessColumn c = new UIBusinessColumn(col);
      this.children.add(c);
    }
  }
  
  public UICategory(String name){
    this.name = name;
  }
  
  public UICategory(String name, List<UIBusinessColumn> columns){
    super(columns);
    this.name = name;
  }
  
  public UICategory(){
    
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

  