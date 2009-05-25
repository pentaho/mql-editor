package org.pentaho.commons.metadata.mqleditor.editor;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConnectionController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.CsvDatasourceController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.DatasourceController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.RelationalDatasourceController;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.gwt.widgets.client.utils.IMessageBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.MessageBundle;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.components.XulLabel;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.gwt.GwtXulDomContainer;
import org.pentaho.ui.xul.gwt.GwtXulLoader;
import org.pentaho.ui.xul.gwt.GwtXulRunner;
import org.pentaho.ui.xul.gwt.binding.GwtBindingFactory;
import org.pentaho.ui.xul.gwt.util.EventHandlerWrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.XMLParser;

public class GwtDatasourceEditor implements IMessageBundleLoadCallback {

  private MessageBundle bundle;
  private DatasourceController datasourceController = new DatasourceController();
  private CsvDatasourceController csvDatasourceController = new CsvDatasourceController();
  private RelationalDatasourceController relationalDatasourceController = new RelationalDatasourceController();
  private ConnectionController connectionController = new ConnectionController();
  private ConnectionService connectionService;
  private DatasourceService datasourceService;
  private DatasourceModel datasourceModel = new DatasourceModel();
  private ConnectionModel connectionModel = new ConnectionModel();
  private GwtXulDomContainer container;
  
  public GwtDatasourceEditor(){
    
  }
  
  public void show(){
    if(container == null){
      try {
        bundle = new MessageBundle(GWT.getModuleBaseURL(),"connectionFrame", this );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("datasourceDialog");
      datasourceModel.clearModel();
      connectionModel.clearModel();
      if(connectionService != null) {
        connectionService.getConnections(new XulServiceCallback<List<IConnection>>(){
  
          public void error(String message, Throwable error) {
            showErrorDialog("Error Occurred","Unable to show the dialog." +error.getLocalizedMessage());
          }
  
          public void success(List<IConnection> connections) {
            datasourceModel.getRelationalModel().setConnections(connections);
          }
          
        });
        dialog.show();
      } else {
        showErrorDialog("Error Occurred","Unable to show the dialog. Connection Service is null");
      }
    }
  }

  private void showErrorDialog(String title, String message) {
    XulDialog errorDialog = (XulDialog) container.getDocumentRoot().getElementById("errorDialog");
    XulLabel errorLabel = (XulLabel) container.getDocumentRoot().getElementById("errorLabel");        
    errorDialog.setTitle(title);
    errorLabel.setValue(message);
  }
  public void hide(){
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("datasourceDialog");
    dialog.hide();
  }
  
  public void addDatasourceDialogListener(DatasourceDialogListener listener){
    datasourceController.addDatasourceDialogListener(listener);
  }
  
  public void removeDatasourceDialogListener(DatasourceDialogListener listener){
    datasourceController.removeDatasourceDialogListener(listener);
  }
  
  public void addConnectionDialogListener(ConnectionDialogListener listener){
    connectionController.addConnectionDialogListener(listener);
  }
  
  public void removeConnectionDialogListener(ConnectionDialogListener listener){
    connectionController.removeConnectionDialogListener(listener);
  }
  
  private void loadContainer(String xul){
    try{

      
      GwtXulLoader loader = new GwtXulLoader();
  
      com.google.gwt.xml.client.Document gwtDoc = XMLParser.parse(xul);
      
      if(bundle != null){
        container = loader.loadXul(gwtDoc, bundle);
      } else {
        container = loader.loadXul(gwtDoc);
      }
      
      

      try {

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "connectionFrame-gwt-overlay.xul");

        try {
          Request response = builder.sendRequest(null, new RequestCallback() {
            public void onError(Request request, Throwable exception) {
              // Code omitted for clarity
            }

            public void onResponseReceived(Request request, Response response) {
              
              loadOverlay(response.getText());
            }
          });
        } catch (RequestException e) {
          // Code omitted for clarity
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  private void loadOverlay(String xul){

    com.google.gwt.xml.client.Document gwtDoc = XMLParser.parse(xul);
    try{
      container.loadOverlay(gwtDoc);

    } catch (Exception e) {
      e.printStackTrace();
    }
    displayXulDialog();
  }
  
  private void displayXulDialog() {
    try {
      
      final GwtXulRunner runner = new GwtXulRunner();
      GwtBindingFactory bf = new GwtBindingFactory(container.getDocumentRoot());
      
      EventHandlerWrapper wrapper = GWT.create(DatasourceController.class);
      datasourceController.setBindingFactory(bf);
      wrapper.setHandler(datasourceController);      
      container.addEventHandler(wrapper);

      wrapper = GWT.create(CsvDatasourceController.class);
      csvDatasourceController.setBindingFactory(bf);
      wrapper.setHandler(csvDatasourceController);      
      container.addEventHandler(wrapper);

      wrapper = GWT.create(RelationalDatasourceController.class);
      relationalDatasourceController.setBindingFactory(bf);
      wrapper.setHandler(relationalDatasourceController);      
      container.addEventHandler(wrapper);

      
      wrapper = GWT.create(ConnectionController.class);
      connectionController.setBindingFactory(bf);
      wrapper.setHandler(connectionController);      
      container.addEventHandler(wrapper);

      runner.addContainer(container);
      datasourceController.setConnectionModel(connectionModel);
      datasourceController.setDatasourceModel(datasourceModel);
      csvDatasourceController.setDatasourceModel(datasourceModel);
      relationalDatasourceController.setConnectionModel(connectionModel);
      relationalDatasourceController.setDatasourceModel(datasourceModel);
      connectionController.setConnectionModel(connectionModel);
      connectionController.setDatasourceModel(datasourceModel);
      runner.initialize();
      runner.start();
      RootPanel.get().add(runner.getRootPanel());
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void setConnectionService(ConnectionService service){
    this.connectionService = service;
    connectionController.setService(service);
  }

  public void setDatasourceService(DatasourceService service){
    this.datasourceService = service;
    datasourceController.setService(service);
    csvDatasourceController.setService(service);
    relationalDatasourceController.setService(service);
  }

  
  public void bundleLoaded(String bundleName) {
    try {

      RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "connectionFrame.xul");

      try {
        Request response = builder.sendRequest(null, new RequestCallback() {
          public void onError(Request request, Throwable exception) {
            // Code omitted for clarity
          }

          public void onResponseReceived(Request request, Response response) {
            
            loadContainer(response.getText());
            // Code omitted for clarity

          }
        });
      } catch (RequestException e) {
        // Code omitted for clarity
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public DatasourceModel getDatasourceModel() {
    return datasourceModel;
  }
  
  public ConnectionModel getConnectionModel() {
    return connectionModel;
  }
}
