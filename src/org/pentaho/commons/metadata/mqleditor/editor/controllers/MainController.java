package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.MqlDialogListener;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumn;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulMenuList;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;


/**
 *
 * This is the main XulEventHandler for the dialog. It sets up the main bindings for the user interface and responds
 * to some of the main UI events such as closing and accepting the dialog.
 * 
 */
public class MainController extends AbstractXulEventHandler {


  private XulMenuList modelList;
  private XulMenuList domainList;
  private XulTree categoryTree;;

  private Workspace workspace;
  private XulTree fieldTable;
  private XulTree conditionsTable;
  private XulTree ordersTable;
  private XulDialog dialog;
  private MQLEditorService service;
  private List<MqlDialogListener> listeners = new ArrayList<MqlDialogListener>();
  
  private Query savedQuery;
  
  BindingFactory bf;

  public MainController() {
    
    
  }

  public void setSavedQuery(Query savedQuery) {
    this.savedQuery = savedQuery;  
  }
  
  public void init() {

    createBindings();
    
    if (savedQuery != null) {
      workspace.wrap(savedQuery);
    }
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
    domainList = (XulMenuList) document.getElementById("domainList");
    categoryTree = (XulTree) document.getElementById("categoryTree");
    conditionsTable = (XulTree) document.getElementById("conditionsTree");
    ordersTable = (XulTree) document.getElementById("orderTable");
    fieldTable = (XulTree) document.getElementById("selectedColumnTree");

    // Bind the domain list to the domain menulist drop-down.
    bf.setBindingType(Binding.Type.ONE_WAY);
    final Binding domainBinding = bf.createBinding(this.workspace, "domains", domainList, "elements");

    // Bind the selected index from the domain drop-down to the selectedDomain in the workspace
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(domainList, "selectedIndex", workspace, "selectedDomain", new BindingConvertor<Integer, UIDomain>() {
      @Override
      public UIDomain sourceToTarget(Integer value) {
        return workspace.getDomains().get(value);
      }
      @Override
      public Integer targetToSource(UIDomain value) {
        return workspace.getDomains().indexOf(value);
      }
    });

    // Bind the selectedDomain to the list of models menulist drop-down
    bf.setBindingType(Binding.Type.ONE_WAY);
    Binding domainToList = bf.createBinding(this.workspace, "selectedDomain", modelList, "elements", new BindingConvertor<UIDomain, List<UIModel>>() {

      @Override
      public List<UIModel> sourceToTarget(UIDomain value) {
        return value.getModels();
      }

      @Override
      public UIDomain targetToSource(List<UIModel> value) {
        return null; // not used   
      }
      
    });
    
    // Bind the selected index of the model dro-down the the selectedModel in the workspace
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    Binding modelToList = bf.createBinding(workspace, "selectedModel", modelList, "selectedIndex", new BindingConvertor<UIModel, Integer>() {

      @Override
      public Integer sourceToTarget(UIModel value) {
        return workspace.getSelectedDomain().getModels().indexOf(value);
      }

      @Override
      public UIModel targetToSource(Integer value) {
        return workspace.getSelectedDomain().getModels().get(value);
      }

    });

    // Bind the available categories from  the selected model to the category/column tree.
    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(workspace, "categories", categoryTree, "elements");
    
    // Bind the selected column from the tree to the workspace 
    bf.createBinding(categoryTree, "selectedRows", workspace, "selectedColumn", new BindingConvertor<int[], UIColumn>() {
      @Override
      public UIColumn sourceToTarget(int[] array) {
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
      public int[] targetToSource(UIColumn value) {
        return new int[]{workspace.getSelectedCategory().getChildren().indexOf(value)};
      }
    });
    
    // Bind the selected columns, conditions and orders to their respective tables
    bf.createBinding(workspace, "selectedColumns", fieldTable, "elements");
    bf.createBinding(workspace, "conditions", conditionsTable, "elements");
    bf.createBinding(workspace, "orders", ordersTable, "elements");

    try {
      // Fires the population of the model listbox. This cascades down to the categories and columns. In essence, this
      // call initializes the entire UI.
      domainBinding.fireSourceChanged();
      
    } catch (Exception e) {
      System.out.println(e.getMessage()); e.printStackTrace();
    }

    
    
  }
  
  public void moveSelectionToFields(){
    UIColumn col = workspace.getSelectedColumn();
    if(col != null && workspace.getSelectedColumns().contains(col) == false){
      workspace.addColumn(col);
    }
  }
  

  public void moveSelectionToConditions(){
    UIColumn col = workspace.getSelectedColumn();
    if(col != null){
      workspace.addCondition(col);
    }
  }

  public void moveSelectionToOrders(){
    UIColumn col = workspace.getSelectedColumn();
    if(col != null && workspace.getOrders().contains(col) == false){
      workspace.addOrder(col);
    }
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
    
    // listeners may remove themselves, old-style iteration
    for(int i=0; i<listeners.size(); i++){
      listeners.get(i).onDialogCancel();
    }
  }
  public void saveQuery(){
    service.saveQuery(workspace.getMqlQuery(),
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
          workspace.setMqlStr(retVal);
          dialog.hide();
          for(MqlDialogListener listener : listeners){
            listener.onDialogAccept(workspace.getMqlQuery());
          }
          System.out.println(retVal);
          
        }
      
      }
    );
    

    service.serializeModel(workspace.getMqlQuery(),
      new XulServiceCallback<String>(){

        public void error(String message, Throwable error) {
          System.out.println(message);
          error.printStackTrace();
        }

        public void success(String retVal) {
          
            System.out.println(retVal);
          
          dialog.hide();
          
        }
      
      }
    );
  }

  public MQLEditorService getService() {
  
    return service;
  }

  public void setService(MQLEditorService service) {
  
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
