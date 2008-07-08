/*
 * Copyright 2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * Created Mar 25, 2008
 * @author Michael D'Amour
 */
package org.pentaho.gwt.widgets.client.dialogs;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MessageDialogBox extends DialogBox {

  public MessageDialogBox(String title, String message, boolean isHTML, final IDialogCallback callback, boolean autoHide, boolean modal) {
    super(autoHide, modal);

    setText(title);
    Button ok = new Button("OK");

    ok.addClickListener(new ClickListener() {

      public void onClick(Widget sender) {
        hide();
        if (callback != null) {
          callback.okPressed();
        }
      }
    });
    final HorizontalPanel dialogButtonPanel = new HorizontalPanel();
    dialogButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    dialogButtonPanel.add(ok);
    Widget messageLabel = null;
    if (isHTML) {
      messageLabel = new HTML(message, true);
    } else {
      messageLabel = new Label(message, true);
    }
    messageLabel.setWidth("100%");
    FlexTable dialogContent = new FlexTable();
    dialogContent.setStyleName("dialogContentPanel");
    dialogContent.setWidth("400px");
    dialogContent.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
    dialogContent.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
    dialogContent.setWidget(0, 0, messageLabel);
    dialogContent.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
    dialogContent.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
    // add button panel
    dialogContent.setWidget(2, 0, dialogButtonPanel);
    dialogContent.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
    dialogContent.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    // dialogContent.getFlexCellFormatter().setColSpan(2, 0, 2);
    setWidget(dialogContent);
  }
}
