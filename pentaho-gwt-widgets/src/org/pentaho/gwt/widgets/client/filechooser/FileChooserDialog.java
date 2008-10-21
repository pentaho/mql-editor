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

import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.IDialogValidatorCallback;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.dialogs.ResizableDialogBox;
import org.pentaho.gwt.widgets.client.filechooser.FileChooser.FileChooserMode;

import com.google.gwt.xml.client.Document;

public class FileChooserDialog extends ResizableDialogBox {

  FileChooser fileChooser;

  public FileChooserDialog(FileChooserMode mode, String selectedPath, boolean autoHide, boolean modal) {
    super(mode == FileChooserMode.OPEN ? "Open" : "Save", mode == FileChooserMode.OPEN ? "Open" : "Save", "Cancel", new FileChooser(mode,
        selectedPath), true);
    fileChooser = (FileChooser) getContent();
    fileChooser.setWidth("100%");
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
    super(mode == FileChooserMode.OPEN ? "Open" : "Save", mode == FileChooserMode.OPEN ? "Open" : "Save", "Cancel", new FileChooser(), true);
    fileChooser = (FileChooser) getContent();
    fileChooser.setWidth("100%");
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
    fileChooser.initUI(false);
  }

  public void addFileChooserListener(FileChooserListener listener) {
    fileChooser.addFileChooserListener(listener);
  }

  public void removeFileChooserListener(FileChooserListener listener) {
    fileChooser.removeFileChooserListener(listener);
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
    if (fileNameFromTextBox != null && !"".equals(fileNameFromTextBox)) {      
      return fileNameFromTextBox;
    } else if ( actualFileName != null && !"".equals(actualFileName) ) {
      return actualFileName;
    } else {
      return "";
    }
  }
  
  /*
   * If the file name is empty or null then return false, else return true.
   */
  private boolean isFileNameValid() {
    final String fileName = getFileName();
    if (fileName == null || "".equals(fileName)) {
      MessageDialogBox dialogBox = new MessageDialogBox("Error", "No filename has been entered.", false, false, true);
      dialogBox.center();
      return false;
    }    
    return true;
  }
}