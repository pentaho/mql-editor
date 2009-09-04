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
 *
 * Created September 2, 2009
 * @author rmansoor
 */
package org.pentaho.gwt.widgets.login.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This interface needs to be implemented by the service using the AuthenticatedGwtService wrapper
 * and put any work then needs to be performed when the user selects the service
 */
public interface IAuthenticatedGwtCommand {
  /**
   * Work to be done. 
   * @param AsyncCallback - call back 
   */
  public void execute(AsyncCallback callback);
}

  