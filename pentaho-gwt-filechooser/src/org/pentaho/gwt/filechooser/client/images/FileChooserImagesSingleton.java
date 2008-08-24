package org.pentaho.gwt.filechooser.client.images;

import com.google.gwt.core.client.GWT;

public class FileChooserImagesSingleton {
  private static FileChooserImages images = (FileChooserImages) GWT.create(FileChooserImages.class);

  public static FileChooserImages getImages() {
    return images;
  }
}
