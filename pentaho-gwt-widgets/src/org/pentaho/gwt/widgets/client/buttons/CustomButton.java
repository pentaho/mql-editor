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

import java.util.ArrayList;
import java.util.List;

import org.pentaho.gwt.widgets.client.utils.ButtonHelper;
import org.pentaho.gwt.widgets.client.utils.ElementUtils;
import org.pentaho.gwt.widgets.client.utils.ButtonHelper.ButtonLabelType;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomButton extends Widget {

  private String baseStyleName = "customButton"; //$NON-NLS-1$
  private Command command;
  private boolean enabled = true;
  private Image image;
  private String text;
  private ButtonLabelType type;
  private List<ClickListener> listeners = new ArrayList<ClickListener>();
  HorizontalPanel buttonPanel;
  public CustomButton() {
  }

  public CustomButton(Image image, String text, ButtonLabelType type) {
    SimplePanel spacer = new SimplePanel();
    spacer.setWidth("10px");
    buttonPanel = new HorizontalPanel();
    buttonPanel.add(spacer);
    buttonPanel.add(ButtonHelper.createButtonElement(image, text, type));
    buttonPanel.add(spacer);
    this.setElement(buttonPanel.getElement());
    buttonPanel.setStylePrimaryName(baseStyleName);
    sinkEvents(Event.MOUSEEVENTS);
    sinkEvents(Event.ONDBLCLICK);    
  }

  public CustomButton(Image image, String text, ButtonLabelType type, String className) {
    SimplePanel spacer = new SimplePanel();
    spacer.setWidth("10px");
    buttonPanel = new HorizontalPanel();
    buttonPanel.add(spacer);
    buttonPanel.add(ButtonHelper.createButtonElement(image, text, type, className));
    buttonPanel.add(spacer);
    this.setElement(buttonPanel.getElement());
    buttonPanel.setStylePrimaryName(baseStyleName);
    sinkEvents(Event.MOUSEEVENTS);
    sinkEvents(Event.ONDBLCLICK);    
  }
  
  public CustomButton(Image image, String text, ButtonLabelType type, Command command) {
    this(image, text, type);
    setCommand(command);
  }  

  
  private void setCommand(Command command) {
    this.command = command;
  }
  

  @Override
  public void setStylePrimaryName(String style) {
    super.setStylePrimaryName(style);
    baseStyleName = style;
    buttonPanel.setStylePrimaryName(style); //$NON-NLS-1$
  }

  @Override
  public void addStyleDependentName(String style) {
    super.addStyleDependentName(style);
    buttonPanel.addStyleDependentName(style);
  }

  @Override
  public void removeStyleDependentName(String style) {
    super.removeStyleDependentName(style);
    buttonPanel.removeStyleDependentName(style);
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setEnabled(boolean enabled) {
    boolean prevVal = this.enabled;
    this.enabled = enabled;

    if (prevVal && enabled) {
      return;
    } else if (prevVal && !enabled) {
      this.addStyleDependentName("disabled"); //$NON-NLS-1$
    } else {
      this.removeStyleDependentName("disabled"); //$NON-NLS-1$
    }
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void onBrowserEvent(Event event) {
    switch (event.getTypeInt()) {
    case Event.ONDBLCLICK:
    case Event.ONCLICK:
    case Event.ONMOUSEUP:
      if (CustomButton.this.isEnabled()) {
        fireClicked();
        if (command != null) {
          command.execute();
        }
        event.cancelBubble(true);
        event.preventDefault();
      }
      break;
    case Event.ONMOUSEOVER:
      if (CustomButton.this.isEnabled()) {
        CustomButton.this.addStyleDependentName("hover"); //$NON-NLS-1$
      }
      break;
    case Event.ONMOUSEOUT:
      if (CustomButton.this.isEnabled()) {
        CustomButton.this.removeStyleDependentName("hover"); //$NON-NLS-1$
      }
      break;
    case Event.ONMOUSEDOWN:
      if (CustomButton.this.isEnabled()) {
        CustomButton.this.addStyleDependentName("down"); //$NON-NLS-1$
      }
      break;
    }
  }

  private void fireClicked() {
    for (ClickListener listener : listeners) {
      listener.onClick(this);
    }
  }

  public void addClickListener(ClickListener listener) {
    listeners.add(listener);
  }

  public void removeClickListener(ClickListener listener) {
    listeners.remove(listener);
  }
  
  public void setFocus(boolean focus){
    this.setFocus(focus);
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  public ButtonLabelType getType() {
    return type;
  }

  public void setType(ButtonLabelType type) {
    this.type = type;
  }

  public Command getCommand() {
    return command;
  }


}
