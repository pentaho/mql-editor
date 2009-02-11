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
      List<GlassPaneListener> listenersToRemove = new ArrayList<GlassPaneListener>();
      
      for (GlassPaneListener listener : listeners) {
        try{
          listener.glassPaneShown();
        } catch(Exception e){
          // If a Listener is a reference from a window that has since closed, it throws an exception here. Remove it.
          listenersToRemove.add(listener);
        }
      }
      removeBadListeners(listenersToRemove);
    }
  }
  
  private void removeBadListeners(List<GlassPaneListener> removeThese){
    for (GlassPaneListener listener : removeThese) {
      listeners.remove(listener);
    }
  }

  public void hide() {
    if (shown) {
      shown = false;

      List<GlassPaneListener> listenersToRemove = new ArrayList<GlassPaneListener>();
      
      for (GlassPaneListener listener : listeners) {
        try{
          listener.glassPaneHidden();
        } catch(Exception e){
          // If a Listener is a reference from a window that has since closed, it throws an exception here. Remove it.
          listenersToRemove.add(listener);
        }
      }
      removeBadListeners(listenersToRemove);
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
