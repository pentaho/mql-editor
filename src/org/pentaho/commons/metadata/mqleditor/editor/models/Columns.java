package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.Column;

public class Columns extends AbstractModelNode<UIColumn>{

    
  public Columns(){
  }
  
  public Columns(List<UIColumn> conditions){
    super(conditions);
  }
  
  public List<Column> getBeanCollection(){
    List<Column> cols = new ArrayList<Column>();
    
    for(UIColumn c : this.getChildren()){
      cols.add(c.getBean());
    }
    return cols;
  }
}

