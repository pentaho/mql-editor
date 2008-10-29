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
package org.pentaho.gwt.widgets.client.text;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolTip extends PopupPanel implements MouseListener{

  private Timer timer = new Timer() {
    public void run() {
      show();
    }
  };
  private int delay;
  
  public ToolTip(String message, int delay){
    super(true);
    Label lbl = new Label(message);
    lbl.setStyleName("tooltip-label"); //$NON-NLS-1$
    setWidget(lbl);
    this.delay = delay;
    this.setStyleName("tooltip"); //$NON-NLS-1$
  }

  public void onMouseDown(Widget sender, int x, int y) {
    timer.cancel();
    hide();
  }

  public void onMouseEnter(Widget sender) {
    timer.schedule(delay);
    this.setPopupPosition(sender.getAbsoluteLeft()+sender.getOffsetWidth()-3, sender.getAbsoluteTop()+sender.getOffsetHeight()+2);
  }

  public void onMouseLeave(Widget sender) {
    timer.cancel();
    hide();
  }

  public void onMouseMove(Widget sender, int x, int y) {}
  public void onMouseUp(Widget sender, int x, int y) {}
  
}

