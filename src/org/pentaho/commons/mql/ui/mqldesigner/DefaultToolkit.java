package org.pentaho.commons.mql.ui.mqldesigner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class DefaultToolkit extends FormToolkit {
  DefaultToolkit(Display display) {
    super(display);
  }

  public void adapt(Composite composite) {
  }

  public void adapt(Control control, boolean trackFocus, boolean trackKeyboard) {
  }

  public void paintBordersFor(Composite parent) {
  }

  public Composite createComposite(Composite parent, int style) {
    return new Composite(parent, style);
  }

  public Composite createComposite(Composite parent) {
    return createComposite(parent, SWT.NULL);
  }

}
