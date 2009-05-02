package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MQLEditorGwtServiceAsync {

  void getMetadataDomains(AsyncCallback<List<MqlDomain>> callback);
  void getDomainByName(String name, AsyncCallback<MqlDomain> callback);
  void saveQuery(MqlQuery model, AsyncCallback<String> callback);
  void serializeModel(MqlQuery query, AsyncCallback<String> callback);
  void deserializeModel(String serializedQuery, AsyncCallback<MqlQuery> callback);
  void getPreviewData(String query, int page, int limit, AsyncCallback<String[][]> callback);
}

  