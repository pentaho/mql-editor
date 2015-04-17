/*!
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
* Copyright (c) 2002-2015 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.commons.metadata.mqleditor.messages;

import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;

public class GwtMqlMessages implements IMqlMessages {
  private ResourceBundle messages;

  public GwtMqlMessages( ResourceBundle messages ) {
    this.messages = messages;
  }

  public String getString( String key, String defaultMessage, String... args ) {
    return messages.getString( key, defaultMessage, args );
  }
}
