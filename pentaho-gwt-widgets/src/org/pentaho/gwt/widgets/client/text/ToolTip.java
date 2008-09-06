package org.pentaho.gwt.widgets.client.text;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
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
    lbl.setStyleName("tooltip-label");
    setWidget(lbl);
    this.delay = delay;
    this.setStyleName("tooltip");
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

