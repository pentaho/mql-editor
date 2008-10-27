package org.pentaho.gwt.widgets.client.utils;

/**
 * This class is a simple callback for ResourceBundle
 * 
 * @author Michael D'Amour
 */
public interface IMessageBundleLoadCallback {

  /**
   * This method is called by ResourceBundle when the ResourceBundle has been loaded. Once loaded, a resource is safe to use.
   * 
   * @param bundleName
   *          The name of the bundle which has loaded. This is provided in case the caller is loading multiple bundles and wants to be notified specifically for
   *          each loaded bundle.
   */
  public void bundleLoaded(String bundleName);
}
