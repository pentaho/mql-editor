package org.pentaho.gwt.widgets.client.filechooser;

public interface FileFilter {
  boolean accept(String name, boolean isDirectory, boolean isVisible);
}

  