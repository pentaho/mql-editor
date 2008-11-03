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

import java.util.ArrayList;

import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.IDialogValidatorCallback;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.dialogs.ResizableDialogBox;
import org.pentaho.gwt.widgets.client.filechooser.FileChooser.FileChooserMode;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;

import com.google.gwt.xml.client.Document;

public class FileChooserDialog extends ResizableDialogBox implements FileChooserListener{

  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();

  private ArrayList<FileChooserListener> listeners = new ArrayList<FileChooserListener>();
  
  FileChooser fileChooser;

  public FileChooserDialog(FileChooserMode mode, String selectedPath, boolean autoHide, boolean modal) {
    super(mode == FileChooserMode.OPEN ? "Open" : "Save", mode == FileChooserMode.OPEN ? "Open" : "Save", "Cancel", new FileChooser(mode, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        selectedPath), true);
    fileChooser = (FileChooser) getContent();
    fileChooser.setWidth("100%"); //$NON-NLS-1$
    setValidatorCallback(new IDialogValidatorCallback() {
      public boolean validate() {
        return isFileNameValid();
      }
    });
    IDialogCallback callback = new IDialogCallback() {

      public void cancelPressed() {
      }

      public void okPressed() {
        fileChooser.fireFileSelected();
      }

    };
    setCallback(callback);
  }

  public FileChooserDialog(FileChooserMode mode, String selectedPath, Document repositoryDocument, boolean autoHide, boolean modal) {
    super(mode == FileChooserMode.OPEN ? "Open" : "Save", mode == FileChooserMode.OPEN ? "Open" : "Save", "Cancel", new FileChooser(), true); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    fileChooser = (FileChooser) getContent();
    fileChooser.setWidth("100%"); //$NON-NLS-1$
    fileChooser.setMode(mode);
    fileChooser.setSelectedPath(selectedPath);
    fileChooser.solutionRepositoryDocument = repositoryDocument;
    fileChooser.repositoryTree = TreeBuilder.buildSolutionTree(repositoryDocument, fileChooser.showHiddenFiles, fileChooser.showLocalizedFileNames);
    setValidatorCallback(new IDialogValidatorCallback() {
      public boolean validate() {
        return isFileNameValid();
      }
    });
    IDialogCallback callback = new IDialogCallback() {

      public void cancelPressed() {
      }

      public void okPressed() {
        fileChooser.fireFileSelected();
      }

    };
    setCallback(callback);
    fileChooser.addFileChooserListener(this);
    fileChooser.initUI(false);
  }

  public void addFileChooserListener(FileChooserListener listener) {
    listeners.add(listener);
  }

  public void removeFileChooserListener(FileChooserListener listener) {
    if(listeners.contains(listener)){
      listeners.remove(listener);
    }
  }

  public void setShowSearch(boolean showSearch) {
    fileChooser.setShowSearch(showSearch);
  }

  public boolean isShowSearch() {
    return fileChooser.isShowSearch();
  }

  public boolean doesSelectedFileExist() {
    return fileChooser.doesSelectedFileExist();
  }
  
  /*
   * Give precedence to file name text box content as file name.
   * It should never be empty, but in the unlikely scenario of it 
   * being empty get the actual file name. If both are empty return 
   * empty string.
   */
  private String getFileName() {    
    final String fileNameFromTextBox = fileChooser.fileNameTextBox.getText().trim();
    final String actualFileName = fileChooser.getActualFileName();
    if (fileNameFromTextBox != null && !"".equals(fileNameFromTextBox)) { //$NON-NLS-1$
      return fileNameFromTextBox;
    } else if ( actualFileName != null && !"".equals(actualFileName) ) { //$NON-NLS-1$
      return actualFileName;
    } else {
      return ""; //$NON-NLS-1$
    }
  }
  
  /*
   * If the file name is empty or null then return false, else return true.
   */
  private boolean isFileNameValid() {
    final String fileName = getFileName();
    if (fileName == null || "".equals(fileName)) { //$NON-NLS-1$
      MessageDialogBox dialogBox = new MessageDialogBox(MSGS.error(), MSGS.noFilenameEntered(), false, false, true);
      dialogBox.center();
      return false;
    }    
    return true;
  }

  public void fileSelected(String solution, String path, String name, String localizedFileName) {
    for(FileChooserListener listener : listeners){
      listener.fileSelected(solution, path, name, localizedFileName);
    }
    this.hide();
  }

  public void fileSelectionChanged(String solution, String path, String name) {
    for(FileChooserListener listener : listeners){
      listener.fileSelectionChanged(solution, path, name);
    }
  }
}