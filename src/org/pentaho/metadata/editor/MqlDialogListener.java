package org.pentaho.metadata.editor;

import org.pentaho.metadata.IQuery;

public interface MqlDialogListener {

  public void onDialogAccept(IQuery queryModel);

  public void onDialogCancel();
}
