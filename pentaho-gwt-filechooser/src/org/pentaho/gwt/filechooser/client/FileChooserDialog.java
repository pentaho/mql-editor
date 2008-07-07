package org.pentaho.gwt.filechooser.client;

import org.pentaho.gwt.filechooser.client.dialogs.IDialogCallback;
import org.pentaho.gwt.filechooser.client.dialogs.PromptDialogBox;

import com.google.gwt.xml.client.Document;

public class FileChooserDialog extends PromptDialogBox {

  FileChooser fileChooser;

  public FileChooserDialog(int mode, String selectedPath, boolean autoHide, boolean modal) {
    super(mode == FileChooser.OPEN ? "Open" : "Save", new FileChooser(mode, selectedPath), mode == FileChooser.OPEN ? "Open" : "Save", "Cancel", null,
        autoHide, modal);
    fileChooser = (FileChooser) getContent();
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
    super(mode == FileChooser.OPEN ? "Open" : "Save", new FileChooser(), mode == FileChooser.OPEN ? "Open" : "Save", "Cancel", null,
        autoHide, modal);
    fileChooser = (FileChooser) getContent();
    fileChooser.setMode(mode);
    fileChooser.setSelectedPath(selectedPath);
    fileChooser.repositoryTree = TreeBuilder.buildSolutionTree(repositoryDocument, fileChooser.showHiddenFiles, fileChooser.showLocalizedFileNames);
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

  // public void onModuleLoad() {
  // final FileChooserDialog dialogBox = new FileChooserDialog(FileChooser.SAVE, "/samples/reporting", true, true);
  // dialogBox.addFileChooserListener(new FileChooserListener() {
  // public void fileSelected(String path, String file) {
  // Window.alert("fileSelected: path=" + path + " file=" + file);
  // dialogBox.hide();
  // }
  // });
  // dialogBox.center();
  // }

}
