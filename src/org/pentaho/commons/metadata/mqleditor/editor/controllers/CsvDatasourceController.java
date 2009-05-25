package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.metadata.model.Domain;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulCheckbox;
import org.pentaho.ui.xul.components.XulFileDialog;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.components.XulFileDialog.RETURN_CODE;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.containers.XulVbox;
import org.pentaho.ui.xul.gwt.tags.GwtFileDialog;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class CsvDatasourceController extends AbstractXulEventHandler {
  private DatasourceService service;

  private XulDialog waitingDialog = null;

  private XulLabel waitingDialogLabel = null;

  private DatasourceModel datasourceModel;

  BindingFactory bf;

  XulTextbox datasourceName = null;

  private XulDialog errorDialog;

  private XulDialog successDialog;

  private XulLabel errorLabel = null;

  private XulLabel successLabel = null;

  private XulTree csvDataTable = null;

  private XulFileDialog fileDialog = null;

  XulTextbox selectedFile = null;

  XulCheckbox headersPresent = null;

  private XulVbox fileUploadVBox = null;

  public CsvDatasourceController() {

  }

  public void init() {
    fileUploadVBox = (XulVbox) document.getElementById("fileUploadVBox"); //$NON-NLS-1$
    csvDataTable = (XulTree) document.getElementById("csvDataTable");
    waitingDialog = (XulDialog) document.getElementById("waitingDialog"); //$NON-NLS-1$
    waitingDialogLabel = (XulLabel) document.getElementById("waitingDialogLabel");//$NON-NLS-1$    
    errorDialog = (XulDialog) document.getElementById("errorDialog"); //$NON-NLS-1$    
    errorLabel = (XulLabel) document.getElementById("errorLabel");//$NON-NLS-1$
    successDialog = (XulDialog) document.getElementById("successDialog"); //$NON-NLS-1$
    successLabel = (XulLabel) document.getElementById("successLabel");//$NON-NLS-1$
    headersPresent = (XulCheckbox) document.getElementById("headersPresent"); //$NON-NLS-1$
    datasourceName = (XulTextbox) document.getElementById("datasourcename"); //$NON-NLS-1$
    selectedFile = (XulTextbox) document.getElementById("selectedFile"); //$NON-NLS-1$
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    final Binding domainBinding = bf.createBinding(datasourceModel.getCsvModel(), "dataRows", csvDataTable, "elements");
    bf.createBinding(datasourceModel.getCsvModel(), "headersPresent", headersPresent, "checked"); //$NON-NLS-1$ //$NON-NLS-2$    
    bf.createBinding(datasourceModel, "datasourceName", datasourceName, "value"); //$NON-NLS-1$ //$NON-NLS-2$
    try {
      domainBinding.fireSourceChanged();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    try {
      fileDialog = (XulFileDialog) document.createElement("filedialog");
      fileUploadVBox.addChild(fileDialog);
      fileUploadVBox.addComponent(fileDialog);
    } catch (XulException e) {
      // TODO Auto-generated catch block
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

  public String getName() {
    return "csvDatasourceController";
  }

  public void setService(DatasourceService service) {
    this.service = service;
  }

  private void generateModel() {
    if (validateIputForCsv()) {
      try {
        showWaitingDialog("Generating Metadata Model", "Please wait ....");
        service.generateInlineEtlModel(datasourceModel.getDatasourceName(), datasourceModel.getCsvModel()
            .getSelectedFile(), datasourceModel.getCsvModel().isHeadersPresent(), "\"", ",",
            new XulServiceCallback<Domain>() {

              public void error(String message, Throwable error) {
                hideWaitingDialog();
                openErrorDialog("Error occurred", "Unable to generate the model. " + error.getLocalizedMessage());
              }

              public void success(Domain csvDomain) {
                try {
                  hideWaitingDialog();
                  datasourceModel.getCsvModel().setDomain(csvDomain);
                } catch (Exception xe) {
                  xe.printStackTrace();
                }
              }
            });
      } catch (DatasourceServiceException e) {
        hideWaitingDialog();
        openErrorDialog("Error occurred", "Unable to retrieve business data. " + e.getLocalizedMessage());
      }
    } else {
      openErrorDialog("Missing Input", "Some of the required inputs are missing");
    }
  }

  private boolean validateIputForCsv() {
    return (datasourceModel.getCsvModel().getSelectedFile() != null && (datasourceModel.getDatasourceName() != null && datasourceModel
        .getDatasourceName().length() > 0));
  }

  public void browseFile() {
    RETURN_CODE returnValue = fileDialog.showOpenDialog();
    if (returnValue == RETURN_CODE.OK) {
      datasourceModel.getCsvModel()
          .setSelectedFile(fileDialog.getFile() != null ? fileDialog.getFile().toString() : "");
      uploadFile();
    }
  }

  private void uploadFile() {
    try {

      service.uploadFile(((GwtFileDialog) fileDialog).getUploadForm(), new XulServiceCallback<String>() {
        public void error(String message, Throwable error) {
          openErrorDialog("Upload Failed", error.getLocalizedMessage());
        }

        public void success(String filePath) {
          datasourceModel.getCsvModel().setSelectedFile(filePath);
          generateModel();
        }
      });
    } catch (DatasourceServiceException e) {
      openErrorDialog("Upload Failed", e.getLocalizedMessage());
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
