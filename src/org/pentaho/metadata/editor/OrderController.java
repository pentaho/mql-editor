package org.pentaho.metadata.editor;

import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class OrderController extends AbstractXulEventHandler{

  private Workspace workspace;
  private BindingFactory bf;
  private XulTree orderTree;
  
  public void init(){
    orderTree = (XulTree) document.getElementById("orderTable");
    bf.createBinding(workspace, "orders", orderTree, "elements");
    

    BindingConvertor<int[], Boolean> buttonConvertor = new BindingConvertor<int[], Boolean>(){

      @Override
      public Boolean sourceToTarget(int[] value) {
        return (value == null || value.length ==0);  
      }

      @Override
      public int[] targetToSource(Boolean value) {return null;}
        
    };
    
    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(orderTree,"selectedRows", "orderUp", "disabled", buttonConvertor);
    bf.createBinding(orderTree,"selectedRows", "orderDown", "disabled", buttonConvertor);
    bf.createBinding(orderTree,"selectedRows", "orderRemove", "disabled", buttonConvertor);
  }

  public int getSelectedIndex() {
    int[] rows = this.orderTree.getSelectedRows();
    return (rows != null && rows.length == 0) ? -1 : rows[0];
  }

  public void moveUp(){
    try{
      int prevIndex = getSelectedIndex();
      workspace.getOrders().moveChildUp(getSelectedIndex());
      orderTree.clearSelection();
      orderTree.setSelectedRows(new int[]{prevIndex -1});
    } catch(IllegalArgumentException e){
      //out of bounds
    }
  }

  public void moveDown(){
    int prevIndex = getSelectedIndex();
    try{
      workspace.getOrders().moveChildDown(getSelectedIndex());
      orderTree.clearSelection();
      orderTree.setSelectedRows(new int[]{prevIndex+1});
    } catch(IllegalArgumentException e){
      //out of bounds
    }
  }
  
  public void remove(){
    if(getSelectedIndex() < 0){
      return;
    }
    workspace.getOrders().remove(getSelectedIndex());
    orderTree.clearSelection();
  }

  
  public void setBindingFactory(BindingFactory bf){
    this.bf = bf;
  }
  
  public void setWorkspace(Workspace workspace) {
    this.workspace = workspace;
  }
  
  public String getName(){
    return "orderController";
    
  }
  
}

  