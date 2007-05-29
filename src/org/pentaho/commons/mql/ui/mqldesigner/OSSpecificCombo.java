/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
*/
package org.pentaho.commons.mql.ui.mqldesigner;

import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public abstract class OSSpecificCombo {

  private static final boolean isMacOS = System.getProperty("os.name").startsWith("Mac"); //$NON-NLS-1$ //$NON-NLS-2$
  
  public abstract void setLayoutData(Object layoutData);
  
  public abstract void addModifyListener(ModifyListener modifyListener);
  
  public abstract void addSelectionListener(SelectionListener selectionListener);
  
  public abstract void removeModifyListener(ModifyListener modifyListener);
  
  public abstract void removeSelectionListener(SelectionListener selectionListener);
  
  public abstract String getText();
  
  public abstract void setText(String text);
  
  public abstract void setItems(String[] items);
  
  public abstract Control getControl();
  
  public abstract void clearSelection();
  
  public abstract void select(int index);
  
  public abstract int getSelectionIndex();

  public static OSSpecificCombo createCombo(Composite parent, int style) {
    OSSpecificCombo combo = null;
    if (isMacOS) {
      combo = new MacCombo(parent, style);
    } else {
      combo = new WindowsCombo(parent, style);
    }
    return combo;
  }
}
