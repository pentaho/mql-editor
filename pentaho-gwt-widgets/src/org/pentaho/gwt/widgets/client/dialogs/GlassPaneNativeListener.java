package org.pentaho.gwt.widgets.client.dialogs;

import com.google.gwt.core.client.JavaScriptObject;

public class GlassPaneNativeListener implements GlassPaneListener {
  private JavaScriptObject callback;

  public GlassPaneNativeListener(JavaScriptObject callback) {
    this.callback = callback;
  }

  public void glassPaneHidden() throws Exception{
    sendHide(callback);
  }

  public void glassPaneShown() throws Exception{
    sendShown(callback);
  }

  private native void sendHide(JavaScriptObject obj) throws Exception/*-{
    try{
      obj.glassPaneHidden();
    } catch(e){
      throw e;
    }
    
  }-*/;

  private native void sendShown(JavaScriptObject obj) throws Exception/*-{
    try{
      obj.glassPaneShown();
    } catch (e){
      throw e;
    }
  }-*/;
}
