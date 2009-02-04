package org.pentaho.metadata.editor.models;

import java.util.List;

import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;

public class UIDomain extends AbstractModelNode<UIModel> implements IDomain<UIModel>{
  
  private String id, name;
  
  public UIDomain(IDomain<IModel> domain){
    this.name = domain.getName();
    this.id = domain.getId();
    for(IModel model : domain.getModels()){
      this.children.add(new UIModel(model));
    }
  }
  public UIDomain(String name){
    this.name = name;
  }
  
  public UIDomain(List<UIModel> models, String name){
    super(models);
    this.name = name;
  }

  public List<UIModel> getModels() {
    return this.getChildren();
  }

  public void setModels(List<UIModel> models) {
    this.children = models;
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

  