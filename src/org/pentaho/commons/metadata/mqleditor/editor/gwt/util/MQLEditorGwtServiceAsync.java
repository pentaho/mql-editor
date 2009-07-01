package org.pentaho.commons.metadata.mqleditor.editor.gwt.util;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.*;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MQLEditorGwtServiceAsync {

  void getMetadataDomains(AsyncCallback<List<MqlDomain>> callback);
  void refreshMetadataDomains(AsyncCallback<List<MqlDomain>> callback);
  void getDomainByName(String name, AsyncCallback<MqlDomain> callback);
  void saveQuery(MqlQuery model, AsyncCallback<String> callback);
  void serializeModel(MqlQuery query, AsyncCallback<String> callback);
  void deserializeModel(String serializedQuery, AsyncCallback<MqlQuery> callback);
  void getPreviewData(MqlQuery query, int page, int limit, AsyncCallback<String[][]> callback);
}

  