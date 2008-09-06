/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
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
 * Created  
 * @author Steven Barkdull
 */
package org.pentaho.gwt.widgets.client.wizards.panels.validators;

import org.pentaho.gwt.widgets.client.controls.schededitor.RunOnceEditor;


public class RunOnceEditorValidator implements IUiValidator {

  private RunOnceEditor editor = null;
  
  public RunOnceEditorValidator( RunOnceEditor editor ) {
    this.editor = editor;
  }
  
  public boolean isValid() {
    boolean isValid = true;
    if ( null == editor.getStartDate() ) {
      isValid = false;
    }
    return isValid;
  }

  public void clear() {
    editor.setStartDateError( null );
  }
}
