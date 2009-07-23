package org.pentaho.commons.metadata.mqleditor.editor;

import java.awt.Window;
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
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorServiceImpl;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceDelegate;
import org.pentaho.metadata.query.model.Query;
import org.pentaho.metadata.query.model.util.QueryXmlHelper;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.pms.core.exception.PentahoMetadataException;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.swing.SwingXulLoader;
import org.pentaho.ui.xul.swing.SwingXulRunner;
import org.pentaho.ui.xul.swing.tags.SwingDialog;

/**
 * Default Swing implementation. This class requires a concreate Service
 * implemetation
 */
public class SwingMqlEditor {

  private static Log log = LogFactory.getLog(SwingMqlEditor.class);
  private MainController mainController = new MainController();
  private SelectedColumnController selectedColumnController = new SelectedColumnController();
  private ConditionsController constraintController = new ConditionsController();
  private OrderController orderController = new OrderController();
  private PreviewController previewController = new PreviewController();
  private IMetadataDomainRepository repo;
  
  private Workspace workspace = new Workspace();
  private XulDomContainer container;
  private XulRunner runner = new SwingXulRunner();
  private MQLEditorServiceDelegate delegate;
  private Window parentWindow;
    
  public SwingMqlEditor(Window parent, IMetadataDomainRepository repo) {
    parentWindow = parent;
    init();
    setService(new MQLEditorServiceImpl(repo));
  }
  
  
  public SwingMqlEditor(IMetadataDomainRepository repo) {
    init();
    this.repo = repo;
    setService(new MQLEditorServiceImpl(repo));
    delegate = new MQLEditorServiceDelegate(repo);
  }
  
  private void setService(MQLEditorService service){
    mainController.setService(service);
    previewController.setService(service);

    service.getMetadataDomains(new XulServiceCallback<List<MqlDomain>>() {

      public void error(String message, Throwable error) {
        log.error("Error loading Metadata Domain list",error);
      }

      public void success(List<MqlDomain> retVal) {

        List<UIDomain> uiDomains = new ArrayList<UIDomain>();
        // Wrap Beans as UI peers
        for (MqlDomain d : retVal) {
          uiDomains.add(new UIDomain((Domain) d));
        }
        workspace.setDomains(uiDomains);

        try {
          runner.initialize();
        } catch (XulException e) {
          log.error("error starting Xul application", e);
        }
      }

    });
  }
  
  private void init() {
    try {

      SwingXulLoader loader = new SwingXulLoader();
      loader.setOuterContext(parentWindow);
      container = loader.loadXul("org/pentaho/commons/metadata/mqleditor/editor/xul/mainFrame.xul");

      runner.addContainer(container);

      BindingFactory bf = new DefaultBindingFactory();
      bf.setDocument(container.getDocumentRoot());

      mainController.setBindingFactory(bf);
      selectedColumnController.setBindingFactory(bf);
      constraintController.setBindingFactory(bf);      
      orderController.setBindingFactory(bf);
      previewController.setBindingFactory(bf);
      
      container.addEventHandler(mainController);
      container.addEventHandler(selectedColumnController);
      container.addEventHandler(constraintController);
      container.addEventHandler(orderController);
      container.addEventHandler(previewController);

      mainController.setWorkspace(workspace);
      selectedColumnController.setWorkspace(workspace);
      constraintController.setWorkspace(workspace);
      orderController.setWorkspace(workspace);
      previewController.setWorkspace(workspace);
      
    } catch (XulException e) {
      log.error("error loading Xul application", e);
    }
  }

  public void show() {
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("mqlEditorDialog");
    dialog.show();
  }

  public void setQuery(String query) throws PentahoMetadataException {
    if (query == null) {
      mainController.clearWorkspace();
    } else {
      QueryXmlHelper helper = new QueryXmlHelper();
      Query queryObject = helper.fromXML(repo, query);
      //mainController.setSavedQuery((Query) this.delegate.convertModelToThin(query));
    }
  }
  
  public String getQuery() {
    return delegate.saveQuery(workspace.getMqlQuery());
  }
  
  public void hidePreview(){
    SwingDialog dialog = (SwingDialog) container.getDocumentRoot().getElementById("mqlEditorDialog");
    dialog.setButtons("accept,cancel");
   }

}
