package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.*;
import org.pentaho.commons.metadata.mqleditor.editor.service.CWMStartup;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.mql.MQLQueryImpl;
import org.pentaho.pms.schema.SchemaMeta;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MQLEditorDebugGwtServlet extends RemoteServiceServlet implements MQLEditorGwtService {

  org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceDeligate deligate;
  private SchemaMeta meta;

  public MQLEditorDebugGwtServlet() {

    CWMStartup.loadCWMInstance("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWMStartup.loadMetadata("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata_steelwheels.xmi", "/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$

    List<CWM> cwms = new ArrayList<CWM>();
    cwms.add(cwm);
    CwmSchemaFactory factory = new CwmSchemaFactory();
    meta = factory.getSchemaMeta(cwm);
    deligate = new org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceDeligate(cwms, factory);
  }

  public MqlDomain getDomainByName(String name) {
    return deligate.getDomainByName(name);
  }

  public List<MqlDomain> getMetadataDomains() {
    return deligate.getMetadataDomains();
  }

  public String saveQuery(MqlQuery model) {
    return deligate.saveQuery(model);
  }

  public String serializeModel(MqlQuery query) {
    return deligate.serializeModel(query);
  }
  
  public String[][] getPreviewData(MqlQuery query, int page, int limit) {
    try{
      MQLQuery mqlQuery = this.deligate.convertModel(query);
      
      DatabaseMeta databaseMeta = mqlQuery.getSelections().get(0).getBusinessColumn().getPhysicalColumn().getTable()
          .getDatabaseMeta();
      Database database = new Database(databaseMeta);
      String[][] results = executeSQL(database, mqlQuery.getQuery().getQuery(), limit);
      return results;
    } catch(Exception e){
      // TODO: add logging
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return null;
  }

  private String[][] executeSQL(Database database, String sql, int limit) {
    String[][] queryResults = new String[0][0];
    ResultSet rows = null;

    try {
      database.connect();
      database.setQueryLimit(limit);
      rows = database.openQuery(sql);
      
      int colCount = 0;
      int rowCount = 0;
      List<ArrayList<String>> listofRows = new ArrayList<ArrayList<String>>();
      
      if(rows.next()){
        colCount = rows.getMetaData().getColumnCount();
      }

      do{
        ArrayList<String> row = new ArrayList<String>();
        for(int i=1; i<=colCount; i++){
          row.add(""+rows.getObject(i));
        }
        listofRows.add(row);
        rowCount++;
      } while(rows.next());
      queryResults = new String[rowCount][colCount];
      
      for(int i=0; i< listofRows.size(); i++){
        queryResults[i] = listofRows.get(i).toArray(new String[]{});
      }
      
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (database != null){
        try{
          database.closeQuery(rows);
        } catch (Exception ignored){}
        database.disconnect();
      }
    }
    
    return queryResults;
  }

  public MqlQuery deserializeModel(String serializedQuery) {
    return deligate.deserializeModel(serializedQuery);
  }

}