/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.gwt.widgets.client.filechooser;

import org.pentaho.gwt.widgets.client.filechooser.FileChooser.FileChooserMode;
import org.pentaho.gwt.widgets.client.utils.i18n.IResourceBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;

public class FileChooserEntryPoint implements EntryPoint, IResourceBundleLoadCallback {

  public static ResourceBundle messages = new ResourceBundle();

  public void onModuleLoad() {
    if (messages == null) {
      messages = new ResourceBundle();
    }
    messages.loadBundle("messages/", "filechooser_messages", true, FileChooserEntryPoint.this); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public void bundleLoaded(String bundleName) {
    setupNativeHooks(this);
  }

  public native void notifyCallback(JavaScriptObject callback, RepositoryFile file)
  /*-{
   try {
     callback.fileSelected(file);
   } catch (ex) {
   }
  }-*/;
  
  public void openFileChooserDialog(final JavaScriptObject callback, String selectedPath) {
    FileChooserDialog dialog = new FileChooserDialog(FileChooserMode.OPEN, selectedPath, false, true);
    dialog.addFileChooserListener(new FileChooserListener() {
      public void fileSelected(RepositoryFile file) {
        notifyCallback(callback, file);
      }
      public void fileSelectionChanged(RepositoryFile file) {
      }
    });
    dialog.center();    
  }
  
  public void saveFileChooserDialog(final JavaScriptObject callback, String selectedPath) {
    FileChooserDialog dialog = new FileChooserDialog(FileChooserMode.OPEN, selectedPath, false, true);
    dialog.addFileChooserListener(new FileChooserListener() {
      public void fileSelected(RepositoryFile file) {
        notifyCallback(callback, file);
      }
      public void fileSelectionChanged(RepositoryFile file) {
      }
    });
    dialog.center();    
  }
  
  public native void setupNativeHooks(FileChooserEntryPoint fileChooserEntryPoint)
  /*-{
    $wnd.openFileChooserDialog = function(callback, selectedPath) {
      fileChooserEntryPoint.@org.pentaho.gwt.widgets.client.filechooser.FileChooserEntryPoint::openFileChooserDialog(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(callback, selectedPath);
    }
    $wnd.saveFileChooserDialog = function(callback, selectedPath) {
      fileChooserEntryPoint.@org.pentaho.gwt.widgets.client.filechooser.FileChooserEntryPoint::saveFileChooserDialog(Lcom/google/gwt/core/client/JavaScriptObject;Ljava/lang/String;)(callback, selectedPath);
    }
  }-*/;

  

}