package org.pentaho.gwt.widgets.client.filechooser;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class TreeBuilder {

  public static Tree buildSolutionTree(Document doc, boolean showHiddenFiles, boolean showLocalizedFileNames) {
    // build a tree structure to represent the document
    Tree repositoryTree = new Tree();
    // get document root item
    Element solutionRoot = doc.getDocumentElement();
    TreeItem rootItem = new TreeItem();
    rootItem.setText(solutionRoot.getAttribute("path"));
    HashMap<String, Object> attributeMap = new HashMap<String, Object>();
    attributeMap.put("path", solutionRoot.getAttribute("path"));
    rootItem.setUserObject(attributeMap);
    repositoryTree.addItem(rootItem);
    buildSolutionTree(rootItem, solutionRoot, showHiddenFiles, showLocalizedFileNames);
    return repositoryTree;
  }

  private static void buildSolutionTree(TreeItem parentTreeItem, Element parentElement, boolean showHiddenFiles, boolean showLocalizedFileNames) {
    NodeList children = parentElement.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Element childElement = (Element) children.item(i);
      boolean isVisible = "true".equals(childElement.getAttribute("visible"));
      if (isVisible || showHiddenFiles) {
        String fileName = childElement.getAttribute("name");
        String localizedName = childElement.getAttribute("localized-name");
        TreeItem childTreeItem = new TreeItem();
        if (showLocalizedFileNames) {
          childTreeItem.setText(localizedName);
          childTreeItem.setTitle(fileName);
        } else {
          childTreeItem.setText(fileName);
          childTreeItem.setTitle(localizedName);
        }

        HashMap<String, Object> attributeMap = new HashMap<String, Object>();
        attributeMap.put("name", childElement.getAttribute("name"));
        attributeMap.put("localized-name", childElement.getAttribute("localized-name"));
        attributeMap.put("description", childElement.getAttribute("description"));
        attributeMap.put("lastModifiedDate", childElement.getAttribute("lastModifiedDate"));
        attributeMap.put("visible", childElement.getAttribute("visible"));
        attributeMap.put("isDirectory", childElement.getAttribute("isDirectory"));
        childTreeItem.setUserObject(attributeMap);

        parentTreeItem.addItem(childTreeItem);
        boolean isDirectory = "true".equals(childElement.getAttribute("isDirectory"));
        if (isDirectory) {
          buildSolutionTree(childTreeItem, childElement, showHiddenFiles, showLocalizedFileNames);
        }
      }
    }
  }

}
