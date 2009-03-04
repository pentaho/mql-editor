package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class ConditionsController extends AbstractXulEventHandler{

  private Workspace workspace;
  private XulTree conditionsTree;
  private BindingFactory bf;
  private XulTree conditionTree;
  
  public void init(){
    conditionsTree = (XulTree) document.getElementById("conditionsTree");
    
    bf.createBinding(workspace, "conditions", conditionsTree, "elements");
    
    conditionTree = (XulTree) document.getElementById("conditionsTree");

    BindingConvertor<int[], Boolean> buttonConvertor = new BindingConvertor<int[], Boolean>(){

      @Override
      public Boolean sourceToTarget(int[] value) {
        return (value == null || value.length ==0);
      }

      @Override
      public int[] targetToSource(Boolean value) {return null;}
        
    };
    
    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(conditionTree,"selectedRows", "conditionUp", "disabled", buttonConvertor);
    bf.createBinding(conditionTree,"selectedRows", "conditionDown", "disabled", buttonConvertor);
    bf.createBinding(conditionTree,"selectedRows", "conditionRemove", "disabled", buttonConvertor);
  }

  public int getSelectedIndex() {
    int[] rows = this.conditionTree.getSelectedRows();
    return (rows != null && rows.length == 0) ? -1 : rows[0];
  }

  public void moveUp(){
    try{
      int prevIndex = getSelectedIndex();
      workspace.getConditions().moveChildUp(getSelectedIndex());
      conditionTree.clearSelection();
      conditionTree.setSelectedRows(new int[]{prevIndex -1});
    } catch(IllegalArgumentException e){
      //out of bounds
    }
  }

  public void moveDown(){
    int prevIndex = getSelectedIndex();
    try{
      workspace.getConditions().moveChildDown(getSelectedIndex());
      conditionTree.clearSelection();
      conditionTree.setSelectedRows(new int[]{prevIndex+1});
    } catch(IllegalArgumentException e){
      //out of bounds
    }
  }
  
  public void remove(){
    if(getSelectedIndex() < 0){
      return;
    }
    workspace.getConditions().remove(getSelectedIndex());
    conditionTree.clearSelection();
  }

  
  public void setBindingFactory(BindingFactory bf){
    this.bf = bf;
  }
  
  public void setWorkspace(Workspace workspace) {
    this.workspace = workspace;
  }
  
  public String getName(){
    return "conditionsController";
    
  }
  
}

  