package org.pentaho.commons.metadata.mqleditor.editor;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;

public interface MqlDialogListener {

  public void onDialogAccept(MqlQuery queryModel);

  public void onDialogCancel();
  
  public void onDialogReady();
}
