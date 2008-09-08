package org.pentaho.gwt.widgets.client.menuitem;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public class PentahoMenuItem extends MenuItem {

  private boolean enabled = false;
  
  public PentahoMenuItem(String text, Command cmd) {
    super(text, cmd);
    setEnabled(enabled);
  }
  
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    if (enabled) {
      setStyleName("gwt-MenuItem");
    } else {
      setStyleName("disabledMenuItem");
    }    
  }
  
  public boolean isEnabled() {
    return enabled;
  }
}
