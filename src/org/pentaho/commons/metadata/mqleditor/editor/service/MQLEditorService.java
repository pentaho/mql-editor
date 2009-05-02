package org.pentaho.commons.metadata.mqleditor.editor.service;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.ui.xul.XulServiceCallback;


/**
 * Interface to the service that the MQL Editor uses for operation. Due to the fact that the MQL Editor may be run
 * in GWT, all service calls use an asynchronous pattern.
 *
 */
public interface MQLEditorService {
  void getMetadataDomains(XulServiceCallback<List<MqlDomain>> callback);
  void getDomainByName(String name, XulServiceCallback<MqlDomain> callback);
  void saveQuery(MqlQuery model, XulServiceCallback<String> callback);
  void serializeModel(MqlQuery query, XulServiceCallback<String> callback);
  void deserializeModel(String serializedQuery, XulServiceCallback<MqlQuery> callback);
  void getPreviewData(String query, int page, int limit, XulServiceCallback<String[][]> callback);
}

  