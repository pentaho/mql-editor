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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.pentaho.pms.mql.OrderBy;
import org.pentaho.pms.mql.WhereCondition;

/**
 * Abstract table viewer used for viewing and modifying the inputs and outputs of an action definition element.
 * 
 * @author Angelo Rodriguez
 */
public class NewMQLOrderTable extends Composite {

  static final String ORDER_LABEL = Messages.getString("MQLOrderTableModel.SORT_ORDER"); //$NON-NLS-1$
  static final String COLUMN_LABEL = Messages.getString("MQLOrderTableModel.SORT_COLUMN"); //$NON-NLS-1$
  static final String TABLE_LABEL = Messages.getString("MQLOrderTableModel.SORT_TABLE"); //$NON-NLS-1$

  static final String ORDER_PROP = "Order"; //$NON-NLS-1$
  static final String COLUMN_PROP = "Column"; //$NON-NLS-1$
  static final String TABLE_PROP = "Table"; //$NON-NLS-1$

  static final String ASCENDING_ORDER = Messages.getString("MQLOrderTableModel.ASCENDING_SORT_ORDER"); //$NON-NLS-1$
  static final String DESCENDING_ORDER = Messages.getString("MQLOrderTableModel.DESCENDING_SORT_ORDER"); //$NON-NLS-1$

  static final String[] ORDER_ITEMS = new String[] { ASCENDING_ORDER, DESCENDING_ORDER };

  static final String LOCALE = Locale.getDefault().toString();

  List<OrderBy> orderList = new ArrayList();

  ComboBoxCellEditor directionCellEditor;

  Table table = null;

  // static final String NAME_COLUMN_PROP = "NAME"; //$NON-NLS-1$
  //
  // static final String TYPE_COLUMN_PROP = "TYPE"; //$NON-NLS-1$

  /**
   * Creates an viewer
   * 
   * @param parent
   *          the parent of this viewer.
   * @param toolkit
   *          the form toolkit.
   */
  public NewMQLOrderTable(Composite parent, int flags) {
    super(parent, flags);
    setLayout(new FillLayout());
    table = new Table(this, SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.BORDER);
    table.setHeaderVisible(true);
    createTableColumns();
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

  public String getColumnText(Object element, int columnIndex) {
    String columnText = ""; //$NON-NLS-1$
    if (element instanceof OrderBy) {
      OrderBy orderBy = (OrderBy) element;
      switch (columnIndex) {
      case 0:
        columnText = orderBy.getBusinessColumn().getBusinessTable().getDisplayName(LOCALE);
        break;
      case 1:
        columnText = orderBy.getBusinessColumn().getDisplayName(LOCALE);
        break;
      case 2:
        columnText = orderBy.isAscending() ? ASCENDING_ORDER : DESCENDING_ORDER;
        break;
      }
    }
    return columnText;
  }

  List editors = new ArrayList();

  public void loadOrders() {

    table.removeAll();
    for (int i = 0; i < editors.size(); i++) {
      if (editors.get(i) instanceof Widget) {
        Widget widget = (Widget) editors.get(i);
        widget.dispose();
      } else if (editors.get(i) instanceof TableEditor) {
        TableEditor editor = (TableEditor) editors.get(i);
        editor.dispose();
      }
    }
    for (int i = 0; i < table.getItemCount(); i++) {
      TableItem item = table.getItem(i);
      item.dispose();
    }

    editors.clear();
    for (int j = 0; j < orderList.size(); j++) {
      final OrderBy orderBy = orderList.get(j);
      TableItem tableItem = new TableItem(table, SWT.NONE);
      tableItem.setText(0, orderBy.getBusinessColumn().getBusinessTable().getDisplayName(LOCALE));
      tableItem.setText(1, orderBy.getBusinessColumn().getDisplayName(LOCALE));

      TableEditor editor = new TableEditor(table);
      final CCombo sortCombo = new CCombo(table, SWT.NONE);
      sortCombo.addSelectionListener(new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent arg0) {
        }

        public void widgetSelected(SelectionEvent arg0) {
          orderBy.setAscending(sortCombo.getSelectionIndex() == 0 ? true : false);
        }

      });
      sortCombo.setEditable(false);
      sortCombo.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
      sortCombo.add(ASCENDING_ORDER);
      sortCombo.add(DESCENDING_ORDER);
      sortCombo.select(orderBy.isAscending() ? 0 : 1);
      editor.grabHorizontal = true;
      editor.setEditor(sortCombo, tableItem, 2);
      editors.add(editor);
      editors.add(sortCombo);

      tableItem.setText(2, orderBy.isAscending() ? ASCENDING_ORDER : DESCENDING_ORDER);

    }
  }

  public void setOrderBy(OrderBy[] orderBys) {
    this.orderList.clear();
    for (OrderBy order : orderBys) {
      this.orderList.add(order);
    }
    loadOrders();
  }

  public OrderBy[] getOrderBy() {
    return (OrderBy[]) orderList.toArray(new OrderBy[0]);
  }

  public void add(OrderBy orderBy) {
    if (!orderList.contains(orderBy)) {
      orderList.add(orderBy);
      loadOrders();
    }
  }

  public void move(int fromRow, int toRow) {
    if ((fromRow < 0) || (fromRow >= orderList.size()) || (toRow < 0) || (toRow >= orderList.size())) {
      throw new IndexOutOfBoundsException();
    }

    if (fromRow != toRow) {
      OrderBy orderBy = (OrderBy) orderList.get(fromRow);
      orderList.remove(fromRow);
      if (toRow == orderList.size()) {
        add(orderBy);
      } else {
        orderList.add(toRow, orderBy);
      }
    }
    loadOrders();
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
      orderList.remove(((Integer) iterator.next()).intValue());
    }

    if (list.size() == 1) {
      int deletedRow = ((Integer) list.get(0)).intValue();
      while (deletedRow >= getTable().getItemCount()) {
        deletedRow--;
      }
      if (deletedRow >= 0) {
        getTable().select(deletedRow);
      }
    }
    loadOrders();
  }

  public void clear() {
    orderList.clear();
    loadOrders();
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }
}
