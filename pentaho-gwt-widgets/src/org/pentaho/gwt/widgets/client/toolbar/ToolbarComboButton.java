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
package org.pentaho.gwt.widgets.client.toolbar;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolbarComboButton extends ToolbarButton implements ToolbarPopupSource, PopupListener{
  
  private String COMBO_STYLE = "toolbar-combo-button";   //$NON-NLS-1$
  private MenuBar menu;
  PopupPanel popup = new PopupPanel(true){
    @Override
    public void show() {
      super.show();
      notifyPopupListeners(this, true);
    }
  };
  
  private List<ToolbarPopupListener> popupListeners = new ArrayList<ToolbarPopupListener>();
  
  /**
   * Constructs a toolbar button with an image and a label
   * 
   * @param img GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarComboButton(Image img, String label){
    super(img, label);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  
  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarComboButton(Image img, Image disabledImage, String label){
    super(img, disabledImage, label);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  

  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarComboButton(Image img, Image disabledImage){
    super(img, disabledImage);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  
  /**
   * Constructs a toolbar button with an image
   * 
   * @param img GWT Image object 
   */
  public ToolbarComboButton(Image img){
    super(img);
    addDropdownControl();
    super.setStylePrimaryName(COMBO_STYLE);
  }
  
  private void addDropdownControl(){
    popup.addPopupListener(this);
  }
  
  @Override
  public void setCommand(Command cmd) {
    throw new UnsupportedOperationException("Not implemented in this class");   //$NON-NLS-1$
  }
    
  @Override
  protected void addStyleMouseListener(){
    // a click listener is more appropriate here to fire the click events
    // rather than a mouse-up because the focus panel can (and does) sometimes
    // receive mouse up events if a widget 'above' it has been clicked and
    // dismissed (on mouse-down).  The ensures that only a true click will
    // fire a button's command
    eventWrapper.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if(!enabled){
          return;
        }        
        popup.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop() + sender.getOffsetHeight());
        popup.show();
      }
    });
    eventWrapper.addMouseListener(new MouseListener(){
      public void onMouseDown(Widget w, int x, int y) {
      }
      public void onMouseEnter(Widget w) {
        if(!enabled){
          return;
        }
        button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$
      }
      public void onMouseLeave(Widget w) {
        if(!enabled){
          return;
        }
        button.removeStyleName(stylePrimaryName+"-hovering");   //$NON-NLS-1$
      }
      public void onMouseUp(Widget w, int x, int y) {
        if(!enabled){
          return;
        }        
        popup.setPopupPosition(w.getAbsoluteLeft(), w.getAbsoluteTop() + w.getOffsetHeight());
      }
      public void onMouseMove(Widget w, int x, int y) {}
    });
  }
  
  public void setMenu(MenuBar bar){
    menu = bar;
    popup.setWidget(menu);
  }

  public PopupPanel getPopup() {
    return popup;
  }
  
  public void addPopupPanelListener(ToolbarPopupListener listener){
    if(popupListeners.contains(listener) == false){
      popupListeners.add(listener);
    }
  }
  public void removePopupPanelListener(ToolbarPopupListener listener){
    if(popupListeners.contains(listener)){
      popupListeners.remove(listener);
    }
  }
  
  public void notifyPopupListeners(PopupPanel panel, boolean visible){
    for(ToolbarPopupListener listener : popupListeners){
      if(visible){
        listener.popupOpened(panel);
      } else {
        listener.popupClosed(panel);
      }
    }
  }

  public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
    notifyPopupListeners(sender, false);
  }

}
