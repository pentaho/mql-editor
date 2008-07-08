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
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

public class PromptDialogBox extends DialogBox {

  IDialogCallback callback;
  IDialogValidatorCallback validatorCallback;
  Widget content;

  public PromptDialogBox(String title, Widget content, String okText, String cancelText, IDialogCallback inCallback,
      IDialogValidatorCallback inValidatorCallback, boolean autoHide, boolean modal) {
    super(autoHide, modal);
    this.callback = inCallback;
    this.validatorCallback = inValidatorCallback;
    this.content = content;
    setText(title);
    Button ok = new Button(okText);
    ok.addClickListener(new ClickListener() {

      public void onClick(Widget sender) {
        if (validatorCallback != null && validatorCallback.validate()) {
          hide();
          if (callback != null) {
            callback.okPressed();
          }
        }
      }
    });
    final HorizontalPanel dialogButtonPanel = new HorizontalPanel();
    dialogButtonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    dialogButtonPanel.add(ok);
    if (cancelText != null) {
      Button cancel = new Button(cancelText);
      cancel.addClickListener(new ClickListener() {

        public void onClick(Widget sender) {
          hide();
          if (callback != null) {
            callback.cancelPressed();
          }
        }
      });
      dialogButtonPanel.add(cancel);
    }
    FlexTable dialogContent = new FlexTable();
    // dialogContent.setWidth("400px");
    if (content != null) {
      dialogContent.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
      dialogContent.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
      dialogContent.setWidget(0, 0, content);
      content.setHeight("100%");
      content.setWidth("100%");
    }
    if (content instanceof FocusWidget) {
      setFocusWidget((FocusWidget) content);
    }
    dialogContent.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
    dialogContent.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
    // add button panel
    dialogContent.setWidget(2, 0, dialogButtonPanel);
    dialogContent.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
    dialogContent.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    // dialogContent.getFlexCellFormatter().setColSpan(2, 0, 2);
    dialogContent.setWidth("100%");
    setWidget(dialogContent);
  }

  public boolean onKeyDownPreview(char key, int modifiers) {
    // Use the popup's key preview hooks to close the dialog when either
    // enter or escape is pressed.
    switch (key) {
    case KeyboardListener.KEY_ENTER:
      if (validatorCallback != null && validatorCallback.validate()) {
        hide();
        if (callback != null) {
          callback.okPressed();
        }
      }
      break;
    case KeyboardListener.KEY_ESCAPE:
      callback.cancelPressed();
      hide();
      break;
    }
    return true;
  }

  public IDialogCallback getCallback() {
    return callback;
  }

  public Widget getContent() {
    return content;
  }

  public void setContent(Widget content) {
    this.content = content;
  }

  public void setCallback(IDialogCallback callback) {
    this.callback = callback;
  }

  public IDialogValidatorCallback getValidatorCallback() {
    return validatorCallback;
  }

  public void setValidatorCallback(IDialogValidatorCallback validatorCallback) {
    this.validatorCallback = validatorCallback;
  }

}
