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

package org.pentaho.gwt.widgets.login.client;

import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.login.client.messages.Messages;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class is a wrapper class to any GWT service which needs authentication before it performs any work
 * The class first tries to execute the service and if the service returned an Response.SC_UNAUTHORIZED
 * error, the user is presented with a login dialog. Once the login is successful it performs the service
 * which was request originally. If the error is anything different than the unauthorized error, user 
 * is presented with the error dialog
 *
 */
public class AuthenticatedGwtServiceUtil {

  private AuthenticatedGwtServiceUtil() {
    
  }

  /**
   * invokeCommand method tries to execute the service and incase of SC_UNAUTHORIZED error
   * the login dialog is presented where the user can enter their credentials. Once
   * you are successfully logged in, the requested services is performed again
   * @param command
   * @param theirCallback
   */
  public static void invokeCommand(final IAuthenticatedGwtCommand command, final AsyncCallback theirCallback) {
      command.execute(new AsyncCallback(){

        public void onFailure(final Throwable caught) {
          // The request has failed. We need to check if the request failed due to authentication
          // We will simply to a get of an image and if it responds back with Response.SC_UNAUTHORIZED
          // We will display a login page to the user otherwise we will return the error back to the 
          // user
          String[] pathArray = Window.Location.getPath().split("/");
          String webAppPath = "/" + pathArray[1] + "/";
          RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, webAppPath + "mantle/images/spacer.gif"); //$NON-NLS-1$
          builder.setHeader("Content-Type", "application/xml");
          RequestCallback callback = new RequestCallback() {

            public void onError(Request request, Throwable exception) {
                theirCallback.onFailure(caught);
            }

            public void onResponseReceived(Request request, Response response) {
              if (Response.SC_UNAUTHORIZED == response.getStatusCode()) {
                doLogin(command, theirCallback);
              } else {
                theirCallback.onFailure(caught);
              }
            }
          };
         
            try {
              builder.sendRequest("", callback); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (RequestException e) {
              e.printStackTrace();
            }
        }

        public void onSuccess(Object result) {
          theirCallback.onSuccess(result);
        }
      }); 
  }

  /**
   * Display the login screen and and validate the credentials supplied by the user
   * if the credentials are correct, the execute method is being invoked other wise
   * error dialog is being display. On clicking ok button on the dialog box, login
   * screen is displayed again and process is repeated until the user click cancel 
   * or user is successfully authenticated
   *  */
  private static void doLogin(final IAuthenticatedGwtCommand command, final AsyncCallback theirCallback) {
    LoginDialog.performLogin(new AsyncCallback<Object>() {

      public void onFailure(Throwable caught) {
        if(caught instanceof AuthenticationFailedException) {
        MessageDialogBox dialogBox = new MessageDialogBox(
            Messages.getString("error"), Messages.getString("invalidLogin"), false, false, true); //$NON-NLS-1$ //$NON-NLS-2$
        dialogBox.addStyleName("error-login-dialog");
        dialogBox.setCallback(new IDialogCallback() {
          public void cancelPressed() {
            // do nothing
          }

          public void okPressed() {
            doLogin(command, theirCallback);
          }

        });
        dialogBox.center();
        } else if(caught instanceof AuthenticationCanceledException) {
          theirCallback.onFailure(caught);
        }
      }

      public void onSuccess(Object result) {
        invokeCommand(command, theirCallback);
      }

    });
  }
}
