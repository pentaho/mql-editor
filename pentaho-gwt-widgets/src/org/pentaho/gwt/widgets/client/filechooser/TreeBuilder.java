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
import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class TreeBuilder {

  public static Tree buildSolutionTree(Document doc, boolean showHiddenFiles, boolean showLocalizedFileNames, FileFilter filter) {
    // build a tree structure to represent the document
    Tree repositoryTree = new Tree();
    // get document root item
    Element solutionRoot = doc.getDocumentElement();
    TreeItem rootItem = new TreeItem();
    rootItem.setText(solutionRoot.getAttribute("path")); //$NON-NLS-1$
    HashMap<String, Object> attributeMap = new HashMap<String, Object>();
    attributeMap.put("path", solutionRoot.getAttribute("path")); //$NON-NLS-1$ //$NON-NLS-2$
    rootItem.setUserObject(attributeMap);
    repositoryTree.addItem(rootItem);
    
    //default file filter that accepts anything
    if(filter == null){
      filter = new DefaultFileFilter();
    }
    buildSolutionTree(rootItem, solutionRoot, showHiddenFiles, showLocalizedFileNames, filter);
    return repositoryTree;
  }

  private static void buildSolutionTree(TreeItem parentTreeItem, Element parentElement, boolean showHiddenFiles, boolean showLocalizedFileNames, FileFilter filter) {
    NodeList children = parentElement.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Element childElement = (Element) children.item(i);
      boolean isVisible = "true".equals(childElement.getAttribute("visible")); //$NON-NLS-1$ //$NON-NLS-2$
      boolean isDirectory = "true".equals(childElement.getAttribute("isDirectory")); //$NON-NLS-1$ //$NON-NLS-2$
      
      if (isVisible || showHiddenFiles) {
        String fileName = childElement.getAttribute("name"); //$NON-NLS-1$
        if(filter.accept(fileName, isDirectory, isVisible) == false){
          continue;
        }
        
        String localizedName = childElement.getAttribute("localized-name"); //$NON-NLS-1$
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
        attributeMap.put("name", fileName); //$NON-NLS-1$ //$NON-NLS-2$
        attributeMap.put("localized-name", childElement.getAttribute("localized-name")); //$NON-NLS-1$ //$NON-NLS-2$
        attributeMap.put("description", childElement.getAttribute("description")); //$NON-NLS-1$ //$NON-NLS-2$
        attributeMap.put("lastModifiedDate", childElement.getAttribute("lastModifiedDate")); //$NON-NLS-1$ //$NON-NLS-2$
        attributeMap.put("visible", childElement.getAttribute("visible")); //$NON-NLS-1$ //$NON-NLS-2$
        attributeMap.put("isDirectory", childElement.getAttribute("isDirectory")); //$NON-NLS-1$ //$NON-NLS-2$
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

        if (isDirectory) {
          buildSolutionTree(childTreeItem, childElement, showHiddenFiles, showLocalizedFileNames, filter);
        }
      }
    }
  }

  private static class DefaultFileFilter implements FileFilter{
    public boolean accept(String name, boolean isDirectory, boolean isVisible) {
      return true;
    }
  }
}
