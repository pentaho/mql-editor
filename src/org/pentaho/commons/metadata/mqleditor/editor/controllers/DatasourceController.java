package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.DatasourceDialogListener;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.CsvModelDataRow;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.ModelDataRow;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.metadata.model.Category;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.metadata.model.concept.types.LocalizedString;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulButton;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulDeck;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulHbox;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class DatasourceController extends AbstractXulEventHandler {
  private XulDialog datasourceDialog;

  private XulDialog waitingDialog = null;

  private XulLabel waitingDialogLabel = null;

  private DatasourceService service;

  public static final int RELATIONAL_DECK = 0;

  public static final int CSV_DECK = 1;

  private List<DatasourceDialogListener> listeners = new ArrayList<DatasourceDialogListener>();

  private DatasourceModel datasourceModel;

  private ConnectionModel connectionModel;

  BindingFactory bf;

  XulTextbox datasourceName = null;

  XulButton okButton = null;

  XulButton cancelButton = null;

  private XulDialog errorDialog;

  private XulDialog successDialog;

  private XulLabel errorLabel = null;

  private XulLabel successLabel = null;

  private XulHbox buttonBox = null;

  private XulDeck datasourceDeck = null;

  public DatasourceController() {

  }

  public void init() {
    datasourceDeck = (XulDeck) document.getElementById("datasourceDeck"); //$NON-NLS-1$
    buttonBox = (XulHbox) document.getElementById("buttonBox");
    errorDialog = (XulDialog) document.getElementById("errorDialog"); //$NON-NLS-1$
    errorLabel = (XulLabel) document.getElementById("errorLabel");//$NON-NLS-1$    
    waitingDialog = (XulDialog) document.getElementById("waitingDialog"); //$NON-NLS-1$
    waitingDialogLabel = (XulLabel) document.getElementById("waitingDialogLabel");//$NON-NLS-1$    
    successDialog = (XulDialog) document.getElementById("successDialog"); //$NON-NLS-1$
    successLabel = (XulLabel) document.getElementById("successLabel");//$NON-NLS-1$    
    datasourceName = (XulTextbox) document.getElementById("datasourcename"); //$NON-NLS-1$
    datasourceDialog = (XulDialog) document.getElementById("datasourceDialog");//$NON-NLS-1$
    okButton = (XulButton) document.getElementById("datasourceDialog_accept"); //$NON-NLS-1$
    cancelButton = (XulButton) document.getElementById("datasourceDialog_cancel"); //$NON-NLS-1$
    bf.setBindingType(Binding.Type.ONE_WAY);
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
    final Binding domainBinding = bf.createBinding(datasourceModel, "datasourceName", datasourceName, "value"); //$NON-NLS-1$ //$NON-NLS-2$

    okButton.setDisabled(true);
    // Setting the Button Panel background to white
    buttonBox.setBgcolor("#FFFFFF");
    datasourceDeck.setSelectedIndex(RELATIONAL_DECK);
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

  public void saveModel() {

    if (RELATIONAL_DECK == datasourceDeck.getSelectedIndex()) {
      saveRelationalModel();
    } else if (CSV_DECK == datasourceDeck.getSelectedIndex()) {
      saveCsvModel();
    }
  }

  private boolean validateIputForCsv() {
    return (datasourceModel.getCsvModel().getSelectedFile() != null && (datasourceModel.getDatasourceName() != null && datasourceModel
        .getDatasourceName().length() > 0));
  }

  private void saveCsvModel() {
    List<CsvModelDataRow> dataRows = datasourceModel.getCsvModel().getDataRows();
    try {
      // Get the domain from the business data
      Domain domain = datasourceModel.getCsvModel().getDomain();
      List<LogicalModel> logicalModels = domain.getLogicalModels();
      for (LogicalModel logicalModel : logicalModels) {
        List<Category> categories = logicalModel.getCategories();
        for (Category category : categories) {
          List<LogicalColumn> logicalColumns = category.getLogicalColumns();
          int i = 0;
          for (LogicalColumn logicalColumn : logicalColumns) {
            CsvModelDataRow row = dataRows.get(i++);
            logicalColumn.setDataType(row.getSelectedDataType());
            logicalColumn.setName(new LocalizedString(domain.getLocales().get(0).getCode(), row.getColumnName()));
          }
        }
      }
      saveCsvModel(domain, false);
    } catch (Exception xe) {
      openErrorDialog("Error occurred", "Unable to save model. " + datasourceModel.getDatasourceName()
          + xe.getLocalizedMessage());
    }
  }

  public void exitDatasourceDialog() {
    this.datasourceDialog.hide();
  }

  private boolean validateInputs() {
    return (datasourceModel.getRelationalModel().getSelectedConnection() != null
        && (datasourceModel.getRelationalModel().getQuery() != null && datasourceModel.getRelationalModel().getQuery()
            .length() > 0) && (datasourceModel.getDatasourceName() != null && datasourceModel.getDatasourceName()
        .length() > 0));
  }

  private void saveRelationalModel() {
    List<ModelDataRow> dataRows = datasourceModel.getRelationalModel().getDataRows();
    if (dataRows != null && dataRows.size() > 0) {
      // User has decided to choose data modeling process and have customized the mode. So we will save the customized model
      try {
        // Get the domain from the business data
        BusinessData businessData = datasourceModel.getRelationalModel().getBusinessData();
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
              logicalColumn.setName(new LocalizedString(domain.getLocales().get(0).getCode(), row.getColumnName()));
            }
          }
        }
        saveRelationalModel(businessData, false);
      } catch (Exception xe) {
        openErrorDialog("Error occurred", "Unable to save model. " + datasourceModel.getDatasourceName()
            + xe.getLocalizedMessage());
      }
    } else {
      // User has decided to skip the data modeling process. So we will generate the default model and save it
      if (validateInputs()) {
        try {

          service.saveModel(datasourceModel.getDatasourceName(), datasourceModel.getRelationalModel()
              .getSelectedConnection(), datasourceModel.getRelationalModel().getQuery(), false, datasourceModel
              .getRelationalModel().getPreviewLimit(), new XulServiceCallback<BusinessData>() {

            public void error(String message, Throwable error) {
              openErrorDialog("Error occurred", "Unable to save model. " + datasourceModel.getDatasourceName()
                  + error.getLocalizedMessage());
            }

            public void success(BusinessData businessData) {
              datasourceDialog.hide();
              datasourceModel.getRelationalModel().setBusinessData(businessData);
              for (DatasourceDialogListener listener : listeners) {
                listener.onDialogFinish(datasourceModel.getRelationalModel().getDatasource());
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

  private void saveRelationalModel(BusinessData businessData, boolean overwrite) {
    try {
      // TODO setting value to false to always create a new one. Save as is not yet implemented
      service.saveModel(businessData, overwrite, new XulServiceCallback<Boolean>() {
        public void error(String message, Throwable error) {
          openErrorDialog("Error occurred", "Unable to save model: " + datasourceModel.getDatasourceName()
              + error.getLocalizedMessage());
        }

        public void success(Boolean value) {
          datasourceDialog.hide();
          for (DatasourceDialogListener listener : listeners) {
            listener.onDialogFinish(datasourceModel.getRelationalModel().getDatasource());
          }
        }
      });
    } catch (DatasourceServiceException e) {
      openErrorDialog("Error occurred", "Unable to save model: " + datasourceModel.getDatasourceName()
          + e.getLocalizedMessage());
    }
  }

  private void saveCsvModel(Domain domain, boolean overwrite) {
    try {
      // TODO setting value to false to always create a new one. Save as is not yet implemented
      service.saveInlineEtlModel(domain, overwrite, new XulServiceCallback<Boolean>() {
        public void error(String message, Throwable error) {
          openErrorDialog("Error occurred", "Unable to save model: " + datasourceModel.getDatasourceName()
              + error.getLocalizedMessage());
        }

        public void success(Boolean value) {
          datasourceDialog.hide();
          for (DatasourceDialogListener listener : listeners) {
            listener.onDialogFinish(datasourceModel.getDatasource());
          }
        }
      });
    } catch (DatasourceServiceException e) {
      openErrorDialog("Error occurred", "Unable to save model: " + datasourceModel.getDatasourceName()
          + e.getLocalizedMessage());
    }
  }

  public void selectSql() {
    datasourceDeck.setSelectedIndex(RELATIONAL_DECK);
  }

  public void selectOlap() {

  }

  public void selectCsv() {
    datasourceDeck.setSelectedIndex(CSV_DECK);
  }

  public void selectMql() {

  }

  public void selectXml() {

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

  public void showWaitingDialog(String title, String message) {
    waitingDialog.setTitle(title);
    waitingDialogLabel.setValue(message);
    waitingDialog.show();

  }

  public void hideWaitingDialog() {
    waitingDialog.hide();
  }
}
