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
  boolean showSearch = true;
  com.google.gwt.user.client.Element lastSelectedFileElement;
  TextBox fileNameTextBox = new TextBox();
  DateTimeFormat dateFormat = DateTimeFormat.getMediumDateTimeFormat();
  Document solutionRepositoryDocument;
  
  ArrayList<FileChooserListener> listeners = new ArrayList<FileChooserListener>();
  private String actualFileName;
  boolean fileSelected = false;
  
  private static final String ACTUAL_FILE_NAME="name";
  private static final String LOCALIZED_FILE_NAME="localized-name";
  
  public FileChooser() {
    fileNameTextBox.addKeyboardListener(new KeyboardListener() {

      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
      }

      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
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
      repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames);
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
      builder = new RequestBuilder(RequestBuilder.GET, "/pentaho/SolutionRepositoryService?component=getSolutionRepositoryDoc&filter=*.xaction,*.url");
    } else {
      builder = new RequestBuilder(RequestBuilder.GET,
          "http://localhost:8080/pentaho/SolutionRepositoryService?component=getSolutionRepositoryDoc&userid=joe&password=password");
    }
    RequestCallback callback = new RequestCallback() {

      public void onError(Request request, Throwable exception) {
        Window.alert(exception.toString());
      }

      public void onResponseReceived(Request request, Response response) {
        // ok, we have a repository document, we can build the GUI
        // consider caching the document
        solutionRepositoryDocument = (Document) XMLParser.parse((String) response.getText());
        repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames);
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
    boolean isVisible = "true".equals(element.getAttribute("visible"));
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
      int index = path.indexOf("/", 0);
      while (index >= 0) {
        int oldIndex = index;
        index = path.indexOf("/", oldIndex + 1);
        if (index >= 0) {
          pathSegments.add(path.substring(oldIndex + 1, index));
        }
      }
      pathSegments.add(path.substring(path.lastIndexOf("/") + 1));
    }
    navigationListBox = new ListBox();
    navigationListBox.setWidth("350px");
    // now we can find the tree nodes who match the path segments
    navigationListBox.addItem("/", "/");

    for (int i = 0; i < pathSegments.size(); i++) {
      String segment = pathSegments.get(i);
      String fullPath = "";
      for (int j = 0; j <= i; j++) {
        fullPath += "/" + pathSegments.get(j);
      }
      if (!fullPath.equals("/")) {
        navigationListBox.addItem(fullPath, segment);
      }
    }

    if (fromSearch) {
      navigationListBox.addItem("Search Results", "Search Results");
    }

    navigationListBox.setSelectedIndex(navigationListBox.getItemCount() - 1);
    navigationListBox.addChangeListener(new ChangeListener() {
      public void onChange(Widget sender) {
        changeToPath(navigationListBox.getItemText(navigationListBox.getSelectedIndex()));
        resetFileName();
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
    searchImage.setTitle("Search");
    DOM.setStyleAttribute(searchImage.getElement(), "border", "1px solid white");
    searchImage.addMouseListener(new MouseListener() {

      public void onMouseDown(Widget sender, int x, int y) {
      }

      public void onMouseEnter(Widget sender) {
        DOM.setStyleAttribute(searchImage.getElement(), "borderLeft", "1px solid gray");
        DOM.setStyleAttribute(searchImage.getElement(), "borderTop", "1px solid gray");
        DOM.setStyleAttribute(searchImage.getElement(), "borderRight", "1px solid black");
        DOM.setStyleAttribute(searchImage.getElement(), "borderBottom", "1px solid black");
      }

      public void onMouseLeave(Widget sender) {
        DOM.setStyleAttribute(searchImage.getElement(), "border", "1px solid white");
      }

      public void onMouseMove(Widget sender, int x, int y) {
      }

      public void onMouseUp(Widget sender, int x, int y) {
        DOM.setStyleAttribute(searchImage.getElement(), "border", "1px solid white");
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
            parentItem.setText("Search Results");
            parentItem.setTitle("Search Results");

            HashMap<String, Object> attributeMap = new HashMap<String, Object>();
            attributeMap.put(ACTUAL_FILE_NAME, "Search Results");
            attributeMap.put(LOCALIZED_FILE_NAME, "Search Results");
            attributeMap.put("description", "Search Results");
            attributeMap.put("lastModifiedDate", "" + (new Date()).getTime());
            attributeMap.put("visible", "true");
            attributeMap.put("isDirectory", "true");
            parentItem.setUserObject(attributeMap);

            findMatchingTreeItems(parentItem, repositoryTree.getItem(0), searchTextBox.getText().replace("*", ""));
            selectedTreeItem = parentItem;
            previousPath = finalPath;
            selectedPath = "Search Results";
            initUI(true);
          }

        };
        PromptDialogBox searchDialog = new PromptDialogBox("Search", "OK", "Cancel", false, true, suggestTextBox);
        searchDialog.setCallback(callback);
        searchDialog.setFocusWidget(searchTextBox);
        searchDialog.center();
      }
    });

    final Image upDirImage = new Image();
    FileChooserImages.images.up().applyTo(upDirImage);
    upDirImage.setTitle("Up One Level");
    DOM.setStyleAttribute(upDirImage.getElement(), "border", "1px solid white");
    upDirImage.addMouseListener(new MouseListener() {

      public void onMouseDown(Widget sender, int x, int y) {
      }

      public void onMouseEnter(Widget sender) {
        DOM.setStyleAttribute(upDirImage.getElement(), "borderLeft", "1px solid gray");
        DOM.setStyleAttribute(upDirImage.getElement(), "borderTop", "1px solid gray");
        DOM.setStyleAttribute(upDirImage.getElement(), "borderRight", "1px solid black");
        DOM.setStyleAttribute(upDirImage.getElement(), "borderBottom", "1px solid black");
      }

      public void onMouseLeave(Widget sender) {
        DOM.setStyleAttribute(upDirImage.getElement(), "border", "1px solid white");
      }

      public void onMouseMove(Widget sender, int x, int y) {
      }

      public void onMouseUp(Widget sender, int x, int y) {
        DOM.setStyleAttribute(searchImage.getElement(), "border", "1px solid white");
      }

    });
    upDirImage.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        // go up a dir
        TreeItem tmpItem = selectedTreeItem;
        List<String> parentSegments = new ArrayList<String>();
        while (tmpItem != null) {
          HashMap<String, Object> attributeMap = (HashMap<String, Object>) tmpItem.getUserObject();
          if (attributeMap.get(ACTUAL_FILE_NAME) != null) {
            parentSegments.add((String) attributeMap.get(ACTUAL_FILE_NAME));
          }
          tmpItem = tmpItem.getParentItem();
        }
        Collections.reverse(parentSegments);
        String myPath = "";
        // If we have a file selected then we need to go one lesser level deep
        final int loopCount = isFileSelected()? parentSegments.size() - 2 : parentSegments.size() - 1;
        for (int i = 0; i < loopCount; i++) {
          String pathSegment = parentSegments.get(i);
          myPath += "/" + pathSegment;
        }
        if (myPath.equals("")) {
          myPath = "/";
        }
        resetFileName();
        changeToPath(myPath);        
      }
    });
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    navigationBar.add(navigationListBox);
    if (showSearch) {
      navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
      navigationBar.add(searchImage);
    }
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    navigationBar.add(upDirImage);
    navigationBar.setWidth("100%");

    locationBar.add(navigationBar);
    locationBar.setWidth("100%");

    add(new Label(MSGS.filename()));
    fileNameTextBox.setWidth("300px");
    add(fileNameTextBox);
    add(locationBar);
    add(buildFilesList(selectedTreeItem));
  }

  public void findMatchingTreeItems(TreeItem rootItem, TreeItem parentItem, String searchText) {
    searchText = searchText.toLowerCase();
    for (int i = 0; i < parentItem.getChildCount(); i++) {
      final TreeItem childItem = parentItem.getChild(i);
      HashMap<String, Object> attributeMap = (HashMap<String, Object>) childItem.getUserObject();
      final boolean isDir = "true".equals(attributeMap.get("isDirectory"));
      String name = ((String) attributeMap.get(ACTUAL_FILE_NAME)).toLowerCase();
      String localizedName = ((String) attributeMap.get(LOCALIZED_FILE_NAME)).toLowerCase();
      if (isDir) {
        findMatchingTreeItems(rootItem, childItem, searchText);
      }
      if (name.indexOf(searchText) != -1 || localizedName.indexOf(searchText) != -1) {
        TreeItem copyItem = new TreeItem();
        copyItem.setText(childItem.getText());
        copyItem.setTitle(childItem.getTitle());
        attributeMap.put("original", childItem);
        copyItem.setUserObject(attributeMap);
        rootItem.addItem(copyItem);
      }
    }
  }

  public Widget buildFilesList(TreeItem parentTreeItem) {
    VerticalPanel filesListPanel = new VerticalPanel();
    filesListPanel.setWidth("100%");

    ScrollPanel filesScroller = new ScrollPanel();
    DOM.setStyleAttribute(filesScroller.getElement(), "background", "#ffffff");
    DOM.setStyleAttribute(filesScroller.getElement(), "border", "1px solid #707070");

    FlexTable filesListTable = new FlexTable();
    filesListTable.setCellSpacing(0);
    Label nameLabel = new Label(MSGS.name());
    Label typeLabel = new Label(MSGS.type());
    Label dateLabel = new Label(MSGS.dateModified());

    filesListTable.setWidget(0, 0, nameLabel);
    filesListTable.getCellFormatter().setWidth(0, 0, "100%");
    filesListTable.setWidget(0, 1, typeLabel);
    filesListTable.setWidget(0, 2, dateLabel);

    // name
    DOM.setStyleAttribute(nameLabel.getElement(), "background", "#f0f0f0");
    DOM.setStyleAttribute(nameLabel.getElement(), "border", "1px solid black");
    // type
    DOM.setStyleAttribute(typeLabel.getElement(), "background", "#f0f0f0");
    DOM.setStyleAttribute(typeLabel.getElement(), "borderTop", "1px solid black");
    DOM.setStyleAttribute(typeLabel.getElement(), "borderRight", "1px solid black");
    DOM.setStyleAttribute(typeLabel.getElement(), "borderBottom", "1px solid black");
    // date
    DOM.setStyleAttribute(dateLabel.getElement(), "background", "#f0f0f0");
    DOM.setStyleAttribute(dateLabel.getElement(), "borderTop", "1px solid black");
    DOM.setStyleAttribute(dateLabel.getElement(), "borderRight", "1px solid black");
    DOM.setStyleAttribute(dateLabel.getElement(), "borderBottom", "1px solid black");

    int row = 0;
    for (int i = 0; i < parentTreeItem.getChildCount(); i++) {
      final TreeItem childItem = parentTreeItem.getChild(i);
      HashMap<String, String> attributeMap = (HashMap<String, String>) childItem.getUserObject();
      final boolean isDir = "true".equals(attributeMap.get("isDirectory"));
      if (isDir) {
        addFileToList(attributeMap, childItem, filesListTable, row++);
      }
    }
    for (int i = 0; i < parentTreeItem.getChildCount(); i++) {
      final TreeItem childItem = parentTreeItem.getChild(i);
      HashMap<String, String> attributeMap = (HashMap<String, String>) childItem.getUserObject();
      final boolean isDir = "true".equals(attributeMap.get("isDirectory"));
      if (!isDir) {
        addFileToList(attributeMap, childItem, filesListTable, row++);
      }
    }
    filesScroller.setWidget(filesListTable);
    filesScroller.setHeight("200px");

    filesListPanel.add(filesScroller);
    return filesListPanel;
  }

  private void addFileToList(final HashMap<String, String> attributeMap, final TreeItem item, final FlexTable filesListTable, int row) {
    Date lastModDate = new Date(Long.parseLong(attributeMap.get("lastModifiedDate")));
    final boolean isDir = "true".equals(attributeMap.get("isDirectory"));
    Label myDateLabel = new Label(dateFormat.format(lastModDate));
    myDateLabel.setWordWrap(false);

    final Label myNameLabel = new Label(attributeMap.get(ACTUAL_FILE_NAME)) {
      public void onBrowserEvent(Event event) {
        handleFileClicked(item, isDir, event, this.getElement());
      }
    };
    myNameLabel.getElement().setAttribute("id", attributeMap.get("name"));
    myNameLabel.sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
    myNameLabel.setWordWrap(false);
    myNameLabel.setTitle(getTitle(item));
    if (showLocalizedFileNames) {
      myNameLabel.setText(attributeMap.get(LOCALIZED_FILE_NAME));
    }

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
      FileChooserImages.images.file().applyTo(fileImage);
    }
    fileNamePanel.add(fileImage);
    fileNamePanel.add(myNameLabel);
    DOM.setStyleAttribute(myNameLabel.getElement(), "cursor", "default");

    filesListTable.setWidget(row + 1, 0, fileNamePanel);
    filesListTable.setWidget(row + 1, 1, new Label(isDir ? "Folder" : "File"));
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
      HashMap<String, Object> attributeMap = (HashMap<String, Object>) item.getUserObject();
      TreeItem originalItem = (TreeItem) attributeMap.get("original");
      TreeItem tmpItem = originalItem;
      if (originalItem == null) {
        tmpItem = item;
      }
      selectedTreeItem = tmpItem;

      List<String> parentSegments = new ArrayList<String>();
      while (tmpItem != null) {
        HashMap<String, Object> tmpAttributeMap = (HashMap<String, Object>) tmpItem.getUserObject();
        if (tmpAttributeMap.get(ACTUAL_FILE_NAME) != null) {
          parentSegments.add((String) tmpAttributeMap.get(ACTUAL_FILE_NAME));
        }
        tmpItem = tmpItem.getParentItem();
      }
      Collections.reverse(parentSegments);
      String myPath = "";
      for (int i = 0; isDir ? i < parentSegments.size() : i < parentSegments.size() - 1; i++) {
        myPath += "/" + parentSegments.get(i);
      }
      setSelectedPath(myPath);
      if (!isDir) {
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
        resetFileName();
        initUI(false);
      } else {
        fireFileSelected();
      }
    } else if ((DOM.eventGetType(event) & Event.ONCLICK) == Event.ONCLICK) {
      fireFileSelectionChanged();
      // single click
      // highlight row
      if (lastSelectedFileElement != null) {
        DOM.setStyleAttribute(lastSelectedFileElement, "background", "white");
        DOM.setStyleAttribute(lastSelectedFileElement, "color", "black");
      }
      DOM.setStyleAttribute(sourceElement, "background", "#7070ff");
      DOM.setStyleAttribute(sourceElement, "color", "white");
      lastSelectedFileElement = sourceElement;
    }
  }

  private void setFileSelected(boolean selected) {
    fileSelected = selected;
  }
  
  public boolean isFileSelected() {
    return fileSelected;
  }
  
  private String getTitle(TreeItem item) {
    List<String> parentSegments = new ArrayList<String>();
    while (item != null) {
      HashMap<String, Object> tmpAttributeMap = (HashMap<String, Object>) item.getUserObject();
      if (tmpAttributeMap.get(ACTUAL_FILE_NAME) != null) {
        parentSegments.add((String) tmpAttributeMap.get(ACTUAL_FILE_NAME));
      }
      TreeItem originalItem = (TreeItem) tmpAttributeMap.get("original");
      if (originalItem != null) {
        item = originalItem.getParentItem();
      } else {
        item = item.getParentItem();
      }
    }
    Collections.reverse(parentSegments);
    String myPath = "";
    for (int i = 0; i < parentSegments.size(); i++) {
      String pathSegment = parentSegments.get(i);
      myPath += "/" + pathSegment;
    }
    if (myPath.equals("")) {
      myPath = "/";
    }
    return myPath;
  }

  public TreeItem getTreeItem(List<String> pathSegments) {
    // find the tree node whose location matches the pathSegment paths
    TreeItem selectedItem = repositoryTree.getItem(0);
    for (String segment : pathSegments) {
      for (int i = 0; i < selectedItem.getChildCount(); i++) {
        TreeItem item = selectedItem.getChild(i);
        HashMap<String, String> attributeMap = (HashMap<String, String>) item.getUserObject();
        if (segment.equals(attributeMap.get(ACTUAL_FILE_NAME))) {
          selectedItem = item;
        }
      }
    }

    HashMap<String, Object> attributeMap = (HashMap<String, Object>) selectedItem.getUserObject();
    TreeItem originalItem = (TreeItem) attributeMap.get("original");
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
    repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames);
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
    if (getSelectedPath().indexOf("/", 1) == -1) {
      return getSelectedPath().substring(1);
    } else {
      return getSelectedPath().substring(1, getSelectedPath().indexOf("/", 1));
    }
  }

  public String getPath() {
    int startIdx = getSelectedPath().indexOf("/", 1);
    if (-1 == startIdx) {
      return "";
    } else {
      return "/" + getSelectedPath().substring(startIdx + 1);
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
    if (!"".equals(name)) {
      name = "/" + name;
    }
    return getSolution() + getPath() + name;
  }

  public void addFileChooserListener(FileChooserListener listener) {
    listeners.add(listener);
  }

  public void removeFileChooserListener(FileChooserListener listener) {
    listeners.remove(listener);
  }

  public boolean doesSelectedFileExist() {
    String path = "/" + getFullPath();
    // find the selected item from the list
    List<String> pathSegments = new ArrayList<String>();
    if (path != null) {
      int index = path.indexOf("/", 0);
      while (index >= 0) {
        int oldIndex = index;
        index = path.indexOf("/", oldIndex + 1);
        if (index >= 0) {
          pathSegments.add(path.substring(oldIndex + 1, index));
        }
      }
      pathSegments.add(path.substring(path.lastIndexOf("/") + 1));
    }
    TreeItem treeItem = getTreeItem(pathSegments);
    if (treeItem != null) {
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
  
  /*
   * When called, we want to clear the file name textbox 
   * and also reset any reference to previous file name string. 
   */
  private void resetFileName() {
    fileNameTextBox.setText("");
    actualFileName = "";
  }
}
