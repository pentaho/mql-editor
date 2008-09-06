package org.pentaho.gwt.widgets.client.toolbar;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;


/**
 * 
 * Represents a collection of related buttons in a {@linkplain Toolbar}. Buttons in the group can be disabled/enabled and
 * have their visibility changed together.
 * 
 * @author nbaker
 */
public class ToolbarGroup {
  
  private List<ToolbarButton> buttons = new ArrayList<ToolbarButton>();
  private boolean enabled = true;
  private boolean visible = true;
  private String label = null;
  private Image trailingSeparator = new Image( "style/images/toolbarDivider.png", 0, 0, 2, 16 ); //$NON-NLS-1$;
  private Image leadingSeparator = new Image( "style/images/toolbarDivider.png", 0, 0, 2, 16 ); //$NON-NLS-1$;
  private Label groupLabel = new Label();
  
  public static final String CSS_ENABLED= "toolbar-group-label";  //$NON-NLS-1$ 
  public static final String CSS_DISABLED = "toolbar-group-label-disabled";  //$NON-NLS-1$ 

  public ToolbarGroup(){
    groupLabel.setStyleName(CSS_ENABLED);    
  }
  
  /**
   * Initialized the ToolbarGroup with a label description
   * @param groupName
   */
  public ToolbarGroup(String groupName){
    this();
    setLabel(groupName);
  }
  
  /**
   * Adds a {@link ToolbarButton} to this group.
   *  
   * @param btn ToolbarButton
   */
  public void add(ToolbarButton btn){
    if(!buttons.contains(btn)){
      buttons.add(btn);
    } else {
      //log error
    }
  }
  
  /**
   * Changes the enabled status of the group. If enabled is false, the buttons will be disabled.
   * If enabled is true, it will consult the buttons for their current enabled state.
   * 
   * @param enabled boolena flag
   */
  public void setEnabled(boolean enabled){
    if(enabled == this.enabled){ // no change
      return;
    }
    this.enabled = enabled;
    for(ToolbarButton btn : buttons){
      btn.setEnabled(this.enabled);
    }
    this.groupLabel.setStyleName(
        (this.enabled)? CSS_ENABLED : CSS_DISABLED
    );
  }
  

  public void setTempDisabled(boolean disable) {
    
    for(ToolbarButton btn : buttons){
      btn.setTempDisabled(disable);
    }
    this.groupLabel.setStyleName(
        (disable)?  CSS_DISABLED : CSS_ENABLED
    );
    
  }

  
  /** 
   * Returns the enabled status of this group
   * 
   * @return boolean flag
   */
  public boolean isEnabled(){
    return this.enabled;
  }

  /**
   * Returns the collection of buttons managed by this ToolbarGroup.
   * 
   * @return List of ToolbarButtons
   */
  public List<ToolbarButton> getButtons() {
    return buttons;
  }

  /**
   * Returns the visibility of the group.
   * 
   * @return boolean flag
   */
  public boolean isVisible() {
    return visible;
  }

  /**
   * Sets the visibility of the group. If visible is false the group will be hidden. 
   * If visible is true, the buttons will be returned to their previous visible state.
   * 
   * @param visible boolean flag
   */
  public void setVisible(boolean visible) {
    if(visible == this.visible){ // no change
      return;
    }
    
    this.visible = visible;
    for(ToolbarButton btn : buttons){
      btn.setVisible(this.visible);
    }
    groupLabel.setVisible(this.visible);
    trailingSeparator.setVisible(this.visible);
    leadingSeparator.setVisible(this.visible);
  }

  /**
   * Returns the optional label to be displayed before the group buttons in the Toolbar.
   * 
   * @return String
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the optional label to be displayed before the group button in the Toolbar
   * 
   * @param label String to be used as a label
   */
  public void setLabel(String label) {
    this.label = label;
    this.groupLabel.setText(this.label);
  }
  
  /**
   * Returns the image separator to be shown before the group. The ToolbarGroup 
   * manages this object so it can toggle it's visibility.
   * 
   * @return Image
   */
  public Image getTrailingSeparator() {
    return trailingSeparator;
  }

  /**
   * Returns the image separator to be shown after the group. The ToolbarGroup 
   * manages this object so it can toggle it's visibility.
   * 
   * @return Image
   */
  public Image getLeadingSeparator() {
    return leadingSeparator;
  }

  /**
   * Returns the Label object to be optionally displayed by the Toolbar.
   * The ToolbarGroup manages this object so it can toggle it's disabled/visible
   * state.
   * 
   * @return Label
   */
  public Label getGroupLabel() {
    return groupLabel;
  }
  
  
}
