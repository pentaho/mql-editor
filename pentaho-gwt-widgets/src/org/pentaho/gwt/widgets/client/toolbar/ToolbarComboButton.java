package org.pentaho.gwt.widgets.client.toolbar;

import org.pentaho.gwt.widgets.client.utils.ElementUtils;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class ToolbarComboButton extends ToolbarButton{
  
  private String COMBO_STYLE = "toolbar-combo-button";   //$NON-NLS-1$
  private MenuBar menu;
  PopupPanel popup = new PopupPanel(true);
  
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
          ElementUtils.blur(ToolbarComboButton.this.eventWrapper.getElement());
          return;
        }        
        popup.setPopupPosition(sender.getAbsoluteLeft(), sender.getAbsoluteTop() + sender.getOffsetHeight());
        popup.show();
        ElementUtils.blur(ToolbarComboButton.this.eventWrapper.getElement());
      }
    });
    eventWrapper.addMouseListener(new MouseListener(){
      public void onMouseDown(Widget w, int x, int y) {
      }
      public void onMouseEnter(Widget w) {
        button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$
      }
      public void onMouseLeave(Widget w) {
        button.removeStyleName(stylePrimaryName+"-hovering");   //$NON-NLS-1$
      }
      public void onMouseUp(Widget w, int x, int y) {
        if(!enabled){
          ElementUtils.blur(ToolbarComboButton.this.eventWrapper.getElement());
          return;
        }        
        popup.setPopupPosition(w.getAbsoluteLeft(), w.getAbsoluteTop() + w.getOffsetHeight());
        ElementUtils.blur(ToolbarComboButton.this.eventWrapper.getElement());
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
}
