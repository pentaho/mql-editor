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

import org.pentaho.gwt.widgets.client.controls.schededitor.CronEditor;
import org.pentaho.gwt.widgets.client.utils.CronParser;

public class CronEditorValidator implements IUiValidator {

  private CronEditor editor = null;
  private DateRangeEditorValidator dateRangeEditorValidator = null;
  
  public CronEditorValidator( CronEditor editor ) {
    this.editor = editor;
    this.dateRangeEditorValidator = new DateRangeEditorValidator( editor.getDateRangeEditor() );
  }
  
  public boolean isValid() {
    boolean isValid = true;
    
    if ( !CronParser.isValidCronString( editor.getCronString() ) ) {
      isValid = false;
    }
    isValid &= dateRangeEditorValidator.isValid();
    
    return isValid;
  }

  public void clear() {
    editor.setCronError( null );
    dateRangeEditorValidator.clear();
  }
}
