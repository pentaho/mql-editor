package org.pentaho.commons.metadata.mqleditor.editor;

import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jmi.xmi.MalformedXMIException;
import javax.swing.JDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
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
import org.pentaho.commons.metadata.mqleditor.utils.ModelSerializer;
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
  
  private Workspace workspace = new Workspace();
  private XulDomContainer container;
  private XulRunner runner = new SwingXulRunner();
  private MQLEditorService service;
  private MQLEditorServiceDelegate delegate;
  private Window parentWindow;
  private boolean initialized = false;
  
  static {
    CWMStartup
    .loadCWMInstance(
        "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public SwingMqlEditor(Window parent) {
    parentWindow = parent;
    init();
  }

  public SwingMqlEditor(MQLEditorService service, SchemaMeta meta) {
    this.delegate = new MQLEditorServiceDelegate(meta);
    init();
    setService(service);
  }
  
  private void setService(MQLEditorService service){
    this.service = service;
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


        try {
          runner.initialize();
        } catch (XulException e) {
          log.error("error starting Xul application", e);
        }
      }

    });
    initialized = true;
  }
  
  public void setXMI(String xmiFile){

    CWM cwm = CWMStartup.loadMetadata(xmiFile,
        "/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$

    SchemaMeta meta = null;

    CwmSchemaFactory factory = new CwmSchemaFactory();
    meta = factory.getSchemaMeta(cwm);

    this.delegate = new MQLEditorServiceDelegate(meta);
    setService(new MQLEditorServiceImpl(meta));

  }
  
  private void init() {
    try {

      SwingXulLoader loader = new SwingXulLoader();
      loader.setOuterContext(parentWindow);
      container =

      loader
          .loadXul("org/pentaho/commons/metadata/mqleditor/editor/public/mainFrame.xul");

      runner.addContainer(container);

      BindingFactory bf = new DefaultBindingFactory();
      bf.setDocument(container.getDocumentRoot());

      mainController.setBindingFactory(bf);
      container.addEventHandler(mainController);

      selectedColumnController.setBindingFactory(bf);
      container.addEventHandler(selectedColumnController);

      constraintController.setBindingFactory(bf);
      container.addEventHandler(constraintController);
      
      orderController.setBindingFactory(bf);
      container.addEventHandler(orderController);

      
      previewController.setBindingFactory(bf);
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

    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById(
        "mqlEditorDialog");
    dialog.show();

  }

  public void setSavedQuery(MQLQuery query) {
    if (query == null) {
      mainController.clearWorkspace();
    } else {
      mainController.setSavedQuery((Query) this.delegate
          .convertModelToThin(query));
    }
  }

  public MQLQuery getMQLQuery() {
    MqlQuery query = workspace.getMqlQuery();

    return delegate.convertModel(query);
  }

  public static void main(String[] args) {

    CWMStartup
        .loadCWMInstance(
            "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWMStartup
        .loadMetadata(
            "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata_steelwheels.xmi", "/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$

    CwmSchemaFactory factory = new CwmSchemaFactory();
    SchemaMeta meta = factory.getSchemaMeta(cwm);

    SwingMqlEditor editor = new SwingMqlEditor(new MQLEditorServiceDebugImpl(
        meta), meta);
    editor.show();
  }

}
