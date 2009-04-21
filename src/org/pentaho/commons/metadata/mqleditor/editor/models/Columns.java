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
  
}

