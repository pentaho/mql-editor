package org.pentaho.commons.metadata.mqleditor.editor;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConnectionController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.DatasourceController;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.gwt.widgets.client.utils.IMessageBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.MessageBundle;
import org.pentaho.ui.xul.XulServiceCallback;
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
  private ConnectionController connectionController = new ConnectionController();
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
      XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("newdatasourceDialog");
      dialog.show();
    }
  }

  public void hide(){
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("newdatasourceDialog");
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
  
  private void displayXulDialog(String xul) {
    try {
      
      GwtXulLoader loader = new GwtXulLoader();
      final GwtXulRunner runner = new GwtXulRunner();
  
      com.google.gwt.xml.client.Document gwtDoc = XMLParser.parse(xul);
      
      if(bundle != null){
        container = loader.loadXul(gwtDoc, bundle);
      } else {
        container = loader.loadXul(gwtDoc);
      }

      GwtBindingFactory bf = new GwtBindingFactory(container.getDocumentRoot());
      
      EventHandlerWrapper wrapper = GWT.create(DatasourceController.class);
      datasourceController.setBindingFactory(bf);
      wrapper.setHandler(datasourceController);      
      container.addEventHandler(wrapper);

      wrapper = GWT.create(ConnectionController.class);
      connectionController.setBindingFactory(bf);
      wrapper.setHandler(connectionController);      
      container.addEventHandler(wrapper);

      runner.addContainer(container);
      datasourceController.setConnectionModel(connectionModel);
      datasourceController.setDatasourceModel(datasourceModel);
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
    connectionController.setService(service);
    service.getConnections(new XulServiceCallback<List<IConnection>>(){

      public void error(String message, Throwable error) {
        
      }

      public void success(List<IConnection> connections) {
        datasourceModel.setConnections(connections);
      }
      
    });
  }

  public void setDatasourceService(DatasourceService service){
    datasourceController.setService(service);
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
            
            displayXulDialog(response.getText());
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
