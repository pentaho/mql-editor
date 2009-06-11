package org.pentaho.commons.metadata.mqleditor.editor;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConditionsController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.MainController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.OrderController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.PreviewController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.SelectedColumnController;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.gwt.widgets.client.utils.IMessageBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.MessageBundle;
import org.pentaho.gwt.widgets.client.utils.i18n.IResourceBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.gwt.GwtXulDomContainer;
import org.pentaho.ui.xul.gwt.GwtXulLoader;
import org.pentaho.ui.xul.gwt.GwtXulRunner;
import org.pentaho.ui.xul.gwt.binding.GwtBindingFactory;
import org.pentaho.ui.xul.gwt.util.AsyncConstructorListener;
import org.pentaho.ui.xul.gwt.util.EventHandlerWrapper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.XMLParser;

public class GwtMqlEditor implements IResourceBundleLoadCallback {

  private ResourceBundle bundle;
  private MainController mainController = new MainController();
  private Workspace workspace = new Workspace();
  private GwtXulDomContainer container;
  SelectedColumnController selectedColumnController = new SelectedColumnController();
  ConditionsController constraintController = new ConditionsController();
  OrderController orderController = new OrderController();
  PreviewController previewController = new PreviewController();
  private AsyncConstructorListener constructorListener;
  private List<MqlDialogListener> listeners = new ArrayList<MqlDialogListener>();
  private MQLEditorService service;
  
  public GwtMqlEditor(final MQLEditorService service, final AsyncConstructorListener constructorListener){
    mainController.setWorkspace(workspace);
    selectedColumnController.setWorkspace(workspace);
    constraintController.setWorkspace(workspace);
    orderController.setWorkspace(workspace);
    previewController.setWorkspace(workspace);
    this.constructorListener = constructorListener;
    setService(service);
  }
  
  /**
   * Set the selected domain using the domain ID.
   */
  public void setSelectedDomainId(String domainId) {
    workspace.setSelectedDomainId(domainId);
  }
  
  public void setSelectedModelId(String modelId) {
    workspace.setSelectedModelId(modelId);
  }
  
  public void setSavedQuery(Query savedQuery) {
    mainController.setSavedQuery(savedQuery);  
  }
  
  public String getMqlQuery(){
    return workspace.getMqlStr();
  }
  
  public void show(){
    mainController.showDialog();
  }

  public void hide(){
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("mqlEditorDialog");
    dialog.hide();
  }
  
  public void addMqlDialogListener(MqlDialogListener listener){
    if(this.listeners.contains(listener) == false){
      this.listeners.add(listener);
    }
    mainController.addMqlDialogListener(listener);
  }
  
  public void removeMqlDialogListener(MqlDialogListener listener){
    if(this.listeners.contains(listener)){
      this.listeners.remove(listener);
    }
    mainController.removeMqlDialogListener(listener);
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

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "mainFrame-gwt-overlay.xul");

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
      Window.alert("Error Loading MQLEditor Xul file: "+e.getMessage());
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

    try{
      final GwtXulRunner runner = new GwtXulRunner();
    
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
      
      wrapper = GWT.create(PreviewController.class);
      previewController.setBindingFactory(bf);
      wrapper.setHandler(previewController);      
      container.addEventHandler(wrapper);
      
      
      runner.addContainer(container);
      
      runner.initialize();
      runner.start();

      
      RootPanel.get().add(runner.getRootPanel());
      if (constructorListener != null) {
        constructorListener.asyncConstructorDone();
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void updateDomainList(){
    service.refreshMetadataDomains(new XulServiceCallback<List<MqlDomain>>() {

      public void error(String message, Throwable error) {
        Window.alert("could not get list of metadata domains");
      }

      public void success(List<MqlDomain> domains) {
        updateDomains(domains);
        for(MqlDialogListener listener : listeners){
          listener.onDialogReady();
        }
      }
      
    });
  }
  
  private void setService(MQLEditorService service){
    this.service = service;
    previewController.setService(service);
    mainController.setService(service);
    service.getMetadataDomains(new XulServiceCallback<List<MqlDomain>>() {

      public void error(String message, Throwable error) {
        Window.alert("could not get list of metadata domains");
      }

      public void success(List<MqlDomain> domains) {
        updateDomains(domains);
        
        for(MqlDialogListener listener : listeners){
          listener.onDialogReady();
        }
        
        try {
          bundle = new ResourceBundle(GWT.getModuleBaseURL(),"mainFrame", true, GwtMqlEditor.this );
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      
    });
    
  }
  
  public void updateDomains(List<MqlDomain> domains) {
    List<UIDomain> uiDomains = new ArrayList<UIDomain>();
    for (MqlDomain domain : domains) {
      uiDomains.add(new UIDomain((Domain) domain));
    }
    workspace.setDomains(uiDomains);
    
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
  
  public Workspace getWorkspace() {
    return workspace;
  }

}
