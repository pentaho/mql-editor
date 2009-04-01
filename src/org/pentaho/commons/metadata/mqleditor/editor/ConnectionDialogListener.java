package org.pentaho.commons.metadata.mqleditor.editor;

import org.pentaho.commons.metadata.mqleditor.IConnection;

public interface ConnectionDialogListener {

  public void onDialogAccept(IConnection connection);

  public void onDialogCancel();
}
