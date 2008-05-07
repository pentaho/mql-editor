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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.pentaho.pms.mql.WhereCondition;
import org.pentaho.pms.schema.concept.types.datatype.DataTypeSettings;

/**
 * Abstract table viewer used for viewing and modifying the inputs and outputs of an action definition element.
 * 
 */
public class NewMQLConditionsTable extends Composite {

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

  List<MQLWhereConditionModel> conditions = new ArrayList();

  Table table = null;

  public NewMQLConditionsTable(Composite parent, int flags) {
    super(parent, flags);
    setLayout(new FillLayout());
    table = new Table(this, SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.BORDER);
    table.setHeaderVisible(true);
    createTableColumns();
  }

  /**
   * Creates the table columns for this table viewer.
   */
  public void createTableColumns() {
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

  public void clear() {
    conditions.clear();
    loadConditions();
  }

  public Table getTable() {
    return table;
  }

  public MQLWhereConditionModel[] getConditions() {
    return (MQLWhereConditionModel[]) conditions.toArray(new MQLWhereConditionModel[] {});
  }

  public void setConditions(MQLWhereConditionModel[] conditions) {
    this.conditions.clear();
    for (MQLWhereConditionModel condition : conditions) {
      this.conditions.add(condition);
    }
    loadConditions();
  }

  List editors = new ArrayList();

  public void loadConditions() {
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
    for (int j = 0; j < conditions.size(); j++) {
      final MQLWhereConditionModel condition = (MQLWhereConditionModel) conditions.get(j);
      TableItem tableItem = new TableItem(table, SWT.NONE);

      TableEditor editor = new TableEditor(table);
      final CCombo operatorCombo = new CCombo(table, SWT.NONE);
      operatorCombo.addSelectionListener(new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent arg0) {
        }

        public void widgetSelected(SelectionEvent arg0) {
          condition.setOperator(operatorCombo.getItem(operatorCombo.getSelectionIndex()));
        }

      });
      operatorCombo.setEditable(false);
      operatorCombo.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
      for (int i = 0; i < WhereCondition.operators.length; i++) {
        operatorCombo.add(WhereCondition.operators[i]);
        if (condition.getOperator() != null && condition.getOperator().equals(WhereCondition.operators[i])) {
          operatorCombo.select(i);
        }
      }
      editor.grabHorizontal = true;
      editor.setEditor(operatorCombo, tableItem, 0);
      editors.add(editor);
      editors.add(operatorCombo);

      tableItem.setText(1, condition.getField().getBusinessTable().getDisplayName(Locale.getDefault().getDisplayName()));
      tableItem.setText(2, condition.getField().getDisplayName(Locale.getDefault().getDisplayName()));

      final Text valueText = new Text(table, SWT.NONE);
      final CCombo comparisonCombo = new CCombo(table, SWT.NONE);
      valueText.addModifyListener(new ModifyListener() {

        @Override
        public void modifyText(ModifyEvent arg0) {
          String conditionStr = getConditionType(condition);
          String value = valueText.getText();
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
            condition.setCondition(conditionStr + stringValue);
          } else if (value.toString().trim().length() > 0) {
            condition.setCondition(conditionStr + value.toString().trim());
          }
        }

      });

      editor = new TableEditor(table);
      comparisonCombo.setEditable(false);
      comparisonCombo.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
      for (int i = 0; i < WhereCondition.comparators.length; i++) {
        comparisonCombo.add(WhereCondition.comparators[i]);
        String conditionStr = getConditionType(condition);
        if (conditionStr != null && conditionStr.equals(WhereCondition.comparators[i])) {
          comparisonCombo.select(i);
        }
      }
      comparisonCombo.addSelectionListener(new SelectionListener() {

        public void widgetDefaultSelected(SelectionEvent arg0) {
        }

        public void widgetSelected(SelectionEvent arg0) {
          String value = valueText.getText();
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
            condition.setCondition(comparisonCombo.getItem(comparisonCombo.getSelectionIndex()) + stringValue);
          } else if (value.toString().trim().length() > 0) {
            condition.setCondition(comparisonCombo.getItem(comparisonCombo.getSelectionIndex()) + value.toString().trim());
          }
        }

      });
      editor.grabHorizontal = true;
      editor.setEditor(comparisonCombo, tableItem, 3);
      editors.add(editor);
      editors.add(comparisonCombo);

      valueText.setText(getConditionValue(condition));
      editor = new TableEditor(table);
      editor.grabHorizontal = true;
      editor.setEditor(valueText, tableItem, 4);
      editor = new TableEditor(table);
      editors.add(editor);
      editors.add(valueText);
    }
  }

  public void add(MQLWhereConditionModel condition) {
    conditions.add(condition);
    loadConditions();
  }

  public void move(int rowFrom, int rowTo) {
    MQLWhereConditionModel from = conditions.get(rowFrom);
    if (rowFrom == 0) {
      MQLWhereConditionModel whereCondition = (MQLWhereConditionModel) conditions.get(0);
      whereCondition.setOperator("AND"); //$NON-NLS-1$
      if (conditions.size() > 1) {
        whereCondition = (MQLWhereConditionModel) conditions.get(1);
        whereCondition.setOperator(null);
      }
    } else if (rowTo == 0) {
      MQLWhereConditionModel whereCondition = (MQLWhereConditionModel) conditions.get(0);
      whereCondition.setOperator("AND"); //$NON-NLS-1$
      whereCondition = (MQLWhereConditionModel) conditions.get(rowFrom);
      whereCondition.setOperator(null);
    }
    conditions.remove(rowFrom);
    conditions.add(rowTo, from);
    loadConditions();
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
      conditions.remove(((Integer) iterator.next()).intValue());
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
    loadConditions();
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
    if (comparator != null) {
      String condition = whereCondition.getCondition();
      if (condition.length() > comparator.length()) {
        value = condition.substring(comparator.length()).trim();
      }
    }
    return value;
  }

}
