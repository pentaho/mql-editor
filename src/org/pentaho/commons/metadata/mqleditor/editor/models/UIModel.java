package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlModel;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Model;

public class UIModel extends AbstractModelNode<UICategory> implements MqlModel<UICategory> {
  
  private List<UICategory> categories = new ArrayList<UICategory>();

  
  public UIModel(Model model){
    this.id = model.getId();
    this.name = model.getName();
    
    for(Category cat : model.getCategories()){
      this.children.add(new UICategory(cat));
    }
  }
  
  public UIModel(){
    
  }
  
  public List<UICategory> getCategories() {
    return this.getChildren();  
  }
  
}

  