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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.utils.StringTokenizer;
import org.pentaho.gwt.widgets.login.client.messages.Messages;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class LoginDialog extends PromptDialogBox {

  private AsyncCallback<Boolean> outerCallback; // from outside context

  private final TextBox userTextBox = new TextBox();

  private final ListBox usersListBox = new ListBox();

  private final PasswordTextBox passwordTextBox = new PasswordTextBox();

  private CheckBox newWindowChk = new CheckBox();

  private String returnLocation = null;

  private static boolean showUsersList = false;

  private static boolean showNewWindowOption = false;

  private static boolean openInNewWindowDefault = false;

  private static LinkedHashMap<String, String[]> defaultUsers = new LinkedHashMap<String, String[]>();
  
  private final IDialogCallback myCallback = new IDialogCallback() {

    public void cancelPressed() {
      outerCallback.onFailure(new AuthenticationCanceledException(Messages.getString("authCanceled"))); //$NON-NLS-1$
    }

    public void okPressed() {
      RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, getWebAppPath() + "j_spring_security_check"); //$NON-NLS-1$
      String username = userTextBox.getText();
      String password = passwordTextBox.getText();
      String requestData = "j_username=" + URL.encodeComponent(username) + "&j_password=" + URL.encodeComponent(password); //$NON-NLS-1$ //$NON-NLS-2$
      builder.setHeader("Content-Type", "application/x-www-form-urlencoded"); //$NON-NLS-1$ //$NON-NLS-2$
      RequestCallback callback = new RequestCallback() {

        public void onError(Request request, Throwable exception) {
          outerCallback.onFailure(exception);
        }

        public void onResponseReceived(Request request, Response response) {
          if (Response.SC_OK == response.getStatusCode()) {
              outerCallback.onSuccess(false);
          } else {
              outerCallback.onFailure(new AuthenticationFailedException(Messages.getString("authFailed"))); //$NON-NLS-1$
          }
        }
      };

      try {
        builder.sendRequest(requestData, callback); //$NON-NLS-1$ //$NON-NLS-2$
      } catch (RequestException e) {
        e.printStackTrace();
      }
    }

  };

  public LoginDialog() {
    
    super(Messages.getString("login"), Messages.getString("login"), Messages.getString("cancel"), false, true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    setCallback(myCallback);
    this.addStyleName("login-dialog");
    //TODO Once we have a service available which actually returns the show user list information we can uncomment this section
    /*
    RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, getWebAppPath() + "content/ws-run/loginService/isShowUsersList"); //$NON-NLS-1$
    try {
      requestBuilder.sendRequest(null, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          getLoginSettingsAndShow(false);
        }

        public void onResponseReceived(Request request, Response response) {
          if (Response.SC_OK == response.getStatusCode()) {
            Document messageDom = XMLParser.parse(response.getText());
            NodeList list = messageDom.getElementsByTagName("return"); //$NON-NLS-1$
            if(list != null && list.getLength() == 1) {
              String returnValue = list.item(0).getFirstChild().getNodeValue();
              if(returnValue != null && returnValue.length() > 0) {
                getLoginSettingsAndShow(new Boolean(returnValue));  
              }
            }
          } else {
            getLoginSettingsAndShow(false);
          }
        }
      });
    } catch (RequestException e) {
      getLoginSettingsAndShow(false);
    }*/
    getLoginSettingsAndShow(false);
  }

  public void getLoginSettingsAndShow(final boolean showUsersListDefault) {
    //TODO Once we have a service available which actually returns the login setings information we can uncomment this section
    /*
    RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, getWebAppPath() + "content/ws-run/loginService/getLoginSettings"); //$NON-NLS-1$
    try {
      requestBuilder.sendRequest(null, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          setContent(buildLoginPanel(false));
          if (isAttached() && isVisible()) {
            center();
          }
        }

        public void onResponseReceived(Request request, Response response) {
          if (Response.SC_OK == response.getStatusCode()) {
            Document messageDom = XMLParser.parse(response.getText());
            List<String> userIdList = new ArrayList<String>();
            List<String> userDisplayNameList = new ArrayList<String>();
            List<String> passwordList = new ArrayList<String>();
            Boolean showUser = null;
            Boolean openInNewWindow = null;
  
            NodeList passwordNodeList = messageDom.getElementsByTagName("passwords"); //$NON-NLS-1$
            NodeList userDisplayNameNodeList = messageDom.getElementsByTagName("userDisplayNames"); //$NON-NLS-1$
            NodeList userIdNodeList = messageDom.getElementsByTagName("userIds"); //$NON-NLS-1$
            NodeList openInNewWindowNodeList = messageDom.getElementsByTagName("openInNewWindow"); //$NON-NLS-1$
            NodeList showUsersNodeList = messageDom.getElementsByTagName("showUsersList"); //$NON-NLS-1$
            
            for (int i = 0; i < passwordNodeList.getLength(); i++) {
              Element element = (Element)passwordNodeList.item(i);
              passwordList.add(element.getFirstChild().getNodeValue());
            }
            for (int i = 0; i < userDisplayNameNodeList.getLength(); i++) {
              Element element = (Element)userDisplayNameNodeList.item(i);
              userDisplayNameList.add(element.getFirstChild().getNodeValue());
            }
            for (int i = 0; i < userIdNodeList.getLength(); i++) {
              Element element = (Element)userIdNodeList.item(i);
              userIdList.add(element.getFirstChild().getNodeValue());
            }
            if(openInNewWindowNodeList != null && openInNewWindowNodeList.getLength() == 1) {
              String openInNewWindowValue = openInNewWindowNodeList.item(0).getFirstChild().getNodeValue();
              if(openInNewWindowValue != null && openInNewWindowValue.length() > 0) {
                openInNewWindow = new Boolean(openInNewWindowValue);  
              }
            }
            if(showUsersNodeList != null && showUsersNodeList.getLength() == 1) {
              String showUserValue = showUsersNodeList.item(0).getFirstChild().getNodeValue();
              if(showUserValue != null && showUserValue.length() > 0) {
                showUser = new Boolean(showUserValue);  
              }
            }
            // build default users list
            defaultUsers.clear();
            defaultUsers.put(Messages.getString("selectUser"), new String[] { "", "" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            for (int i = 0; i < userIdList.size(); i++) {
              defaultUsers.put(userDisplayNameList.get(i), new String[] { userIdList.get(i).trim(),
                passwordList.get(i).trim() }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            // provide the opportunity to override showUsersList with a setting
            if (showUser != null) { //$NON-NLS-1$
              showUsersList = showUser; //$NON-NLS-1$ //$NON-NLS-2$
            }
            // get the default 'open in new window' flag, this is the default, overridden by a cookie
            if(openInNewWindow != null) {
              openInNewWindowDefault = openInNewWindow; //$NON-NLS-1$ //$NON-NLS-2$  
            }
          }
          setContent(buildLoginPanel(openInNewWindowDefault));
          if (isAttached() && isVisible()) {
            center();
          }
        }
      });
    } catch (RequestException e) {
      setContent(buildLoginPanel(openInNewWindowDefault));
      if (isAttached() && isVisible()) {
        center();
      }
    }*/
    setContent(buildLoginPanel(openInNewWindowDefault));
    if (isAttached() && isVisible()) {
      center();
    }
  }
  public LoginDialog(AsyncCallback callback, boolean showNewWindowOption) {
    this();
    setCallback(callback);
    setShowNewWindowOption(showNewWindowOption);
  }

  public void setShowNewWindowOption(boolean show) {
    showNewWindowOption = show;
  }

  public static void performLogin(final AsyncCallback callback) {
    LoginDialog dialog = new LoginDialog(callback, false);
    dialog.show();
    dialog.center();
  }

  private Widget buildLoginPanel(boolean openInNewWindowDefault) {
    userTextBox.setWidth("100%"); //$NON-NLS-1$
    passwordTextBox.setWidth("100%"); //$NON-NLS-1$
    usersListBox.setWidth("100%"); //$NON-NLS-1$

    VerticalPanel loginPanel = new VerticalPanel();

    loginPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    loginPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
    SimplePanel spacer;
    if (showUsersList) {
      // populate default users list box
      addDefaultUsers();
      loginPanel.add(new Label(Messages.getString("sampleUser") + ":")); //$NON-NLS-1$ //$NON-NLS-2$
      loginPanel.add(usersListBox);
      spacer = new SimplePanel();
      spacer.setHeight("8px"); //$NON-NLS-1$
      loginPanel.add(spacer);
    }
    loginPanel.add(new Label(Messages.getString("username") + ":")); //$NON-NLS-1$ //$NON-NLS-2$
    loginPanel.add(userTextBox);

    spacer = new SimplePanel();
    spacer.setHeight("8px"); //$NON-NLS-1$
    loginPanel.add(spacer);

    loginPanel.setCellHeight(spacer, "8px"); //$NON-NLS-1$
    loginPanel.add(new HTML(Messages.getString("password") + ":")); //$NON-NLS-1$ //$NON-NLS-2$
    loginPanel.add(passwordTextBox);

    boolean reallyShowNewWindowOption = openInNewWindowDefault;

    String showNewWindowOverride = Window.Location.getParameter("showNewWindowOption"); //$NON-NLS-1$
    if (showNewWindowOverride != null && !"".equals(showNewWindowOverride)) { //$NON-NLS-1$
      // if the override is set, we MUST obey it above all else
      reallyShowNewWindowOption = "true".equals(showNewWindowOverride); //$NON-NLS-1$
    } else if (getReturnLocation() != null && !"".equals(getReturnLocation())) { //$NON-NLS-1$
      StringTokenizer st = new StringTokenizer(getReturnLocation(), "?&"); //$NON-NLS-1$
      // first token will be ignored, it is 'up to the ?'
      for (int i = 1; i < st.countTokens(); i++) {
        StringTokenizer paramTokenizer = new StringTokenizer(st.tokenAt(i), "="); //$NON-NLS-1$
        if (paramTokenizer.countTokens() == 2) {
          // we've got a name=value token
          if (paramTokenizer.tokenAt(0).equalsIgnoreCase("showNewWindowOption")) { //$NON-NLS-1$
            reallyShowNewWindowOption = "true".equals(paramTokenizer.tokenAt(1)); //$NON-NLS-1$
            break;
          }
        }
      }
    }

    // New Window checkbox
    if (reallyShowNewWindowOption) {
      spacer = new SimplePanel();
      spacer.setHeight("8px"); //$NON-NLS-1$
      loginPanel.add(spacer);
      loginPanel.setCellHeight(spacer, "8px"); //$NON-NLS-1$

      newWindowChk.setText(Messages.getString("launchInNewWindow")); //$NON-NLS-1$

      String cookieCheckedVal = Cookies.getCookie("loginNewWindowChecked"); //$NON-NLS-1$
      if (cookieCheckedVal != null) {
        newWindowChk.setChecked(Boolean.parseBoolean(cookieCheckedVal));
      } else {
        // default is false, per BISERVER-2384
        newWindowChk.setChecked(openInNewWindowDefault);
      }

      loginPanel.add(newWindowChk);
    }

    userTextBox.setTabIndex(1);
    passwordTextBox.setTabIndex(2);
    if (reallyShowNewWindowOption) {
      newWindowChk.setTabIndex(3);
    }
    passwordTextBox.setText(""); //$NON-NLS-1$
    setFocusWidget(userTextBox);

    return loginPanel;
  }

  public void setCallback(AsyncCallback<Boolean> callback) {
    outerCallback = callback;
  }

  public void addDefaultUsers() {
    usersListBox.clear();
    for (Map.Entry<String, String[]> entry : defaultUsers.entrySet()) {
      usersListBox.addItem(entry.getKey());
    }
    usersListBox.addChangeListener(new ChangeListener() {

      public void onChange(Widget sender) {
        String key = usersListBox.getValue(usersListBox.getSelectedIndex());
        userTextBox.setText(defaultUsers.get(key)[0]);
        passwordTextBox.setText(defaultUsers.get(key)[1]);
      }
    });
  }

  public String getReturnLocation() {
    return returnLocation;
  }

  public void setReturnLocation(String returnLocation) {
    this.returnLocation = returnLocation;
    // the return location might have a parameter in the url to configure options,
    // so we must rebuild the UI if the return location is changed
    setContent(buildLoginPanel(openInNewWindowDefault));
  }
  
  private String getWebAppPath() {
    String[] pathArray = Window.Location.getPath().split("/");
    // TODO Figure out how this webAppPath will work in a root web app
    return  "/" + pathArray[1] + "/";
 }
}
