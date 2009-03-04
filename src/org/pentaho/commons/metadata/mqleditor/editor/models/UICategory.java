package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.ICategory;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessColumn;
import org.pentaho.commons.metadata.mqleditor.beans.Category;

public class UICategory extends AbstractModelNode<UIBusinessColumn> implements ICategory<UIBusinessColumn>{
  
  private Category bean;

  // The supplied Beans are a Graph of objects. In order to maintain those relationships, we track
  // previously created objects in order to serve the same objects when needed.
  private static Map<Category, UICategory> wrappedCats = new HashMap<Category, UICategory>();
  
  public static UICategory wrap(Category cat){
    if(wrappedCats.containsKey(cat)){
      return wrappedCats.get(cat);
    }
    UICategory c = new UICategory(cat);
    wrappedCats.put(cat, c);
    return c;
  }
  
  private UICategory(Category category){
    this.bean = category;
    for(BusinessColumn col : category.getBusinessColumns()){
      UIBusinessColumn c = UIBusinessColumn.wrap(col);
      this.children.add(c);
    }
  }
  
  public UICategory(){
    
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
  
}

  