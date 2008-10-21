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

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;

final class WindowController {

  private final AbsolutePanel boundaryPanel;

  private PickupDragController pickupDragController;

  private ResizeDragController resizeDragController;

  WindowController(AbsolutePanel boundaryPanel) {
    this.boundaryPanel = boundaryPanel;

    pickupDragController = new PickupDragController(boundaryPanel, true);
    pickupDragController.setBehaviorConstrainedToBoundaryPanel(true);
    pickupDragController.setBehaviorMultipleSelection(false);

    resizeDragController = new ResizeDragController(boundaryPanel);
    resizeDragController.setBehaviorConstrainedToBoundaryPanel(true);
    resizeDragController.setBehaviorMultipleSelection(false);
  }

  public AbsolutePanel getBoundaryPanel() {
    return boundaryPanel;
  }

  public PickupDragController getPickupDragController() {
    return pickupDragController;
  }

  public ResizeDragController getResizeDragController() {
    return resizeDragController;
  }
}
