package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;

public class UIDomain extends AbstractModelNode<UIModel> implements IDomain<UIModel>{
  
  private Domain bean;
  
  public UIDomain(){
    this.bean = new Domain();
  }
  
  public UIDomain(Domain domain){
    this.bean = domain;
    for(Model model : domain.getModels()){
      this.children.add(UIModel.wrap(model));
    }
  }
  
  public List<UIModel> getModels() {
    return this.getChildren();
  }

  public void setModels(List<UIModel> models) {
    this.children = models;
  }

  public String getId() {
    return bean.getId();
  }

  public String getName() {
   return bean.getName();
  }

  public Domain getBean(){
    return bean;
  }
  
}

  