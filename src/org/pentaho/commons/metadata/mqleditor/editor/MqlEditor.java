package org.pentaho.commons.metadata.mqleditor.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.commons.metadata.mqleditor.IDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConditionsController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.MainController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.OrderController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.PreviewController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.SelectedColumnController;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.MetadataService;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.MetadataServiceImpl;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.swing.SwingXulLoader;
import org.pentaho.ui.xul.swing.SwingXulRunner;

public class MqlEditor {

  private static Log log = LogFactory.getLog(MqlEditor.class);
  
  public MqlEditor(){
    try{
      XulDomContainer container = new SwingXulLoader().loadXul("org/pentaho/commons/metadata/mqleditor/editor/public/mainFrame.xul");
    
      final XulRunner runner = new SwingXulRunner();
      runner.addContainer(container);
      
      
      BindingFactory bf = new DefaultBindingFactory();
      bf.setDocument(container.getDocumentRoot());
      
      final MainController mainController = new MainController();
      mainController.setBindingFactory(bf);
      container.addEventHandler(mainController);
      
      final SelectedColumnController selectedColumnController = new SelectedColumnController();
      selectedColumnController.setBindingFactory(bf);
      container.addEventHandler(selectedColumnController);

      final ConditionsController constraintController = new ConditionsController();
      constraintController.setBindingFactory(bf);
      container.addEventHandler(constraintController);
      
      final OrderController orderController = new OrderController();
      orderController.setBindingFactory(bf);
      container.addEventHandler(orderController);
      
      final PreviewController previewController = new PreviewController();
      previewController.setBindingFactory(bf);
      container.addEventHandler(previewController);
      
      MetadataService service = new MetadataServiceImpl();
      mainController.setService(service);
      previewController.setService(service);
      
      service.getMetadataDomains(new XulServiceCallback<List<IDomain>>(){

        public void error(String message, Throwable error) {
          
        }

        public void success(List<IDomain> retVal) {
          
          List<UIDomain> uiDomains = new ArrayList<UIDomain>();
          for(IDomain d : retVal){
            uiDomains.add(new UIDomain((Domain) d));
          }
          
          Workspace workspace = new Workspace();

          workspace.setDomains(uiDomains);

          mainController.setWorkspace(workspace);
          selectedColumnController.setWorkspace(workspace);
          constraintController.setWorkspace(workspace);
          orderController.setWorkspace(workspace);
          previewController.setWorkspace(workspace);
          
          try{
            runner.initialize();
            runner.start();
          } catch(XulException e){
            log.error("error starting Xul application", e);
          }
        }
        
      });
      
      
    } catch(XulException e){
      log.error("error loading Xul application", e);
    }
  }
  
  public static void main(String[] args){
    new MqlEditor();
  }
  
}
