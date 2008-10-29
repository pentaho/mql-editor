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

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProgressPopupPanel extends PopupPanel {

  private Label label;
  private int width = 150;
  private int height = 100;
  
  public ProgressPopupPanel() {
    super( false /*autohide*/, true /*modal*/);
    init();
  }
  
  private void init() {
    this.setStyleName( "progressPopupPanel" ); //$NON-NLS-1$
    setPixelSize( width, height );
    
    label = new Label();
    label.setStyleName( "progressPopupPanel.label" ); //$NON-NLS-1$
    
    VerticalPanel vp = new VerticalPanel();
    vp.add( label );

    vp.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE );
    vp.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER ); 
//    vp.setCellHeight( label, "100%" );
//    vp.setCellWidth( label, "100%" );
    add( vp );
  }
  
  public void setLabelText( String text ) {
    label.setText( text );
  }
}
