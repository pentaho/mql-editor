package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.IModel;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Model;

public class UIModel extends AbstractModelNode<UICategory> implements IModel<UICategory>{
  
  private List<UICategory> categories = new ArrayList<UICategory>();

  private Model bean;

  // The supplied Beans are a Graph of objects. In order to maintain those relationships, we track
  // previously created objects in order to serve the same objects when needed.
  private static Map<Model, UIModel> wrappedModels = new HashMap<Model, UIModel>();
  
  public static UIModel wrap(Model model){
    if(wrappedModels.containsKey(model)){
      return wrappedModels.get(model);
    }
    UIModel m = new UIModel(model);
    wrappedModels.put(model, m);
    return m;
  }
  
  private UIModel(Model model){
    this.bean = model;
    
    for(Category cat : model.getCategories()){
      this.children.add(UICategory.wrap(cat));
    }
  }
  
  public UIModel(){
    bean = new Model();
  }
  
  public List<UICategory> getCategories() {
    return this.getChildren();  
  }

  public String getId() {
    return bean.getId();
  }

  public String getName() {
   return bean.getName();   
  }
  
}

  