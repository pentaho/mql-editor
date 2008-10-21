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
 */
package org.pentaho.gwt.widgets.client.controls;

import org.pentaho.gwt.widgets.client.utils.StringUtils;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Steven Barkdull
 *
 */

public class ErrorLabel extends VerticalPanel {

    private Label errorLabel = null;
    
    public ErrorLabel( Widget w ) {
      errorLabel = new Label();
      errorLabel.setStyleName( "errorLabel" ); //$NON-NLS-1$
      errorLabel.setVisible( false );
      add( errorLabel );
      
      add( w );
    }
    
    /**
     * Set an error message to be associated with the label
     * 
     * @param msg String if null, clear the error message, else set
     * the error message to <param>mgs</param>.
     */
    public void setErrorMsg( String msg ) {
      if ( !StringUtils.isEmpty( msg ) ) {
        errorLabel.setText( msg );
        errorLabel.setVisible( true );
      } else {
        errorLabel.setText( "" ); //$NON-NLS-1$
        errorLabel.setVisible( false );
      }
    }
}
