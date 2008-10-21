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
package org.pentaho.gwt.widgets.client.dialogs;

import org.pentaho.gwt.widgets.client.buttons.RoundedButton;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResizableDialogBox {

  private AbsolutePanel boundaryPanel;
  private WindowPanel windowPanel;
  private IDialogValidatorCallback validatorCallback;
  private IDialogCallback callback;
  private Widget content;

  public ResizableDialogBox(final String headerText, String okText, String cancelText, final Widget content, final boolean modal) {
    this.content = content;
    boundaryPanel = new AbsolutePanel() {
      public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (!modal && event.getTypeInt() == Event.ONCLICK) {
          hide();
        }
      }
    };
    boundaryPanel.setSize("100%", Window.getClientHeight() + Window.getScrollTop() + "px"); //$NON-NLS-1$ //$NON-NLS-2$
    boundaryPanel.setVisible(true);
    RootPanel.get().add(boundaryPanel, 0, 0);
    boundaryPanel.sinkEvents(Event.ONCLICK);

    // initialize window controller which provides drag and resize windows
    WindowController windowController = new WindowController(boundaryPanel);

    // content wrapper
    RoundedButton ok = new RoundedButton(okText);
    ok.getElement().setAttribute("id", "okButton"); //$NON-NLS-1$ //$NON-NLS-2$
    ok.addClickListener(new ClickListener() {

      public void onClick(Widget sender) {
        if (validatorCallback == null || (validatorCallback != null && validatorCallback.validate())) {
          try {
            if (callback != null) {
              callback.okPressed();
            }
          } catch (Throwable dontCare) {
          }
          hide();
        }
      }
    });
    final HorizontalPanel dialogButtonPanel = new HorizontalPanel();
    dialogButtonPanel.setSpacing(2);
    dialogButtonPanel.add(ok);
    if (cancelText != null) {
      RoundedButton cancel = new RoundedButton(cancelText);
      cancel.getElement().setAttribute("id", "cancelButton"); //$NON-NLS-1$ //$NON-NLS-2$
      cancel.addClickListener(new ClickListener() {

        public void onClick(Widget sender) {
          try {
            if (callback != null) {
              callback.cancelPressed();
            }
          } catch (Throwable dontCare) {
          }
          hide();
        }
      });
      dialogButtonPanel.add(cancel);
    }
    HorizontalPanel dialogButtonPanelWrapper = new HorizontalPanel();
    if (okText != null && cancelText != null) {
      dialogButtonPanelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    } else {
      dialogButtonPanelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }
    dialogButtonPanelWrapper.setStyleName("dialogButtonPanel"); //$NON-NLS-1$
    dialogButtonPanelWrapper.setWidth("100%"); //$NON-NLS-1$
    dialogButtonPanelWrapper.add(dialogButtonPanel);

    Grid dialogContent = new Grid(2, 1);
    dialogContent.setCellPadding(0);
    dialogContent.setCellSpacing(0);
    dialogContent.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
    dialogContent.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
    // add content
    dialogContent.setWidget(0, 0, content);
    dialogContent.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
    // add button panel
    dialogContent.setWidget(1, 0, dialogButtonPanelWrapper);
    dialogContent.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    dialogContent.setWidth("100%"); //$NON-NLS-1$
    dialogContent.setHeight("100%"); //$NON-NLS-1$

    windowPanel = new WindowPanel(windowController, headerText, dialogContent, true);
  }

  public void hide() {
    boundaryPanel.clear();
    RootPanel.get().remove(boundaryPanel);
  }

  public void center() {
    boundaryPanel.clear();
    int left = (Window.getClientWidth() - windowPanel.getOffsetWidth()) >> 1;
    int top = (Window.getClientHeight() - windowPanel.getOffsetHeight()) >> 1;
    boundaryPanel.add(windowPanel, Window.getScrollLeft() + left, Window.getScrollTop() + top);
    left = (Window.getClientWidth() - windowPanel.getOffsetWidth()) >> 1;
    top = (Window.getClientHeight() - windowPanel.getOffsetHeight()) >> 1;
    boundaryPanel.clear();
    boundaryPanel.add(windowPanel, Window.getScrollLeft() + left, Window.getScrollTop() + top);
  }

  public void show() {
    center();
  }

  public IDialogValidatorCallback getValidatorCallback() {
    return validatorCallback;
  }

  public void setValidatorCallback(IDialogValidatorCallback validatorCallback) {
    this.validatorCallback = validatorCallback;
  }

  public IDialogCallback getCallback() {
    return callback;
  }

  public void setCallback(IDialogCallback callback) {
    this.callback = callback;
  }

  public Widget getContent() {
    return content;
  }

  public void setText(String text) {
    windowPanel.setText(text);
  }

  public void setTitle(String title) {
    windowPanel.setTitle(title);
  }

  public void setPixelSize(int width, int height) {
    windowPanel.setPixelSize(width, height);
  }
  
}
