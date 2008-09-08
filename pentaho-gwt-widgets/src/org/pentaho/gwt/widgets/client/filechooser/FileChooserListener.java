package org.pentaho.gwt.widgets.client.filechooser;

import java.util.EventListener;

public interface FileChooserListener extends EventListener {
  public void fileSelected(String solution, String path, String name);
  public void fileSelectionChanged(String solution, String path, String name);
}
