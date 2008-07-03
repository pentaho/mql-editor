package org.pentaho.gwt.filechooser.client;

import java.util.EventListener;

public interface FileChooserListener extends EventListener {
  public void fileSelected(String path, String file);
}
