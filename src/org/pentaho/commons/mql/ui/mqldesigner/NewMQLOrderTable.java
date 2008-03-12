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
import java.util.Locale;
import java.util.TreeSet;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
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
import org.eclipse.swt.widgets.TableItem;
import org.pentaho.pms.mql.OrderBy;

/**
 * Abstract table viewer used for viewing and modifying the inputs and outputs of an action
 * definition element.
 * 
 * @author Angelo Rodriguez
 */
public class NewMQLOrderTable extends TableViewer implements IStructuredContentProvider, ITableLabelProvider, ICellModifier {

  static final String ORDER_LABEL = Messages.getString("MQLOrderTableModel.SORT_ORDER"); //$NON-NLS-1$
  static final String COLUMN_LABEL = Messages.getString("MQLOrderTableModel.SORT_COLUMN"); //$NON-NLS-1$
  static final String TABLE_LABEL = Messages.getString("MQLOrderTableModel.SORT_TABLE"); //$NON-NLS-1$
  
  static final String ORDER_PROP = "Order"; //$NON-NLS-1$
  static final String COLUMN_PROP = "Column"; //$NON-NLS-1$
  static final String TABLE_PROP = "Table"; //$NON-NLS-1$
  
  static final String ASCENDING_ORDER = Messages.getString("MQLOrderTableModel.ASCENDING_SORT_ORDER"); //$NON-NLS-1$
  static final String DESCENDING_ORDER = Messages.getString("MQLOrderTableModel.DESCENDING_SORT_ORDER"); //$NON-NLS-1$
  
  static final String[] ORDER_ITEMS = new String[] {ASCENDING_ORDER, DESCENDING_ORDER};
  
  static final String LOCALE = Locale.getDefault().toString();
  
  ArrayList orderList = new ArrayList();
  
  ComboBoxCellEditor directionCellEditor;
  
  List listeners = new ArrayList();
//  static final String NAME_COLUMN_PROP = "NAME"; //$NON-NLS-1$
//
//  static final String TYPE_COLUMN_PROP = "TYPE"; //$NON-NLS-1$


  /** 
   * Creates an viewer
   * @param parent   the parent of this viewer.
   * @param toolkit  the form toolkit.
   */
  public NewMQLOrderTable(Composite parent) {
    super(WidgetFactory.createTable(parent, SWT.FULL_SELECTION | SWT.BORDER));
    
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
    Table table = getTable();
    
    directionCellEditor = new ComboBoxCellEditor(table, new String[] {ASCENDING_ORDER, DESCENDING_ORDER}, SWT.READ_ONLY);
    
    CellEditor[] editors = new CellEditor[] {directionCellEditor, directionCellEditor, directionCellEditor};
    setCellEditors(editors);
    setCellModifier(this);
    setColumnProperties(new String[] { TABLE_PROP, COLUMN_PROP, ORDER_PROP});
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
    tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(ORDER_LABEL);
    tableColumn.setWidth(200);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
   */
  public void inputChanged(Viewer viewer, Object input, Object oldInput) {
    this.orderList.clear();
    if (input instanceof OrderBy[]) {
      this.orderList.addAll(Arrays.asList((OrderBy[])input));
    }
    super.inputChanged(input, oldInput);
  }
  
  public Image getColumnImage(Object element, int columnIndex) {
    return null;
  }

  public String getColumnText(Object element, int columnIndex) {
    String columnText = ""; //$NON-NLS-1$
    if (element instanceof OrderBy) {
      OrderBy orderBy = (OrderBy) element;
      switch (columnIndex) {
        case 0:
          columnText = orderBy.getSelection().getBusinessColumn().getBusinessTable().getDisplayName(LOCALE);
          break;
        case 1:
          columnText = orderBy.getSelection().getBusinessColumn().getDisplayName(LOCALE);
          break;
        case 2:
          columnText = orderBy.isAscending() ? ASCENDING_ORDER : DESCENDING_ORDER;
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
  
  public void setOrderBy(OrderBy[] orderBy) {
    setInput(orderBy);
  }
  
  public OrderBy[] getOrderBy() {
    return (OrderBy[])orderList.toArray(new OrderBy[0]);
  }
  
  public void add(OrderBy orderBy) {
    if (!orderList.contains(orderBy)) {
      orderList.add(orderBy);
      super.add(orderBy);
    }
  }
  
  public void add(int row, OrderBy orderBy) {
    if (!orderList.contains(orderBy)) {
      orderList.add(row, orderBy);
      super.insert(orderBy, row);
    }
  }
  
  public void remove(int row) {
    if (orderList.size() > row) {
      OrderBy orderBy = (OrderBy)orderList.get(row);
      orderList.remove(row);
      super.remove(orderBy);
    }
  }
  
  public OrderBy getOrderBy(int row) {
    return (OrderBy)orderList.get(row);
  }
  
  public void move(int fromRow, int toRow) {
    if ((fromRow < 0)
        || (fromRow >= orderList.size())
        || (toRow < 0)
        || (toRow >= orderList.size()))
    {
      throw new IndexOutOfBoundsException();
    }
    
    if (fromRow != toRow) {
      OrderBy orderBy = (OrderBy)orderList.get(fromRow);
      remove(fromRow);
      if (toRow == orderList.size()) {
        add(orderBy);
      } else {
        add(toRow, orderBy);
      }
    }
  }
  
  public void clear() {
    setSelection(new StructuredSelection());
    orderList.clear();
    refresh();
  }
  
  public Object[] getElements(Object arg0) {
    return getOrderBy();
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

  public boolean canModify(Object element, String property) {
    return property.equals(ORDER_PROP);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
   */
  public Object getValue(Object tableObject, String property) {
    OrderBy orderBy = (OrderBy)tableObject;
    String direction = orderBy.isAscending() ? ASCENDING_ORDER : DESCENDING_ORDER;
    return new Integer(Arrays.asList(ORDER_ITEMS).indexOf(direction));
  }

  public void modify(Object tableObject, String property, Object value) {
    TableItem tableItem = (TableItem)tableObject;
    OrderBy orderBy = (OrderBy)tableItem.getData();
    int index = ((Integer)value).intValue();
    orderBy.setAscending(!DESCENDING_ORDER.equals(ORDER_ITEMS[index]));
    refresh(orderBy);
  }
}
