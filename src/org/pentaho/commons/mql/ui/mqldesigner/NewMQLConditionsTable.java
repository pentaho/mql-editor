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
import java.util.Comparator;
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
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.pentaho.pms.schema.WhereCondition;
import org.pentaho.pms.schema.concept.types.datatype.DataTypeSettings;

/**
 * Abstract table viewer used for viewing and modifying the inputs and outputs of an action
 * definition element.
 * 
 * @author Angelo Rodriguez
 */
public class NewMQLConditionsTable extends TableViewer implements IStructuredContentProvider, ITableLabelProvider, ICellModifier {

  static final String VALUE_LABEL = Messages.getString("MQLConditionsTableModel.COMPARE_VALUE"); //$NON-NLS-1$
  static final String LOGICAL_OPERATOR_LABEL = Messages.getString("MQLConditionsTableModel.LOGICAL_OPERATION"); //$NON-NLS-1$
  static final String COMPARISON_LABEL = Messages.getString("MQLConditionsTableModel.COMPARISON_OPERATION"); //$NON-NLS-1$
  static final String COLUMN_LABEL = Messages.getString("MQLConditionsTableModel.DB_COLUMN"); //$NON-NLS-1$
  static final String TABLE_LABEL = Messages.getString("MQLConditionsTableModel.DB_TABLE"); //$NON-NLS-1$
  
  static final String COMPARISON_PROP = "Comparison"; //$NON-NLS-1$
  static final String COLUMN_PROP = "Column"; //$NON-NLS-1$
  static final String TABLE_PROP = "Table"; //$NON-NLS-1$
  static final String LOGICAL_OP_PROP = "LogicalOp"; //$NON-NLS-1$
  static final String VALUE_PROP = "Value"; //$NON-NLS-1$
  
  static final String LOCALE = Locale.getDefault().toString();
  
  ArrayList conditions = new ArrayList();
  
  ComboBoxCellEditor comparisonEditor;
  ComboBoxCellEditor logicalOpEditor;
  TextCellEditor valueEditor;
  
  List listeners = new ArrayList();


  /** 
   * Creates an viewer
   * @param parent   the parent of this viewer.
   * @param toolkit  the form toolkit.
   */
  public NewMQLConditionsTable(Composite parent) {
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
    
    comparisonEditor = new ComboBoxCellEditor(table, WhereCondition.comparators, SWT.READ_ONLY);
    logicalOpEditor = new ComboBoxCellEditor(table, WhereCondition.operators, SWT.READ_ONLY);
    valueEditor = new TextCellEditor(table);
    
    CellEditor[] editors = new CellEditor[] {logicalOpEditor, null, null, comparisonEditor, valueEditor};
    setCellEditors(editors);
    setCellModifier(this);
    setColumnProperties(new String[] {LOGICAL_OP_PROP, TABLE_PROP, COLUMN_PROP, COMPARISON_PROP, VALUE_PROP});
  }

