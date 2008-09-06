package org.pentaho.gwt.widgets.client.menuitem;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuItem;

public class CheckBoxMenuItem extends MenuItem {

  boolean checked = true;

  public CheckBoxMenuItem(String text, Command cmd) {
    super(text, cmd);
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
    if (checked) {
      setStylePrimaryName("gwt-MenuItem-checkbox-checked");
    } else {
      setStylePrimaryName("gwt-MenuItem-checkbox-unchecked");
    }
  }

  public boolean isChecked() {
    return checked;
  }

}
