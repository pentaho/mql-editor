package org.pentaho.commons.mql.ui.mqldesigner;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.pentaho.designstudio.editors.actionsequence.mql.Messages;

public class MQLPreviewDialog extends Dialog {

  FormToolkit toolkit;
  String html;
  Browser browser;
  
  public MQLPreviewDialog(FormToolkit toolkit, Shell parentShell, String html) {
      super(parentShell);
      this.html = html;
      this.toolkit = toolkit;
  }

  protected void configureShell(Shell shell) {
      super.configureShell(shell);
      shell.setText(Messages.getString("MQLPreviewDialog.PREVIEW_TITLE")); //$NON-NLS-1$
  }

  protected void createButtonsForButtonBar(Composite parent) {
      createButton(parent, IDialogConstants.OK_ID,
              IDialogConstants.OK_LABEL, true);
      parent.setBackground(toolkit.getColors().getBackground());
  }

  protected Control createDialogArea(Composite parent) {
      toolkit.adapt(parent);
      Composite composite = (Composite) super.createDialogArea(parent);      
      toolkit.adapt(composite);
      toolkit.paintBordersFor(composite);
      browser = new Browser(composite, SWT.NONE);
      browser.setLayoutData(new GridData(GridData.FILL_BOTH));
      toolkit.adapt(browser);
      browser.setText(html);
      GridData gridData = new GridData();
      gridData.heightHint = 700;
      gridData.widthHint = 700;
      composite.setLayoutData(gridData);
      return composite;
  }

  
  public void dispose() {
  }

}
