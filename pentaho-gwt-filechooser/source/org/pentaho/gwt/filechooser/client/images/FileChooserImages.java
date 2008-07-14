package org.pentaho.gwt.filechooser.client.images;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.TreeImages;

public interface FileChooserImages extends ImageBundle, TreeImages {
  AbstractImagePrototype file();
  AbstractImagePrototype folder();
  AbstractImagePrototype up();
  AbstractImagePrototype search();
}
