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
import java.util.HashMap;
import java.util.List;

import org.pentaho.gwt.widgets.client.dialogs.IDialogCallback;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.filechooser.images.FileChooserImages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;
import org.pentaho.gwt.widgets.client.utils.ElementUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
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
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@SuppressWarnings("deprecation")
public class FileChooser extends VerticalPanel {

  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();

  public enum FileChooserMode {
    OPEN, OPEN_READ_ONLY, SAVE
  }

  FileChooserMode mode = FileChooserMode.OPEN;
  String selectedPath;
  String previousPath;

  ListBox navigationListBox;
  Tree repositoryTree;
  TreeItem selectedTreeItem;
  boolean showHiddenFiles = false;
  boolean showLocalizedFileNames = true;
  boolean showSearch = false;
  com.google.gwt.user.client.Element lastSelectedFileElement;
  TextBox fileNameTextBox = new TextBox();
  DateTimeFormat dateFormat = DateTimeFormat.getMediumDateTimeFormat();
  Document solutionRepositoryDocument;

  ArrayList<FileChooserListener> listeners = new ArrayList<FileChooserListener>();
  private String actualFileName;
  boolean fileSelected = false;

  private static final String ACTUAL_FILE_NAME = "name"; //$NON-NLS-1$
  private static final String LOCALIZED_FILE_NAME = "localized-name"; //$NON-NLS-1$
  private FileFilter fileFilter;

