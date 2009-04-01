package org.pentaho.commons.metadata.mqleditor.editor;

import org.pentaho.commons.metadata.mqleditor.IDatasource;

public interface DatasourceDialogListener {

  public void onDialogAccept(IDatasource datasource);

  public void onDialogCancel();
}
