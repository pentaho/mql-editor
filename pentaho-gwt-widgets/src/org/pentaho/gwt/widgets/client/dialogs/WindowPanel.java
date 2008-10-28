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

import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class WindowPanel extends FocusPanel {

  /**
   * WindowPanel direction constant, used in
   * {@link ResizeDragController#makeDraggable(com.google.gwt.user.client.ui.Widget, com.allen_sauer.gwt.dnd.demo.client.example.resize.WindowPanel.DirectionConstant)}.
   */
  public static class DirectionConstant {

    public final int directionBits;

    public final String directionLetters;

    private DirectionConstant(int directionBits, String directionLetters) {
      this.directionBits = directionBits;
      this.directionLetters = directionLetters;
    }
  }

  /**
   * Specifies that resizing occur at the east edge.
   */
  public static final int DIRECTION_EAST = 0x0001;

  /**
   * Specifies that resizing occur at the both edge.
   */
  public static final int DIRECTION_NORTH = 0x0002;

  /**
   * Specifies that resizing occur at the south edge.
   */
  public static final int DIRECTION_SOUTH = 0x0004;

  /**
   * Specifies that resizing occur at the west edge.
   */
  public static final int DIRECTION_WEST = 0x0008;

  /**
   * Specifies that resizing occur at the east edge.
   */
  public static final DirectionConstant EAST = new DirectionConstant(DIRECTION_EAST, "e");

  /**
   * Specifies that resizing occur at the both edge.
   */
  public static final DirectionConstant NORTH = new DirectionConstant(DIRECTION_NORTH, "n");

  /**
   * Specifies that resizing occur at the north-east edge.
   */
  public static final DirectionConstant NORTH_EAST = new DirectionConstant(DIRECTION_NORTH | DIRECTION_EAST, "ne");

  /**
   * Specifies that resizing occur at the north-west edge.
   */
  public static final DirectionConstant NORTH_WEST = new DirectionConstant(DIRECTION_NORTH | DIRECTION_WEST, "nw");

  /**
   * Specifies that resizing occur at the south edge.
   */
  public static final DirectionConstant SOUTH = new DirectionConstant(DIRECTION_SOUTH, "s");

  /**
   * Specifies that resizing occur at the south-east edge.
   */
  public static final DirectionConstant SOUTH_EAST = new DirectionConstant(DIRECTION_SOUTH | DIRECTION_EAST, "se");

  /**
   * Specifies that resizing occur at the south-west edge.
   */
  public static final DirectionConstant SOUTH_WEST = new DirectionConstant(DIRECTION_SOUTH | DIRECTION_WEST, "sw");

  /**
   * Specifies that resizing occur at the west edge.
   */
  public static final DirectionConstant WEST = new DirectionConstant(DIRECTION_WEST, "w");

  private static final int BORDER_THICKNESS = 5;

  private static final String CSS_RESIZE_PANEL = "resizable-WindowPanel";

  private static final String CSS_RESIZE_PANEL_HEADER = "resizable-WindowPanel-header";

  private int contentHeight;

  private Widget contentOrScrollPanelWidget;

  private int contentWidth;

  private Widget eastWidget;

  private Grid grid = new Grid(3, 3);

  private final FocusPanel headerContainer;

  private final HTML headerWidget;

  private Widget northWidget;

  private Widget southWidget;

  private Widget westWidget;

  private final WindowController windowController;

  public WindowPanel(final WindowController windowController, String headerText, Widget contentWidget, boolean wrapContentInScrollPanel) {
    Window.alert("windowpanel");
    this.windowController = windowController;
    this.headerWidget = new HTML(headerText);
    setStyleName(CSS_RESIZE_PANEL);

    contentOrScrollPanelWidget = wrapContentInScrollPanel ? new ScrollPanel(contentWidget) : contentWidget;

    headerContainer = new FocusPanel();
    headerContainer.setStyleName(CSS_RESIZE_PANEL_HEADER);
    headerContainer.add(headerWidget);

    windowController.getPickupDragController().makeDraggable(this, headerContainer);

//    addClickListener(new ClickListener() {
//
//      public void onClick(Widget sender) {
//        // force our panel to the top of our z-index context
//        AbsolutePanel boundaryPanel = windowController.getBoundaryPanel();
//        WidgetLocation location = new WidgetLocation(WindowPanel.this, boundaryPanel);
//        boundaryPanel.add(WindowPanel.this, location.getLeft(), location.getTop());
//      }
//    });

    Grid verticalPanel = new Grid(2, 1);
    verticalPanel.setCellPadding(0);
    verticalPanel.setCellSpacing(0);
    verticalPanel.setWidget(0, 0, headerContainer);
    verticalPanel.setWidget(1, 0, contentOrScrollPanelWidget);
    verticalPanel.getCellFormatter().addStyleName(1, 0, "resizable-DialogContentWidget");
    verticalPanel.addStyleName("resizable-DialogHeaderAndContent");

    grid.setCellSpacing(0);
    grid.setCellPadding(0);
    add(grid);

    setupCell(0, 0, NORTH_WEST);
    northWidget = setupCell(0, 1, NORTH);
    setupCell(0, 2, NORTH_EAST);

    westWidget = setupCell(1, 0, WEST);
    grid.setWidget(1, 1, verticalPanel);
    eastWidget = setupCell(1, 2, EAST);

    setupCell(2, 0, SOUTH_WEST);
    southWidget = setupCell(2, 1, SOUTH);
    setupCell(2, 2, SOUTH_EAST);
  }

  public void setText(String text) {
    headerWidget.setHTML(text);
  }
  
  public void setTitle(String text) {
    setText(text);
  }
  
  public int getContentHeight() {
    return contentHeight;
  }

  public int getContentWidth() {
    return contentWidth;
  }

  public void moveBy(int right, int down) {
    AbsolutePanel parent = (AbsolutePanel) getParent();
    Location location = new WidgetLocation(this, parent);
    int left = location.getLeft() + right;
    int top = location.getTop() + down;
    parent.setWidgetPosition(this, left, top);
  }

  public void setContentSize(int width, int height) {
    if (width != contentWidth) {
      contentWidth = width;
      headerContainer.setPixelSize(contentWidth, headerWidget.getOffsetHeight());
      northWidget.setPixelSize(contentWidth, BORDER_THICKNESS);
      southWidget.setPixelSize(contentWidth, BORDER_THICKNESS);
    }
    if (height != contentHeight) {
      contentHeight = height;
      int headerHeight = headerContainer.getOffsetHeight();
      westWidget.setPixelSize(BORDER_THICKNESS, contentHeight + headerHeight);
      eastWidget.setPixelSize(BORDER_THICKNESS, contentHeight + headerHeight);
    }
    contentOrScrollPanelWidget.setPixelSize(contentWidth, contentHeight);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    if (contentOrScrollPanelWidget.getOffsetHeight() != 0) {
      headerWidget.setPixelSize(headerWidget.getOffsetWidth(), headerWidget.getOffsetHeight());
      setContentSize(contentOrScrollPanelWidget.getOffsetWidth(), contentOrScrollPanelWidget.getOffsetHeight());
    }
  }

  private Widget setupCell(int row, int col, DirectionConstant direction) {
    final FocusPanel widget = new FocusPanel();
    widget.setPixelSize(BORDER_THICKNESS, BORDER_THICKNESS);
    grid.setWidget(row, col, widget);
    windowController.getResizeDragController().makeDraggable(widget, direction);
    widget.setStyleName("dialog-resize-" + direction.directionLetters);
    //grid.getCellFormatter().setStyleName(row, col, "demo-resize-" + direction.directionLetters);
    return widget;
  }
}
