
package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.*;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface MQLEditorGwtService extends RemoteService{

  List<MqlDomain> getMetadataDomains();
  MqlDomain getDomainByName(String name);
  String saveQuery(MqlModel model, List<MqlColumn> cols, List<MqlCondition> conditions, List<MqlOrder> orders);
  String serializeModel(MqlQuery query);
  MqlQuery deserializeModel(String serializedQuery);
  String[][] getPreviewData(String query, int page, int limit);

}

  