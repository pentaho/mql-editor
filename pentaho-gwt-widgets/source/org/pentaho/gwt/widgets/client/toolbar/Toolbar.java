package org.pentaho.gwt.widgets.client.toolbar;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Displays a collection of buttons in a standard toolbar view. Also supports 
 * ToolbarGroup objects that manage related buttons.
 * 
 * @author nbaker
 */
public class Toolbar extends HorizontalPanel{
  
  public static final int SEPARATOR = 1;
  public static final int GLUE = 2;
  
  //table holding the buttons
  private HorizontalPanel bar = new HorizontalPanel();
  
  //Collection of buttons
  private List<ToolbarButton> buttons = new ArrayList<ToolbarButton>();
  
  //collection of groups
  private List<ToolbarGroup> groups = new ArrayList<ToolbarGroup>();
  
  public Toolbar(){
    this.setStylePrimaryName("toolbar");   //$NON-NLS-1$
    add(bar);
    bar.setSpacing(1);
    setWidth("100%");   //$NON-NLS-1$
    setHeight("100%");  //$NON-NLS-1$
  }
  
  /**
   * Add in a collection of buttons assembled as a ToolbarGroup
   * 
   * @param group ToolbarGroup to add.
   */
  public void add(ToolbarGroup group){
    
    //check to see if there's already a separator added before this group
    if( !(bar.getWidget(bar.getWidgetCount()-1) instanceof Image)){
      bar.add(group.getLeadingSeparator());
      bar.setCellVerticalAlignment(group.getLeadingSeparator(), ALIGN_MIDDLE);
    }
    
    //if the group has a label tag, add it before the buttons
    if(group.getLabel() != null){
      bar.add(group.getGroupLabel());
      bar.setCellVerticalAlignment(group.getGroupLabel(), ALIGN_MIDDLE);
    }
    
    //add the buttons to the bar and buttons collection
    for(ToolbarButton btn : group.getButtons()){
      bar.add(btn.getPushButton());
    }
    bar.add(group.getTrailingSeparator());
    bar.setCellVerticalAlignment(group.getTrailingSeparator(), ALIGN_MIDDLE);
    groups.add(group);
  }
  
  /**
   * Add a Button to the Toolbar
   */
  public void add(ToolbarButton button){
    bar.add(button.getPushButton());
    buttons.add(button);
  }
  
  /**
   * Add a special element to the toolbar. Support for separator and glue.
   * @param key id of element to add
   */
  public void add(int key){
    switch(key){
      case Toolbar.SEPARATOR:
        Image img = new Image( "style/images/toolbarDivider.png"); //$NON-NLS-1$
        bar.add(img);
        bar.setCellVerticalAlignment(img, ALIGN_MIDDLE);
        break;
      case Toolbar.GLUE:
        SimplePanel panel = new SimplePanel();
        bar.add(panel);
        bar.setCellWidth(panel, "100%");  //$NON-NLS-1$
        break;
      default:
        //add error logging message
    }
  }
  
  
  /**
   * Enable or Disable the toolbar. If passed in false it will disable all buttons, if true it 
   * will restore the buttons to their previous state.
   * 
   * @param enabled boolean flag
   */
  public void setEnabled(boolean enabled){
    try{
      for(ToolbarButton button : this.buttons){
        button.setTempDisabled(!enabled);
      }
      
      for(ToolbarGroup gp : groups){
       gp.setTempDisabled(!enabled);
      }
      
    } catch(Exception e){
      System.out.println("Error with Disable: "+e);   //$NON-NLS-1$
      e.printStackTrace(System.out);
    }
  }
  
  
}
