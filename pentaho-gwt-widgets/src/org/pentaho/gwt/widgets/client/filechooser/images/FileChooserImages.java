package org.pentaho.gwt.widgets.client.filechooser.images;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.TreeImages;

public interface FileChooserImages extends ImageBundle, TreeImages {
  public static final FileChooserImages images = (FileChooserImages) GWT.create(FileChooserImages.class);

  AbstractImagePrototype file();
  AbstractImagePrototype file_report();
  AbstractImagePrototype file_analysis();
  AbstractImagePrototype file_url();
  AbstractImagePrototype file_action();
  AbstractImagePrototype folder();
  AbstractImagePrototype up();
  AbstractImagePrototype search();
}
