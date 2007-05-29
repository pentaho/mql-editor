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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class MacCombo extends OSSpecificCombo {

  Combo combo;
  
  public MacCombo(Composite parent, int style) {
    combo = new Combo(parent, style);
  }

  public void setLayoutData(Object layoutData) {
    combo.setLayoutData(layoutData);  
  }

  public void addModifyListener(ModifyListener modifyListener) {
    combo.addModifyListener(modifyListener);  
  }

  public void addSelectionListener(SelectionListener selectionListener) {
    combo.addSelectionListener(selectionListener); 
  }

  public void removeModifyListener(ModifyListener listener) {
    combo.removeModifyListener(listener);
  }

  public void removeSelectionListener(SelectionListener listener) {
    combo.removeSelectionListener(listener);
  }

  public String getText() {
    return combo.getText();
  }

  public void setText(String string) {
    combo.setText(string);
  }

  public void setItems(String[] items) {
    combo.setItems(items);
  }

  public Control getControl() {
    return combo;
  }

  public void clearSelection() {
    combo.clearSelection();
  }

  public void select(int index) {
    combo.select(index);
  }

  public int getSelectionIndex() {
    return combo.getSelectionIndex();
  }
  
}