  /** 
   * Creates the table columns for this table viewer.
   */
  protected void createTableColumns() {
    Table table = getTable();
    TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(LOGICAL_OPERATOR_LABEL);
    tableColumn.setWidth(75);
    tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(TABLE_LABEL);
    tableColumn.setWidth(125);
    tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(COLUMN_LABEL);
    tableColumn.setWidth(125);
    tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(COMPARISON_LABEL);
    tableColumn.setWidth(100);
    tableColumn = new TableColumn(table, SWT.LEFT);
    tableColumn.setText(VALUE_LABEL);
    tableColumn.setWidth(100);
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.Viewer#inputChanged(java.lang.Object, java.lang.Object)
   */
  public void inputChanged(Viewer viewer, Object input, Object oldInput) {
    this.conditions.clear();
    if (input instanceof MQLWhereConditionModel[]) {
      this.conditions.addAll(Arrays.asList((MQLWhereConditionModel[])input));
    }
    super.inputChanged(input, oldInput);
  }
  
  public Image getColumnImage(Object element, int columnIndex) {
    return null;
  }

  public String getColumnText(Object element, int columnIndex) {
    String columnText = ""; //$NON-NLS-1$
    if (element instanceof MQLWhereConditionModel) {
      MQLWhereConditionModel condition = (MQLWhereConditionModel) element;
      switch (columnIndex) {
        case 0:
          if (conditions.indexOf(condition) != 0) {
            columnText = condition.getOperator();
          }
          break;
        case 1:
          columnText = condition.getField().getBusinessTable().getDisplayName(LOCALE);
          break;
        case 2:
          columnText = condition.getField().getDisplayName(LOCALE);
          break;
        case 3:
          columnText = getConditionType(condition); 
          break;
        case 4:
          columnText = getConditionValue(condition);
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
  
  public void setConditions(MQLWhereConditionModel[] conditions) {
    setInput(conditions);
  }
  
  public MQLWhereConditionModel[] getConditions() {
    return (MQLWhereConditionModel[])conditions.toArray(new MQLWhereConditionModel[0]);
  }
  
  public void add(MQLWhereConditionModel condition) {
    if (!conditions.contains(condition)) {
      conditions.add(condition);
      super.add(condition);
    }
  }
  
  public void add(int row, MQLWhereConditionModel condition) {
    if (!conditions.contains(condition)) {
      conditions.add(row, condition);
      super.insert(condition, row);
    }
  }
  
  public void remove(int row) {
    if (conditions.size() > row) {
      MQLWhereConditionModel condition = (MQLWhereConditionModel)conditions.get(row);
      conditions.remove(row);
      super.remove(condition);
      if ((row == 0) && (conditions.size() > 0)) {
        condition = (MQLWhereConditionModel)conditions.get(0);
        condition.setOperator(null);
        refresh(condition);
      }
    }
  }
  
  public MQLWhereConditionModel getCondition(int row) {
    return (MQLWhereConditionModel)conditions.get(row);
  }
  
  public void move(int fromRow, int toRow) {
    if ((fromRow < 0)
        || (fromRow >= conditions.size())
        || (toRow < 0)
        || (toRow >= conditions.size()))
    {
      throw new IndexOutOfBoundsException();
    }
    
    if (fromRow != toRow) {
      if (fromRow == 0) {
        MQLWhereConditionModel whereCondition = (MQLWhereConditionModel)conditions.get(0);
        whereCondition.setOperator("AND"); //$NON-NLS-1$
        if (conditions.size() > 1) {
          whereCondition = (MQLWhereConditionModel)conditions.get(1);
          whereCondition.setOperator(null);
        }
      } else if (toRow == 0) {
        MQLWhereConditionModel whereCondition = (MQLWhereConditionModel)conditions.get(0);
        whereCondition.setOperator("AND"); //$NON-NLS-1$
        whereCondition = (MQLWhereConditionModel)conditions.get(fromRow);
        whereCondition.setOperator(null);
      }
      
      MQLWhereConditionModel condition = (MQLWhereConditionModel)conditions.get(fromRow);
      remove(fromRow);
      if (toRow == conditions.size()) {
        add(condition);
      } else {
        add(toRow, condition);
      }
    }
  }
  
  public void clear() {
    setSelection(new StructuredSelection());
    conditions.clear();
    refresh();
  }
  
  public Object[] getElements(Object arg0) {
    return getConditions();
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
    return COMPARISON_PROP.equals(property) 
      || (LOGICAL_OP_PROP.equals(property) && (conditions.indexOf(element) != 0))
      || VALUE_PROP.equals(property); 
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
   */
  public Object getValue(Object tableObject, String property) {
    MQLWhereConditionModel condition = (MQLWhereConditionModel)tableObject;
    Object value = null;
    if (COMPARISON_PROP.equals(property)) {
      value = new Integer(Arrays.asList(WhereCondition.comparators).indexOf(getConditionType(condition)));
    } else if (LOGICAL_OP_PROP.equals(property)) {
      value = new Integer(Arrays.asList(WhereCondition.operators).indexOf(condition.getOperator()));
    } else if (VALUE_PROP.equals(property)) {
      value = getConditionValue(condition);
    }
    return value;
  }

  public void modify(Object tableObject, String property, Object value) {
    TableItem tableItem = (TableItem)tableObject;
    MQLWhereConditionModel condition = (MQLWhereConditionModel)tableItem.getData();
    if (COMPARISON_PROP.equals(property)) {
      int index = ((Integer)value).intValue();
      String conditionString = WhereCondition.comparators[index];
      String conditionValue = getConditionValue(condition);
      if (conditionValue != null) {
        conditionString = conditionString  + conditionValue;
      }
      condition.setCondition(conditionString);
    } else if (LOGICAL_OP_PROP.equals(property)) {
      int index = ((Integer)value).intValue();
      condition.setOperator(WhereCondition.operators[index]);
    } else if (VALUE_PROP.equals(property)) {
      if (condition.getField().getDataType().getType() == DataTypeSettings.DATA_TYPE_STRING) {
        String stringValue = value.toString().trim();
        if (stringValue.length() == 0) {
          stringValue = "\"\"";
        } else if (stringValue.equals("\"")) {
          stringValue = "\\\"";
        } else {
          if (!stringValue.startsWith("\"")) {
            stringValue = "\"" + stringValue;
          }
          if (!stringValue.endsWith("\"")) {
            stringValue = stringValue + "\"";
          }
        }
        condition.setCondition(getConditionType(condition) + stringValue);
      } else if (value.toString().trim().length() > 0) {
        condition.setCondition(getConditionType(condition) + value.toString().trim());
      }
    }
    refresh(condition);
  }
  
  private String getConditionType(MQLWhereConditionModel whereCondition) {
    String value = null;
    TreeSet treeSet = new TreeSet(new Comparator() {
      public int compare(Object arg0, Object arg1) {
        int value = 0;
        String string0 = arg0.toString();
        String string1 = arg1.toString();
        if (string0.length() == string1.length()) {
          value = string0.compareTo(arg1.toString());
        } else {
          value = string0.length() > string1.length() ? -1 : 1;
        }
        return value;
      }
    });
    
    treeSet.addAll(Arrays.asList(WhereCondition.comparators));
    for (Iterator iter = treeSet.iterator(); iter.hasNext() && (value == null);) {
      String comparator = iter.next().toString().toUpperCase();
      String condition = whereCondition.getCondition().toUpperCase();
      if (condition.startsWith(comparator)) {
        value = comparator;
      }
    }
    return value;
  }
  
  private String getConditionValue(MQLWhereConditionModel whereCondition) {
    String value = null;
    String comparator = getConditionType(whereCondition);
    if (comparator != null){
      String condition = whereCondition.getCondition();
      if (condition.length() > comparator.length()) {
        value = condition.substring(comparator.length()).trim();
      }
    }
    return value;
  }
}
