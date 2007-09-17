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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.pentaho.pms.schema.BusinessColumn;

/**
 * Abstract table viewer used for viewing and modifying the inputs and outputs of an action
 * definition element.
 * 
 * @author Angelo Rodriguez
 */
public class NewMQLColumnsTable extends TableViewer implements IStructuredContentProvider, ITableLabelProvider {

  static final String COLUMN_LABEL = Messages.getString("MQLColumnsTableModel.DB_COLUMN"); //$NON-NLS-1$
  static final String TABLE_LABEL = Messages.getString("MQLColumnsTableModel.DB_TABLE"); //$NON-NLS-1$
  static final int BUSINESS_COLUMN_INDEX = 1;
  static final int BUSINESS_TABLE_INDEX = 0;

  ArrayList businessColumns = new ArrayList();
  String locale;
  
  List listeners = new ArrayList();
//  static final String NAME_COLUMN_PROP = "NAME"; //$NON-NLS-1$
//
//  static final String TYPE_COLUMN_PROP = "TYPE"; //$NON-NLS-1$


  public NewMQLColumnsTable(Composite parent) {
    this(parent, "en_US");
  }
  
  /** 
   * Creates an viewer
   * @param parent   the parent of this viewer.
   * @param toolkit  the form toolkit.
   */
  public NewMQLColumnsTable(Composite parent, String locale) {
    super(WidgetFactory.createTable(parent, SWT.FULL_SELECTION | SWT.BORDER));
    
    this.locale = locale;
    Table table = getTable();
    table.setHeaderVisible(true);
    createTableColumns();
    setContentProvider(this);
    setLabelProvider(this);
    createCellEditors();
  }

  /** 
   * Initializes this viewer with the appropriate cell editors.
   */
  protected void createCellEditors() {
    
  }

  /** 
   * Creates the table columns for this table viewer.
   */
  protected void createTableColumns() {
    Table table = getTable();
    TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(TABLE_LABEL);
    tableColumn.setWidth(200);
    tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(COLUMN_LABEL);
    tableColumn.setWidth(200);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
   */
  public void inputChanged(Viewer viewer, Object input, Object oldInput) {
    this.businessColumns.clear();
    if (input instanceof BusinessColumn[]) {
      this.businessColumns.addAll(Arrays.asList((BusinessColumn[])input));
    }
    super.inputChanged(input, oldInput);
  }
  
  public Image getColumnImage(Object element, int columnIndex) {
    return null;
  }

  public String getColumnText(Object element, int columnIndex) {
    String columnText = ""; //$NON-NLS-1$
    if (element instanceof BusinessColumn) {
      BusinessColumn businessColumn = (BusinessColumn) element;
      switch (columnIndex) {
        case BUSINESS_TABLE_INDEX:
          columnText = businessColumn.getBusinessTable().getDisplayName(locale); //$NON-NLS-1$
          break;
        case BUSINESS_COLUMN_INDEX:
          columnText = businessColumn.getDisplayName(locale); //$NON-NLS-1$
          break;
      }
    }
    return columnText;
  }

  public void addListener(ILabelProviderListener listener) {
    listeners.add(listener);
  }

  public void dispose() {
  }

  public boolean isLabelProperty(Object element, String property) {
    return true;
  }

  public void removeListener(ILabelProviderListener listener) {
    listeners.add(listener);
  }
  
  void setBusinessColumns(BusinessColumn[] businessColumns) {
    setInput(businessColumns);
  }
  
  public BusinessColumn[] getBusinessColumns() {
    return (BusinessColumn[])businessColumns.toArray(new BusinessColumn[0]);
  }
  
  public void add(BusinessColumn businessColumn) {
    if (!businessColumns.contains(businessColumn)) {
      businessColumns.add(businessColumn);
      super.add(businessColumn);
    }
  }
  
  public void add(int row, BusinessColumn businessColumn) {
    if (!businessColumns.contains(businessColumn)) {
      businessColumns.add(row, businessColumn);
      super.insert(businessColumn, row);
    }
  }
  
  public void remove(int row) {
    if (businessColumns.size() > row) {
      BusinessColumn businessColumn = (BusinessColumn)businessColumns.get(row);
      businessColumns.remove(row);
      super.remove(businessColumn);
    }
  }
  
  public BusinessColumn getBusinessColumn(int row) {
    return (BusinessColumn)businessColumns.get(row);
  }
  
  public void move(int fromRow, int toRow) {
    if ((fromRow < 0)
        || (fromRow >= businessColumns.size())
        || (toRow < 0)
        || (toRow >= businessColumns.size()))
    {
      throw new IndexOutOfBoundsException();
    }
    
    if (fromRow != toRow) {
      BusinessColumn businessColumn = (BusinessColumn)businessColumns.get(fromRow);
      remove(fromRow);
      if (toRow == businessColumns.size()) {
        add(businessColumn);
      } else {
        add(toRow, businessColumn);
      }
    }
  }
  
  public void clear() {
    setSelection(new StructuredSelection());
    businessColumns.clear();
    refresh();
  }
  
  public Object[] getElements(Object arg0) {
    return getBusinessColumns();
  }

  protected void removeSelectedRows() {
    int[] rows = getTable().getSelectionIndices();
    TreeSet set = new TreeSet();
    
    for (int i = 0; i < rows.length; i++) {
      set.add(new Integer(rows[i]));
    }
    
    ArrayList list = new ArrayList();
    for (Iterator iterator = set.iterator(); iterator.hasNext();) {
      if (list.size() == 0) {
        list.add(iterator.next());
      } else {
        list.add(0, iterator.next());
      }
    }
    
    for (Iterator iterator = list.iterator(); iterator.hasNext();) {
      remove(((Integer)iterator.next()).intValue());
    }
    
    if (list.size() == 1) {
      int deletedRow = ((Integer)list.get(0)).intValue();
      while (deletedRow >= getTable().getItemCount()) {
        deletedRow--;
      }
      if (deletedRow >= 0) {
        getTable().select(deletedRow);
      }
    }
  }
}
