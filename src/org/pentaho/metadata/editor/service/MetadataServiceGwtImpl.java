package org.pentaho.metadata.editor.service;

import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
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

  public void getMetadataDomains(final XulServiceCallback<String[]> callback) {
    SERVICE.getMetadataDomains(new AsyncCallback<String[]>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String[] arg0) {
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

}
