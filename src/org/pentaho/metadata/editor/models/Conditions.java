package org.pentaho.metadata.editor.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.pentaho.metadata.beans.BusinessColumn;
import org.pentaho.metadata.beans.Condition;

public class Conditions extends AbstractModelNode<UICondition>{

    
  public Conditions(){
  }
  
  public Conditions(List<UICondition> conditions){
    super(conditions);
  }

  
  public List<Condition> getBeanCollection(){
    List<Condition> conditions = new ArrayList<Condition>();
    
    for(UICondition c : this.getChildren()){
      conditions.add(c.getBean());
    }
    return conditions;
  }
}

  