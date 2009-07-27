package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.Condition;

public class UIConditions extends AbstractModelNode<UICondition>{

    
  public UIConditions(){
  }
  
  public UIConditions(List<UICondition> conditions){
    super(conditions);
  }

  @Override
  protected void fireCollectionChanged() {
    markTopMostCondition();
    this.changeSupport.firePropertyChange("children", null, this.getChildren());
  }
  
  private void markTopMostCondition(){
    for(int index = 0; index < children.size(); index++){
      if(index == 0){
        children.get(index).setTopMost(true);
      } else {
        children.get(index).setTopMost(false);
      }
    }
  }

}

  