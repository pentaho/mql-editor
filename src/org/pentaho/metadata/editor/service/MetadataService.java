package org.pentaho.metadata.editor.service;

import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.IQuery;
import org.pentaho.ui.xul.XulServiceCallback;

public interface MetadataService {
  void getMetadataDomains(XulServiceCallback<List<IDomain>> callback);
  void getDomainByName(String name, XulServiceCallback<IDomain> callback);
  void saveQuery(IModel model, List<? extends IBusinessColumn> cols, List<? extends ICondition> conditions, List<? extends IOrder> orders, XulServiceCallback<String> callback);
  void serializeModel(IQuery query, XulServiceCallback<String> callback);
}

  