/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
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
