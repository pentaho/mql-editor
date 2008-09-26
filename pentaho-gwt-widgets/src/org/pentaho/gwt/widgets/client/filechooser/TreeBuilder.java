package org.pentaho.gwt.widgets.client.filechooser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.pentaho.gwt.widgets.client.utils.ElementUtils;

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

        //ElementUtils.preventTextSelection(childTreeItem.getElement());
        
        HashMap<String, Object> attributeMap = new HashMap<String, Object>();
        attributeMap.put("name", childElement.getAttribute("name"));
        attributeMap.put("localized-name", childElement.getAttribute("localized-name"));
        attributeMap.put("description", childElement.getAttribute("description"));
        attributeMap.put("lastModifiedDate", childElement.getAttribute("lastModifiedDate"));
        attributeMap.put("visible", childElement.getAttribute("visible"));
        attributeMap.put("isDirectory", childElement.getAttribute("isDirectory"));
        childTreeItem.setUserObject(attributeMap);

        // find the spot in the parentTreeItem to insert the node (based on showLocalizedFileNames)
        if (parentTreeItem.getChildCount() == 0) {
          parentTreeItem.addItem(childTreeItem);
        } else {
          // this does sorting
          boolean inserted = false;
          for (int j = 0; j < parentTreeItem.getChildCount(); j++) {
            TreeItem kid = (TreeItem) parentTreeItem.getChild(j);
            if (showLocalizedFileNames) {
              if (childTreeItem.getText().compareTo(kid.getText()) <= 0) {
                // leave all items ahead of the insert point
                // remove all items between the insert point and the end
                // add the new item
                // add back all removed items
                List<TreeItem> removedItems = new ArrayList<TreeItem>();
                for (int x = j; x < parentTreeItem.getChildCount(); x++) {
                  TreeItem removedItem = (TreeItem) parentTreeItem.getChild(x);
                  removedItems.add(removedItem);
                }
                for (TreeItem removedItem : removedItems) {
                  parentTreeItem.removeItem(removedItem);
                }
                parentTreeItem.addItem(childTreeItem);
                inserted = true;
                for (TreeItem removedItem : removedItems) {
                  parentTreeItem.addItem(removedItem);
                }
                break;
              }
            } else {
              parentTreeItem.addItem(childTreeItem);
            }
          }
          if (!inserted) {
            parentTreeItem.addItem(childTreeItem);
          }
        }

        boolean isDirectory = "true".equals(childElement.getAttribute("isDirectory"));
        if (isDirectory) {
          buildSolutionTree(childTreeItem, childElement, showHiddenFiles, showLocalizedFileNames);
        }
      }
    }
  }

}
