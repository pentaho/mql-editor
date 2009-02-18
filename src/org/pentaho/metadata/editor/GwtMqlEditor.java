package org.pentaho.metadata.editor;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.gwt.widgets.client.utils.IMessageBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.MessageBundle;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.editor.models.UIDomain;
import org.pentaho.metadata.editor.service.MetadataService;
import org.pentaho.metadata.editor.service.MetadataServiceGwtImpl;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.gwt.GwtXulDomContainer;
import org.pentaho.ui.xul.gwt.GwtXulLoader;
import org.pentaho.ui.xul.gwt.GwtXulRunner;
import org.pentaho.ui.xul.gwt.binding.GwtBindingFactory;
import org.pentaho.ui.xul.gwt.util.EventHandlerWrapper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.XMLParser;

public class GwtMqlEditor implements EntryPoint, IMessageBundleLoadCallback {

  private MessageBundle bundle;
  private MainController mainController = new MainController();
  private Workspace workspace = new Workspace();
  private GwtXulDomContainer container;
  final SelectedColumnController selectedColumnController = new SelectedColumnController();
  final ConditionsController constraintController = new ConditionsController();
  final OrderController orderController = new OrderController();
  
  
  public GwtMqlEditor(){
    
  }
  
  public void onModuleLoad() {
    this.setService(new MetadataServiceGwtImpl());
    show();
  }
  
  public String getMqlQuery(){
    return workspace.getMqlQuery();
  }
  
  public void show(){
    if(container == null){
      try {
        bundle = new MessageBundle(GWT.getModuleBaseURL(),"mainFrame", this );
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("mqlEditorDialog");
      dialog.show();
    }
  }
  
  public void addMqlDialogListener(MqlDialogListener listener){
    mainController.addMqlDialogListener(listener);
  }
  
  public void removeMqlDialogListener(MqlDialogListener listener){
    mainController.removeMqlDialogListener(listener);
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
      
      EventHandlerWrapper wrapper = GWT.create(MainController.class);
      mainController.setBindingFactory(bf);
      wrapper.setHandler(mainController);      
      container.addEventHandler(wrapper);
      
      wrapper = GWT.create(ConditionsController.class);
      constraintController.setBindingFactory(bf);
      wrapper.setHandler(constraintController);      
      container.addEventHandler(wrapper);
      
      wrapper = GWT.create(SelectedColumnController.class);
      selectedColumnController.setBindingFactory(bf);
      wrapper.setHandler(selectedColumnController);      
      container.addEventHandler(wrapper);

      wrapper = GWT.create(OrderController.class);
      orderController.setBindingFactory(bf);
      wrapper.setHandler(orderController);      
      container.addEventHandler(wrapper);
      
      runner.addContainer(container);
      mainController.setWorkspace(workspace);
      selectedColumnController.setWorkspace(workspace);
      constraintController.setWorkspace(workspace);
      orderController.setWorkspace(workspace);
      

      runner.initialize();
      runner.start();
      RootPanel.get().add(runner.getRootPanel());
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void setService(MetadataService service){
    mainController.setService(service);
    service.getDomainByName("bs", new XulServiceCallback<IDomain>(){

      public void error(String message, Throwable error) {
        int i=0;
      }

      public void success(IDomain retVal) {
        UIDomain domain = new UIDomain(retVal);
        workspace.setDomain(domain);
      }
      
    });
  }
  
  public void bundleLoaded(String bundleName) {
    try {

      RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "mainFrame.xul");

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
  
  public Workspace getWorkspace() {
    return workspace;
  }
}
