package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource.EditType;
import org.pentaho.commons.metadata.mqleditor.editor.ConnectionDialogListener;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionService;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulButton;
import org.pentaho.ui.xul.components.XulMessageBox;
import org.pentaho.ui.xul.components.XulTextbox;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class ConnectionController extends AbstractXulEventHandler {
  private XulDialog dialog;

  private ConnectionService service;

  private List<ConnectionDialogListener> listeners = new ArrayList<ConnectionDialogListener>();

  private ConnectionModel connectionModel;

  private DatasourceModel datasourceModel;

  private XulDialog removeConfirmationDialog;

  private XulDialog saveConnectionConfirmationDialog;


  BindingFactory bf;

  XulTextbox name = null;

  XulTextbox driverClass = null;

  XulTextbox username = null;

  XulTextbox password = null;

  XulTextbox url = null;

  XulButton okBtn = null;

  XulButton testBtn = null;

  public ConnectionController() {

  }

  public void init() {
    saveConnectionConfirmationDialog = (XulDialog) document.getElementById("saveConnectionConfirmationDialog");
    name = (XulTextbox) document.getElementById("connectionname"); //$NON-NLS-1$
    driverClass = (XulTextbox) document.getElementById("driverClass"); //$NON-NLS-1$
    username = (XulTextbox) document.getElementById("username"); //$NON-NLS-1$
    password = (XulTextbox) document.getElementById("password"); //$NON-NLS-1$
    url = (XulTextbox) document.getElementById("url"); //$NON-NLS-1$
    dialog = (XulDialog) document.getElementById("connectionDialog");
    removeConfirmationDialog = (XulDialog) document.getElementById("removeConfirmationDialog");
    bf.setBindingType(Binding.Type.BI_DIRECTIONAL);
    final Binding domainBinding = bf.createBinding(connectionModel, "name", name, "value");
    bf.createBinding(connectionModel, "driverClass", driverClass, "value");
    bf.createBinding(connectionModel, "username", username, "value");
    bf.createBinding(connectionModel, "password", password, "value");
    bf.createBinding(connectionModel, "url", url, "value");
    okBtn = (XulButton) document.getElementById("connectionDialog_accept");
    testBtn = (XulButton) document.getElementById("testButton");
    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(connectionModel, "validated", okBtn, "!disabled");
    bf.createBinding(connectionModel, "validated", testBtn, "!disabled");
    okBtn.setDisabled(true);
    testBtn.setDisabled(true);
    try {
      // Fires the population of the model listbox. This cascades down to the categories and columns. In essence, this
      // call initializes the entire UI.
      domainBinding.fireSourceChanged();

    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  public void showDialog() {
    dialog.show();
  }

  public void setBindingFactory(BindingFactory bf) {
    this.bf = bf;
  }

  public void setDatasourceModel(DatasourceModel model) {
    this.datasourceModel = model;
  }

  public void setConnectionModel(ConnectionModel model) {
    this.connectionModel = model;
  }

  public ConnectionModel getConnectionModel() {
    return this.connectionModel;
  }

  public DatasourceModel getDatasourceModel() {
    return this.datasourceModel;
  }

  public String getName() {
    return "connectionController";
  }

  public void closeDialog() {
    dialog.hide();
    for (ConnectionDialogListener listener : listeners) {
      listener.onDialogCancel();
    }
  }
  public void  closeSaveConnectionConfirmationDialog(){
    saveConnectionConfirmationDialog.hide(); 
 }
  public void addConnection() {
    try {
      service.testConnection(connectionModel.getConnection(), new XulServiceCallback<Boolean>() {
        public void error(String message, Throwable error) {
          System.out.println(message);
          error.printStackTrace();
        }

        public void success(Boolean value) {
            try {
            XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
            if (value) {
              saveConnection();
            } else {
              saveConnectionConfirmationDialog.show();
            }
            } catch(Exception e) {

            }
        }
      }  
      );
    } catch (Exception e) {
      saveConnectionConfirmationDialog.show();
    }
  }

  public void testConnection() {
    try {
      service.testConnection(connectionModel.getConnection(), new XulServiceCallback<Boolean>() {
        public void error(String message, Throwable error) {
          System.out.println(message);
          error.printStackTrace();
        }

        public void success(Boolean value) {
          try {
            XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
            if (value) {
              box.setTitle("Connection Test Successful");
              box.setMessage("Successfully tested the connection");
              box.open();
            } else {
              box.setTitle("Connection Test Not Successful");
              box.setMessage("Unable to test the connection");
              box.open();
            }

          } catch (Exception e) {
          }
        }
      });
    } catch (Exception e) {
      try {
        XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
        box.setTitle("Connection Test Not Successful");
        box.setMessage("Unable to test the connection");
        box.open();
      } catch (Exception ee) {
      }
    }
  }

  public void deleteConnection() {
    removeConfirmationDialog.hide();
    service.deleteConnection(datasourceModel.getSelectedConnection().getName(), new XulServiceCallback<Boolean>() {

      public void error(String message, Throwable error) {
        System.out.println(message);
        error.printStackTrace();
      }

      public void success(Boolean value) {
        try {
          XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
          if (value) {
            box.setTitle("Connection Deleted");
            box.setMessage("Successfully deleted the connection");
            box.open();
            datasourceModel.setConnections(datasourceModel.getConnections());
            List<IConnection> connections = datasourceModel.getConnections();
            if (connections != null && connections.size() > 0) {
              datasourceModel.setSelectedConnection(connections.get(connections.size() - 1));
            } else {
              datasourceModel.setSelectedConnection(null);
            }

          } else {
            box.setTitle("Connection Not Deleted");
            box.setMessage("Unable to deleted the connection");
            box.open();
          }

        } catch (Exception e) {
        }
      }
    });
  }

  public void saveConnection() {
    if(!saveConnectionConfirmationDialog.isHidden()) {
      saveConnectionConfirmationDialog.hide();
    }
    if (EditType.ADD.equals(datasourceModel.getEditType())) {
      service.addConnection(connectionModel.getConnection(), new XulServiceCallback<Boolean>() {

        public void error(String message, Throwable error) {
          System.out.println(message);
          error.printStackTrace();
        }

        public void success(Boolean value) {
          try {
            dialog.hide();
            XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
            if (value) {
              box.setTitle("Connection Saved");
              box.setMessage("Successfully saved the connection");
              box.open();
              datasourceModel.setConnections(datasourceModel.getConnections());
              datasourceModel.setSelectedConnection(connectionModel.getConnection());
            } else {
              box.setTitle("Connection Not saved");
              box.setMessage("Unable to save the connection");
              box.open();
            }

          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
    } else {
      service.updateConnection(connectionModel.getConnection(), new XulServiceCallback<Boolean>() {

        public void error(String message, Throwable error) {
          System.out.println(message);
          error.printStackTrace();
        }

        public void success(Boolean value) {
          try {
            dialog.hide();
            XulMessageBox box = (XulMessageBox) document.createElement("messagebox");
            if (value) {
              box.setTitle("Connection Updated");
              box.setMessage("Successfully updated the connection");
              box.open();
              datasourceModel.setConnections(datasourceModel.getConnections());
              datasourceModel.setSelectedConnection(connectionModel.getConnection());
            } else {
              box.setTitle("Connection Not updated");
              box.setMessage("Unable to updated the connection");
              box.open();
            }

          } catch (Exception e) {
          }
        }
      });
    }
  }

  public ConnectionService getService() {
    return service;
  }

  public void setService(ConnectionService service) {
    this.service = service;
  }

  public void addConnectionDialogListener(ConnectionDialogListener listener) {
    if (listeners.contains(listener) == false) {
      listeners.add(listener);
    }
  }

  public void removeConnectionDialogListener(ConnectionDialogListener listener) {
    if (listeners.contains(listener)) {
      listeners.remove(listener);
    }
  }

}
