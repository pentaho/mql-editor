/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Aug 1, 2008 
 * @author wseyler
 */


package org.pentaho.gwt.widgets.client.text;

import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author wseyler
 *
 */
public class DigitsOnlyListener extends KeyboardListenerAdapter {
  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    if ((!Character.isDigit(keyCode)) && (keyCode != (char) KEY_TAB)
        && (keyCode != (char) KEY_BACKSPACE)
        && (keyCode != (char) KEY_DELETE) && (keyCode != (char) KEY_ENTER) 
        && (keyCode != (char) KEY_HOME) && (keyCode != (char) KEY_END)
        && (keyCode != (char) KEY_LEFT) && (keyCode != (char) KEY_UP)
        && (keyCode != (char) KEY_RIGHT) && (keyCode != (char) KEY_DOWN)) {
      // TextBox.cancelKey() suppresses the current keyboard event.
      ((TextBox)sender).cancelKey();
    }
  }
}
