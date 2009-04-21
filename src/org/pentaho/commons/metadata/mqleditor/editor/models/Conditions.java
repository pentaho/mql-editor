package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.Condition;

public class Conditions extends AbstractModelNode<UICondition>{

    
  public Conditions(){
  }
  
  public Conditions(List<UICondition> conditions){
    super(conditions);
  }

}

  