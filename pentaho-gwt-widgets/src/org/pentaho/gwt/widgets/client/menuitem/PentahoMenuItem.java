/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
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
      setStyleName("gwt-MenuItem"); //$NON-NLS-1$
    } else {
      setStyleName("disabledMenuItem"); //$NON-NLS-1$
    }    
  }
  
  public boolean isEnabled() {
    return enabled;
  }
}
