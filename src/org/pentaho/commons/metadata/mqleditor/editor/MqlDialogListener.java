package org.pentaho.commons.metadata.mqleditor.editor;

import org.pentaho.commons.metadata.mqleditor.IQuery;

public interface MqlDialogListener {

  public void onDialogAccept(IQuery queryModel);

  public void onDialogCancel();
}
