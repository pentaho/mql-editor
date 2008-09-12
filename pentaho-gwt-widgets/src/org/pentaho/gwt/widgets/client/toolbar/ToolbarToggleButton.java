package org.pentaho.gwt.widgets.client.toolbar;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class ToolbarToggleButton extends ToolbarButton {

  private boolean selected = false;
  private String TOGGLE_STYLE = "toolbar-toggle-button";   //$NON-NLS-1$
  /**
   * Constructs a toolbar button with an image and a label
   * 
   * @param img GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, String label, boolean selected) {
    super(img, label);
    super.setStylePrimaryName(TOGGLE_STYLE);
    this.selected = selected;
    updateSelectedStyle();
  }
  
  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, Image disabledImage, String label, boolean selected) {
    super(img, disabledImage, label);
    super.setStylePrimaryName(TOGGLE_STYLE);
    this.selected = selected;
    updateSelectedStyle();
  }
  

  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarToggleButton(Image img, Image disabledImage, boolean selected){
    super(img, disabledImage);
    super.setStylePrimaryName(TOGGLE_STYLE);
    this.selected = selected;
    updateSelectedStyle();
  }
  
  /**
   * Constructs a toolbar button with an image
   * 
   * @param img GWT Image object 
   */
  public ToolbarToggleButton(Image img){
    super(img);
    super.setStylePrimaryName(TOGGLE_STYLE);
  }
  
  /**
   * Returns a boolean based on the selected "down" state of the button
   * 
   * @return boolean flag
   */
  public boolean isSelected(){
    return this.selected;
  }
  
  private void toggleSelectedState(){
    selected = !(this.selected);
    updateSelectedStyle();
  }
  
  private void updateSelectedStyle() {
    if(selected){
      button.addStyleName(stylePrimaryName+"-down");    //$NON-NLS-1$
    } else {
      button.removeStyleName(stylePrimaryName+"-down");    //$NON-NLS-1$
      button.removeStyleName(stylePrimaryName+"-down-hovering");    //$NON-NLS-1$
    }
  }
  
  @Override
  protected void addStyleMouseListener(){
    eventWrapper.addMouseListener(new MouseListener(){
      public void onMouseDown(Widget arg0, int arg1, int arg2) {
        button.addStyleName(stylePrimaryName+"-down-hovering");    //$NON-NLS-1$ 
        button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$   
      }
      public void onMouseEnter(Widget arg0) {
        button.addStyleName(stylePrimaryName+((selected)?"-down":"")+"-hovering");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }
      public void onMouseLeave(Widget arg0) {
        button.removeStyleName(stylePrimaryName+((selected)?"-down":"")+"-hovering");    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        button.removeStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$ 
      }
      public void onMouseUp(Widget arg0, int arg1, int arg2) {
        if(!enabled){
          return;
        }
        toggleSelectedState();
        command.execute();
      }
      public void onMouseMove(Widget arg0, int arg1, int arg2) {}
    });
  }
}
