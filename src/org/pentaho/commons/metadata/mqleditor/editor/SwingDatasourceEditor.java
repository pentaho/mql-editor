package org.pentaho.commons.metadata.mqleditor.editor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConnectionController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.CsvDatasourceController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.DatasourceController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.RelationalDatasourceController;
import org.pentaho.commons.metadata.mqleditor.editor.models.ConnectionModel;
import org.pentaho.commons.metadata.mqleditor.editor.models.DatasourceModel;
import org.pentaho.commons.metadata.mqleditor.editor.service.ConnectionService;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceService;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.ConnectionServiceDebugImpl;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.DatasourceServiceDebugImpl;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.swing.SwingXulLoader;
import org.pentaho.ui.xul.swing.SwingXulRunner;

public class SwingDatasourceEditor {

  private static Log log = LogFactory.getLog(SwingDatasourceEditor.class);
  
  public SwingDatasourceEditor(){
    try{
      XulDomContainer container = new SwingXulLoader().loadXul("org/pentaho/commons/metadata/mqleditor/editor/public/connectionFrame.xul");
    
      final XulRunner runner = new SwingXulRunner();
      runner.addContainer(container);
      
      
      BindingFactory bf = new DefaultBindingFactory();
      bf.setDocument(container.getDocumentRoot());
      
    
      final DatasourceController datasourceController = new DatasourceController();
      datasourceController.setBindingFactory(bf);
      container.addEventHandler(datasourceController);

      final CsvDatasourceController csvDatasourceController = new CsvDatasourceController();
      csvDatasourceController.setBindingFactory(bf);
      container.addEventHandler(csvDatasourceController);

      final RelationalDatasourceController relationalDatasourceController = new RelationalDatasourceController();
      relationalDatasourceController.setBindingFactory(bf);
      container.addEventHandler(relationalDatasourceController);

      final ConnectionController connectionController = new ConnectionController();
      connectionController.setBindingFactory(bf);
      container.addEventHandler(connectionController);
      
      ConnectionService service = new ConnectionServiceDebugImpl();
      connectionController.setService(service);

      DatasourceService datasourceService = new DatasourceServiceDebugImpl();
      datasourceController.setService(datasourceService);
      
      service.getConnections(new XulServiceCallback<List<IConnection>>(){

        public void error(String message, Throwable error) {
          System.out.println(error.getLocalizedMessage());
        }

        public void success(List<IConnection> connections) {
          DatasourceModel datasourceModel = new DatasourceModel();
          ConnectionModel connectionModel = new ConnectionModel();
          datasourceModel.getRelationalModel().setConnections(connections);
          datasourceController.setDatasourceModel(datasourceModel);
          datasourceController.setConnectionModel(connectionModel);
          connectionController.setDatasourceModel(datasourceModel);
          connectionController.setConnectionModel(connectionModel);

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
    new SwingDatasourceEditor();
  }
  
}
