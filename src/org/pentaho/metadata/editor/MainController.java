package org.pentaho.metadata.editor;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.editor.models.UIBusinessColumn;
import org.pentaho.metadata.editor.models.UICondition;
import org.pentaho.metadata.editor.models.UIDomain;
import org.pentaho.metadata.editor.models.UIModel;
import org.pentaho.metadata.editor.models.UIOrder;
import org.pentaho.metadata.editor.service.MetadataService;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulMenuList;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class MainController extends AbstractXulEventHandler {


  private XulMenuList modelList;
  //private XulMenuList viewList;
  private XulTree categoryTree;
  //private XulListbox columnList;

  private Workspace workspace;
  private XulTree fieldTable;
  private XulTree constraintTree;
  private XulDialog dialog;
  private MetadataService service;
  private List<MqlDialogListener> listeners = new ArrayList<MqlDialogListener>();
  BindingFactory bf;

  public MainController() {
    
    
  }

  public void init() {

    createBindings();
  }
  
  public void showDialog(){

    dialog = (XulDialog) document.getElementById("mqlEditorDialog");
    dialog.show();
    
  }
  
  public void clearWorkspace(){
    workspace.clear();
  }
  private void createBindings(){
    modelList = (XulMenuList) document.getElementById("modelList");
    categoryTree = (XulTree) document.getElementById("categoryTree");
    constraintTree = (XulTree) document.getElementById("constraintTree");
    
    fieldTable = (XulTree) document.getElementById("selectedColumnTree");
    
    //Start out one-way
    bf.setBindingType(Binding.Type.ONE_WAY);

    final Binding domainBinding = bf.createBinding(this.workspace, "domain", modelList, "elements", 
        new BindingConvertor<UIDomain, List>(){

          @Override
          public List sourceToTarget(UIDomain value) {
            return value == null ? null : value.getChildren();
          }

          //not used
          @Override
          public UIDomain targetToSource(List value) {
            return null;
          }
      
    });
    
    

    bf.createBinding(modelList, "selectedIndex", workspace, "selectedModel", new BindingConvertor<Integer, UIModel>() {
      @Override
      public UIModel sourceToTarget(Integer value) {
        return workspace.getDomain().getChildren().get(value);
      }
      @Override
      public Integer targetToSource(UIModel value) {
        return workspace.getDomain().getChildren().indexOf(value);
      }
    });
    
    bf.createBinding(workspace, "categories", categoryTree, "elements");
    
    
    bf.createBinding(categoryTree, "selectedRows", workspace, "selectedColumn", new BindingConvertor<int[], UIBusinessColumn>() {
      @Override
      public UIBusinessColumn sourceToTarget(int[] array) {
        if(array.length == 0){
          return null;
        }
        int value = array[0];
        if(value < 0){
          return null;
        }
        return workspace.getColumnByPos(value);
      }
      @Override
      public int[] targetToSource(UIBusinessColumn value) {
        return new int[]{workspace.getSelectedCategory().getChildren().indexOf(value)};
      }
    });
    

    bf.createBinding(workspace, "selectedColumns", fieldTable, "elements");
    
    try {
      //fires the population of the model listbox. This cascades down to the views and columns!
      domainBinding.fireSourceChanged();
    } catch (Exception e) {System.out.println(e.getMessage()); e.printStackTrace();}
  }
  
  public void moveSelectionToFields(){
    UIBusinessColumn col = workspace.getSelectedColumn();
    if(col != null && workspace.getSelectedColumns().contains(col) == false){
      workspace.addColumn(col);
    }
  }
  

  public void moveSelectionToConditions(){
    UIBusinessColumn col = workspace.getSelectedColumn();
    if(col != null){
      workspace.addCondition(col);
    }
  }

  public void moveSelectionToOrders(){
    UIBusinessColumn col = workspace.getSelectedColumn();
    if(col != null && workspace.getOrders().contains(col) == false){
      workspace.addOrder(col);
    }
  }
  public void selectModel() {
    UIModel m = workspace.getDomain().getChildren().get(modelList.getSelectedIndex());

  }

  public void setBindingFactory(BindingFactory bf) {

    this.bf = bf;
  }
  
  public void setWorkspace(Workspace workspace){
    this.workspace = workspace;
  }

  public String getName() {
    return "mainController";
  }
  
  public void closeDialog(){
    this.dialog.hide();
    for(MqlDialogListener listener : listeners){
      listener.onDialogCancel();
    }
  }
  public void saveQuery(){
    service.saveQuery(workspace.getSelectedModel(), workspace.getSelectedColumns().getChildren(), workspace.getConditions().getChildren(), workspace.getOrders().getChildren(), 
      new XulServiceCallback<String>(){

        public void error(String message, Throwable error) {
          System.out.println(message);
          error.printStackTrace();
        }

        public void success(String retVal) {
          try{
              XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
              box.setTitle("Mql Query");
              retVal = retVal.replace("><", ">\n<");
              box.setMessage(retVal);
              box.open();
              
            
          } catch(Exception e){
            //ignore
          }
          workspace.setMqlQuery(retVal);
          dialog.hide();
          for(MqlDialogListener listener : listeners){
            listener.onDialogAccept();
          }
          System.out.println(retVal);
          
        }
      
      }
    );
  }

  public MetadataService getService() {
  
    return service;
  }

  public void setService(MetadataService service) {
  
    this.service = service;
  }

  
  public void addMqlDialogListener(MqlDialogListener listener){
    if(listeners.contains(listener) == false){
      listeners.add(listener);
    }
  }
  
  public void removeMqlDialogListener(MqlDialogListener listener){
    if(listeners.contains(listener)){
      listeners.remove(listener);
    }
  }
  
}
