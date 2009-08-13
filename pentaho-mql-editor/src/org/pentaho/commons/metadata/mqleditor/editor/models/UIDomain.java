package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UIDomain extends AbstractModelNode<UIModel> implements MqlDomain {
  
  private String id = "default";
  private String name;
  
  public UIDomain(){
  }
  
  public UIDomain(Domain domain){
    if(domain.getId() != null){
      this.id = domain.getId();
    }
    this.name = domain.getName();
    
    for(Model model : domain.getModels()){
      this.children.add(new UIModel(model));
    }
  }

  @Bindable
  public String getId() {
    return id;
  }
  
  @Bindable
  public void setId(String id){
    this.id = id;
  }

  @Bindable
  public String getName() {
    return this.name;
  }
  
  @Bindable
  public void setName(String name){
    this.name = name;
  }
  
  @Bindable
  public List<UIModel> getModels() {
    return this.getChildren();
  }

  @Bindable
  public void setModels(List<UIModel> models) {
    this.children = models;
  }
  
}

  