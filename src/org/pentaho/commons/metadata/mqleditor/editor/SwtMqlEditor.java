package org.pentaho.commons.metadata.mqleditor.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConditionsController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.MainController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.OrderController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.PreviewController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.SelectedColumnController;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.CWMStartup;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceDebugImpl;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceDelegate;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceImpl;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.components.XulButton;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.swt.SwtBindingFactory;
import org.pentaho.ui.xul.swt.SwtXulLoader;
import org.pentaho.ui.xul.swt.SwtXulRunner;

/**
 * Default Swt implementation. This class requires a concreate Service
 * implemetation
 */
public class SwtMqlEditor {

  private static Log log = LogFactory.getLog(SwingMqlEditor.class);
  private MainController mainController = new MainController();
  private Workspace workspace = new Workspace();
  private XulDomContainer container;
  private MQLEditorService service;
  private MQLEditorServiceDelegate delegate;
  private static Log logger = LogFactory.getLog(SwtMqlEditor.class);

  public SwtMqlEditor(MQLEditorService service, SchemaMeta meta) {
    try {
      this.service = service;
      if(meta != null){
        this.delegate = new MQLEditorServiceDelegate(meta);
      }
      
      container = new SwtXulLoader()
          .loadXul("org/pentaho/commons/metadata/mqleditor/editor/public/mainFrame.xul");

      final XulRunner runner = new SwtXulRunner();
      runner.addContainer(container);

      BindingFactory bf = new SwtBindingFactory();
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

      service.getMetadataDomains(new XulServiceCallback<List<MqlDomain>>() {

        public void error(String message, Throwable error) {

        }

        public void success(List<MqlDomain> retVal) {

          List<UIDomain> uiDomains = new ArrayList<UIDomain>();
          for (MqlDomain d : retVal) {
            uiDomains.add(new UIDomain((Domain) d));
          }

          workspace.setDomains(uiDomains);

          mainController.setWorkspace(workspace);
          selectedColumnController.setWorkspace(workspace);
          constraintController.setWorkspace(workspace);
          orderController.setWorkspace(workspace);
          previewController.setWorkspace(workspace);

          try {
            runner.initialize();
          } catch (XulException e) {
            log.error("error starting Xul application", e);
          }
        }

      });

    } catch (XulException e) {
      log.error("error loading Xul application", e);
    }
  }

  public Composite getDialogArea(){
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("mqlEditorDialog");
    return (Composite) dialog.getManagedObject();
  }
  
  public void show(){
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("mqlEditorDialog");
    dialog.show();
    
  }
  
  public static void main(String[] args) {
    

    CWMStartup.loadCWMInstance("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWMStartup.loadMetadata("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata_steelwheels.xmi", "/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$

    CwmSchemaFactory factory = new CwmSchemaFactory();
    SchemaMeta meta = factory.getSchemaMeta(cwm);

    SwtMqlEditor editor = new SwtMqlEditor(new MQLEditorServiceDebugImpl(meta), null);
    editor.show();
  }
  
  public void setMqlQuery(MQLQuery query){
    if(query == null){
      mainController.clearWorkspace();
    } else {
      mainController.setSavedQuery((Query) this.delegate.convertModelToThin(query));
    }
  }
  
  public MQLQuery getMqlQuery(){
    return delegate.convertModel(workspace.getMqlQuery());
    
  }

  public void hidePreview(){
   ((Control)  ((XulButton) container.getDocumentRoot().getElementById("previewBtn")).getManagedObject()).dispose();
  }
}
