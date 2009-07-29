
package org.pentaho.commons.metadata.mqleditor.editor.gwt.util;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.*;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
     
public interface MQLEditorGwtService extends RemoteService{

  List<MqlDomain>  refreshMetadataDomains();
  List<MqlDomain> getMetadataDomains();
  MqlDomain getDomainByName(String name);
  String saveQuery(MqlQuery model);
  String serializeModel(MqlQuery query);
  MqlQuery deserializeModel(String serializedQuery);
  String[][] getPreviewData(MqlQuery query, int page, int limit);

}

  