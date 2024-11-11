/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/


package org.pentaho.commons.metadata.mqleditor.messages;

import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;

public class GwtMqlMessages implements IMqlMessages {
  private ResourceBundle messages;

  public GwtMqlMessages( ResourceBundle messages ) {
    this.messages = messages;
  }

  public String getString( String key, String defaultMessage, String... args ) {
    return messages.getString( key, defaultMessage, args );
  }
}
