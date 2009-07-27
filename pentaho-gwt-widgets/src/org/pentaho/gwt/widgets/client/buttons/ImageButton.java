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

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Window;

/**
 * Clickable image with enable/disable functionality built in.
 */
public class ImageButton extends Image {

  private boolean isEnabled = true;
  private String enabledUrl;
  private String disabledUrl;

  public ImageButton(String enabledUrl, String disabledUrl, String tooltip) {
    this(enabledUrl, disabledUrl, tooltip, -1, -1);
  }

  public ImageButton() {
    super();
    setStyleName("image-button"); //$NON-NLS-1$
    this.addMouseDownHandler(new MouseDownHandler() {
      public void onMouseDown(MouseDownEvent event) {
        if (isEnabled) {
          setStyleName("image-button-pressed"); //$NON-NLS-1$
        } else {
          setStyleName("disabled-image-button-pressed"); //$NON-NLS-1$
        }
      }
    });
    this.addMouseUpHandler(new MouseUpHandler() {
      public void onMouseUp(MouseUpEvent event) {
        updateStyles();
      }
    });

    this.addMouseOverHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        if (isEnabled) {
          setStyleName("image-button-over"); //$NON-NLS-1$
        } else {
          setStyleName("disabled-image-button-over"); //$NON-NLS-1$
        }
      }
    });

    this.addMouseOutHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        updateStyles();
      }
    });

  }

  private void updateStyles() {
    if (isEnabled) {
      setStyleName("image-button"); //$NON-NLS-1$
    } else {
      setStyleName("disabled-image-button"); //$NON-NLS-1$
    }
  }

  public ImageButton(String enabledUrl, String disabledUrl, String tooltip,
      int width, int height) {
    super(enabledUrl);

    setSize(width + "px", height + "px"); //$NON-NLS-1$ //$NON-NLS-2$

    this.enabledUrl = enabledUrl;
    this.disabledUrl = disabledUrl;

    if (tooltip != null && tooltip.length() > 0) {
      setTitle(tooltip);
    }

  }

  public void setEnabledUrl(String url) {
    if (this.enabledUrl != null && this.enabledUrl.equals(url)) {
      return;
    }

    this.enabledUrl = url;

    // only change the url if it's different and not null
    if (isEnabled && this.getUrl().equals(enabledUrl) == false) {
      this.setSrc(enabledUrl);
    } else if (!isEnabled && disabledUrl != null
        && this.getUrl().equals(disabledUrl) == false) {
      this.setSrc(disabledUrl);
    }
  }

  public void setDisabledUrl(String url) {
    if (this.disabledUrl != null && this.disabledUrl.equals(url)) {
      return;
    }
    this.disabledUrl = url;

    // only change the url if it's different and not null
    if (isEnabled && enabledUrl != null
        && this.getUrl().equals(enabledUrl) == false) {
      this.setSrc(enabledUrl);
    } else if (!isEnabled && this.getUrl().equals(disabledUrl) == false) {
      this.setSrc(disabledUrl);
    }
  }

  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);

    // This is required to prevent a drag & drop of the Image in the edit text.
    DOM.eventPreventDefault(event);
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean isEnabled) {
    if (this.isEnabled == isEnabled) {
      return;
    }
    this.isEnabled = isEnabled;

    if (isEnabled) {
      this.setSrc(enabledUrl);
    } else if (disabledUrl != null) {
      this.setSrc(disabledUrl);
    }
  }
  
  /**
   * We're manipulating the DOM element directory instead of using the setUrl() method as
   * setUrl(), which does a lot of deferred loading / caching magic, was casing issues with IE. 
   * 
   * @TODO Re-evaluate the need for this after the next GWT release.
   * @param src
   */
  private void setSrc(String src){
    this.getElement().setAttribute("src", src);
  }

  public void setFocus(boolean focus) {
    this.setFocus(focus);
  }

}
