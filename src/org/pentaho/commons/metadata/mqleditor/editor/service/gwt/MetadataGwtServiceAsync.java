package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IBusinessColumn;
import org.pentaho.commons.metadata.mqleditor.ICondition;
import org.pentaho.commons.metadata.mqleditor.IDomain;
import org.pentaho.commons.metadata.mqleditor.IModel;
import org.pentaho.commons.metadata.mqleditor.IOrder;
import org.pentaho.commons.metadata.mqleditor.IQuery;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MetadataGwtServiceAsync {

  void getMetadataDomains(AsyncCallback<List<IDomain>> callback);
  void getDomainByName(String name, AsyncCallback<IDomain> callback);
  void saveQuery(IModel model, List<? extends IBusinessColumn> cols, List<? extends ICondition> conditions, List<? extends IOrder> orders, AsyncCallback<String> callback);
  void serializeModel(IQuery query, AsyncCallback<String> callback);
  void deserializeModel(String serializedQuery, AsyncCallback<IQuery> callback);
  void getPreviewData(String query, int page, int limit, AsyncCallback<String[][]> callback);
}

  