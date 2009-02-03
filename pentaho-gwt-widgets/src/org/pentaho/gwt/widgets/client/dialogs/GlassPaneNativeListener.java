package org.pentaho.gwt.widgets.client.dialogs;

import com.google.gwt.core.client.JavaScriptObject;

public class GlassPaneNativeListener implements GlassPaneListener {
  private JavaScriptObject callback;

  public GlassPaneNativeListener(JavaScriptObject callback) {
    this.callback = callback;
  }

  public void glassPaneHidden() {
    sendHide(callback);
  }

  public void glassPaneShown() {
    sendShown(callback);
  }

  private native void sendHide(JavaScriptObject obj)/*-{
    obj.glassPaneHidden();
  }-*/;

  private native void sendShown(JavaScriptObject obj)/*-{
    obj.glassPaneShown();
  }-*/;
}
