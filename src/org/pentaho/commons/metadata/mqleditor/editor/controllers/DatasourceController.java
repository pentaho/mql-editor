package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource.EditType;
import org.pentaho.commons.metadata.mqleditor.editor.DatasourceDialogListener;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.ResultSetObject;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulButton;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.components.XulTreeCell;
import org.pentaho.ui.xul.components.XulTreeCol;
import org.pentaho.ui.xul.containers.XulDeck;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.containers.XulTreeCols;
import org.pentaho.ui.xul.containers.XulTreeRow;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class DatasourceController extends AbstractXulEventHandler {
  private XulDialog connectionDialog;
  private XulDialog datasourceDialog;
  private XulDialog removeConfirmationDialog;
  private XulDialog waitingDialog = null;
  private XulDialog previewResultsDialog = null;
  private DatasourceService service;
  public static final int CONNECTION_DECK = 0;
  public static final int MODELLING_DECK = 1;
  private List<DatasourceDialogListener> listeners = new ArrayList<DatasourceDialogListener>();

  private DatasourceModel datasourceModel;
  private ConnectionModel connectionModel;
  BindingFactory bf;
  XulTree previewResultsTable = null;
  XulTextbox connectionname = null;
  XulTextbox driverClass = null;
  XulTextbox username = null;
  XulTextbox password = null;
  XulTextbox url = null;
  
  XulTextbox datasourceName = null;
  XulListbox connections = null;
  XulTextbox query = null;
  XulTreeCols previewResultsTreeCols = null;
  XulTextbox previewLimit = null;
  XulButton editConnectionButton = null;
  XulButton removeConnectionButton = null;
  XulButton editQueryButton = null;
  
  XulButton backButton = null;
  XulButton nextButton = null;
  XulButton finishButton = null;
  XulButton cancelButton = null;
  XulButton previewButton = null;
  XulDeck datasourceDeck = null;
  
  public DatasourceController() {

  }

  public void init() {
    
    datasourceDeck = (XulDeck) document.getElementById("datasourceDeck"); //$NON-NLS-1$
    datasourceName = (XulTextbox) document.getElementById("datasourcename"); //$NON-NLS-1$
    connections = (XulListbox) document.getElementById("connectionList"); //$NON-NLS-1$
    query = (XulTextbox) document.getElementById("query"); //$NON-NLS-1$
    connectionDialog = (XulDialog) document.getElementById("connectionDialog");
    datasourceDialog = (XulDialog) document.getElementById("datasourceDialog");
    previewResultsDialog = (XulDialog) document.getElementById("previewResultsDialog");
    removeConfirmationDialog = (XulDialog) document.getElementById("removeConfirmationDialog");
    previewResultsTable = (XulTree) document.getElementById("previewResultsTable"); //$NON-NLS-1$
    previewResultsTreeCols = (XulTreeCols) document.getElementById("previewResultsTreeCols"); //$NON-NLS-1$
    previewLimit = (XulTextbox) document.getElementById("previewLimit"); //$NON-NLS-1$
    
    editConnectionButton = (XulButton) document.getElementById("editConnection"); //$NON-NLS-1$
    removeConnectionButton = (XulButton) document.getElementById("removeConnection"); //$NON-NLS-1$
    
    editQueryButton = (XulButton) document.getElementById("editQuery"); //$NON-NLS-1$
    
    backButton = (XulButton) document.getElementById("datasourceDialog_accept"); //$NON-NLS-1$
    nextButton = (XulButton) document.getElementById("datasourceDialog_cancel"); //$NON-NLS-1$
    finishButton = (XulButton) document.getElementById("datasourceDialog_extra1"); //$NON-NLS-1$
    cancelButton = (XulButton) document.getElementById("datasourceDialog_extra2"); //$NON-NLS-1$
    previewButton = (XulButton) document.getElementById("preview"); //$NON-NLS-1$
    
    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(datasourceModel, "validated", previewButton, "!disabled");
    bf.createBinding(datasourceModel, "validated", nextButton, "!disabled");
    BindingConvertor<IConnection, Boolean> buttonConvertor = new BindingConvertor<IConnection, Boolean>(){

      @Override
      public Boolean sourceToTarget(IConnection value) {
        return !(value == null);
      }

      @Override
      public IConnection targetToSource(Boolean value) {
        return null;
      }
        
    };
    
    bf.setBindingType(Binding.Type.  ONE_WAY);
    final Binding domainBinding = bf.createBinding(datasourceModel, "connections", connections, "elements");
    bf.createBinding(datasourceModel, "selectedConnection", editConnectionButton, "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$ 
    bf.createBinding(datasourceModel, "selectedConnection", removeConnectionButton, "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "selectedConnection", connections, "selectedIndex", new BindingConvertor<IConnection, Integer>() {

      @Override
      public Integer sourceToTarget(IConnection connection) {
        if(connection != null) {
          return datasourceModel.getConnectionIndex(connection);  
        } else {
          return -1;
        }
        
      }

      @Override
      public IConnection targetToSource(Integer value) {
        if(value >= 0) {
          return datasourceModel.getConnections().get(value);  
        } else {
          return null;
        }
        
      }

    });
    
    
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(datasourceModel, "previewLimit", previewLimit, "value");
    // Not sure if editQuery button is doing much
    //bf.createBinding(editQueryButton, "!disabled", "removeConnectionButton", "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(datasourceModel, "query", query, "value");
    bf.createBinding(datasourceModel, "datasourceName", datasourceName, "value");
    datasourceDeck.setSelectedIndex(CONNECTION_DECK);
    
    backButton.setDisabled(true);
    finishButton.setDisabled(true);    
    previewButton.setDisabled(true);
    nextButton.setDisabled(true);
    
    try {
      // Fires the population of the model listbox. This cascades down to the categories and columns. In essence, this
      // call initializes the entire UI.
      domainBinding.fireSourceChanged();
      
    } catch (Exception e) {
      System.out.println(e.getMessage()); e.printStackTrace();
    }
  }

  public void showDatasourceDialog() {
    datasourceDialog.show();
  }

  public void setBindingFactory(BindingFactory bf) {
    this.bf = bf;
  }

  public void setDatasourceModel(DatasourceModel model) {
    this.datasourceModel = model;
  }
  
  public DatasourceModel getDatasourceModel() {
    return this.datasourceModel;
  }

  public void setConnectionModel(ConnectionModel model) {
    this.connectionModel = model;
  }
  
  public ConnectionModel getConnectionModel() {
    
    return this.connectionModel;
  }
  
  public String getName() {
    return "datasourceController";
  }

  public void closeDatasourceDialog() {
    this.datasourceDialog.hide();
    for (DatasourceDialogListener listener : listeners) {
      listener.onDialogCancel();
    }
  }

  public void executeBack() {
    if(datasourceDeck.getSelectedIndex()== MODELLING_DECK) {
      datasourceDeck.setSelectedIndex(CONNECTION_DECK);
      finishButton.setDisabled(true);
      backButton.setDisabled(true);
      nextButton.setDisabled(false);
    }
  }

  public void executeNext() {
    if(allInputsSatisfiedForNext()) {
      if(datasourceDeck.getSelectedIndex()== CONNECTION_DECK) {
        datasourceDeck.setSelectedIndex(MODELLING_DECK);
        finishButton.setDisabled(false);
        backButton.setDisabled(false);
        nextButton.setDisabled(true);
      }
    } else {
      displayMissingInputDialog();
    }
  }
  private void displayMissingInputDialog() {
    try {
      XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
      box.setTitle("Missing Inputs");
      box.setMessage("Some of the required inputs are missing.");
    } catch(Exception e){
    }
  }
  private boolean allInputsSatisfiedForNext() {
    return (datasourceModel.getSelectedConnection() != null 
        && (datasourceModel.getQuery() != null && datasourceModel.getQuery().length() > 0) 
          && (datasourceModel.getDatasourceName() != null && datasourceModel.getDatasourceName().length() > 0)); 
  }
  public void executeCancel() {
    this.datasourceDialog.hide();
  }
  
  public void executeFinish() {
    this.datasourceDialog.hide();
  }
  public void editQuery() {
    
  }
  public void addConnection() {
    datasourceModel.setEditType(EditType.ADD);
    connectionModel.setConnection(null);
    showConnectionDialog();
  }
  
  public void editConnection() {
    datasourceModel.setEditType(EditType.EDIT);
    connectionModel.setConnection(datasourceModel.getSelectedConnection());
    showConnectionDialog();
  }
  
  public void removeConnection() {
    // Display the warning message. If ok then remove the connection from the list
    int index = connections.getSelectedIndex();
    removeConfirmationDialog.show();
  }

  public void selectSql(){
    
  }
  public void selectOlap(){
    
  }
  public void selectCsv(){
    
  }
  public void selectXml(){
    
  }
  public void showConnectionDialog() {
    connectionDialog.show();
  }

  public void closeConnectionDialog() {
    connectionDialog.hide();
  }

  public void closeRemoveConfirmationDialog() {
    removeConfirmationDialog.hide();
  }
  
  public void displayPreview() {

    if(!allInputsSatisfiedForNext()) {
      displayMissingInputDialog();
    } else {
      final XulDialog waitDialog = (XulDialog) document.getElementById("waitingDialog"); //$NON-NLS-1$
      new Thread() {
        @Override
        public void run() {
          // don't proceed until the wait dialog is shown
          while (waitDialog.isHidden()) {
            try {
              sleep(500);
            } catch (InterruptedException e) {
              return;
            }
          }
          try {
            service.doPreview(datasourceModel.getSelectedConnection(), datasourceModel.getQuery(), datasourceModel.getPreviewLimit(), 
                  new XulServiceCallback<ResultSetObject>(){
  
                    public void error(String message, Throwable error) {
                      System.out.println(message);
                      error.printStackTrace();
                    }
  
                    public void success(ResultSetObject rs) {
                          String[] columns = rs.getMetaData();
                          int columnCount = columns.length;
                          int curTreeColCount = previewResultsTable.getColumns().getColumnCount();
                          try{
                            if(columnCount > curTreeColCount){ // Add new Columns
                              for(int i = (columnCount - curTreeColCount); i > 0; i--){
                                previewResultsTable.getColumns().addColumn( (XulTreeCol) document.createElement("treecol"));
                              }
                            } else if (columnCount < curTreeColCount){ // Remove un-needed exiting columns
                              List<XulComponent> cols = previewResultsTable.getColumns().getChildNodes();
                              
                              for(int i = (curTreeColCount - columnCount); i < cols.size(); i++){
                                previewResultsTable.getColumns().removeChild(cols.get(i));
                              }
                            }
                            previewResultsTable.update();
                          } catch (XulException e){
                            // TODO: add logging!!
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                          }
                          XulTreeCols treeCols = previewResultsTable.getColumns();
                          for(int i=0;i<previewResultsTable.getColumns().getColumnCount();i++) {
                           XulTreeCol treeCol = treeCols.getColumn(i);
                           treeCol.setLabel(columns[i]);
                           treeCol.setFlex(1);
                          }
                          
                          try{
                            for (int i=0; i<rs.getRowCount(); i++) {
                              XulTreeRow row = (XulTreeRow) document.createElement("treerow");
  
                              for (int j=0; j<rs.getColumnCount(); j++) {
                                XulTreeCell cell = (XulTreeCell) document.createElement("treecell");
                                cell.setLabel(rs.getCellData(i, j) != null ? rs.getCellData(i, j).toString(): "");
                                row.addCell(cell);
                              }
                              
                              previewResultsTable.addTreeRow(row);
                            }
                            previewResultsTable.update();
                            
                          } catch(XulException e){
                            // TODO: add logging
                            System.out.println(e.getMessage());
                            e.printStackTrace();
                          }
                    }
                });
          } catch (DatasourceServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          } finally {
            waitDialog.hide();
            previewResultsDialog.show();
          }
  
        }
  
      }.start();
      waitDialog.show();
    
    }

        
   }

  public void closePreviewResultsDialog() {
    previewResultsDialog.hide(); 
  }
  
  public DatasourceService getService() {
    return service;
  }

  public void setService(DatasourceService service) {
    this.service = service;
  }
 
  public void addDatasourceDialogListener(DatasourceDialogListener listener) {
    if (listeners.contains(listener) == false) {
      listeners.add(listener);
    }
  }

  public void removeDatasourceDialogListener(DatasourceDialogListener listener) {
    if (listeners.contains(listener)) {
      listeners.remove(listener);
    }
  }

}
