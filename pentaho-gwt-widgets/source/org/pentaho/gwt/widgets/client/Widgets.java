package org.pentaho.gwt.widgets.client;

import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Widgets implements EntryPoint {

  private static final WidgetsLocalizedMessages MSGS = (WidgetsLocalizedMessages)GWT.create(WidgetsLocalizedMessages.class);
  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
  }
  
  public static WidgetsLocalizedMessages getLocalizedMessages() {
    return MSGS;
  }
}
