package org.pentaho.commons.metadata.mqleditor.editor.service;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IBusinessColumn;
import org.pentaho.commons.metadata.mqleditor.ICondition;
import org.pentaho.commons.metadata.mqleditor.IDomain;
import org.pentaho.commons.metadata.mqleditor.IModel;
import org.pentaho.commons.metadata.mqleditor.IOrder;
import org.pentaho.commons.metadata.mqleditor.IQuery;
import org.pentaho.ui.xul.XulServiceCallback;

public interface MetadataService {
  void getMetadataDomains(XulServiceCallback<List<IDomain>> callback);
  void getDomainByName(String name, XulServiceCallback<IDomain> callback);
  void saveQuery(IModel model, List<? extends IBusinessColumn> cols, List<? extends ICondition> conditions, List<? extends IOrder> orders, XulServiceCallback<String> callback);
  void serializeModel(IQuery query, XulServiceCallback<String> callback);
  void deserializeModel(String serializedQuery, XulServiceCallback<IQuery> callback);
  void getPreviewData(String query, int page, int limit, XulServiceCallback<String[][]> callback);
}

  