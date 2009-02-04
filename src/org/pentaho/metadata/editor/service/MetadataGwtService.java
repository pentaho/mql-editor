
package org.pentaho.metadata.editor.service;

import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface MetadataGwtService extends RemoteService{

  String[] getMetadataDomains();
  IDomain getDomainByName(String name);
  String saveQuery(IModel model, List<IBusinessColumn> cols, List<ICondition> conditions, List<IOrder> orders);
}

  