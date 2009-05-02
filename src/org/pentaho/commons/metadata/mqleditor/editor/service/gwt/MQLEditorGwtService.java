
package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface MQLEditorGwtService extends RemoteService{

  List<MqlDomain> getMetadataDomains();
  MqlDomain getDomainByName(String name);
  String saveQuery(MqlQuery model);
  String serializeModel(MqlQuery query);
  MqlQuery deserializeModel(String serializedQuery);
  String[][] getPreviewData(String query, int page, int limit);

}

  