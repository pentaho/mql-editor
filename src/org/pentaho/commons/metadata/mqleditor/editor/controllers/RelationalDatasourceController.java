package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.RelationalModel;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulButton;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.components.XulTreeCell;
import org.pentaho.ui.xul.components.XulTreeCol;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.containers.XulTreeChildren;
import org.pentaho.ui.xul.containers.XulTreeCols;
import org.pentaho.ui.xul.containers.XulTreeRow;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class RelationalDatasourceController extends AbstractXulEventHandler {
  private XulDialog connectionDialog;

  private XulDialog removeConfirmationDialog;

  private XulDialog waitingDialog = null;

  private XulLabel waitingDialogLabel = null;

  private XulDialog previewResultsDialog = null;

  private DatasourceService service;

  private DatasourceModel datasourceModel;

  private ConnectionModel connectionModel;

  BindingFactory bf;

  XulTree previewResultsTable = null;

  XulTextbox datasourceName = null;

  XulListbox connections = null;

  XulTextbox query = null;

  XulTreeCols previewResultsTreeCols = null;

  XulTextbox previewLimit = null;

  XulButton editConnectionButton = null;

  XulButton removeConnectionButton = null;

  XulButton editQueryButton = null;

  XulButton previewButton = null;

  private XulDialog errorDialog;

  private XulDialog successDialog;

  private XulLabel errorLabel = null;

  private XulLabel successLabel = null;

  private XulTree modelDataTable = null;

  private XulButton applyButton = null;

  public RelationalDatasourceController() {

  }

  public void init() {
    applyButton = (XulButton) document.getElementById("apply"); //$NON-NLS-1$
    modelDataTable = (XulTree) document.getElementById("modelDataTable");
    errorDialog = (XulDialog) document.getElementById("errorDialog"); //$NON-NLS-1$
    errorLabel = (XulLabel) document.getElementById("errorLabel");//$NON-NLS-1$    
    waitingDialog = (XulDialog) document.getElementById("waitingDialog"); //$NON-NLS-1$
    waitingDialogLabel = (XulLabel) document.getElementById("waitingDialogLabel");//$NON-NLS-1$    
    successDialog = (XulDialog) document.getElementById("successDialog"); //$NON-NLS-1$
    successLabel = (XulLabel) document.getElementById("successLabel");//$NON-NLS-1$
    datasourceName = (XulTextbox) document.getElementById("datasourcename"); //$NON-NLS-1$
    connections = (XulListbox) document.getElementById("connectionList"); //$NON-NLS-1$
    query = (XulTextbox) document.getElementById("query"); //$NON-NLS-1$
    connectionDialog = (XulDialog) document.getElementById("connectionDialog");//$NON-NLS-1$
    previewResultsDialog = (XulDialog) document.getElementById("previewResultsDialog");//$NON-NLS-1$
    removeConfirmationDialog = (XulDialog) document.getElementById("removeConfirmationDialog");//$NON-NLS-1$
    previewResultsTable = (XulTree) document.getElementById("previewResultsTable"); //$NON-NLS-1$
    previewResultsTreeCols = (XulTreeCols) document.getElementById("previewResultsTreeCols"); //$NON-NLS-1$
    previewLimit = (XulTextbox) document.getElementById("previewLimit"); //$NON-NLS-1$
    editConnectionButton = (XulButton) document.getElementById("editConnection"); //$NON-NLS-1$
    removeConnectionButton = (XulButton) document.getElementById("removeConnection"); //$NON-NLS-1$
    editQueryButton = (XulButton) document.getElementById("editQuery"); //$NON-NLS-1$
    previewButton = (XulButton) document.getElementById("preview"); //$NON-NLS-1$


    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(datasourceModel.getRelationalModel(), "validated", previewButton, "!disabled");//$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel.getRelationalModel(), "validated", applyButton, "!disabled");//$NON-NLS-1$ //$NON-NLS-2$
    
    BindingConvertor<IConnection, Boolean> buttonConvertor = new BindingConvertor<IConnection, Boolean>() {

      @Override
      public Boolean sourceToTarget(IConnection value) {
        return !(value == null);
      }

      @Override
      public IConnection targetToSource(Boolean value) {
        return null;
      }

    };

    bf.setBindingType(Binding.Type.ONE_WAY);
    final Binding domainBinding = bf.createBinding(datasourceModel.getRelationalModel(), "connections", connections, "elements"); //$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel.getRelationalModel(), "selectedConnection", editConnectionButton, "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$ 
    bf.createBinding(datasourceModel.getRelationalModel(), "selectedConnection", removeConnectionButton, "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(datasourceModel.getRelationalModel(),
        "selectedConnection", connections, "selectedIndex", new BindingConvertor<IConnection, Integer>() { //$NON-NLS-1$ //$NON-NLS-2$

          @Override
          public Integer sourceToTarget(IConnection connection) {
            if (connection != null) {
              return datasourceModel.getRelationalModel().getConnectionIndex(connection);
            } else {
              return -1;
            }

          }

          @Override
          public IConnection targetToSource(Integer value) {
            if (value >= 0) {
              return datasourceModel.getRelationalModel().getConnections().get(value);
            } else {
              return null;
            }

          }

        });
    bf.createBinding(datasourceModel.getRelationalModel(), "dataRows", modelDataTable, "elements");
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);     
    bf.createBinding(datasourceModel.getRelationalModel(), "previewLimit", previewLimit, "value"); //$NON-NLS-1$ //$NON-NLS-2$
    // Not sure if editQuery button is doing much
    //bf.createBinding(editQueryButton, "!disabled", "removeConnectionButton", "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(datasourceModel.getRelationalModel(), "query", query, "value"); //$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "datasourceName", datasourceName, "value"); //$NON-NLS-1$ //$NON-NLS-2$

    try {
      // Fires the population of the model listbox. This cascades down to the categories and columns. In essence, this
      // call initializes the entire UI.
      domainBinding.fireSourceChanged();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
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
    return "relationalDatasourceController";
  }

  public void generateModel() {
      query.setDisabled(true);
      if (validateInputs()) {
        try {
          showWaitingDialog("Generating Metadata Model", "Please wait ....");
          service.generateModel(datasourceModel.getDatasourceName(), datasourceModel.getRelationalModel().getSelectedConnection(),
              datasourceModel.getRelationalModel().getQuery(), datasourceModel.getRelationalModel().getPreviewLimit(), new XulServiceCallback<BusinessData>() {

                public void error(String message, Throwable error) {
                  hideWaitingDialog();
                  query.setDisabled(false);
                  openErrorDialog("Error occurred", "Unable to retrieve business data. " + error.getLocalizedMessage());
                }

                public void success(BusinessData businessData) {
                  try {
                    hideWaitingDialog();
                    datasourceModel.getRelationalModel().setBusinessData(businessData);
                  } catch (Exception xe) {
                    xe.printStackTrace();
                  }
                }
              });
        } catch (DatasourceServiceException e) {
          hideWaitingDialog();
          query.setDisabled(false);
          openErrorDialog("Error occurred", "Unable to retrieve business data. " + e.getLocalizedMessage());
        }
      } else {
        query.setDisabled(false);
        openErrorDialog("Missing Input", "Some of the required inputs are missing");
      }
  }
  private boolean validateInputs() {
    return (datasourceModel.getRelationalModel().getSelectedConnection() != null
        && (datasourceModel.getRelationalModel().getQuery() != null && datasourceModel.getRelationalModel().getQuery().length() > 0) && (datasourceModel
        .getDatasourceName() != null && datasourceModel.getDatasourceName().length() > 0));
  }
  public void editQuery() {

  }

  public void addConnection() {
    datasourceModel.getRelationalModel().setEditType(RelationalModel.EditType.ADD);
    connectionModel.clearModel();
    connectionModel.setDisableConnectionName(false);
    showConnectionDialog();
  }

  public void editConnection() {
    datasourceModel.getRelationalModel().setEditType(RelationalModel.EditType.ADD);
    connectionModel.setDisableConnectionName(true);
    connectionModel.setConnection(datasourceModel.getRelationalModel().getSelectedConnection());
    showConnectionDialog();
  }

  public void removeConnection() {
    // Display the warning message. If ok then remove the connection from the list
    int index = connections.getSelectedIndex();
    removeConfirmationDialog.show();
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

    if (!validateInputs()) {
      openErrorDialog("Missing Input", "Some of the required inputs are missing"); //$NON-NLS-2$
    } else {
      try {
        showWaitingDialog("Generating Preview Data", "Please wait ....");

        service.doPreview(datasourceModel.getRelationalModel().getSelectedConnection(), datasourceModel.getRelationalModel().getQuery(), datasourceModel
            .getRelationalModel().getPreviewLimit(), new XulServiceCallback<SerializedResultSet>() {

          public void error(String message, Throwable error) {
            hideWaitingDialog();
            openErrorDialog("Preview Failed", "Unable to preview data: " + error.getLocalizedMessage()); //$NON-NLS-1$ //$NON-NLS-2$ 
          }

          public void success(SerializedResultSet rs) {
            try {
              String[][] data = rs.getData();
              String[] columns = rs.getColumns();
              int columnCount = columns.length;
              // Remove any existing children
              List<XulComponent> previewResultsList = previewResultsTable.getChildNodes();

              for (int i = 0; i < previewResultsList.size(); i++) {
                previewResultsTable.removeChild(previewResultsList.get(i));
              }
              XulTreeChildren treeChildren = previewResultsTable.getRootChildren();
              if(treeChildren != null) {
                treeChildren.removeAll();
/*                List<XulComponent> treeChildrenList = treeChildren.getChildNodes();
                for (int i = 0; i < treeChildrenList.size(); i++) {
                  treeChildren.removeItem(i);
                }*/
              }
              // Remove all the existing columns
              int curTreeColCount = previewResultsTable.getColumns().getColumnCount();
              List<XulComponent> cols = previewResultsTable.getColumns().getChildNodes();
              for (int i = 0; i < curTreeColCount; i++) {
                previewResultsTable.getColumns().removeChild(cols.get(i));
              }
              previewResultsTable.update();
              // Recreate the colums
              XulTreeCols treeCols = previewResultsTable.getColumns();
              if (treeCols == null) {
                try {
                  treeCols = (XulTreeCols) document.createElement("treecols");
                } catch (XulException e) {

                }
              }
              // Setting column data
              for (int i = 0; i < columnCount; i++) {
                try {
                  XulTreeCol treeCol = (XulTreeCol) document.createElement("treecol");
                  treeCol.setLabel(columns[i]);
                  treeCol.setFlex(1);
                  treeCols.addColumn(treeCol);
                } catch (XulException e) {

                }
              }

              XulTreeCols treeCols1 = previewResultsTable.getColumns();
              int count = previewResultsTable.getColumns().getColumnCount();
              // Create the tree children and setting the data
              try {
                for (int i = 0; i < data.length; i++) {
                  XulTreeRow row = (XulTreeRow) document.createElement("treerow");

                  for (int j = 0; j < columnCount; j++) {
                    XulTreeCell cell = (XulTreeCell) document.createElement("treecell");
                    cell.setLabel(data[i][j]);
                    row.addCell(cell);
                  }

                  previewResultsTable.addTreeRow(row);
                }
                previewResultsTable.update();
                hideWaitingDialog();
                previewResultsDialog.show();
              } catch (XulException e) {
                // TODO: add logging
                hideWaitingDialog();
                System.out.println(e.getMessage());
                e.printStackTrace();
              }
            } catch (Exception e) {
              hideWaitingDialog();
              openErrorDialog("Preview Failed", "Unable to preview data: " + e.getLocalizedMessage());
            }
          }
        });
      } catch (DatasourceServiceException e) {
        hideWaitingDialog();
        openErrorDialog("Preview Failed", "Unable to preview data: " + e.getLocalizedMessage());
      }
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
  public void openErrorDialog(String title, String message) {
    errorDialog.setTitle(title);
    errorLabel.setValue(message);
    errorDialog.show();
  }

  public void closeErrorDialog() {
    if (!errorDialog.isHidden()) {
      errorDialog.hide();
    }
  }

  public void openSuccesDialog(String title, String message) {
    successDialog.setTitle(title);
    successLabel.setValue(message);
    successDialog.show();
  }

  public void closeSuccessDialog() {
    if (!successDialog.isHidden()) {
      successDialog.hide();
    }
  }

  public void showWaitingDialog(String title, String message) {
    waitingDialog.setTitle(title);
    waitingDialogLabel.setValue(message);
    waitingDialog.show();

  }

  public void hideWaitingDialog() {
    waitingDialog.hide();
  }
}
