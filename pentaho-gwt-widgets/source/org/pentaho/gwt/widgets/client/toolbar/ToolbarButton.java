package org.pentaho.gwt.widgets.client.toolbar;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * Manages a PushButton in the common toolbar {@link Toolbar}. 
 * 
 * Note, it's not a subclass of PushButton as the PushButton api does not allow the 
 * changing of the image after instantiation. It is not a decorator because the GWT
 * PushButton does not implement an interface. If these limitations change, please 
 * change this class.
 *  
 * @author nbaker
 *
 */
public class ToolbarButton {
  protected DockPanel button = new DockPanel();
  protected boolean enabled = true;
  protected boolean visible = true;
  protected String text;
  protected Image image;
  protected Image disabledImage;
  protected Label label = new Label();
  protected FocusPanel eventWrapper = new FocusPanel();
  protected String stylePrimaryName = "toolbar-button";    //$NON-NLS-1$
  protected Command command;
  
  /**
   * Constructs a toolbar button with an image and a label
   * 
   * @param img GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarButton(Image img, String label){
    this(img);
    this.label.setText(label);
    button.add(this.label, DockPanel.EAST);
  }
  
  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarButton(Image img, Image disabledImage, String label){
    this(img, label);
    this.disabledImage = disabledImage;
  }
  

  /**
   * Constructs a toolbar button with an enabled image, disabled image and a label
   * 
   * @param img GWT Image object 
   * @param disabledImage GWT Image object 
   * @param label String containing an option label
   */
  public ToolbarButton(Image img, Image disabledImage){
    this(img);
    this.disabledImage = disabledImage;
  }
  
  /**
   * Constructs a toolbar button with an image, currently hardcoded to 16x16
   * 
   * @param img GWT Image object 
   */
  public ToolbarButton(Image img){
    this.image = img;
    button.add(this.image, DockPanel.CENTER);
    button.setCellHorizontalAlignment(this.image, DockPanel.ALIGN_CENTER);
    button.setCellVerticalAlignment(this.image, DockPanel.ALIGN_MIDDLE);

    button.setStyleName(stylePrimaryName);
    eventWrapper.add(button);
    
    addStyleMouseListener();
  }
  
  public void setStylePrimaryName(String styleName){
    this.stylePrimaryName = styleName;
    button.setStylePrimaryName(styleName);
    
  }

  
  protected void addStyleMouseListener(){
    eventWrapper.addMouseListener(new MouseListener(){
      public void onMouseDown(Widget w, int arg1, int arg2) {
        if(!enabled){
          return;
        }
        command.execute();
        button.addStyleName(stylePrimaryName+"-down");    //$NON-NLS-1$
      }
      public void onMouseEnter(Widget arg0) {
        button.addStyleName(stylePrimaryName+"-hovering");    //$NON-NLS-1$
      }
      public void onMouseLeave(Widget arg0) {
        button.removeStyleName(stylePrimaryName+"-hovering");   //$NON-NLS-1$
      }
      public void onMouseUp(Widget arg0, int arg1, int arg2) {
        button.removeStyleName(stylePrimaryName+"-down");   //$NON-NLS-1$
      }
      public void onMouseMove(Widget arg0, int arg1, int arg2) {}
    });
  }
  
  /**
   * Gets the enabled status of the button.
   * 
   * @return boolean flag
   */
  public boolean isEnabled() {
    return enabled;  
  }

  /**
   * Sets the enabled status of the button.
   * 
   * @param enabled boolean flag
   */
  public void setEnabled(boolean enabled) {
    boolean prevState = this.enabled;
    this.enabled = enabled;
    if(enabled){
      button.removeStyleName(stylePrimaryName+"-disabled");    //$NON-NLS-1$

      if(prevState == false && disabledImage != null){
        //was disabled, remove old image and put in the enabled one
        button.remove(disabledImage);
        button.add(image, DockPanel.CENTER);
        button.setCellHorizontalAlignment(this.image, DockPanel.ALIGN_CENTER);
        button.setCellVerticalAlignment(this.image, DockPanel.ALIGN_MIDDLE);
      }
      
    } else {
      button.addStyleName(stylePrimaryName+"-disabled");    //$NON-NLS-1$
      
      if(prevState == true && disabledImage != null){
        //was enabled, remove old image and put in the disabled one
        button.remove(image);
        button.add(disabledImage, DockPanel.CENTER);
        button.setCellHorizontalAlignment(this.disabledImage, DockPanel.ALIGN_CENTER);
        button.setCellVerticalAlignment(this.disabledImage, DockPanel.ALIGN_MIDDLE);
      }
    }
  }

  public void setTempDisabled(boolean disable) {
    button.setStyleName(
        this.enabled && !disable
        ? stylePrimaryName
        : stylePrimaryName+"-disabled"              //$NON-NLS-1$
    );
  }
  
  /**
   * Gets the visibility of the button]
   * 
   * @return boolean flag
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Sets the visibility of the button
   * 
   * @param visible boolean flag
   */
  public void setVisible(boolean visible) {
    this.visible = visible;
    button.setVisible(visible);
  }
  
  /**
   * Returns the managed PushButton object
   * 
   * @return PushButton concreate object
   */
  public FocusPanel getPushButton(){
    return eventWrapper;
  }

  /**
   * Returns the image displayed on this button.
   * 
   * @return GWT Image
   */
  public Image getImage() {
    return image;
  }

  /**
   * Returns the image displayed on this button.
   * 
   * @return GWT Image
   */
  public Image getDisabledImage() {
    return disabledImage;
  }

  /**
   * Sets the image displayed on this button.
   * 
   * @param img GWT Image
   */
  public void setImage(Image img) {
    this.image = img;
  }

  /**
   * Sets the image to be displayed on this button when disabled (greyed out).
   * 
   * @param img GWT Image
   */
  public void setDisabledImage(Image img) {
    this.disabledImage = img;
  }

  
  
  /**
   * Returns the optional text to be displayed on this button.
   * 
   * @return String
   */
  public String getText() {
    return text;
  }

  /**
   * Sets the optional text to be displayed on this button.
   * 
   * @param text String to be displayed
   */
  public void setText(String text) {
    this.text = text;
    label.setText(text);
  }

  /**
   * Returns the click listener attached to the button instance.
   * 
   * @return ClickListener
   */
  public Command getCommand() {
    return command;
  }

  /**
   * Sets the ClickListener on the button. If a ClickListener was previously
   * added to the button, it will be removed (let Nick know if you don't like
   * this behavior).
   * 
   * @param clickListener
   */
  public void setCommand(Command cmd) {
    this.command = cmd;
  }
  
  
  
}
