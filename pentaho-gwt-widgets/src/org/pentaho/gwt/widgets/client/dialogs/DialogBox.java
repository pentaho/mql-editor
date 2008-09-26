/*
 * Copyright 2008 Pentaho Corporation.  All rights reserved. 
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
 * Created Mar 25, 2008
 * @author Michael D'Amour
 */
package org.pentaho.gwt.widgets.client.dialogs;

import org.pentaho.gwt.widgets.client.utils.ElementUtils;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class DialogBox extends com.google.gwt.user.client.ui.DialogBox implements PopupListener {

  private static FocusPanel pageBackground = null;
  private static int clickCount = 0;
  private static int dialogDepthCount = 0;
  private FocusWidget focusWidget = null;
  boolean autoHide = false;
  boolean modal = true;
  boolean centerCalled = false;

  public DialogBox(boolean autoHide, boolean modal) {
    super(autoHide, modal);
    this.autoHide = autoHide;
    this.modal = modal;
    addPopupListener(this);
  }

  public boolean onKeyDownPreview(char key, int modifiers) {
    // Use the popup's key preview hooks to close the dialog when either
    // enter or escape is pressed.
    switch (key) {
    case KeyboardListener.KEY_ENTER:
    case KeyboardListener.KEY_ESCAPE:
      hide();
      break;
    }
    return true;
  }

  public void center() {
    // IE6 has problems with 100% height so is better a huge size
    // pageBackground.setSize("100%", "100%");
    if (pageBackground == null) {
      pageBackground = new FocusPanel();
      pageBackground.setStyleName("modalDialogPageBackground"); //$NON-NLS-1$
      pageBackground.addClickListener(new ClickListener() {

        public void onClick(Widget sender) {
          clickCount++;
          if (clickCount > 2) {
            clickCount = 0;
            pageBackground.setVisible(false);
          }
        }
      });
      RootPanel.get().add(pageBackground, 0, 0);
    }
    super.center();
    if (modal && !centerCalled) {
      pageBackground.setSize("100%", Window.getClientHeight() + Window.getScrollTop() + "px"); //$NON-NLS-1$
      pageBackground.setVisible(true);
      dialogDepthCount++;
      centerCalled = true;
    }
    if (focusWidget != null) {
      focusWidget.setFocus(true);
    }
    //hide <embeds>
    ElementUtils.toggleEmbedVisibility(false);
    
  }

  public void show() {
    super.show();
    if (focusWidget != null) {
      focusWidget.setFocus(true);
    }
  }

  public void setFocusWidget(FocusWidget widget) {
    focusWidget = widget;
    if (focusWidget != null) {
      focusWidget.setFocus(true);
    }
  }
  

  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    if (modal) {
      dialogDepthCount--;
      centerCalled = false;
      if (dialogDepthCount <= 0) {
        pageBackground.setVisible(false);
        
        //reshow <embeds>
        if(this.isVisible()){
          ElementUtils.toggleEmbedVisibility(true);
        }
        
        // just make sure it is zero
        dialogDepthCount = 0;
      }
    }
  }
  
}
