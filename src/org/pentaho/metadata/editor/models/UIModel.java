package org.pentaho.metadata.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.ICategory;
import org.pentaho.metadata.IModel;

public class UIModel extends AbstractModelNode<UICategory> implements IModel<UICategory>{
  
  private List<UICategory> categories = new ArrayList<UICategory>();
  private String name, id;
  
  public UIModel(IModel<ICategory> model){
    this.id = model.getId();
    this.name = model.getName();
    
    for(ICategory cat : model.getCategories()){
      this.children.add(new UICategory(cat));
    }
  }
  
  public UIModel(){
    
  }
  
  public UIModel(List<UICategory> categories, String name){
    super(categories);
    this.name = name;
  }

  public List<UICategory> getCategories() {
    return this.getChildren();  
  }

  public void setCategories(List<UICategory> categories) {
    this.categories = categories;
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

  