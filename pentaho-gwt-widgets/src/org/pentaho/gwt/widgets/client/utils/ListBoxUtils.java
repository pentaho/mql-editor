package org.pentaho.gwt.widgets.client.utils;

import com.google.gwt.user.client.ui.ListBox;

public class ListBoxUtils {
  private ListBoxUtils() {
    
  }
  
  public static void removeAll( ListBox l ) {
    for ( int ii=l.getItemCount()-1; ii>=0; ii-- ) {
      l.removeItem( ii );
    }
  }
}
