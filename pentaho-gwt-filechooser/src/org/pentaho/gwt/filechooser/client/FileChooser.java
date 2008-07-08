package org.pentaho.gwt.filechooser.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.pentaho.gwt.filechooser.client.dialogs.IDialogCallback;
import org.pentaho.gwt.filechooser.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.filechooser.client.images.FileChooserImagesSingleton;

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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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

  public static final int OPEN = 0;
  public static final int SAVE = 1;

  int mode = OPEN;
  String selectedPath;
  String previousPath;

  ListBox navigationListBox;
  Tree repositoryTree;
  TreeItem selectedTreeItem;
  boolean showHiddenFiles = false;
  boolean showLocalizedFileNames = false;
  com.google.gwt.user.client.Element lastSelectedFileElement;
  TextBox fileNameTextBox = new TextBox();
  DateTimeFormat dateFormat = DateTimeFormat.getShortDateTimeFormat();
  Document solutionRepositoryDocument;

  ArrayList<FileChooserListener> listeners = new ArrayList<FileChooserListener>();

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
    setSpacing(2);
    DOM.setStyleAttribute(getElement(), "background", "#ffffff");
    DOM.setStyleAttribute(getElement(), "border", "1px solid #707070");
  }

  public FileChooser(int mode, String selectedPath, Document solutionRepositoryDocument) {
    this();
    this.mode = mode;
    this.selectedPath = selectedPath;
    this.solutionRepositoryDocument = solutionRepositoryDocument;
    repositoryTree = TreeBuilder.buildSolutionTree(solutionRepositoryDocument, showHiddenFiles, showLocalizedFileNames);
    initUI(false);
  }

  public FileChooser(int mode, String selectedPath) {
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
     RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
     "/pentaho/SolutionRepositoryService?component=getSolutionRepositoryDoc&filter=*.xaction,*.url");
    // RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
    // "http://localhost:8080/pentaho/SolutionRepositoryService?component=getSolutionRepositoryDoc&userid=joe&password=password");

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
    String name = element.getAttribute("name");
    String localizedName = element.getAttribute("localized-name");
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
        setSelectedPath(navigationListBox.getItemText(navigationListBox.getSelectedIndex()));
        initUI(false);
      }
    });

    if (!fromSearch) {
      selectedTreeItem = getTreeItem(pathSegments);
    }

    clear();

    VerticalPanel locationBar = new VerticalPanel();
    locationBar.add(new Label("Location:"));

    HorizontalPanel navigationBar = new HorizontalPanel();

    final Image searchImage = new Image();
    FileChooserImagesSingleton.getImages().search().applyTo(searchImage);
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
        for (int i=0;i<documentElement.getChildNodes().getLength();i++) {
          buildOracleValues(oracleValues, (Element)documentElement.getChildNodes().item(i));
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
            attributeMap.put("name", "Search Results");
            attributeMap.put("localized-name", "Search Results");
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
        PromptDialogBox searchDialog = new PromptDialogBox("Search", suggestTextBox, "OK", "Cancel", callback, false, true);
        searchDialog.setFocusWidget(searchTextBox);
        searchDialog.center();
      }
    });

    final Image upDirImage = new Image();
    FileChooserImagesSingleton.getImages().up().applyTo(upDirImage);
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
          if (attributeMap.get("name") != null) {
            parentSegments.add((String) attributeMap.get("name"));
          }
          tmpItem = tmpItem.getParentItem();
        }
        Collections.reverse(parentSegments);
        String myPath = "";
        for (int i = 0; i < parentSegments.size() - 1; i++) {
          String pathSegment = parentSegments.get(i);
          myPath += "/" + pathSegment;
        }
        if (myPath.equals("")) {
          myPath = "/";
        }
        setSelectedPath(myPath);
        initUI(false);

      }
    });
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
    navigationBar.add(navigationListBox);
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    navigationBar.add(searchImage);
    navigationBar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    navigationBar.add(upDirImage);
    navigationBar.setWidth("100%");

    locationBar.add(navigationBar);
    locationBar.setWidth("100%");

    add(locationBar);
    add(buildFilesList(selectedTreeItem));
    HorizontalPanel fileNamePanel = new HorizontalPanel();
    fileNamePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    fileNamePanel.add(new Label("Filename:"));
    fileNameTextBox.setWidth("300px");
    fileNamePanel.add(fileNameTextBox);
    add(fileNamePanel);
    setWidth("400px");
  }

  public void findMatchingTreeItems(TreeItem rootItem, TreeItem parentItem, String searchText) {
    searchText = searchText.toLowerCase();
    for (int i = 0; i < parentItem.getChildCount(); i++) {
      final TreeItem childItem = parentItem.getChild(i);
      HashMap<String, Object> attributeMap = (HashMap<String, Object>) childItem.getUserObject();
      final boolean isDir = "true".equals(attributeMap.get("isDirectory"));
      String name = ((String) attributeMap.get("name")).toLowerCase();
      String localizedName = ((String) attributeMap.get("localized-name")).toLowerCase();
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
    FlexTable filesListTable = new FlexTable();
    filesListTable.setCellSpacing(0);
    Label nameLabel = new Label("Name");
    Label typeLabel = new Label("Type");
    Label dateLabel = new Label("Date Modified");

    filesListTable.setWidget(0, 0, nameLabel);
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

    Label myNameLabel = new Label(attributeMap.get("name")) {
      public void onBrowserEvent(Event event) {
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
          if (tmpAttributeMap.get("name") != null) {
            parentSegments.add((String) tmpAttributeMap.get("name"));
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
          if (tmpAttributeMap.get("name") != null) {
            fileNameTextBox.setText((String) tmpAttributeMap.get("name"));
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
          // single click
          // highlight row
          if (lastSelectedFileElement != null) {
            DOM.setStyleAttribute(lastSelectedFileElement, "background", "white");
            DOM.setStyleAttribute(lastSelectedFileElement, "color", "black");
          }
          DOM.setStyleAttribute(getElement(), "background", "#7070ff");
          DOM.setStyleAttribute(getElement(), "color", "white");
          lastSelectedFileElement = getElement();
        }
      }
    };
    myNameLabel.sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
    myNameLabel.setWordWrap(false);
    myNameLabel.setTitle(getTitle(item));

    HorizontalPanel fileNamePanel = new HorizontalPanel();
    Image fileImage = new Image();
    if (isDir) {
      FileChooserImagesSingleton.getImages().folder().applyTo(fileImage);
    } else {
      FileChooserImagesSingleton.getImages().file().applyTo(fileImage);
    }
    fileNamePanel.add(fileImage);
    fileNamePanel.add(myNameLabel);
    DOM.setStyleAttribute(myNameLabel.getElement(), "cursor", "default");

    filesListTable.setWidget(row + 1, 0, fileNamePanel);
    filesListTable.setWidget(row + 1, 1, new Label(isDir ? "Folder" : "File"));
    filesListTable.setWidget(row + 1, 2, myDateLabel);
    filesListTable.getCellFormatter().setWidth(row + 1, 0, "100px");
    filesListTable.getCellFormatter().setWidth(row + 1, 1, "100px");
    filesListTable.getCellFormatter().setWidth(row + 1, 2, "100px");
  }

  private String getTitle(TreeItem item) {
    List<String> parentSegments = new ArrayList<String>();
    while (item != null) {
      HashMap<String, Object> tmpAttributeMap = (HashMap<String, Object>) item.getUserObject();
      if (tmpAttributeMap.get("name") != null) {
        parentSegments.add((String) tmpAttributeMap.get("name"));
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
        if (segment.equals(attributeMap.get("name"))) {
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

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
//    final FileChooserDialog dialogBox = new FileChooserDialog(FileChooser.SAVE, "/samples/reporting", true, true);
//    dialogBox.addFileChooserListener(new FileChooserListener() {
//      public void fileSelected(String path, String file) {
//        Window.alert("fileSelected: path=" + path + " file=" + file);
//        dialogBox.hide();
//      }
//    });
//    dialogBox.center();
  }

  public int getMode() {
    return mode;
  }

  public void setMode(int mode) {
    this.mode = mode;
  }

  public String getSelectedPath() {
    return selectedPath;
  }

  public void setSelectedPath(String selectedPath) {
    this.selectedPath = selectedPath;
  }

  public void fireFileSelected() {
    for (FileChooserListener listener : listeners) {
      listener.fileSelected(getSelectedPath(), fileNameTextBox.getText());
    }
  }

  public void addFileChooserListener(FileChooserListener listener) {
    listeners.add(listener);
  }

  public void removeFileChooserListener(FileChooserListener listener) {
    listeners.remove(listener);
  }

}
