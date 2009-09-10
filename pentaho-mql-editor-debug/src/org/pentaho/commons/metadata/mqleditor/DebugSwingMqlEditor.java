package org.pentaho.commons.metadata.mqleditor;

import java.io.InputStream;

import org.pentaho.commons.connection.IPentahoResultSet;
import org.pentaho.commons.metadata.mqleditor.editor.SwingMqlEditor;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorServiceImpl;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceDelegate;
import org.pentaho.metadata.query.model.util.QueryXmlHelper;
import org.pentaho.metadata.repository.FileBasedMetadataDomainRepository;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.metadata.util.XmiParser;
import org.pentaho.platform.api.data.IDatasourceService;
import org.pentaho.platform.api.engine.ISolutionEngine;
import org.pentaho.platform.api.engine.IPentahoDefinableObjectFactory.Scope;
import org.pentaho.platform.api.repository.ISolutionRepository;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.core.system.StandaloneSession;
import org.pentaho.platform.engine.services.connection.datasource.dbcp.JndiDatasourceService;
import org.pentaho.platform.engine.services.solution.SolutionEngine;
import org.pentaho.platform.plugin.action.pentahometadata.MetadataQueryComponent;
import org.pentaho.platform.plugin.services.connections.sql.SQLConnection;
import org.pentaho.platform.repository.solution.filebased.FileBasedSolutionRepository;
import org.pentaho.test.platform.engine.core.MicroPlatform;
import org.pentaho.ui.xul.XulServiceCallback;

/**
 * Default Swing implementation. This class requires a concreate Service
 * implemetation
 */
public class DebugSwingMqlEditor {


  public static void main(String[] args) {


    // initialize micro platorm
    MicroPlatform microPlatform = new MicroPlatform("resources/solution1/"); //$NON-NLS-1$
    microPlatform.define(ISolutionEngine.class, SolutionEngine.class);
    microPlatform.define(ISolutionRepository.class, FileBasedSolutionRepository.class);
    microPlatform.define(IMetadataDomainRepository.class, FileBasedMetadataDomainRepository.class, Scope.GLOBAL); 
    microPlatform.define("connection-SQL", SQLConnection.class);  //$NON-NLS-1$

    microPlatform.define(IDatasourceService.class, JndiDatasourceService.class, Scope.GLOBAL);
    // JNDI
    System.setProperty("java.naming.factory.initial", "org.osjava.sj.SimpleContextFactory"); //$NON-NLS-1$ //$NON-NLS-2$
    System.setProperty("org.osjava.sj.root", "resources/solution1/simple-jndi"); //$NON-NLS-1$ //$NON-NLS-2$
    System.setProperty("org.osjava.sj.delimiter", "/"); //$NON-NLS-1$ //$NON-NLS-2$
    
    microPlatform.init();
    new StandaloneSession();

    FileBasedMetadataDomainRepository repo = (FileBasedMetadataDomainRepository) PentahoSystem.get(IMetadataDomainRepository.class, null);
    repo.setDomainFolder("resources/solution1/system/metadata/domains"); //$NON-NLS-1$
    
    // Parse and add legacy CWM domain for testing purposes.
    XmiParser parser = new XmiParser();
    try {
      InputStream inStr = SwingMqlEditor.class.getResourceAsStream("/metadata_steelwheels.xmi"); //$NON-NLS-1$
      if(inStr != null){
        org.pentaho.metadata.model.Domain d = parser.parseXmi(inStr);
        d.setId("Steel-Wheels");  //$NON-NLS-1$
        repo.storeDomain(d, false);
        repo.reloadDomains();
        
      } else {
        System.out.println("Error loading XMI file");
       //  System.exit(1);
      }
    } catch (Exception e) {
      System.out.println("error with XMI input"); //$NON-NLS-1$
    }
    
    MQLEditorServiceDelegate delegate = new MQLEditorServiceDelegate(repo) {
      @Override
      public String[][] getPreviewData(MqlQuery query, int page, int limit) {
        org.pentaho.metadata.query.model.Query mqlQuery = convertQueryModel(query);
        MetadataQueryComponent component = new MetadataQueryComponent();
        String mqlString = new QueryXmlHelper().toXML(mqlQuery);
        component.setQuery(mqlString);
        component.setLive(true);
        IPentahoResultSet rs = null;
        try{
          if (component.execute()) {
            rs = component.getResultSet();
            String[][] results = new String[Math.min(rs.getRowCount(), limit)][rs.getColumnCount()];
            
            for (int i = 0; i < Math.min(rs.getRowCount(), limit); i++) {
              for (int j = 0; j < rs.getColumnCount(); j++) {
                results[i][j] = "" + rs.getValueAt(((page-1) * limit)+i, j); //$NON-NLS-1$
              }
            }
            return results;
          } else {
            return null;
          }
        } finally{
          if(rs != null){
            rs.close();
          }
        }
      }
    };

    MQLEditorServiceImpl service = new MQLEditorServiceImpl(delegate) {
      @Override
      public void getPreviewData(MqlQuery query, int page, int limit, XulServiceCallback<String[][]> callback) {
        callback.success(delegate.getPreviewData(query, page, limit));
      }
    };
    
    
    SwingMqlEditor editor = new SwingMqlEditor(repo, service, delegate);
    //editor.hidePreview();
    editor.show();
  }
  
}
