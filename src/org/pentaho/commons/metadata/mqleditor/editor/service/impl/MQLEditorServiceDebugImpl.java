package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.pentaho.commons.metadata.mqleditor.*;
import org.pentaho.commons.metadata.mqleditor.editor.service.CWMStartup;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.di.core.database.Database;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.metadata.repository.FileBasedMetadataDomainRepository;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.ui.xul.XulServiceCallback;

public class MQLEditorServiceDebugImpl implements MQLEditorService{

  MQLEditorServiceDeligate deligate;

  public MQLEditorServiceDebugImpl(SchemaMeta meta){

    deligate = new MQLEditorServiceDeligate(meta);

    // this is normally provided by PentahoSystem or the metadata editor.
    FileBasedMetadataDomainRepository repo = new FileBasedMetadataDomainRepository();
    repo.setDomainFolder("src/org/pentaho/commons/metadata/mqleditor/sampleMql/thinmodels");
    deligate.initializeThinMetadataDomains(repo);
  }

  public void getDomainByName(String name, XulServiceCallback<MqlDomain> callback) {
    callback.success(deligate.getDomainByName(name));
  }

  public void refreshMetadataDomains(XulServiceCallback<List<MqlDomain>> callback) {
    callback.success(deligate.refreshMetadataDomains());
  }
  
  public void getMetadataDomains(XulServiceCallback<List<MqlDomain>> callback) {
    callback.success(deligate.getMetadataDomains());
  }

  public void saveQuery(MqlQuery model, XulServiceCallback<String> callback) {
    callback.success(deligate.saveQuery(model));
  }

  public void serializeModel(MqlQuery query, XulServiceCallback<String> callback) {
    callback.success(deligate.serializeModel(query));
  }

  public void getPreviewData(MqlQuery query, int page, int limit, XulServiceCallback<String[][]> callback) {
      try{
        MQLQuery mqlQuery = this.deligate.convertModel(query);
        
        DatabaseMeta databaseMeta = mqlQuery.getSelections().get(0).getBusinessColumn().getPhysicalColumn().getTable()
            .getDatabaseMeta();
        Database database = new Database(databaseMeta);
        
        String sqlQuery = mqlQuery.getQuery().getQuery();
        
        Map<String, String> params = query.getDefaultParameterMap();
        for(Map.Entry<String, String> entry : params.entrySet()){
          sqlQuery = sqlQuery.replaceAll("\\{"+entry.getKey()+"\\}", entry.getValue());
        }
        
        String[][] results = executeSQL(database, sqlQuery, limit);
        callback.success(results);
        return;
      } catch(Exception e){
        // TODO: add logging
        System.out.println(e.getMessage());
        e.printStackTrace();
        callback.error("error fetching results", new Throwable("unknown error"));
      }
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

  public void deserializeModel(String serializedQuery, XulServiceCallback<MqlQuery> callback) {
    callback.success(deligate.deserializeModel(serializedQuery));
  }
  
  
  
}

  