  public FileChooser() {
    fileNameTextBox.addKeyboardListener(new KeyboardListener() {

      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        actualFileName = fileNameTextBox.getText();
        if (keyCode == KeyboardListener.KEY_ENTER) {
          fireFileSelected();
        }
      }

    });
    setSpacing(3);
  }

  public FileChooser(FileChooserMode mode, String selectedPath, boolean showLocalizedFileNames) {
    this(mode, selectedPath, showLocalizedFileNames, null);
  }

  public FileChooser(FileChooserMode mode, String selectedPath, boolean showLocalizedFileNames, Document solutionRepositoryDocument) {
    this();
    this.mode = mode;
    this.selectedPath = selectedPath;
    this.solutionRepositoryDocument = solutionRepositoryDocument;
    this.showLocalizedFileNames = showLocalizedFileNames;
    if (null != solutionRepositoryDocument) {
      repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames, fileFilter);
      initUI(false);
    }
  }

  public FileChooser(FileChooserMode mode, String selectedPath) {
    this();
    this.mode = mode;
    this.selectedPath = selectedPath;
    try {
      fetchRepositoryDocument(null);
    } catch (RequestException e) {
      Window.alert(e.toString());
    }
  }

  public void fetchRepositoryDocument(final IDialogCallback completedCallback) throws RequestException {
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
        // ok, we have a repository document, we can build the GUI
        // consider caching the document
        solutionRepositoryDocument = (Document) XMLParser.parse((String) response.getText());
        repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames, fileFilter);
        initUI(false);
        if (completedCallback != null) {
          completedCallback.okPressed();
        }
      }

    };
    builder.sendRequest(null, callback);
  }

  private void buildOracleValues(List<String> oracleValues, Element element) {
    String name = element.getAttribute(ACTUAL_FILE_NAME);
    String localizedName = element.getAttribute(LOCALIZED_FILE_NAME);
    boolean isVisible = "true".equals(element.getAttribute("visible")); //$NON-NLS-1$ //$NON-NLS-2$
    if (isVisible || showHiddenFiles) {
      if (name != null) {
        oracleValues.add(name);
      }
      if (localizedName != null) {
        oracleValues.add(localizedName);
      }
      NodeList children = element.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        buildOracleValues(oracleValues, (Element) children.item(i));
      }
    }
  }

  public void initUI(final boolean fromSearch) {

    if (mode == FileChooserMode.OPEN_READ_ONLY) {
      fileNameTextBox.setReadOnly(true);
    }
    // We are here because we are initiating a fresh UI for a new directory
    // Since there is no file selected currently, we are setting file selected to false.
    setFileSelected(false);

    String path = this.selectedPath;
    if (fromSearch) {
      path = previousPath;
    }
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

    if (fromSearch) {
      navigationListBox.addItem(MSGS.searchResults(), MSGS.searchResults());
    }

    navigationListBox.setSelectedIndex(navigationListBox.getItemCount() - 1);
    navigationListBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        changeToPath(navigationListBox.getItemText(navigationListBox.getSelectedIndex()));
      }
    });

    if (!fromSearch) {
      selectedTreeItem = getTreeItem(pathSegments);
    }

    clear();

    VerticalPanel locationBar = new VerticalPanel();
    locationBar.add(new Label(MSGS.location()));

    HorizontalPanel navigationBar = new HorizontalPanel();

    final Image searchImage = new Image();
    FileChooserImages.images.search().applyTo(searchImage);
    searchImage.setTitle(MSGS.search());
    DOM.setStyleAttribute(searchImage.getElement(), "border", "1px solid white"); //$NON-NLS-1$ //$NON-NLS-2$
    searchImage.addMouseListener(new MouseListener() {

      public void onMouseDown(Widget sender, int x, int y) {
      }

      public void onMouseEnter(Widget sender) {
        DOM.setStyleAttribute(searchImage.getElement(), "borderLeft", "1px solid gray"); //$NON-NLS-1$ //$NON-NLS-2$
        DOM.setStyleAttribute(searchImage.getElement(), "borderTop", "1px solid gray"); //$NON-NLS-1$ //$NON-NLS-2$
        DOM.setStyleAttribute(searchImage.getElement(), "borderRight", "1px solid black"); //$NON-NLS-1$ //$NON-NLS-2$
        DOM.setStyleAttribute(searchImage.getElement(), "borderBottom", "1px solid black"); //$NON-NLS-1$ //$NON-NLS-2$
      }

      public void onMouseLeave(Widget sender) {
        DOM.setStyleAttribute(searchImage.getElement(), "border", "1px solid white"); //$NON-NLS-1$ //$NON-NLS-2$
      }

      public void onMouseMove(Widget sender, int x, int y) {
      }

      public void onMouseUp(Widget sender, int x, int y) {
        DOM.setStyleAttribute(searchImage.getElement(), "border", "1px solid white"); //$NON-NLS-1$ //$NON-NLS-2$
      }

    });
    searchImage.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        // bring up a search
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        List<String> oracleValues = new ArrayList<String>();
        Element documentElement = solutionRepositoryDocument.getDocumentElement();
        for (int i = 0; i < documentElement.getChildNodes().getLength(); i++) {
          buildOracleValues(oracleValues, (Element) documentElement.getChildNodes().item(i));
        }
        oracle.addAll(oracleValues);

        final TextBox searchTextBox = new TextBox();
        SuggestBox suggestTextBox = new SuggestBox(oracle, searchTextBox);
        IDialogCallback callback = new IDialogCallback() {

          public void cancelPressed() {
          }

          public void okPressed() {
            TreeItem parentItem = new TreeItem();
            parentItem.setText(MSGS.searchResults());
            parentItem.setTitle(MSGS.searchResults());

            HashMap<String, Object> attributeMap = new HashMap<String, Object>();
            attributeMap.put(ACTUAL_FILE_NAME, MSGS.searchResults());
            attributeMap.put(LOCALIZED_FILE_NAME, MSGS.searchResults());
            attributeMap.put("description", MSGS.searchResults()); //$NON-NLS-1$
            attributeMap.put("lastModifiedDate", "" + (new Date()).getTime()); //$NON-NLS-1$ //$NON-NLS-2$
            attributeMap.put("visible", "true"); //$NON-NLS-1$ //$NON-NLS-2$
            attributeMap.put("isDirectory", "true"); //$NON-NLS-1$ //$NON-NLS-2$
            parentItem.setUserObject(attributeMap);

            findMatchingTreeItems(parentItem, repositoryTree.getItem(0), searchTextBox.getText().replace("*", "")); //$NON-NLS-1$ //$NON-NLS-2$
            selectedTreeItem = parentItem;
            previousPath = finalPath;
            selectedPath = MSGS.searchResults();
            initUI(true);
          }

        };
        PromptDialogBox searchDialog = new PromptDialogBox(MSGS.search(), MSGS.ok(), MSGS.cancel(), false, true, suggestTextBox);
        searchDialog.setCallback(callback);
        searchDialog.setFocusWidget(searchTextBox);
        searchDialog.center();
      }
    });

    final Image upDirImage = new Image();
    FileChooserImages.images.up().applyTo(upDirImage);
    upDirImage.setTitle(MSGS.upOneLevel());
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
          @SuppressWarnings("unchecked")
          HashMap<String, Object> attributeMap = (HashMap<String, Object>) tmpItem.getUserObject();
          if (attributeMap.get(ACTUAL_FILE_NAME) != null) {
            parentSegments.add((String) attributeMap.get(ACTUAL_FILE_NAME));
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
        changeToPath(myPath);
      }
    });
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    navigationBar.add(navigationListBox);
    if (showSearch) {
      navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      navigationBar.add(searchImage);
    }
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    navigationBar.add(upDirImage);
    navigationBar.setCellWidth(upDirImage, "100%");	//$NON-NLS-1$
    DOM.setStyleAttribute(upDirImage.getElement(), "margin-left", "4px");	//$NON-NLS-1$	//$NON-NLS-2$
    navigationBar.setWidth("100%");	//$NON-NLS-1$

    locationBar.add(navigationBar);
    locationBar.setWidth("100%"); //$NON-NLS-1$

    Label filenameLabel = new Label(MSGS.filename());
    filenameLabel.setWidth("550px"); //$NON-NLS-1$
    add(filenameLabel);
    fileNameTextBox.setWidth("300px"); //$NON-NLS-1$
    add(fileNameTextBox);
    add(locationBar);
    add(buildFilesList(selectedTreeItem));
  }

  public void findMatchingTreeItems(TreeItem rootItem, TreeItem parentItem, String searchText) {
    searchText = searchText.toLowerCase();
    for (int i = 0; i < parentItem.getChildCount(); i++) {
      final TreeItem childItem = parentItem.getChild(i);
      @SuppressWarnings("unchecked")
      HashMap<String, Object> attributeMap = (HashMap<String, Object>) childItem.getUserObject();
      final boolean isDir = "true".equals(attributeMap.get("isDirectory")); //$NON-NLS-1$ //$NON-NLS-2$
      String name = ((String) attributeMap.get(ACTUAL_FILE_NAME)).toLowerCase();
      String localizedName = ((String) attributeMap.get(LOCALIZED_FILE_NAME)).toLowerCase();
      if (isDir) {
        findMatchingTreeItems(rootItem, childItem, searchText);
      }
      if (name.indexOf(searchText) != -1 || localizedName.indexOf(searchText) != -1) {
        TreeItem copyItem = new TreeItem();
        copyItem.setText(childItem.getText());
        copyItem.setTitle(childItem.getTitle());
        attributeMap.put("original", childItem); //$NON-NLS-1$
        copyItem.setUserObject(attributeMap);
        rootItem.addItem(copyItem);
      }
    }
  }

  public Widget buildFilesList(TreeItem parentTreeItem) {
    VerticalPanel filesListPanel = new VerticalPanel();
    filesListPanel.setWidth("100%"); //$NON-NLS-1$

    ScrollPanel filesScroller = new ScrollPanel();
    DOM.setStyleAttribute(filesScroller.getElement(), "background", "#ffffff"); //$NON-NLS-1$ //$NON-NLS-2$
    DOM.setStyleAttribute(filesScroller.getElement(), "border", "1px solid #707070"); //$NON-NLS-1$ //$NON-NLS-2$

    FlexTable filesListTable = new FlexTable();
    filesListTable.setCellSpacing(0);
    Label nameLabel = new Label(MSGS.name(), false);
    nameLabel.setStyleName("fileChooserHeader"); //$NON-NLS-1$
    Label typeLabel = new Label(MSGS.type(), false);
    typeLabel.setStyleName("fileChooserHeader"); //$NON-NLS-1$
    Label dateLabel = new Label(MSGS.dateModified(), false);
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
      @SuppressWarnings("unchecked")
      HashMap<String, String> attributeMap = (HashMap<String, String>) childItem.getUserObject();
      final boolean isDir = "true".equals(attributeMap.get("isDirectory")); //$NON-NLS-1$ //$NON-NLS-2$
      if (isDir) {
        addFileToList(attributeMap, childItem, filesListTable, row++);
      }
    }
    for (int i = 0; i < parentTreeItem.getChildCount(); i++) {
      final TreeItem childItem = parentTreeItem.getChild(i);
      @SuppressWarnings("unchecked")
      HashMap<String, String> attributeMap = (HashMap<String, String>) childItem.getUserObject();
      final boolean isDir = "true".equals(attributeMap.get("isDirectory")); //$NON-NLS-1$ //$NON-NLS-2$
      if (!isDir) {
        addFileToList(attributeMap, childItem, filesListTable, row++);
      }
    }
    filesScroller.setWidget(filesListTable);
    filesScroller.setHeight("220px"); //$NON-NLS-1$

    filesListPanel.add(filesScroller);
    return filesListPanel;
  }

  private void addFileToList(final HashMap<String, String> attributeMap, final TreeItem item, final FlexTable filesListTable, int row) {
    Date lastModDate = new Date(Long.parseLong(attributeMap.get("lastModifiedDate"))); //$NON-NLS-1$
    final boolean isDir = "true".equals(attributeMap.get("isDirectory")); //$NON-NLS-1$ //$NON-NLS-2$
    Label myDateLabel = new Label(dateFormat.format(lastModDate), false);

    String finalFileName;
    if (showLocalizedFileNames) {
      finalFileName = attributeMap.get(LOCALIZED_FILE_NAME);
    } else {
      finalFileName = attributeMap.get(ACTUAL_FILE_NAME);
    }

    final Label myNameLabel = new Label(finalFileName, false) {
      public void onBrowserEvent(Event event) {
        switch(event.getTypeInt()){
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
    myNameLabel.getElement().setAttribute("id", attributeMap.get("name")); //$NON-NLS-1$ //$NON-NLS-2$
    myNameLabel.sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
    myNameLabel.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    myNameLabel.setTitle(attributeMap.get(LOCALIZED_FILE_NAME));

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
      String fileName = attributeMap.get("name"); //$NON-NLS-1$
      
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

    Label typeLabel = new Label(isDir ? MSGS.folder() : MSGS.file(), false);

    ElementUtils.preventTextSelection(myNameLabel.getElement());
    ElementUtils.preventTextSelection(typeLabel.getElement());
    ElementUtils.preventTextSelection(myDateLabel.getElement());

    fileNamePanel.setStyleName("fileChooserCell"); //$NON-NLS-1$
    typeLabel.setStyleName("fileChooserCell"); //$NON-NLS-1$
    myDateLabel.setStyleName("fileChooserCell"); //$NON-NLS-1$

    filesListTable.setWidget(row + 1, 0, fileNamePanel);
    filesListTable.setWidget(row + 1, 1, typeLabel);
    filesListTable.setWidget(row + 1, 2, myDateLabel);
  }

  private void handleFileClicked(final TreeItem item, final boolean isDir, final Event event, com.google.gwt.user.client.Element sourceElement) {
    boolean eventWeCareAbout = false;
    if ((DOM.eventGetType(event) & Event.ONDBLCLICK) == Event.ONDBLCLICK) {
      eventWeCareAbout = true;
    } else if ((DOM.eventGetType(event) & Event.ONCLICK) == Event.ONCLICK) {
      eventWeCareAbout = true;
    }
    if (eventWeCareAbout) {
      setFileSelected(true);
      @SuppressWarnings("unchecked")
      HashMap<String, Object> attributeMap = (HashMap<String, Object>) item.getUserObject();
      TreeItem originalItem = (TreeItem) attributeMap.get("original"); //$NON-NLS-1$
      TreeItem tmpItem = originalItem;
      if (originalItem == null) {
        tmpItem = item;
      }
      selectedTreeItem = tmpItem;

      List<String> parentSegments = new ArrayList<String>();
      while (tmpItem != null) {
        @SuppressWarnings("unchecked")
        HashMap<String, Object> tmpAttributeMap = (HashMap<String, Object>) tmpItem.getUserObject();
        if (tmpAttributeMap.get(ACTUAL_FILE_NAME) != null) {
          parentSegments.add((String) tmpAttributeMap.get(ACTUAL_FILE_NAME));
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
        @SuppressWarnings("unchecked")
        HashMap<String, Object> tmpAttributeMap = (HashMap<String, Object>) selectedTreeItem.getUserObject();
        if (tmpAttributeMap.get(ACTUAL_FILE_NAME) != null) {
          fileNameTextBox.setText((String) tmpAttributeMap.get(LOCALIZED_FILE_NAME));
          actualFileName = (String) tmpAttributeMap.get(ACTUAL_FILE_NAME);
        }
      }
    }
    // double click
    if ((DOM.eventGetType(event) & Event.ONDBLCLICK) == Event.ONDBLCLICK) {
      if (isDir) {
        initUI(false);
      } else {
        fireFileSelected();
      }
    } else if ((DOM.eventGetType(event) & Event.ONCLICK) == Event.ONCLICK) {
      fireFileSelectionChanged();
      // single click
      // highlight row
      if (lastSelectedFileElement != null) {
        com.google.gwt.dom.client.Element parentRow = ElementUtils.findElementAboveByTagName(lastSelectedFileElement, "table"); //$NON-NLS-1$
        parentRow.getStyle().setProperty("background", "white");	//$NON-NLS-1$ //$NON-NLS-2$
      }
      com.google.gwt.dom.client.Element parentRow = ElementUtils.findElementAboveByTagName(sourceElement, "table"); //$NON-NLS-1$
      parentRow.getStyle().setProperty("background", "#B9B9B9");	//$NON-NLS-1$ //$NON-NLS-2$
      lastSelectedFileElement = sourceElement;
    }
  }

  private void setFileSelected(boolean selected) {
    fileSelected = selected;
  }

  public boolean isFileSelected() {
    return fileSelected;
  }

  @SuppressWarnings("unused")
  private String getTitle(TreeItem item) {
    List<String> parentSegments = new ArrayList<String>();
    while (item != null) {
      @SuppressWarnings("unchecked")
      HashMap<String, Object> tmpAttributeMap = (HashMap<String, Object>) item.getUserObject();
      if (tmpAttributeMap.get(ACTUAL_FILE_NAME) != null) {
        parentSegments.add((String) tmpAttributeMap.get(ACTUAL_FILE_NAME));
      }
      TreeItem originalItem = (TreeItem) tmpAttributeMap.get("original"); //$NON-NLS-1$
      if (originalItem != null) {
        item = originalItem.getParentItem();
      } else {
        item = item.getParentItem();
      }
    }
    Collections.reverse(parentSegments);
    String myPath = ""; //$NON-NLS-1$
    for (int i = 0; i < parentSegments.size(); i++) {
      String pathSegment = parentSegments.get(i);
      myPath += "/" + pathSegment; //$NON-NLS-1$
    }
    if (myPath.equals("")) { //$NON-NLS-1$
      myPath = "/"; //$NON-NLS-1$
    }
    return myPath;
  }

  public TreeItem getTreeItem(List<String> pathSegments) {
    // find the tree node whose location matches the pathSegment paths
    TreeItem selectedItem = repositoryTree.getItem(0);
    for (String segment : pathSegments) {
      for (int i = 0; i < selectedItem.getChildCount(); i++) {
        TreeItem item = selectedItem.getChild(i);
        @SuppressWarnings("unchecked")
        HashMap<String, String> attributeMap = (HashMap<String, String>) item.getUserObject();
        if (segment.equals(attributeMap.get(ACTUAL_FILE_NAME))) {
          selectedItem = item;
        }
      }
    }
    
    @SuppressWarnings("unchecked")
    HashMap<String, Object> attributeMap = (HashMap<String, Object>) selectedItem.getUserObject();
    TreeItem originalItem = (TreeItem) attributeMap.get("original"); //$NON-NLS-1$
    if (originalItem != null) {
      selectedItem = originalItem;
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

  public void setSolutionRepositoryDocument(Document doc) {
    solutionRepositoryDocument = doc;
    repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames, fileFilter);
    initUI(false);
  }

  public void fireFileSelected() {
    for (FileChooserListener listener : listeners) {
      listener.fileSelected(getSolution(), getPath(), getActualFileName(), getLocalizedFileName());
    }
  }

  public void fireFileSelectionChanged() {
    for (FileChooserListener listener : listeners) {
      listener.fileSelectionChanged(getSolution(), getPath(), getActualFileName());
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

    repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames, fileFilter);
    initUI(false);
  }

  public boolean doesSelectedFileExist() {
    String path = "/" + getFullPath(); //$NON-NLS-1$
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
    TreeItem treeItem = getTreeItem(pathSegments);
    if (treeItem != null) {
      @SuppressWarnings("unchecked")
      HashMap<String, Object> attributeMap = (HashMap<String, Object>) treeItem.getUserObject();
      if (getActualFileName().equals(attributeMap.get(ACTUAL_FILE_NAME))) {
        return true;
      }
    }
    return false;
  }

  public void setShowLocalizedFileNames(boolean showLocalizedFileNames) {
    this.showLocalizedFileNames = showLocalizedFileNames;
    initUI(false);
  }

  public void changeToPath(String path) {
    setSelectedPath(path);
    initUI(false);
    fireFileSelectionChanged();
  }

  public boolean isShowSearch() {
    return showSearch;
  }

  public void setShowSearch(boolean showSearch) {
    this.showSearch = showSearch;
    initUI(false);
  }
}
