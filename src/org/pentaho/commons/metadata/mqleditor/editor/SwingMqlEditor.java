package org.pentaho.commons.metadata.mqleditor.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConditionsController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.MainController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.OrderController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.PreviewController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.SelectedColumnController;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceDebugImpl;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.swing.SwingXulLoader;
import org.pentaho.ui.xul.swing.SwingXulRunner;

/**
 * Default Swing implementation. This class requires a concreate Service implemetation
 */
public class SwingMqlEditor {

  private static Log log = LogFactory.getLog(SwingMqlEditor.class);
  private MainController mainController = new MainController();
  private Workspace workspace = new Workspace();

  public SwingMqlEditor(MQLEditorService service, List<String> availableFilters){
    try{
      XulDomContainer container = new SwingXulLoader().loadXul("org/pentaho/commons/metadata/mqleditor/editor/public/mainFrame.xul");
    
      final XulRunner runner = new SwingXulRunner();
      runner.addContainer(container);

      this.setAvailableFilters(availableFilters);
      
      BindingFactory bf = new DefaultBindingFactory();
      bf.setDocument(container.getDocumentRoot());

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
      

      mainController.setService(service);
      previewController.setService(service);
      
      service.getMetadataDomains(new XulServiceCallback<List<MqlDomain>>(){

        public void error(String message, Throwable error) {
          
        }

        public void success(List<MqlDomain> retVal) {
          
          List<UIDomain> uiDomains = new ArrayList<UIDomain>();
          for(MqlDomain d : retVal){
            uiDomains.add(new UIDomain((Domain) d));
          }


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
    List<String> filters = new ArrayList<String>();
    filters.add("Filter 1");
    filters.add("Filter 2");
    filters.add("Filter 3");
    filters.add("Filter 4");
    SwingMqlEditor editor = new SwingMqlEditor(new MQLEditorServiceDebugImpl(), filters);
  }

  public void setAvailableFilters(List<String> filters){
    this.workspace.setAvailableFilters(filters);
  }
  
}
