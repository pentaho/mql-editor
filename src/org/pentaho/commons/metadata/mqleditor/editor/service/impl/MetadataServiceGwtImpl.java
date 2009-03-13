package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IBusinessColumn;
import org.pentaho.commons.metadata.mqleditor.ICondition;
import org.pentaho.commons.metadata.mqleditor.IDomain;
import org.pentaho.commons.metadata.mqleditor.IModel;
import org.pentaho.commons.metadata.mqleditor.IOrder;
import org.pentaho.commons.metadata.mqleditor.IQuery;
import org.pentaho.commons.metadata.mqleditor.editor.service.MetadataService;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.MetadataGwtService;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.MetadataGwtServiceAsync;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MetadataServiceGwtImpl implements MetadataService {

  static MetadataGwtServiceAsync SERVICE;

  static {

    SERVICE = (MetadataGwtServiceAsync) GWT.create(MetadataGwtService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    String moduleRelativeURL = GWT.getModuleBaseURL() + "MqlService"; //$NON-NLS-1$
    endpoint.setServiceEntryPoint(moduleRelativeURL);

  }

  public MetadataServiceGwtImpl() {

  }

  public void getDomainByName(String name, final XulServiceCallback<IDomain> callback) {

    SERVICE.getDomainByName(name, new AsyncCallback<IDomain>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(IDomain arg0) {
        callback.success(arg0);
      }

    });

  }

  public void getMetadataDomains(final XulServiceCallback<List<IDomain>> callback) {
    SERVICE.getMetadataDomains(new AsyncCallback<List<IDomain>>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(List<IDomain> arg0) {
        callback.success(arg0);
      }

    });
  }

  public void saveQuery(IModel model, List<? extends IBusinessColumn> cols, List<? extends ICondition> conditions, List<? extends IOrder> orders,
      final XulServiceCallback<String> callback) {
    SERVICE.saveQuery(model, cols, conditions, orders, new AsyncCallback<String>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String arg0) {
        callback.success(arg0);
      }

    });

  }

  public void serializeModel(IQuery query, final XulServiceCallback<String> callback) {
    SERVICE.serializeModel(query, new AsyncCallback<String>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String arg0) {
        callback.success(arg0);
      }

    });

  }

  public void getPreviewData(String query, int page, int limit, final XulServiceCallback<String[][]> callback) {
    SERVICE.getPreviewData(query, page, limit, new AsyncCallback<String[][]>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String[][] arg0) {
        callback.success(arg0);
      }

    });
  }

  public void deserializeModel(final String serializedQuery, final XulServiceCallback<IQuery> callback) {
    SERVICE.deserializeModel(serializedQuery, new AsyncCallback<IQuery>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(IQuery arg0) {
        callback.success(arg0);
      }

    });

  }
  
}
