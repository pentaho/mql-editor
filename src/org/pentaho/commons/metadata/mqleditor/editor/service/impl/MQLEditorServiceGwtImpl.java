package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.*;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MQLEditorServiceGwtImpl implements MQLEditorService {

  static org.pentaho.commons.metadata.mqleditor.editor.service.gwt.MQLEditorGwtServiceAsync SERVICE;

  static {

    SERVICE = (org.pentaho.commons.metadata.mqleditor.editor.service.gwt.MQLEditorGwtServiceAsync) GWT.create(org.pentaho.commons.metadata.mqleditor.editor.service.gwt.MQLEditorGwtService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    endpoint.setServiceEntryPoint(getBaseUrl() + "gwtrpc/MqlService");
  }

  private static String getBaseUrl() {
    String baseUrl = GWT.getModuleBaseURL();
    
    //
    //Set the base url appropriately based on the context in which we are running this client
    //
    if(baseUrl.indexOf("content") > -1) {
      //we are running the client in the context of a BI Server plugin, so 
      //point the request to the GWT rpc proxy servlet
      baseUrl = baseUrl.substring(0, baseUrl.indexOf("content"));
    }
    return  baseUrl;
  }
  
  public MQLEditorServiceGwtImpl() {

  }

  public void getDomainByName(String name, final XulServiceCallback<MqlDomain> callback) {

    SERVICE.getDomainByName(name, new AsyncCallback<MqlDomain>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(MqlDomain arg0) {
        callback.success(arg0);
      }

    });

  }

  public void getMetadataDomains(final XulServiceCallback<List<MqlDomain>> callback) {
    SERVICE.getMetadataDomains(new AsyncCallback<List<MqlDomain>>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(List<MqlDomain> arg0) {
        callback.success(arg0);
      }

    });
  }
  
  public void refreshMetadataDomains(final XulServiceCallback<List<MqlDomain>> callback) {
    SERVICE.refreshMetadataDomains(new AsyncCallback<List<MqlDomain>>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(List<MqlDomain> arg0) {
        callback.success(arg0);
      }

    });
  }

  public void saveQuery(MqlQuery model, final XulServiceCallback<String> callback) {
    SERVICE.saveQuery(model, new AsyncCallback<String>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String arg0) {
        callback.success(arg0);
      }

    });

  }

  public void serializeModel(MqlQuery query, final XulServiceCallback<String> callback) {
    SERVICE.serializeModel(query, new AsyncCallback<String>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String arg0) {
        callback.success(arg0);
      }

    });

  }

  public void getPreviewData(MqlQuery query, int page, int limit, final XulServiceCallback<String[][]> callback) {
    SERVICE.getPreviewData(query, page, limit, new AsyncCallback<String[][]>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String[][] arg0) {
        callback.success(arg0);
      }

    });
  }

  public void deserializeModel(final String serializedQuery, final XulServiceCallback<MqlQuery> callback) {
    SERVICE.deserializeModel(serializedQuery, new AsyncCallback<MqlQuery>() {

      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(MqlQuery arg0) {
        callback.success(arg0);
      }

    });

  }
  
}
