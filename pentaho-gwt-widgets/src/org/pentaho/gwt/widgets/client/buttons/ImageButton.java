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
import com.google.gwt.user.client.ui.Widget;

public class ImageButton extends Image{

  private boolean isEnabled = true;
  private String enabledUrl;
  private String disabledUrl;

  public ImageButton (String enabledUrl, String disabledUrl, String tooltip){
    this(enabledUrl, disabledUrl, tooltip, -1, -1);
  }
  
  public ImageButton (String enabledUrl, String disabledUrl, String tooltip, int width, int height){
    super(enabledUrl);
    
    this.enabledUrl = enabledUrl;
    this.disabledUrl = disabledUrl;
    
    if(tooltip != null && tooltip.length() > 0){
      setTitle(tooltip);
    }
        
    setStyleName("image-button"); //$NON-NLS-1$
    
    setSize(width + "px", height + "px"); //$NON-NLS-1$ //$NON-NLS-2$
    
    addMouseListener(new MouseListener(){
      public void onMouseEnter (Widget sender){
        if (isEnabled){
          setStyleName("image-button-over"); //$NON-NLS-1$
        }else{
          setStyleName("disabled-image-button-over"); //$NON-NLS-1$
        }
      }

      public void onMouseMove(Widget sender, int x, int y) {
        if (isEnabled){
          setStyleName("image-button-over"); //$NON-NLS-1$
        }else{
          setStyleName("disabled-image-button-over"); //$NON-NLS-1$
        }
      }
            
      public void onMouseLeave (Widget sender){
        if (isEnabled){
          setStyleName("image-button"); //$NON-NLS-1$
        }else{
          setStyleName("disabled-image-button"); //$NON-NLS-1$
        }
      }
            
      public void onMouseDown(Widget sender, int x, int y){
        if (isEnabled){
          setStyleName("image-button-pressed"); //$NON-NLS-1$
        }else{
          setStyleName("disabled-image-button-pressed"); //$NON-NLS-1$
        }
      }
      
      public void onMouseUp(Widget sender, int x, int y) {
        if (isEnabled){
          setStyleName("image-button"); //$NON-NLS-1$
        }else{
          setStyleName("disabled-image-button"); //$NON-NLS-1$
        }
      }
    });
  }
    
  public void onBrowserEvent(Event event) {
      super.onBrowserEvent(event);
      
      //This is required to prevent a drag & drop of the Image in the edit text.
      DOM.eventPreventDefault(event);
  }
  
  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
    
    if (isEnabled){
      this.setUrl(enabledUrl);
    }else{
      this.setUrl(disabledUrl);
    }
  }
  
}
