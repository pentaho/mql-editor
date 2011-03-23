/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2009 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.MqlDialogListener;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumn;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumns;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.di.core.Const;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulButton;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.components.XulMenuList;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.ui.xul.stereotype.Bindable;


/**
 *
 * This is the main XulEventHandler for the dialog. It sets up the main bindings for the user interface and responds
 * to some of the main UI events such as closing and accepting the dialog.
 * 
 */
public class MainController extends AbstractXulEventHandler {

	public static final int CANCELLED = 0;
	public static final int OK = 1;
	
	private int lastClicked = CANCELLED;

  private static XulDialog errorDialog;

  private XulMenuList modelList;
  private XulMenuList domainList;
  private XulTree categoryTree;
  private XulButton acceptButton;

  private Workspace workspace;
  private XulTree fieldTable;
  private XulTree conditionsTable;
  private XulTree ordersTable;
  private XulTextbox limit;
  private XulDialog dialog;
  private MQLEditorService service;
  private List<MqlDialogListener> listeners = new ArrayList<MqlDialogListener>();
  
  private Query savedQuery;
  
  BindingFactory bf;

  public MainController() {
    
    
  }
  
  public boolean getOkClicked() {
  	return lastClicked == OK;
  }

  public void setSavedQuery(Query savedQuery) {
    this.savedQuery = savedQuery;  
    if (savedQuery == null) {
      this.clearWorkspace();
    }
    if (savedQuery != null) {
      workspace.wrap(savedQuery);
    }
  }
  
  @Bindable
  public void init() {
    createBindings();
  }
  
  public void showDialog(){

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
    dialog = (XulDialog) document.getElementById("mqlEditorDialog");
    limit = (XulTextbox) document.getElementById("limit");
    acceptButton = (XulButton) document.getElementById("mqlEditorDialog_accept");

    errorDialog = (XulDialog) document.getElementById("errorDialog");

    // bind the selections empty status to the ok button (i.e. if no selections, disable OK button)
    bf.setBindingType(Binding.Type.ONE_WAY);
    final Binding acceptButtonBinding = bf.createBinding(workspace, "selections", acceptButton, "!disabled", new BindingConvertor<List<UIColumns>, Boolean>() {

      @Override
      public Boolean sourceToTarget(List<UIColumns> value) {
        return value != null && !value.isEmpty();
      }

      @Override
      public List<UIColumns> targetToSource(Boolean value) {
        return null;
      }
      
    });
    
    
    
    
    // Bind the domain list to the domain menulist drop-down.
    bf.setBindingType(Binding.Type.ONE_WAY);
    final Binding domainBinding = bf.createBinding(this.workspace, "domains", domainList, "elements");

    // Bind the selected index from the domain drop-down to the selectedDomain in the workspace
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(domainList, "selectedIndex", workspace, "selectedDomain", new BindingConvertor<Integer, UIDomain>() {
      @Override
      public UIDomain sourceToTarget(Integer value) {
        if(value < 0 || value > workspace.getDomains().size()){
          return null;
        }
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
        if(value < 0){
          return null;
        }
        return workspace.getSelectedDomain().getModels().get(value);
      }

    });

    // Bind the available categories from  the selected model to the category/column tree.
    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(workspace, "categories", categoryTree, "elements");
    
    // Bind the selected column from the tree to the workspace 
    bf.createBinding(categoryTree, "absoluteSelectedRows", workspace, "selectedColumns", new BindingConvertor<int[], List<UIColumn>>() {
      @Override
      public List<UIColumn> sourceToTarget(int[] array) {
        if(array.length == 0){
          return null;
        }
        int value = array[0];
        if(value < 0){
          return null;
        }
        return workspace.getColumnsByPos(array);
      }
      @Override
      public int[] targetToSource(List<UIColumn> value) {
        int[] positions = new int[value.size()];
        int i = 0;
        for(UIColumn col : value){
          positions[i++] = workspace.getSelectedCategory().getChildren().indexOf(col); 
        }
        return positions;
      }
    });
    
    // Bind the selected columns, conditions and orders to their respective tables
    bf.createBinding(workspace, "selections", fieldTable, "elements"); //$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(workspace, "conditions", conditionsTable, "elements"); //$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(workspace, "orders", ordersTable, "elements"); //$NON-NLS-1$ //$NON-NLS-2$
    
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(workspace, "limit", limit, "value", new BindingConvertor<Integer, String>() {

       @Override
       public String sourceToTarget(Integer value) {
          if (value > -1) {
             return value.toString();
          }
          else {
             return "";
          }
       }

       @Override
       public Integer targetToSource(String value) {
         if (!Const.isEmpty(value)) {
           return Integer.parseInt(value);
         }
         else {
           return -1;
         }
       }

     });
    
    try {
      // Fires the population of the model listbox. This cascades down to the categories and columns. In essence, this
      // call initializes the entire UI.
      domainBinding.fireSourceChanged();
      
    } catch (Exception e) {
      System.out.println(e.getMessage()); e.printStackTrace();
    }

    
    
  }
  
  @Bindable
  public void moveSelectionToFields(){
    List<UIColumn> cols = workspace.getSelectedColumns();
    for(UIColumn col : cols){
      workspace.addColumn((UIColumn)col.clone());
    }
  }
  

  @Bindable
  public void moveSelectionToConditions(){
    List<UIColumn> cols = workspace.getSelectedColumns();
    for(UIColumn col : cols){
      workspace.addCondition(col);
    }
  }

  @Bindable
  public void moveSelectionToOrders(){
    List<UIColumn> cols = workspace.getSelectedColumns();
    for(UIColumn col : cols){
      if(workspace.getOrders().contains(col) == false){
        workspace.addOrder(col);
      }
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
  
  @Bindable
  public void closeDialog(){
  	lastClicked = CANCELLED;
    this.dialog.hide();
    
    // listeners may remove themselves, old-style iteration
    for(int i=0; i<listeners.size(); i++){
      listeners.get(i).onDialogCancel();
    }
  }
  
  @Bindable
  public void saveQuery(){
  	lastClicked = OK;
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
            //box.open();
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
    
//
//    service.serializeModel(workspace.getMqlQuery(),
//      new XulServiceCallback<String>(){
//
//        public void error(String message, Throwable error) {
//          System.out.println(message);
//          error.printStackTrace();
//        }
//
//        public void success(String retVal) {
//          
//            System.out.println(retVal);
//          
//          dialog.hide();
//          
//        }
//      
//      }
//    );
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

  @Bindable
  public static void showErrorDialog(String message) {
    if (errorDialog == null) {
      throw new IllegalStateException("Error dialog has not been loaded yet");
    } else {
      XulLabel msg = (XulLabel)errorDialog.getElementById("errorLabel");
      msg.setValue(message);
      errorDialog.show();
    }
  }

  @Bindable
  public static void closeErrorDialog(String message) {
    if (errorDialog == null) {
      throw new IllegalStateException("Error dialog has not been loaded yet");
    } else {
      errorDialog.hide();
    }
  }
}
