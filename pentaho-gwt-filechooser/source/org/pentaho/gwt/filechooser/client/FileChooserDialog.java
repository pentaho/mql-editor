package org.pentaho.gwt.filechooser.client;

import org.pentaho.gwt.filechooser.client.FileChooser.FileChooserMode;
import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.IDialogValidatorCallback;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;

import com.google.gwt.xml.client.Document;

public class FileChooserDialog extends PromptDialogBox {

  FileChooser fileChooser;

  public FileChooserDialog(FileChooserMode mode, String selectedPath, boolean autoHide, boolean modal) {
    super(mode == FileChooserMode.OPEN ? "Open" : "Save", new FileChooser(mode, selectedPath), mode == FileChooserMode.OPEN ? "Open" : "Save", "Cancel",
        autoHide, modal);
    fileChooser = (FileChooser) getContent();
    setValidatorCallback(new IDialogValidatorCallback() {
      public boolean validate() {
        boolean isValid = fileChooser.getName() != null && !"".equals(fileChooser.getName());
        if (!isValid) {
          MessageDialogBox dialogBox = new MessageDialogBox("Error", "No filename has been entered.", false, false, true);
          dialogBox.center();
        }
        return isValid;
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
    setAnimationEnabled(true);
  }

  public FileChooserDialog(FileChooserMode mode, String selectedPath, Document repositoryDocument, boolean autoHide, boolean modal) {
    super(mode == FileChooserMode.OPEN ? "Open" : "Save", new FileChooser(), mode == FileChooserMode.OPEN ? "Open" : "Save", "Cancel", autoHide, modal);
    fileChooser = (FileChooser) getContent();
    fileChooser.setMode(mode);
    fileChooser.setSelectedPath(selectedPath);
    fileChooser.solutionRepositoryDocument = repositoryDocument;
    fileChooser.repositoryTree = TreeBuilder.buildSolutionTree(repositoryDocument, fileChooser.showHiddenFiles, fileChooser.showLocalizedFileNames);
    fileChooser.setShowLocalizedFileNames(false);
    setValidatorCallback(new IDialogValidatorCallback() {
      public boolean validate() {
        boolean isValid = fileChooser.getName() != null && !"".equals(fileChooser.getName());
        if (!isValid) {
          MessageDialogBox dialogBox = new MessageDialogBox("Error", "No filename has been entered.", false, false, true);
          dialogBox.center();
        }
        return isValid;
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
    setAnimationEnabled(true);
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
  
  
  
}
