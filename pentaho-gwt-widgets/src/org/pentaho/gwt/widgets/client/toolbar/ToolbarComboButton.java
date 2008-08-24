package org.pentaho.gwt.widgets.client.toolbar;

import com.google.gwt.user.client.Command;
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
    eventWrapper.addMouseListener(new MouseListener(){
      public void onMouseDown(Widget w, int x, int y) {
        if(!enabled){
          return;
        }        
        popup.setPopupPosition(w.getAbsoluteLeft(), w.getAbsoluteTop() + w.getOffsetHeight());
        popup.show();
      }
      public void onMouseEnter(Widget w) {
        button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$
      }
      public void onMouseLeave(Widget w) {
        button.removeStyleName(stylePrimaryName+"-hovering");   //$NON-NLS-1$
      }
      public void onMouseUp(Widget w, int x, int y) {
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
