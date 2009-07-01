package org.pentaho.commons.metadata.mqleditor.editor.gwt;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.editor.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.MqlDialogListener;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.gwt.util.AsyncConstructorListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject; 
import com.google.gwt.user.client.Window;

/**
 * Created by IntelliJ IDEA. User: NBaker Date: Mar 5, 2009 Time: 2:54:31 PM Default implementation usable by standard
 * web applications or other GWT apps through pure Javascript.
 * 
 * This is the version used by Hosted Mode in development.
 */
public class DebugEntryPoint implements EntryPoint {

  private GwtMqlEditor editor;

  private MQLEditorService service;

  public void onModuleLoad() {
    service = new MQLEditorServiceGwtImpl();

    setupNativeHooks(this);
    
    editor = new GwtMqlEditor(service, new AsyncConstructorListener() {

      public void asyncConstructorDone() {
      }
    });
    
  }

  private native void setupNativeHooks(DebugEntryPoint editor)/*-{
            $wnd.openMqlEditor= function(callback) {
              editor.@org.pentaho.commons.metadata.mqleditor.editor.gwt.DebugEntryPoint::show(Lcom/google/gwt/core/client/JavaScriptObject;)(callback);
            }
            
            $wnd.openMqlEditorWithIDs= function(domain_id, model_id, callback) {
            
              editor.@org.pentaho.commons.metadata.mqleditor.editor.gwt.DebugEntryPoint::show(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)(domain_id, model_id, callback);
            }
          }-*/;

  /**
   * Entry-point from Javascript, responds to provided callback with the following: onOk(String JSON, String mqlString);
   * onCancel(); onError(String errorMessage);
   * 
   * @param callback
   */
  @SuppressWarnings("unused")
  private void show(final JavaScriptObject callback) {

    final MqlDialogListener listener = new MqlDialogListener() {
      public void onDialogAccept(final MqlQuery queryModel) {
        service.serializeModel(queryModel, new XulServiceCallback<String>() {
          public void success(String s) {
            notifyCallbackSuccess(callback, s, queryModel.getMqlStr());
          }

          public void error(String s, Throwable throwable) {
            notifyCallbackError(callback, throwable.getMessage());
          }
        });
      }

      public void onDialogCancel() {
        editor.hide();
        notifyCallbackCancel(callback);
      }
      
      public void onDialogReady(){
        notifyCallbackReady(callback);
      }
    };
    editor.addMqlDialogListener(listener);
    editor.show();

  }
  

  private void show(final String domainId, final String modelId, final JavaScriptObject callback) {
    
    // Add a listener to wait for notification of that the dialog has refreshed it domain list.
    editor.addMqlDialogListener(new MqlDialogListener(){
      public void onDialogAccept(MqlQuery query) {
      }

      public void onDialogCancel() {
      }
      
      public void onDialogReady(){
        final MqlDialogListener listener = new MqlDialogListener() {
          public void onDialogAccept(final MqlQuery queryModel) {
            service.serializeModel(queryModel, new XulServiceCallback<String>() {
              public void success(String s) {
                notifyCallbackSuccess(callback, s, queryModel.getMqlStr());
              }

              public void error(String s, Throwable throwable) {
                notifyCallbackError(callback, throwable.getMessage());
              }
            });
          }

          public void onDialogCancel() {
            editor.hide();
            notifyCallbackCancel(callback);
          }
          
          public void onDialogReady(){
            notifyCallbackReady(callback);
          }
        };
        editor.addMqlDialogListener(listener);
        
        Window.alert("showing dialog");
        editor.setSelectedDomainId(domainId);
        editor.setSelectedModelId(modelId);
        editor.show();
        editor.removeMqlDialogListener(this);
      }
      
    });
    editor.updateDomainList();
    
  }
  

  private native void notifyCallbackReady(JavaScriptObject callback)/*-{
    callback.onReady();
  }-*/;

  private native void notifyCallbackSuccess(JavaScriptObject callback, String mqlJSON, String query)/*-{
            callback.onOk(mqlJSON, query);
          }-*/;

  private native void notifyCallbackError(JavaScriptObject callback, String error)/*-{
            callback.onError(error);
          }-*/;

  private native void notifyCallbackCancel(JavaScriptObject callback)/*-{
            callback.onCancel();
          }-*/;
}
