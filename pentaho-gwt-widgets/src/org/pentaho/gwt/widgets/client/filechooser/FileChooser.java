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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.pentaho.gwt.widgets.client.filechooser.images.FileChooserImages;
import org.pentaho.gwt.widgets.client.utils.ElementUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@SuppressWarnings("deprecation")
public class FileChooser extends VerticalPanel {

  public enum FileChooserMode {
    OPEN, OPEN_READ_ONLY, SAVE
  }
  public static final String ETC_FOLDER = "etc";//$NON-NLS-1$
  FileChooserMode mode = FileChooserMode.OPEN;
  String selectedPath;

  ListBox navigationListBox;
  Tree repositoryTree;
  TreeItem selectedTreeItem;
  boolean showHiddenFiles = false;
  boolean showLocalizedFileNames = true;
  com.google.gwt.user.client.Element lastSelectedFileElement;
  TextBox fileNameTextBox = new TextBox();
  DateTimeFormat dateFormat = DateTimeFormat.getMediumDateTimeFormat();
  RepositoryFileTree fileTree;

  ArrayList<FileChooserListener> listeners = new ArrayList<FileChooserListener>();
  private String actualFileName;
  boolean fileSelected = false;

  private FileFilter fileFilter;

  public FileChooser() {
    fileNameTextBox.getElement().setId("fileNameTextBox");
    
    // workaround webkit browsers quirk of not being able to set focus in a widget by clicking on it  
    fileNameTextBox.addClickHandler(new ClickHandler() {      
      public void onClick(ClickEvent event) {
        fileNameTextBox.setFocus(true);
      }
    });
    
    fileNameTextBox.addKeyboardListener(new KeyboardListener() {

      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        actualFileName = fileNameTextBox.getText();
        if (keyCode == KeyboardListener.KEY_ENTER) {
          if(mode != FileChooserMode.SAVE) {
            fireFileSelected(search(fileTree, actualFileName));  
          } else {
            fireFileSelected();
          }
        }
      }

    });
    setSpacing(3);
  }

  private RepositoryFile search(RepositoryFileTree tree, String actualFileName) {
    RepositoryFile file = tree.getFile();
    if(file != null && !file.isFolder() && file.getName().equals(actualFileName)) {
      return file;
    }
    for(RepositoryFileTree treeItem: tree.getChildren()) {
      search(treeItem, actualFileName);
    }
    return null;
  }
  
  public FileChooser(FileChooserMode mode, String selectedPath, boolean showLocalizedFileNames) {
    this(mode, selectedPath, showLocalizedFileNames, null);
  }

  public FileChooser(FileChooserMode mode, String selectedPath, boolean showLocalizedFileNames, RepositoryFileTree fileTree) {
    this();
    this.mode = mode;
    this.selectedPath = selectedPath;
    this.fileTree = fileTree;
    this.showLocalizedFileNames = showLocalizedFileNames;
    if (null != fileTree) {
      repositoryTree = TreeBuilder.buildSolutionTree(fileTree, showHiddenFiles, showLocalizedFileNames, fileFilter);
      selectedTreeItem = repositoryTree.getItem(0);
      initUI();
    }
  }

  public FileChooser(FileChooserMode mode, String selectedPath) {
    this();
    this.mode = mode;
    this.selectedPath = selectedPath;
    /*try {
      fetchRepositoryDocument(null);
    } catch (RequestException e) {
      Window.alert(e.toString());
    }*/
  }

 /* public void fetchRepositoryDocument(final IDialogCallback completedCallback) throws RequestException {
    RequestBuilder builder = null;
    if (GWT.isScript()) {
      builder = new RequestBuilder(RequestBuilder.GET, "SolutionRepositoryService?component=getSolutionRepositoryDoc&filter=*.xaction,*.url,*.prpt"); //$NON-NLS-1$
    } else {
      builder = new RequestBuilder(RequestBuilder.GET,
          "http://localhost:8080/pentaho/SolutionRepositoryService?component=getSolutionRepositoryDoc&userid=joe&password=password"); //$NON-NLS-1$
    }
    RequestCallback callback = new RequestCallback() {

      public void onError(Request request, Throwable exception) {
        Window.alert(exception.toString());
      }

      public void onResponseReceived(Request request, Response response) {
        if (response.getStatusCode() == Response.SC_OK) {
          // ok, we have a repository document, we can build the GUI
          // consider caching the document
          solutionRepositoryDocument = (Document) XMLParser.parse((String) response.getText());
          repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames, fileFilter);
          initUI(false);
          if (completedCallback != null) {
            completedCallback.okPressed();
          }
        } else {
          Window.alert("Solution Repository not found.");
        }
      }

    };
    builder.sendRequest(null, callback);
  }*/

  public void initUI() {

    if (mode == FileChooserMode.OPEN_READ_ONLY) {
      fileNameTextBox.setReadOnly(true);
    }
    // We are here because we are initiating a fresh UI for a new directory
    // Since there is no file selected currently, we are setting file selected to false.
    setFileSelected(false);

    String path = this.selectedPath;
    final String finalPath = path;

    // find the selected item from the list
    List<String> pathSegments = new ArrayList<String>();
    if (path != null) {
      int index = path.indexOf("/", 0); //$NON-NLS-1$
      while (index >= 0) {
        int oldIndex = index;
        index = path.indexOf("/", oldIndex + 1); //$NON-NLS-1$
        if (index >= 0) {
          pathSegments.add(path.substring(oldIndex + 1, index));
        }
      }
      pathSegments.add(path.substring(path.lastIndexOf("/") + 1)); //$NON-NLS-1$
    }
    navigationListBox = new ListBox();
    navigationListBox.getElement().setId("navigationListBox");
    navigationListBox.setWidth("350px"); //$NON-NLS-1$
    // now we can find the tree nodes who match the path segments
    navigationListBox.addItem("/", "/"); //$NON-NLS-1$ //$NON-NLS-2$

    for (int i = 0; i < pathSegments.size(); i++) {
      String segment = pathSegments.get(i);
      String fullPath = ""; //$NON-NLS-1$
      for (int j = 0; j <= i; j++) {
        fullPath += "/" + pathSegments.get(j); //$NON-NLS-1$
      }
      if (!fullPath.equals("/")) { //$NON-NLS-1$
        navigationListBox.addItem(fullPath, segment);
      }
    }

    navigationListBox.setSelectedIndex(navigationListBox.getItemCount() - 1);
    navigationListBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        changeToPath(navigationListBox.getItemText(navigationListBox.getSelectedIndex()));
      }
    });

    clear();

    VerticalPanel locationBar = new VerticalPanel();
    locationBar.add(new Label(FileChooserEntryPoint.messages.getString("location")));

    HorizontalPanel navigationBar = new HorizontalPanel();

    final Image upDirImage = new Image();
    FileChooserImages.images.up().applyTo(upDirImage);
    upDirImage.setTitle(FileChooserEntryPoint.messages.getString("upOneLevel"));
    upDirImage.addMouseListener(new MouseListener() {

      public void onMouseDown(Widget sender, int x, int y) {
      }

      public void onMouseEnter(Widget sender) {
      }

      public void onMouseLeave(Widget sender) {
      }

      public void onMouseMove(Widget sender, int x, int y) {
      }

      public void onMouseUp(Widget sender, int x, int y) {
      }

    });
    upDirImage.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        // go up a dir
        TreeItem tmpItem = selectedTreeItem;
        List<String> parentSegments = new ArrayList<String>();
        while (tmpItem != null) {
          RepositoryFileTree tree = (RepositoryFileTree) tmpItem.getUserObject();
          if (tree.getFile() != null && tree.getFile().getName() != null) {
            parentSegments.add(tree.getFile().getName());
          }
          tmpItem = tmpItem.getParentItem();
        }
        Collections.reverse(parentSegments);
        String myPath = ""; //$NON-NLS-1$
        // If we have a file selected then we need to go one lesser level deep
        final int loopCount = isFileSelected() ? parentSegments.size() - 2 : parentSegments.size() - 1;
        for (int i = 0; i < loopCount; i++) {
          String pathSegment = parentSegments.get(i);
          myPath += "/" + pathSegment; //$NON-NLS-1$
        }
        if (myPath.equals("")) { //$NON-NLS-1$
          myPath = "/"; //$NON-NLS-1$
        }
        selectedTreeItem = selectedTreeItem.getParentItem();
        if(selectedTreeItem == null) {
          selectedTreeItem = repositoryTree.getItem(0);
        }
        changeToPath(myPath);
      }
    });
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    navigationBar.add(navigationListBox);
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    navigationBar.add(upDirImage);
    navigationBar.setCellWidth(upDirImage, "100%"); //$NON-NLS-1$
    DOM.setStyleAttribute(upDirImage.getElement(), "marginLeft", "4px"); //$NON-NLS-1$	//$NON-NLS-2$
    navigationBar.setWidth("100%"); //$NON-NLS-1$

    locationBar.add(navigationBar);
    locationBar.setWidth("100%"); //$NON-NLS-1$

    Label filenameLabel = new Label(FileChooserEntryPoint.messages.getString("filename"));
    filenameLabel.setWidth("550px"); //$NON-NLS-1$
    add(filenameLabel);
    fileNameTextBox.setWidth("300px"); //$NON-NLS-1$
    add(fileNameTextBox);
    add(locationBar);
    add(buildFilesList(selectedTreeItem));
  }

  public Widget buildFilesList(TreeItem parentTreeItem) {
    VerticalPanel filesListPanel = new VerticalPanel();
    filesListPanel.setWidth("100%"); //$NON-NLS-1$

    ScrollPanel filesScroller = new ScrollPanel();
    DOM.setStyleAttribute(filesScroller.getElement(), "background", "#ffffff"); //$NON-NLS-1$ //$NON-NLS-2$
    DOM.setStyleAttribute(filesScroller.getElement(), "border", "1px solid #707070"); //$NON-NLS-1$ //$NON-NLS-2$

    FlexTable filesListTable = new FlexTable();
    filesListTable.setCellSpacing(0);
    Label nameLabel = new Label(FileChooserEntryPoint.messages.getString("name"), false);
    nameLabel.setStyleName("fileChooserHeader"); //$NON-NLS-1$
    Label typeLabel = new Label(FileChooserEntryPoint.messages.getString("type"), false);
    typeLabel.setStyleName("fileChooserHeader"); //$NON-NLS-1$
    Label dateLabel = new Label(FileChooserEntryPoint.messages.getString("dateModified"), false);
    dateLabel.setStyleName("fileChooserHeader"); //$NON-NLS-1$

    ElementUtils.preventTextSelection(nameLabel.getElement());
    ElementUtils.preventTextSelection(typeLabel.getElement());
    ElementUtils.preventTextSelection(dateLabel.getElement());

    filesListTable.setWidget(0, 0, nameLabel);
    filesListTable.getCellFormatter().setWidth(0, 0, "100%"); //$NON-NLS-1$
    filesListTable.setWidget(0, 1, typeLabel);
    filesListTable.setWidget(0, 2, dateLabel);
    
    int row = 0;
    for (int i = 0; i < parentTreeItem.getChildCount(); i++) {
      final TreeItem childItem = parentTreeItem.getChild(i);
      RepositoryFileTree repositoryFileTree = (RepositoryFileTree) childItem.getUserObject();
      RepositoryFile repositoryFile = repositoryFileTree.getFile();
      if (repositoryFile.isFolder() && !(repositoryFile.getName() != null && repositoryFile.getName().equals(ETC_FOLDER))) {
        addFileToList(repositoryFileTree, childItem, filesListTable, row++);
      }
    }
    for (int i = 0; i < parentTreeItem.getChildCount(); i++) {
      final TreeItem childItem = parentTreeItem.getChild(i);
      RepositoryFileTree repositoryFileTree = (RepositoryFileTree) childItem.getUserObject();
      RepositoryFile repositoryFile = repositoryFileTree.getFile();
      if (!repositoryFile.isFolder()) {
        addFileToList(repositoryFileTree, childItem, filesListTable, row++);
      }
    }
    filesScroller.setWidget(filesListTable);
    filesScroller.setHeight("220px"); //$NON-NLS-1$

    filesListPanel.add(filesScroller);
    return filesListPanel;
  }

  private void addFileToList(final RepositoryFileTree repositoryFileTree, final TreeItem item, final FlexTable filesListTable, int row) {
    Label myDateLabel = null;
    RepositoryFile file = repositoryFileTree.getFile();
    Date lastModDate = file.getLastModifiedDate();
    String fileName = file.getName(); 

    final Boolean isDir = file.isFolder(); 
    if(lastModDate != null) {
      myDateLabel = new Label(dateFormat.format(lastModDate), false);
    }

    String finalFileName;
    if (showLocalizedFileNames) {
      finalFileName = file.getTitle();
    } else {
      finalFileName = fileName;
    }

    final Label myNameLabel = new Label(finalFileName, false) {
      public void onBrowserEvent(Event event) {
        switch (event.getTypeInt()) {
        case Event.ONCLICK:
        case Event.ONDBLCLICK:
          handleFileClicked(item, isDir, event, this.getElement());
          break;
        case Event.ONMOUSEOVER:
          this.addStyleDependentName("over"); //$NON-NLS-1$
          break;
        case Event.ONMOUSEOUT:
          this.removeStyleDependentName("over"); //$NON-NLS-1$
          break;
        }
      }
    };
    // biserver-2719: concatenate the name with fileChooser_ so the ids are unique in Mantle
    myNameLabel.getElement().setAttribute("id", "fileChooser_".concat(file.getId())); //$NON-NLS-1$ //$NON-NLS-2$
    myNameLabel.sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
    myNameLabel.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    myNameLabel.setTitle(file.getTitle());
    HorizontalPanel fileNamePanel = new HorizontalPanel();
    Image fileImage = new Image() {
      public void onBrowserEvent(Event event) {
        handleFileClicked(item, isDir, event, myNameLabel.getElement());
      }
    };
    fileImage.sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
    if (isDir) {
      FileChooserImages.images.folder().applyTo(fileImage);
    } else {

      if (fileName.endsWith("waqr.xaction")) { //$NON-NLS-1$
        FileChooserImages.images.file_report().applyTo(fileImage);
      } else if (fileName.endsWith("analysisview.xaction")) { //$NON-NLS-1$
        FileChooserImages.images.file_analysis().applyTo(fileImage);
      } else if (fileName.endsWith(".url")) { //$NON-NLS-1$
        FileChooserImages.images.file_url().applyTo(fileImage);
      } else {
        FileChooserImages.images.file_action().applyTo(fileImage);
      }
    }
    fileNamePanel.add(fileImage);
    fileNamePanel.add(myNameLabel);
    DOM.setStyleAttribute(myNameLabel.getElement(), "cursor", "default"); //$NON-NLS-1$ //$NON-NLS-2$

    Label typeLabel = new Label(isDir ? FileChooserEntryPoint.messages.getString("folder") : FileChooserEntryPoint.messages.getString("file"), false);

    ElementUtils.preventTextSelection(myNameLabel.getElement());
    ElementUtils.preventTextSelection(typeLabel.getElement());
    if(myDateLabel != null) {
      ElementUtils.preventTextSelection(myDateLabel.getElement());  
    }
    fileNamePanel.setStyleName("fileChooserCell"); //$NON-NLS-1$
    typeLabel.setStyleName("fileChooserCell"); //$NON-NLS-1$
    if(myDateLabel != null) {
      myDateLabel.setStyleName("fileChooserCell"); //$NON-NLS-1$
    }
    filesListTable.setWidget(row + 1, 0, fileNamePanel);
    filesListTable.setWidget(row + 1, 1, typeLabel);
    if(myDateLabel != null) {
      filesListTable.setWidget(row + 1, 2, myDateLabel);
    }
  }

  private void handleFileClicked(final TreeItem item, final boolean isDir, final Event event, com.google.gwt.user.client.Element sourceElement) {
    boolean eventWeCareAbout = false;
    TreeItem tmpItem = null;
    if ((DOM.eventGetType(event) & Event.ONDBLCLICK) == Event.ONDBLCLICK) {
      eventWeCareAbout = true;
    } else if ((DOM.eventGetType(event) & Event.ONCLICK) == Event.ONCLICK) {
      eventWeCareAbout = true;
    }
    if (eventWeCareAbout) {
      setFileSelected(true);
      selectedTreeItem = tmpItem = item;

      List<String> parentSegments = new ArrayList<String>();
      while (tmpItem != null) {
        RepositoryFileTree tree = (RepositoryFileTree) tmpItem.getUserObject();
        RepositoryFile file = tree.getFile();
        if (file != null && file.getName() != null) {
          parentSegments.add(file.getName());
        }
        tmpItem = tmpItem.getParentItem();
      }
      Collections.reverse(parentSegments);
      String myPath = ""; //$NON-NLS-1$
      for (int i = 0; isDir ? i < parentSegments.size() : i < parentSegments.size() - 1; i++) {
        myPath += "/" + parentSegments.get(i); //$NON-NLS-1$
      }
      setSelectedPath(myPath);
      if (!isDir) {
        RepositoryFileTree tree = (RepositoryFileTree)  selectedTreeItem.getUserObject();
        if (tree.getFile() != null) {
          fileNameTextBox.setText(tree.getFile().getTitle());
          actualFileName = tree.getFile().getName();
        }
      }
    }
    // double click
    if ((DOM.eventGetType(event) & Event.ONDBLCLICK) == Event.ONDBLCLICK) {
      if (isDir) {
        initUI();
      } else {
        fireFileSelected();
      }
    } else if ((DOM.eventGetType(event) & Event.ONCLICK) == Event.ONCLICK) {
      fireFileSelectionChanged();
      // single click
      // highlight row
      if (lastSelectedFileElement != null) {
        com.google.gwt.dom.client.Element parentRow = ElementUtils.findElementAboveByTagName(lastSelectedFileElement, "table"); //$NON-NLS-1$
        parentRow.getStyle().setProperty("background", "white"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      com.google.gwt.dom.client.Element parentRow = ElementUtils.findElementAboveByTagName(sourceElement, "table"); //$NON-NLS-1$
      parentRow.getStyle().setProperty("background", "#B9B9B9"); //$NON-NLS-1$ //$NON-NLS-2$
      lastSelectedFileElement = sourceElement;
    }
  }

  private void setFileSelected(boolean selected) {
    fileSelected = selected;
  }

  public boolean isFileSelected() {
    return fileSelected;
  }


  public TreeItem getTreeItem(List<String> pathSegments) {
    // find the tree node whose location matches the pathSegment paths
    TreeItem selectedItem = repositoryTree.getItem(0);
    for (String segment : pathSegments) {
      for (int i = 0; i < selectedItem.getChildCount(); i++) {
        TreeItem item = selectedItem.getChild(i);
        @SuppressWarnings("unchecked")
        RepositoryFileTree tree = (RepositoryFileTree) item.getUserObject();        
        if (segment.equals(tree.getFile().getName())) {
          selectedItem = item;
        }
      }
    }
    return selectedItem;
  }

  public FileChooserMode getMode() {
    return mode;
  }

  public void setMode(FileChooserMode mode) {
    this.mode = mode;
  }

  public String getSelectedPath() {
    return selectedPath;
  }

  public void setSelectedPath(String selectedPath) {
    this.selectedPath = selectedPath;
  }

  public void setFileChooserRepositoryFileTree(RepositoryFileTree fileChooserRepositoryFileTree) {
    this.fileTree = fileChooserRepositoryFileTree;
    repositoryTree = TreeBuilder.buildSolutionTree(fileChooserRepositoryFileTree, showHiddenFiles, showLocalizedFileNames, fileFilter);
    initUI();
  }

  public void fireFileSelected(RepositoryFile file) {
    for (FileChooserListener listener : listeners) {
      listener.fileSelected(file);
    }
  }

  public void fireFileSelected() {
    for (FileChooserListener listener : listeners) {
      RepositoryFileTree tree = (RepositoryFileTree) selectedTreeItem.getUserObject();
      listener.fileSelected(tree.getFile());
    }
  }

  public void fireFileSelectionChanged() {
    for (FileChooserListener listener : listeners) {
      RepositoryFileTree tree = (RepositoryFileTree) selectedTreeItem.getUserObject();
      listener.fileSelectionChanged(tree.getFile());
    }
  }

  public String getSolution() {
    if (getSelectedPath().indexOf("/", 1) == -1) { //$NON-NLS-1$
      return getSelectedPath().substring(1);
    } else {
      return getSelectedPath().substring(1, getSelectedPath().indexOf("/", 1)); //$NON-NLS-1$
    }
  }

  public String getPath() {
    int startIdx = getSelectedPath().indexOf("/", 1); //$NON-NLS-1$
    if (-1 == startIdx) {
      return ""; //$NON-NLS-1$
    } else {
      return "/" + getSelectedPath().substring(startIdx + 1); //$NON-NLS-1$
    }
  }

  public String getActualFileName() {
    return actualFileName;
  }

  public String getLocalizedFileName() {
    return fileNameTextBox.getText();
  }

  public String getFullPath() {
    String name = getActualFileName();
    if (!"".equals(name)) { //$NON-NLS-1$
      name = "/" + name; //$NON-NLS-1$
    }
    return getSolution() + getPath() + name;
  }

  public void addFileChooserListener(FileChooserListener listener) {
    listeners.add(listener);
  }

  public void removeFileChooserListener(FileChooserListener listener) {
    listeners.remove(listener);
  }

  public FileFilter getFileFilter() {

    return fileFilter;
  }

  public void setFileFilter(FileFilter fileFilter) {

    this.fileFilter = fileFilter;

    repositoryTree = TreeBuilder.buildSolutionTree(fileTree, showHiddenFiles, showLocalizedFileNames, fileFilter);
    initUI();
  }

  public boolean doesSelectedFileExist() {
    return search(fileTree, getActualFileName()) != null; 
  }

  public void setShowLocalizedFileNames(boolean showLocalizedFileNames) {
    this.showLocalizedFileNames = showLocalizedFileNames;
    initUI();
  }

  public void changeToPath(String path) {
    setSelectedPath(path);
    initUI();
    fireFileSelectionChanged();
  }

}
