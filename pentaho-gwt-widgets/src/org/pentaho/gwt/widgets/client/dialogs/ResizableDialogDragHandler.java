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

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.HTML;

/**
 * Shared drag handler which display events as they are received by the various
 * drag controllers.
 */
public final class ResizableDialogDragHandler implements DragHandler {

  /**
   * CSS blue.
   */
  private static final String BLUE = "#4444BB"; //$NON-NLS-1$

  /**
   * CSS green.
   */
  private static final String GREEN = "#44BB44"; //$NON-NLS-1$

  /**
   * CSS red.
   */
  private static final String RED = "#BB4444"; //$NON-NLS-1$

  /**
   * Text area where event messages are shown.
   */
  private final HTML eventTextArea;

  ResizableDialogDragHandler(HTML dragHandlerHTML) {
    eventTextArea = dragHandlerHTML;
  }

  /**
   * Log the drag end event.
   * 
   * @param event the event to log
   */
  public void onDragEnd(DragEndEvent event) {
    log("onDragEnd: " + event, RED); //$NON-NLS-1$
  }

  /**
   * Log the drag start event.
   * 
   * @param event the event to log
   */
  public void onDragStart(DragStartEvent event) {
    log("onDragStart: " + event, GREEN); //$NON-NLS-1$
  }

  /**
   * Log the preview drag end event.
   * 
   * @param event the event to log
   * @throws VetoDragException exception which may be thrown by any drag handler
   */
  public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
    log("<br>onPreviewDragEnd: " + event, BLUE); //$NON-NLS-1$
  }

  /**
   * Log the preview drag start event.
   * 
   * @param event the event to log
   * @throws VetoDragException exception which may be thrown by any drag handler
   */
  public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
    clear();
    log("onPreviewDragStart: " + event, BLUE); //$NON-NLS-1$
  }

  private void clear() {
    eventTextArea.setHTML(""); //$NON-NLS-1$
  }

  private void log(String text, String color) {
    eventTextArea.setHTML(eventTextArea.getHTML()
        + (eventTextArea.getHTML().length() == 0 ? "" : "<br>") + "<span style='color: " + color //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        + "'>" + text + "</span>"); //$NON-NLS-1$ //$NON-NLS-2$
  }
}
