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
  private static final String BLUE = "#4444BB";

  /**
   * CSS green.
   */
  private static final String GREEN = "#44BB44";

  /**
   * CSS red.
   */
  private static final String RED = "#BB4444";

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
    log("onDragEnd: " + event, RED);
  }

  /**
   * Log the drag start event.
   * 
   * @param event the event to log
   */
  public void onDragStart(DragStartEvent event) {
    log("onDragStart: " + event, GREEN);
  }

  /**
   * Log the preview drag end event.
   * 
   * @param event the event to log
   * @throws VetoDragException exception which may be thrown by any drag handler
   */
  public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
    log("<br>onPreviewDragEnd: " + event, BLUE);
  }

  /**
   * Log the preview drag start event.
   * 
   * @param event the event to log
   * @throws VetoDragException exception which may be thrown by any drag handler
   */
  public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
    clear();
    log("onPreviewDragStart: " + event, BLUE);
  }

  private void clear() {
    eventTextArea.setHTML("");
  }

  private void log(String text, String color) {
    eventTextArea.setHTML(eventTextArea.getHTML()
        + (eventTextArea.getHTML().length() == 0 ? "" : "<br>") + "<span style='color: " + color
        + "'>" + text + "</span>");
  }
}
