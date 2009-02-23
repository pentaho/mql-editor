
package org.pentaho.metadata.editor.service.gwt;

import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.IQuery;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface MetadataGwtService extends RemoteService{

  List<IDomain> getMetadataDomains();
  IDomain getDomainByName(String name);
  String saveQuery(IModel model, List<IBusinessColumn> cols, List<ICondition> conditions, List<IOrder> orders);
  String serialzieModel(IQuery query);
}

  