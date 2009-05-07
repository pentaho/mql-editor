package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource.EditType;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.DatasourceDialogListener;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.ModelDataRow;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;
import org.pentaho.metadata.model.Category;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.IPhysicalColumn;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.metadata.model.concept.types.LocalizedString;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulButton;
import org.pentaho.ui.xul.components.XulCheckbox;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.components.XulMenuList;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.components.XulTreeCell;
import org.pentaho.ui.xul.components.XulTreeCol;
import org.pentaho.ui.xul.containers.XulColumns;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulGrid;
import org.pentaho.ui.xul.containers.XulListbox;
import org.pentaho.ui.xul.containers.XulMenupopup;
import org.pentaho.ui.xul.containers.XulRows;
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

  XulCheckbox mqlModelCheckBox = null;

  XulTreeCols previewResultsTreeCols = null;

  XulTextbox previewLimit = null;

  XulButton editConnectionButton = null;

  XulButton removeConnectionButton = null;

  XulButton editQueryButton = null;

  XulButton okButton = null;

  XulButton cancelButton = null;

  XulButton previewButton = null;

  private XulDialog errorDialog;

  private XulDialog successDialog;

  private XulLabel errorLabel = null;

  private XulLabel successLabel = null;

  private XulColumns columns = null;

  private XulRows rows = null;

  private XulGrid grid = null;

  private XulTree modelDataTable = null;

  XulMenuList<XulMenupopup> dataTypeMenuList = null;

  public DatasourceController() {

  }

  public void init() {
    modelDataTable = (XulTree) document.getElementById("modelDataTable");

    errorDialog = (XulDialog) document.getElementById("errorDialog"); //$NON-NLS-1$
    errorLabel = (XulLabel) document.getElementById("errorLabel");//$NON-NLS-1$
    successDialog = (XulDialog) document.getElementById("successDialog"); //$NON-NLS-1$
    successLabel = (XulLabel) document.getElementById("successLabel");//$NON-NLS-1$

    datasourceName = (XulTextbox) document.getElementById("datasourcename"); //$NON-NLS-1$
    connections = (XulListbox) document.getElementById("connectionList"); //$NON-NLS-1$
    query = (XulTextbox) document.getElementById("query"); //$NON-NLS-1$
    connectionDialog = (XulDialog) document.getElementById("connectionDialog");//$NON-NLS-1$
    datasourceDialog = (XulDialog) document.getElementById("datasourceDialog");//$NON-NLS-1$
    previewResultsDialog = (XulDialog) document.getElementById("previewResultsDialog");//$NON-NLS-1$
    removeConfirmationDialog = (XulDialog) document.getElementById("removeConfirmationDialog");//$NON-NLS-1$
    previewResultsTable = (XulTree) document.getElementById("previewResultsTable"); //$NON-NLS-1$
    previewResultsTreeCols = (XulTreeCols) document.getElementById("previewResultsTreeCols"); //$NON-NLS-1$
    previewLimit = (XulTextbox) document.getElementById("previewLimit"); //$NON-NLS-1$

    editConnectionButton = (XulButton) document.getElementById("editConnection"); //$NON-NLS-1$
    removeConnectionButton = (XulButton) document.getElementById("removeConnection"); //$NON-NLS-1$

    editQueryButton = (XulButton) document.getElementById("editQuery"); //$NON-NLS-1$

    okButton = (XulButton) document.getElementById("datasourceDialog_accept"); //$NON-NLS-1$
    cancelButton = (XulButton) document.getElementById("datasourceDialog_cancel"); //$NON-NLS-1$
    previewButton = (XulButton) document.getElementById("preview"); //$NON-NLS-1$

    mqlModelCheckBox = (XulCheckbox) document.getElementById("metadataModelCheckbox"); //$NON-NLS-1$

    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(datasourceModel, "validated", previewButton, "!disabled");//$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "validated", mqlModelCheckBox, "!disabled");//$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "validated", okButton, "!disabled");//$NON-NLS-1$ //$NON-NLS-2$
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
    final Binding domainBinding = bf.createBinding(datasourceModel, "connections", connections, "elements"); //$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "selectedConnection", editConnectionButton, "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$ 
    bf.createBinding(datasourceModel, "selectedConnection", removeConnectionButton, "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(datasourceModel,
        "selectedConnection", connections, "selectedIndex", new BindingConvertor<IConnection, Integer>() { //$NON-NLS-1$ //$NON-NLS-2$

          @Override
          public Integer sourceToTarget(IConnection connection) {
            if (connection != null) {
              return datasourceModel.getConnectionIndex(connection);
            } else {
              return -1;
            }

          }

          @Override
          public IConnection targetToSource(Integer value) {
            if (value >= 0) {
              return datasourceModel.getConnections().get(value);
            } else {
              return null;
            }

          }

        });
    bf.createBinding(datasourceModel, "dataRows", modelDataTable, "elements");
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(datasourceModel, "generateModelChecked", mqlModelCheckBox, "checked");//$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "generateModelChecked", mqlModelCheckBox, "selected");//$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "modelTableVisible", modelDataTable, "visible");//$NON-NLS-1$ //$NON-NLS-2$
    
    bf.createBinding(datasourceModel, "previewLimit", previewLimit, "value"); //$NON-NLS-1$ //$NON-NLS-2$
    // Not sure if editQuery button is doing much
    //bf.createBinding(editQueryButton, "!disabled", "removeConnectionButton", "!disabled", buttonConvertor); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    bf.createBinding(datasourceModel, "query", query, "value"); //$NON-NLS-1$ //$NON-NLS-2$
    bf.createBinding(datasourceModel, "datasourceName", datasourceName, "value"); //$NON-NLS-1$ //$NON-NLS-2$
    
    okButton.setDisabled(true);
    modelDataTable.setVisible(false);
    try {
      // Fires the population of the model listbox. This cascades down to the categories and columns. In essence, this
      // call initializes the entire UI.
      domainBinding.fireSourceChanged();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
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

  public void generateModel() {
    if (mqlModelCheckBox.isChecked()) {
      if (validateInputs()) {
        try {

          service.generateModel(datasourceModel.getDatasourceName(), datasourceModel.getSelectedConnection(),
              datasourceModel.getQuery(), datasourceModel.getPreviewLimit(), new XulServiceCallback<BusinessData>() {

                public void error(String message, Throwable error) {
                  mqlModelCheckBox.setChecked(false);
                  openErrorDialog("Error occurred", "Unable to retrieve business data. " + error.getLocalizedMessage());
                }

                public void success(BusinessData businessData) {
                  try {
                    datasourceModel.setBusinessData(businessData);
                    modelDataTable.setVisible(true);                    
                  } catch (Exception xe) {
                    xe.printStackTrace();
                  }
                }
              });
        } catch (DatasourceServiceException e) {
          mqlModelCheckBox.setChecked(false);
          openErrorDialog("Error occurred", "Unable to retrieve business data. " + e.getLocalizedMessage());
        }
      } else {
        mqlModelCheckBox.setChecked(false);
        openErrorDialog("Missing Input", "Some of the required inputs are missing");
      }
    } else {
      modelDataTable.setVisible(false);      
      datasourceModel.setBusinessData(null);
    }
  }

  private boolean validateInputs() {
    return (datasourceModel.getSelectedConnection() != null
        && (datasourceModel.getQuery() != null && datasourceModel.getQuery().length() > 0) && (datasourceModel
        .getDatasourceName() != null && datasourceModel.getDatasourceName().length() > 0));
  }

  public void exitDatasourceDialog() {
    this.datasourceDialog.hide();
  }

  public void saveModel() {
    List<ModelDataRow> dataRows = datasourceModel.getDataRows();
    if (dataRows != null && dataRows.size() > 0) {
      // Get the domain from the business data
      try {
      BusinessData businessData = datasourceModel.getBusinessData();
      Domain domain = businessData.getDomain();
      List<LogicalModel> logicalModels = domain.getLogicalModels();
      for (LogicalModel logicalModel : logicalModels) {
        List<Category> categories = logicalModel.getCategories();
        for (Category category : categories) {
          List<LogicalColumn> logicalColumns = category.getLogicalColumns();
          int i = 0;
          for (LogicalColumn logicalColumn : logicalColumns) {
            ModelDataRow row = dataRows.get(i++);
            logicalColumn.setDataType(row.getSelectedDataType());
            logicalColumn.setName(new LocalizedString(row.getColumnName()));
            //logicalColumn.setName(row.getColumnName());
          }
        }
      }
      saveModel(businessData, false);
      } catch (Exception xe) {
        openErrorDialog("Error occurred", "Unable to save model. " + datasourceModel.getDatasourceName()
            + xe.getLocalizedMessage());
      }
    } else {
      if (validateInputs()) {
        try {

          service.generateModel(datasourceModel.getDatasourceName(), datasourceModel.getSelectedConnection(),
              datasourceModel.getQuery(), datasourceModel.getPreviewLimit(), new XulServiceCallback<BusinessData>() {

                public void error(String message, Throwable error) {
                  openErrorDialog("Error occurred", "Unable to save model. " + datasourceModel.getDatasourceName()
                      + error.getLocalizedMessage());
                }

                public void success(BusinessData businessData) {
                  try {
                    // Get the domain from the business data
                    Domain domain = businessData.getDomain();
                    List<LogicalModel> logicalModels = domain.getLogicalModels();
                    for (LogicalModel logicalModel : logicalModels) {
                      List<Category> categories = logicalModel.getCategories();
                      for (Category category : categories) {
                        List<LogicalColumn> logicalColumns = category.getLogicalColumns();
                        for (LogicalColumn logicalColumn : logicalColumns) {
                          IPhysicalColumn physicalColumn = logicalColumn.getPhysicalColumn();
                          logicalColumn.setDataType(physicalColumn.getDataType());
                          //logicalColumn.setName(new LocalizedString(physicalColumn.getName());
                          logicalColumn.setName(physicalColumn.getName());
                        }
                      }
                    }
                    saveModel(businessData, false);
                  } catch (Exception xe) {
                    openErrorDialog("Error occurred", "Unable to save model. " + datasourceModel.getDatasourceName()
                        + xe.getLocalizedMessage());
                  }
                }
              });
        } catch (DatasourceServiceException e) {
          openErrorDialog("Error occurred", "Unable to save model. " + datasourceModel.getDatasourceName()
              + e.getLocalizedMessage());
        }
      } else {
        openErrorDialog("Missing Input", "Some of the required inputs are missing");
      }
    }
  }

  private void saveModel(BusinessData businessData, boolean overwrite) {
    try {
      // TODO setting value to false to always create a new one. Save as is not yet implemented
      service.saveModel(businessData, overwrite, new XulServiceCallback<Boolean>() {
        public void error(String message, Throwable error) {
          openErrorDialog("Error occurred", "Unable to save model: " + datasourceModel.getDatasourceName()
              + error.getLocalizedMessage());
        }

        public void success(Boolean value) {
          openSuccesDialog("Success", "Successfully saved model: " + datasourceModel.getDatasourceName());
          datasourceDialog.hide();
        }
      });
    } catch (DatasourceServiceException e) {
      openErrorDialog("Error occurred", "Unable to save model: " + datasourceModel.getDatasourceName()
          + e.getLocalizedMessage());
    }
  }

  public void editQuery() {

  }

  public void addConnection() {
    datasourceModel.setEditType(EditType.ADD);
    connectionModel.clearModel();
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

  public void selectSql() {

  }

  public void selectOlap() {

  }

  public void selectCsv() {

  }

  public void selectMql() {

  }

  public void selectXml() {

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
        service.doPreview(datasourceModel.getSelectedConnection(), datasourceModel.getQuery(), datasourceModel
            .getPreviewLimit(), new XulServiceCallback<SerializedResultSet>() {

          public void error(String message, Throwable error) {
            openErrorDialog("Preview Failed", "Unable to preview data: " + error.getLocalizedMessage()); //$NON-NLS-1$ //$NON-NLS-2$ 
          }

          public void success(SerializedResultSet rs) {
            String[][] data = rs.getData();
            String[] columns = rs.getColumns();
            int columnCount = columns.length;
            // Remove any existing children
            List<XulComponent> previewResultsList = previewResultsTable.getChildNodes();

            for (int i = 0; i < previewResultsList.size(); i++) {
              previewResultsTable.removeChild(previewResultsList.get(i));
            }
            // Remove all the existing columns
            int curTreeColCount = previewResultsTable.getColumns().getColumnCount();
            List<XulComponent> cols = previewResultsTable.getColumns().getChildNodes();
            for (int i = 0; i < curTreeColCount; i++) {
              previewResultsTable.getColumns().removeChild(cols.get(i));
            }
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
            previewResultsTable.update();
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
              previewResultsDialog.show();
            } catch (XulException e) {
              // TODO: add logging
              System.out.println(e.getMessage());
              e.printStackTrace();
            }
          }
        });
      } catch (DatasourceServiceException e) {
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
}
