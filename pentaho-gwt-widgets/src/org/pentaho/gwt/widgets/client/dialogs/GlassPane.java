package org.pentaho.gwt.widgets.client.dialogs;

import java.util.ArrayList;
import java.util.List;

public class GlassPane {

  private static GlassPane instance = new GlassPane();

  private List<GlassPaneListener> listeners = new ArrayList<GlassPaneListener>();

  private boolean shown = false;

  private GlassPane() {

  }

  public static GlassPane getInstance() {
    return instance;
  }

  public void show() {
    if (!shown) {
      shown = true;
      for (GlassPaneListener listener : listeners) {
        listener.glassPaneShown();
      }
    }
  }

  public void hide() {
    if (shown) {
      shown = false;
      for (GlassPaneListener listener : listeners) {
        listener.glassPaneHidden();
      }
    }

  }

  public void addGlassPaneListener(GlassPaneListener listener) {
    if (listeners.contains(listener) == false) {
      this.listeners.add(listener);
    }
  }

  public void removeGlassPaneListener(GlassPaneListener listener) {
    if (listeners.contains(listener)) {
      this.listeners.remove(listener);
    }
  }
}
