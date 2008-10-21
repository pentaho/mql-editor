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

import org.pentaho.gwt.widgets.client.utils.ElementUtils;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class ToolbarToggleButton extends ToolbarButton {

  private boolean selected = false;
  private String TOGGLE_STYLE = "toolbar-toggle-button";   //$NON-NLS-1$
  private Image downImage;
  private Image downImageDisabled;
  
  /**
   * Constructs a toolbar button with an image and a label
   * 
   * @param img GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, String label, boolean selected) {
    super(img, label);
    super.setStylePrimaryName(TOGGLE_STYLE);
    this.selected = selected;
    updateSelectedStyle();
  }
  
  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, Image disabledImage, String label, boolean selected) {
    super(img, disabledImage, label);
    super.setStylePrimaryName(TOGGLE_STYLE);
    this.selected = selected;
    updateSelectedStyle();
  }
  

  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, Image disabledImage, boolean selected){
    super(img, disabledImage);
    super.setStylePrimaryName(TOGGLE_STYLE);
    this.selected = selected;
    updateSelectedStyle();
  }
  
  /**
   * Constructs a toolbar button with an image
   * 
   * @param img GWT Image object 
   */
  public ToolbarToggleButton(Image img){
    super(img);
    super.setStylePrimaryName(TOGGLE_STYLE);
  }
  
  /**
   * Returns a boolean based on the selected "down" state of the button
   * 
   * @return boolean flag
   */
  public boolean isSelected(){
    return this.selected;
  }
  
  /**
   * Programatically change the state of the button. If passed true, it fires the command,
   * false and the command will be ignored.
   * 
   * @paran selected whether or not this button should be displayed selected.
   * @param fireEvent boolean fire associated Command
   */
  public void setSelected(boolean selected, boolean fireEvent){
    this.selected = selected;
    if(fireEvent){
      this.command.execute();
    }
    updateSelectedStyle();
  }
  
  private void toggleSelectedState(){
    selected = !(this.selected);
    updateSelectedStyle();
  }
  
  private void updateSelectedStyle() {
    if(selected){
      button.addStyleName(stylePrimaryName+"-down");    //$NON-NLS-1$
      if(this.downImage != null){
        button.remove(currentImage);
        button.add(calculateApporiateImage(), DockPanel.CENTER);
      }
    } else {
      if(this.downImage != null){
        button.remove(currentImage);
        button.add(calculateApporiateImage(), DockPanel.CENTER);
      }
      
      button.removeStyleName(stylePrimaryName+"-down");    //$NON-NLS-1$
      button.removeStyleName(stylePrimaryName+"-down-hovering");    //$NON-NLS-1$
    }
  }
  
  @Override
  protected Image calculateApporiateImage(){
    Image retVal;
    if(enabled){
      if(selected && this.downImage != null){ //Enabled, down and with image
        retVal = this.downImage;
      } else {
        retVal = super.calculateApporiateImage();
      }
    } else {
      if(selected && this.downImageDisabled != null){ //Disabled, down with image
        retVal = this.downImageDisabled;
      } else {
        retVal = super.calculateApporiateImage();
      }
    }
    this.currentImage = retVal;
    return retVal;
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
          ElementUtils.blur(ToolbarToggleButton.this.eventWrapper.getElement());
          return;
        }
        toggleSelectedState();
        command.execute();
        ElementUtils.blur(ToolbarToggleButton.this.eventWrapper.getElement());      }
    });
    eventWrapper.addMouseListener(new MouseListener(){
      public void onMouseDown(Widget arg0, int arg1, int arg2) {
        button.addStyleName(stylePrimaryName+"-down-hovering");    //$NON-NLS-1$ 
        button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$   
      }
      public void onMouseEnter(Widget arg0) {
        button.addStyleName(stylePrimaryName+((selected)?"-down":"")+"-hovering");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
      public void onMouseLeave(Widget arg0) {
        button.removeStyleName(stylePrimaryName+((selected)?"-down":"")+"-hovering");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        button.removeStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$ 
      }
      public void onMouseUp(Widget arg0, int arg1, int arg2) {
        if(!enabled){
          ElementUtils.blur(ToolbarToggleButton.this.eventWrapper.getElement());
          return;
        }
        toggleSelectedState();
        ElementUtils.blur(ToolbarToggleButton.this.eventWrapper.getElement());
      }
      public void onMouseMove(Widget arg0, int arg1, int arg2) {}
    });
  }

  /**
   * Sets the image to be displayed on this button when depressed
   * 
   * @param img GWT Image
   */
  public void setDownImage(Image img) {
    this.downImage = img;
  }
  
  /**
   * Gets the image to be displayed on this button when depressed
   * 
   * @param img GWT Image
   */
  public Image getDownImage() {
    return this.downImage;
  }

  /**
   * Sets the image to be displayed on this button when depressed yet disabled
   * 
   * @param img GWT Image
   */
  public void setDownImageDisabled(Image img) {
    this.downImageDisabled = img;
  }
  
  /**
   * Gets the image to be displayed on this button when depressed yet disabled
   * 
   * @param img GWT Image
   */
  public Image getDownImageDisabled() {
    return this.downImageDisabled;
  }
  
}
