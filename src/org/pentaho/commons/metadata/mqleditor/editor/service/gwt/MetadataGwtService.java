
package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IBusinessColumn;
import org.pentaho.commons.metadata.mqleditor.ICondition;
import org.pentaho.commons.metadata.mqleditor.IDomain;
import org.pentaho.commons.metadata.mqleditor.IModel;
import org.pentaho.commons.metadata.mqleditor.IOrder;
import org.pentaho.commons.metadata.mqleditor.IQuery;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface MetadataGwtService extends RemoteService{

  List<IDomain> getMetadataDomains();
  IDomain getDomainByName(String name);
  String saveQuery(IModel model, List<IBusinessColumn> cols, List<ICondition> conditions, List<IOrder> orders);
  String serializeModel(IQuery query);
  IQuery deserializeModel(String serializedQuery);
  String[][] getPreviewData(String query, int page, int limit);

}

  