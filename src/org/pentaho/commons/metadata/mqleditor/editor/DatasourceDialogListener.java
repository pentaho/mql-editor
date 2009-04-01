package org.pentaho.commons.metadata.mqleditor.editor;

import org.pentaho.commons.metadata.mqleditor.IDatasource;

public interface DatasourceDialogListener {

  public void onDialogFinish(IDatasource datasource);

  public void onDialogCancel();
}
