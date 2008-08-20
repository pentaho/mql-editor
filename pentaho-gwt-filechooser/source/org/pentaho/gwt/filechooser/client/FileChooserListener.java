package org.pentaho.gwt.filechooser.client;

import java.util.EventListener;

public interface FileChooserListener extends EventListener {
  public void fileSelected(String solution, String path, String name);
  public void fileSelectionChanged(String solution, String path, String name);
}
