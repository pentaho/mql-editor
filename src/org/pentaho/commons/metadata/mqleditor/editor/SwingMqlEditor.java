package org.pentaho.commons.metadata.mqleditor.editor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.jmi.xmi.MalformedXMIException;

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

/**
 * Default Swing implementation. This class requires a concreate Service implemetation
 */
public class SwingMqlEditor {

  private static Log log = LogFactory.getLog(SwingMqlEditor.class);
  private MainController mainController = new MainController();
  private Workspace workspace = new Workspace();
  private XulDomContainer container;
  private MQLEditorService service;
  private MQLEditorServiceDelegate delegate;
  
  public SwingMqlEditor(String xmiFile){
    CWM cwm = CWM.getInstance("");

    SchemaMeta meta = null;
    InputStream xmiInputStream = CWMStartup.class.getResourceAsStream(xmiFile);
    if (xmiInputStream != null) {
      try {
        cwm.importFromXMI(xmiInputStream);

        CwmSchemaFactory factory = new CwmSchemaFactory();
        meta = factory.getSchemaMeta(cwm);

        this.delegate = new MQLEditorServiceDelegate(meta);
        this.service = new MQLEditorServiceImpl(meta);
      } catch (IOException e) {
        log.error(e);
      } catch (MalformedXMIException e) {
        log.error(e);
      }
    }

    init();
    
  }
  
  public SwingMqlEditor(MQLEditorService service, SchemaMeta meta){

    this.service = service;
    if(meta != null){
      this.delegate = new MQLEditorServiceDelegate(meta);
    }
    
    init();
  }
  private void init(){
    try{
      
      container = new SwingXulLoader().loadXul("org/pentaho/commons/metadata/mqleditor/editor/public/mainFrame.xul");
    
      final XulRunner runner = new SwingXulRunner();
      runner.addContainer(container);
      
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
            

//            Query mqlQuery = (Query) ModelSerializer.deSerialize("{\"MQLQuery\":{\"cols\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"table\":{\"id\":\"mytable\",\"name\":\"mytable\",\"columns\":\"\"},\"type\":[\"TEXT\"]}]},\"conditions\":{\"org.pentaho.commons.metadata.mqleditor.beans.Condition\":[{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"table\":{\"id\":\"mytable\",\"name\":\"mytable\",\"columns\":\"\"},\"type\":[\"TEXT\"]},\"operator\":\"EQUAL\",\"value\":\"myvalue1\",\"comboType\":\"OR\",\"parameterized\":false},{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"table\":{\"id\":\"mytable\",\"name\":\"mytable\",\"columns\":\"\"},\"type\":[\"TEXT\"]},\"operator\":\"EQUAL\",\"value\":\"myvalue2\",\"comboType\":\"OR\",\"parameterized\":false},{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"table\":{\"id\":\"mytable\",\"name\":\"mytable\",\"columns\":\"\"},\"type\":[\"TEXT\"]},\"operator\":\"EQUAL\",\"value\":\"myparameter\",\"comboType\":\"OR\",\"parameterized\":true}]},\"orders\":{\"org.pentaho.commons.metadata.mqleditor.beans.Order\":[{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"table\":{\"id\":\"mytable\",\"name\":\"mytable\",\"columns\":\"\"},\"type\":[\"TEXT\"]},\"orderType\":\"ASC\"}]},\"domain\":{\"id\":\"mydomain\",\"name\":\"mydomain\",\"models\":{\"org.pentaho.commons.metadata.mqleditor.beans.Model\":[{\"categories\":{\"org.pentaho.commons.metadata.mqleditor.beans.Category\":[{\"id\":\"mycategory\",\"name\":\"mycategory\",\"columns\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"table\":{\"id\":\"mytable\",\"name\":\"mytable\",\"columns\":\"\"},\"type\":[\"TEXT\"]}]}}]},\"id\":\"mymodel\",\"name\":\"mymodel\"}]}},\"model\":{\"categories\":{\"org.pentaho.commons.metadata.mqleditor.beans.Category\":[{\"id\":\"mycategory\",\"name\":\"mycategory\",\"columns\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"table\":{\"id\":\"mytable\",\"name\":\"mytable\",\"columns\":\"\"},\"type\":[\"TEXT\"]}]}}]},\"id\":\"mymodel\",\"name\":\"mymodel\"},\"defaultParameterMap\":{\"entry\":[{\"string\":[\"myparameter\",\"myvalue3\"]}]}}}");

            Query query = new Query();
            query.setDomain((Domain)retVal.get(1));
            Model model = ((Domain)retVal.get(1)).getModels().get(0);
            query.setModel(model);
            List<Column> cols = new ArrayList<Column>();
            cols.add(model.getCategories().get(0).getBusinessColumns().get(0));
            query.setColumns(cols);
            mainController.setSavedQuery(query);

          } catch(XulException e){
            log.error("error starting Xul application", e);
          }
        }
        
      });
      
      
    } catch(XulException e){
      log.error("error loading Xul application", e);
    }
  }
  
  public void show(){

    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById("mqlEditorDialog");
    dialog.show();
    
  }
  
  public void setSavedQuery(MQLQuery query){
    if(query == null){
      mainController.clearWorkspace();
    } else {
      mainController.setSavedQuery((Query) this.delegate.convertModelToThin(query));
    }
  }
  
  public MQLQuery getMQLQuery(){
    MqlQuery query = workspace.getMqlQuery();
    
    return delegate.convertModel(query);
  }

  public static void main(String[] args){

    CWMStartup.loadCWMInstance("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWMStartup.loadMetadata("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata_steelwheels.xmi", "/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$

    CwmSchemaFactory factory = new CwmSchemaFactory();
    SchemaMeta meta = factory.getSchemaMeta(cwm);
    
    SwingMqlEditor editor = new SwingMqlEditor(new MQLEditorServiceDebugImpl(meta), meta);
    editor.show();
  }

}
