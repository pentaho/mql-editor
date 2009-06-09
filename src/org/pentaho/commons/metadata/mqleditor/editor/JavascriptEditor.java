package org.pentaho.commons.metadata.mqleditor.editor;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceGwtImpl;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.gwt.util.AsyncConstructorListener;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;

/**
 * Created by IntelliJ IDEA. User: NBaker Date: Mar 5, 2009 Time: 2:54:31 PM Default implementation usable by standard
 * web applications or other GWT apps through pure Javascript
 */
public class JavascriptEditor implements EntryPoint {

  private GwtMqlEditor editor;

  private MQLEditorService service;

  public void onModuleLoad() {
    service = new MQLEditorServiceGwtImpl();

    setupNativeHooks(this);

  }

  private native void setupNativeHooks(JavascriptEditor editor)/*-{
            $wnd.openMqlEditor= function(callback) {
              editor.@org.pentaho.commons.metadata.mqleditor.editor.JavascriptEditor::show(Lcom/google/gwt/core/client/JavaScriptObject;)(callback);
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
    editor = new GwtMqlEditor(service, new AsyncConstructorListener() {

      public void asyncConstructorDone() {
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
        };
        editor.addMqlDialogListener(listener);
        JavascriptEditor.this.service
            .deserializeModel(
                "{\"MQLQuery\":{\"cols\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"BC_CUSTOMER_W_TER_CUSTOMERNUMBER\",\"name\":\"Customernumber\",\"type\":\"FLOAT\",\"aggTypes\":\"\"},{\"id\":\"BC_ORDERDETAILS_TOTAL\",\"name\":\"Total\",\"type\":\"FLOAT\",\"aggTypes\":\"\"},{\"id\":\"BC_ORDERS_STATUS\",\"name\":\"Status\",\"type\":\"TEXT\",\"aggTypes\":\"\"}]},\"conditions\":[{\"org.pentaho.commons.metadata.mqleditor.beans.Condition\":[{\"@combinationType\":\"AND\",\"@defaultValue\":\"\",\"@operator\":\"=\",\"@selectedAggType\":\"\",\"@value\":\"131\",\"column\":{\"id\":\"BC_CUSTOMER_W_TER_CUSTOMERNUMBER\",\"name\":\"Customernumber\",\"type\":\"FLOAT\",\"aggTypes\":\"\"}},{\"@combinationType\":\"OR\",\"@defaultValue\":\"\",\"@operator\":\"=\",\"@selectedAggType\":\"\",\"@value\":\"145\",\"column\":{\"id\":\"BC_CUSTOMER_W_TER_CUSTOMERNUMBER\",\"name\":\"Customernumber\",\"type\":\"FLOAT\",\"aggTypes\":\"\"}}]}],\"orders\":[\"\"]},\"domain\":{\"@id\":\"default\",\"@name\":\"\\/org\\/pentaho\\/commons\\/metadata\\/mqleditor\\/sampleMql\"},\"model\":{\"@id\":\"BV_ORDERS\",\"@name\":\"Orders\"}}"

                , new XulServiceCallback<MqlQuery>() {

                  public void error(String arg0, Throwable arg1) {
                    Window.alert("error deserializing model");
                    editor.show();
                  }

                  public void success(MqlQuery savedQuery) {

                    editor.setSavedQuery((Query) savedQuery);
                    editor.show();
                  }
                });
      }
    });
  }

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
