package org.pentaho.gwt.filechooser.client;

import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.IDialogValidatorCallback;
import org.pentaho.gwt.widgets.client.dialogs.MessageDialogBox;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;

import com.google.gwt.xml.client.Document;

public class FileChooserDialog extends PromptDialogBox {

  FileChooser fileChooser;

  public FileChooserDialog(int mode, String selectedPath, boolean autoHide, boolean modal) {
    super(mode == FileChooser.OPEN ? "Open" : "Save", new FileChooser(mode, selectedPath), mode == FileChooser.OPEN ? "Open" : "Save", "Cancel", null, null,
        autoHide, modal);
    fileChooser = (FileChooser) getContent();
    setValidatorCallback(new IDialogValidatorCallback() {
      public boolean validate() {
        boolean isValid = fileChooser.getName() != null && !"".equals(fileChooser.getName());
        if (isValid) {
          MessageDialogBox dialogBox = new MessageDialogBox("Error", "No filename has been entered.", false, null, false, true);
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

  public FileChooserDialog(int mode, String selectedPath, Document repositoryDocument, boolean autoHide, boolean modal) {
    super(mode == FileChooser.OPEN ? "Open" : "Save", new FileChooser(), mode == FileChooser.OPEN ? "Open" : "Save", "Cancel", null, null, autoHide, modal);
    fileChooser = (FileChooser) getContent();
    fileChooser.setMode(mode);
    fileChooser.setSelectedPath(selectedPath);
    fileChooser.solutionRepositoryDocument = repositoryDocument;
    fileChooser.repositoryTree = TreeBuilder.buildSolutionTree(repositoryDocument, fileChooser.showHiddenFiles, fileChooser.showLocalizedFileNames);
    setValidatorCallback(new IDialogValidatorCallback() {
      public boolean validate() {
        boolean isValid = fileChooser.getName() != null && !"".equals(fileChooser.getName());
        if (isValid) {
          MessageDialogBox dialogBox = new MessageDialogBox("Error", "No filename has been entered.", false, null, false, true);
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

}
