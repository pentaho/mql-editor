package org.pentaho.gwt.widgets.client.i18n;

import com.google.gwt.core.client.GWT;

public class WidgetsLocalizedMessagesSingleton {

  private WidgetsLocalizedMessages MSGS = GWT.create(WidgetsLocalizedMessages.class);
  private static WidgetsLocalizedMessagesSingleton instance = new WidgetsLocalizedMessagesSingleton();

  private WidgetsLocalizedMessagesSingleton() {
  }

  public WidgetsLocalizedMessages getMessages() {
    return MSGS;
  }

  public static WidgetsLocalizedMessagesSingleton getInstance() {
    return instance;
  }
}
