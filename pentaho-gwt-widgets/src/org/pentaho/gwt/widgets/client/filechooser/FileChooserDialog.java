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

import org.pentaho.gwt.widgets.client.dialogs.GlassPane;
import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.IDialogValidatorCallback;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.dialogs.ResizableDialogBox;
import org.pentaho.gwt.widgets.client.filechooser.FileChooser.FileChooserMode;
import org.pentaho.gwt.widgets.client.utils.string.StringUtils;

import com.google.gwt.xml.client.Document;

public class FileChooserDialog extends ResizableDialogBox implements FileChooserListener {

  private static final String ILLEGAL_NAME_CHARS = "\\\'/?%*:|\"<>&";
  
  private ArrayList<FileChooserListener> listeners = new ArrayList<FileChooserListener>();

  private FileChooser fileChooser;
  private FileFilter filter;

  public FileChooserDialog(FileChooserMode mode, String selectedPath, boolean autoHide, boolean modal) {
    this(mode, selectedPath, autoHide, modal, mode == FileChooserMode.OPEN ? FileChooserEntryPoint.messages.getString("Open") : FileChooserEntryPoint.messages.getString("Save"), mode == FileChooserMode.OPEN ? FileChooserEntryPoint.messages.getString("Open") : FileChooserEntryPoint.messages.getString("Save")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
  }

  public FileChooserDialog(FileChooserMode mode, String selectedPath, boolean autoHide, boolean modal, String title, String okText) {
    super(title, okText, FileChooserEntryPoint.messages.getString("Cancel"), new FileChooser(mode, //$NON-NLS-1$
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

  public FileChooserDialog(FileChooserMode mode, String selectedPath, Document repositoryDocument, boolean autoHide, boolean modal, String title, String okText) {
    super(title, okText, FileChooserEntryPoint.messages.getString("Cancel"), new FileChooser(), true); //$NON-NLS-1$
    fileChooser = (FileChooser) getContent();
    fileChooser.setWidth("100%"); //$NON-NLS-1$
    fileChooser.setMode(mode);
    fileChooser.setSelectedPath(selectedPath);
    fileChooser.solutionRepositoryDocument = repositoryDocument;
    fileChooser.repositoryTree = TreeBuilder.buildSolutionTree(repositoryDocument, fileChooser.showHiddenFiles, fileChooser.showLocalizedFileNames, filter);
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

  public FileChooserDialog(FileChooserMode mode, String selectedPath, Document repositoryDocument, boolean autoHide, boolean modal) {
    this(
        mode,
        selectedPath,
        repositoryDocument,
        autoHide,
        modal,
        mode == FileChooserMode.OPEN ? FileChooserEntryPoint.messages.getString("Open") : FileChooserEntryPoint.messages.getString("Save"), mode == FileChooserMode.OPEN ? FileChooserEntryPoint.messages.getString("Open") : FileChooserEntryPoint.messages.getString("Save")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
  }

  public void addFileChooserListener(FileChooserListener listener) {
    listeners.add(listener);
  }

  public void removeFileChooserListener(FileChooserListener listener) {
    if (listeners.contains(listener)) {
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

  public FileFilter getFileFilter() {

    return filter;
  }

  public void setFileFilter(FileFilter filter) {

    this.filter = filter;
    fileChooser.setFileFilter(filter);
  }

  /**
   * This method get the actual file name of the selected file
   * 
   * @return the actual file name
   */
  private String getActualFileName() {
    final String actualFileName = fileChooser.getActualFileName();
    if (actualFileName != null && !"".equals(actualFileName)) { //$NON-NLS-1$
      return actualFileName;
    } else {
      return "";
    }
  }

  /*
   * If the file name is empty or null then return false, else return true.
   */
  private boolean isFileNameValid() {

    // don't allow saving in the root of the solution repository
    String solution = fileChooser.getSolution();
    if (solution == null || solution.trim().length() == 0) {
      MessageDialogBox dialogBox = new MessageDialogBox(FileChooserEntryPoint.messages.getString("error"), FileChooserEntryPoint.messages
          .getString("noSolutionSelected"), false, false, true);
      dialogBox.center();
      return false;
    }

    final String fileName = getActualFileName();
    if (StringUtils.isEmpty(fileName)) {
      MessageDialogBox dialogBox = new MessageDialogBox(FileChooserEntryPoint.messages.getString("error"), FileChooserEntryPoint.messages
          .getString("noFilenameEntered"), false, false, true);
      dialogBox.center();
      return false;
    } else if (StringUtils.containsAnyChars(fileName, ILLEGAL_NAME_CHARS)) { //$NON-NLS-1$
      MessageDialogBox dialogBox = new MessageDialogBox(FileChooserEntryPoint.messages.getString("error"), FileChooserEntryPoint.messages
          .getString("invalidFilename"), false, false, true);
      dialogBox.center();
      return false;
    }
    return true;
  }

  public void fileSelected(String solution, String path, String name, String localizedFileName) {
    if (isFileNameValid()) {
      for (FileChooserListener listener : listeners) {
        listener.fileSelected(solution, path, name, localizedFileName);
      }
      this.hide();
    }
  }

  public void fileSelectionChanged(String solution, String path, String name) {
    for (FileChooserListener listener : listeners) {
      listener.fileSelectionChanged(solution, path, name);
    }
  }

  @Override
  public void hide() {
    GlassPane.getInstance().hide();
    super.hide();
  }

  @Override
  public void center() {
    GlassPane.getInstance().show();
    super.center();
    setFocus();
  }
  
  private void setFocus() {
    fileChooser.fileNameTextBox.setFocus(true);
  }

}