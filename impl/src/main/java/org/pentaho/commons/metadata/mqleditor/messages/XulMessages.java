package org.pentaho.commons.metadata.mqleditor.messages;

import org.pentaho.metadata.messages.LocaleHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class XulMessages {
  private static final String BUNDLE_NAME = "org.pentaho.commons.metadata.mqleditor.editor.xul.mainFrame";
  private static final Map<Locale, ResourceBundle> locales = Collections
    .synchronizedMap( new HashMap<>() );

  private XulMessages() {
  }

  private static ResourceBundle getBundle() {
    Locale locale = LocaleHelper.getLocale();
    return locales.computeIfAbsent( locale, l -> ResourceBundle.getBundle( BUNDLE_NAME, locale ) );
  }

  public static String getString( String key, String defaultMessage ) {
    try {
      return getBundle().getString( key );
    } catch ( MissingResourceException e ) {
      return defaultMessage;
    }
  }
}
