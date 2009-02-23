package org.pentaho.metadata.editor.service.gwt;

import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.IQuery;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MetadataGwtServiceAsync {

  void getMetadataDomains(AsyncCallback<List<IDomain>> callback);
  void getDomainByName(String name, AsyncCallback<IDomain> callback);
  void saveQuery(IModel model, List<? extends IBusinessColumn> cols, List<? extends ICondition> conditions, List<? extends IOrder> orders, AsyncCallback<String> callback);
  void serialzieModel(IQuery query, AsyncCallback<String> callback);
}

  