package org.pentaho.metadata.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.beans.Domain;
import org.pentaho.metadata.editor.models.UIDomain;
import org.pentaho.metadata.editor.service.MetadataService;
import org.pentaho.metadata.editor.service.MetadataServiceImpl;
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
      XulDomContainer container = new SwingXulLoader().loadXul("org/pentaho/metadata/editor/public/mainFrame.xul");
    
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
      
      MetadataService service = new MetadataServiceImpl();
      mainController.setService(service);
      
      service.getMetadataDomains(new XulServiceCallback<List<IDomain>>(){

        public void error(String message, Throwable error) {
          
        }

        public void success(List<IDomain> retVal) {
          
          List<UIDomain> uiDomains = new ArrayList<UIDomain>();
          for(IDomain d : retVal){
            uiDomains.add(new UIDomain((Domain) d));
          }
          
          Workspace workspace = new Workspace();

          // Currently there's only one domain supported.
          if(uiDomains.size() > 0){
            workspace.setDomain(uiDomains.get(0));
          }
          mainController.setWorkspace(workspace);
          selectedColumnController.setWorkspace(workspace);
          constraintController.setWorkspace(workspace);
          orderController.setWorkspace(workspace);
          
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
