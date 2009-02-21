package org.pentaho.metadata.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.beans.BusinessColumn;

public class Columns extends AbstractModelNode<UIBusinessColumn>{

    
  public Columns(){
  }
  
  public Columns(List<UIBusinessColumn> conditions){
    super(conditions);
  }
  
  public List<BusinessColumn> getBeanCollection(){
    List<BusinessColumn> cols = new ArrayList<BusinessColumn>();
    
    for(UIBusinessColumn c : this.getChildren()){
      cols.add(c.getBean());
    }
    return cols;
  }
}

