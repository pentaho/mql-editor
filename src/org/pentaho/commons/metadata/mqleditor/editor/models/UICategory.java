package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlCategory;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Category;

public class UICategory extends AbstractModelNode<UIColumn> implements MqlCategory<UIColumn> {
  
  public UICategory(Category category){
    this.id = category.getId();
    this.name = category.getName();
    
    for(Column col : category.getBusinessColumns()){
      UIColumn c = new UIColumn(col);
      this.children.add(c);
    }
  }
  
  public UICategory(){
    
  }

  public List<UIColumn> getBusinessColumns() {
    return this.getChildren(); 
  }
  
}

  