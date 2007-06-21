/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 */
package org.pentaho.commons.mql.ui.mqldesigner;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.pentaho.pms.schema.BusinessCategory;
import org.pentaho.pms.schema.BusinessColumn;
import org.pentaho.pms.schema.BusinessModel;

/**
 * Tree viewer used for viewing and modifying the "actions" and "action-definition"
 * elements within an action sequence.
 * 
 * @author Angelo Rodriguez
 */
public class BusinessTablesTree extends TreeViewer implements ITreeContentProvider {

  private static Image FOLDER_IMAGE = null;
  private static Image RESOURCE_IMAGE = null;
  
  public class DefaultLabelProvider extends LabelProvider {

    /** 
     * Creates a label provider Tress which display action sequence contents.
     */
    public DefaultLabelProvider() {
      super();
    }

    public Image getImage(Object obj) {
      Image image = null;
      if (obj instanceof BusinessCategory) {
        if (FOLDER_IMAGE == null) {
          FOLDER_IMAGE = loadImageResource("folder.gif");
        }
        image = FOLDER_IMAGE;
      } else {
        if (RESOURCE_IMAGE == null) {
          RESOURCE_IMAGE = loadImageResource("resource.gif");
        }
        image = RESOURCE_IMAGE;
      }
      return image;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    public String getText(Object obj) {
      String text = obj.toString();
      if (obj instanceof BusinessCategory) {
        text = ((BusinessCategory) obj).getDisplayName("en_US"); //$NON-NLS-1$
      } else if (obj instanceof BusinessColumn) {
        text = ((BusinessColumn) obj).getDisplayName("en_US"); //$NON-NLS-1$
      }
      return text;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
     */
    public boolean isLabelProperty(Object obj, String property) {
      return true;
    }

  }

  public BusinessTablesTree(Composite parent, int style) {
    super(parent, style);
    init();
  }

  public BusinessTablesTree(Tree tree) {
    super(tree);
    init();
  }

  protected void init() {
    setContentProvider(this);
    setLabelProvider(new DefaultLabelProvider());
  }

  public Object[] getChildren(Object parent) {
    List list = ((BusinessCategory)parent).getBusinessColumns().getList();
    if (parent instanceof BusinessCategory) {
      list = ((BusinessCategory)parent).getBusinessColumns().getList();
    }
    return list == null ? new Object[0] : list.toArray();
  }

  public Object getParent(Object child) {
    Object parent = null;
    if (child instanceof BusinessColumn) {
      parent = ((BusinessColumn)child).getBusinessTable();
    }
    return parent;
  }

  public boolean hasChildren(Object node) {
    return (node instanceof BusinessCategory) 
      && (((BusinessCategory)node).getBusinessColumns().size() > 0);
  }  

  public Object[] getElements(Object inputElement) {
    BusinessModel businessModel = (BusinessModel)getInput();
    List businessCategories = null;
    if (businessModel != null) {
      businessCategories = businessModel.getRootCategory().getBusinessCategories().getList();
    }
    return businessCategories != null ? businessCategories.toArray() : new Object[0];
  }

  public void dispose() {
  }

  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
  }

  public BusinessColumn[] getSelectedBusinessColumns() {
   ArrayList businessColumns = new ArrayList();
    IStructuredSelection selection = (IStructuredSelection) getSelection();
    for (Iterator iter = selection.iterator(); iter.hasNext();) {
      Object selectedObject = iter.next();
      if (selectedObject instanceof BusinessColumn) {
        businessColumns.add(selectedObject);
      }
    }
    return (BusinessColumn[])businessColumns.toArray(new BusinessColumn[0]);
  }

  private Image loadImageResource(String name) {
    try {
      Image ret = null;
      InputStream is = BusinessTablesTree.class.getResourceAsStream(name);
      if (is != null) {
        ret = new Image(getTree().getDisplay(),is);
        is.close();
      }
      return ret;
    } catch (Exception e1) {
      return null;
    }
  }
}