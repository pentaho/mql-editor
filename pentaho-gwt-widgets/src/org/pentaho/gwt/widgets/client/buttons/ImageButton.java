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
package org.pentaho.gwt.widgets.client.buttons;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class ImageButton extends Image{

  public ImageButton (Panel parent,String url, String tooltip){
    super(url);
        
    if(tooltip != null && tooltip.length() > 0){
      setTitle(tooltip);
    }
        
    setStyleName("image-button"); //$NON-NLS-1$
    
    addMouseListener(new MouseListener(){
      public void onMouseEnter (Widget sender){
        setStyleName("image-button-over"); //$NON-NLS-1$
      }

      public void onMouseMove(Widget sender, int x, int y) {
        setStyleName("image-button-over"); //$NON-NLS-1$
      }
            
      public void onMouseLeave (Widget sender){
        setStyleName("image-button"); //$NON-NLS-1$
      }
            
      public void onMouseDown(Widget sender, int x, int y){
        setStyleName("image-button-pressed"); //$NON-NLS-1$
      }
      public void onMouseUp(Widget sender, int x, int y) {
        setStyleName("image-button"); //$NON-NLS-1$
      }
    });
  }
    
  public void onBrowserEvent(Event event) {
      super.onBrowserEvent(event);
      
      //This is required to prevent a drag & drop of the Image in the edit text.
      DOM.eventPreventDefault(event);
  }
    
}
