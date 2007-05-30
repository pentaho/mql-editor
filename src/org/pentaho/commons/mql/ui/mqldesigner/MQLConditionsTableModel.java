package org.pentaho.commons.mql.ui.mqldesigner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import org.eclipse.swt.graphics.Point;
import org.pentaho.pms.schema.WhereCondition;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.editors.KTableCellEditorCombo;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

public class MQLConditionsTableModel extends KTableDefaultModel {

  static final String VALUE_LABEL = Messages.getString("MQLConditionsTableModel.COMPARE_VALUE"); //$NON-NLS-1$
  static final String LOGICAL_OPERATOR_LABEL = Messages.getString("MQLConditionsTableModel.LOGICAL_OPERATION"); //$NON-NLS-1$
  static final String COMPARISON_LABEL = Messages.getString("MQLConditionsTableModel.COMPARISON_OPERATION"); //$NON-NLS-1$
  static final String COLUMN_LABEL = Messages.getString("MQLConditionsTableModel.DB_COLUMN"); //$NON-NLS-1$
  static final String TABLE_LABEL = Messages.getString("MQLConditionsTableModel.DB_TABLE"); //$NON-NLS-1$
  static final String LOCALE = Locale.getDefault().toString();
  
  class ComparatorEditorCombo extends KTableCellEditorCombo {

    public void close(boolean save) {
      super.close(save);
      if (save) {
        m_Table.redraw(m_Col + 1, m_Row, 1, 1);
      }
    }    
  }
  
  private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT);
  private final TextCellRenderer m_textRenderer = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS);
  ArrayList conditions = new ArrayList();
  final KTableCellEditorText valueCellEditor = new KTableCellEditorText();
  
  public MQLConditionsTableModel() {
    initialize();
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
  
  public Object doGetContentAt(int col, int row) {
    String value = null;
    if ((row >= 0) && (row < getRowCount()) && (col >= 0) && (col < getColumnCount())) {
      if (row == 0) {
        switch (col) {
          case 0:
            value = LOGICAL_OPERATOR_LABEL;
            break;
          case 1:
            value = TABLE_LABEL;
            break;
          case 2:
            value = COLUMN_LABEL;
            break;
          case 3:
            value = COMPARISON_LABEL;
            break;
          case 4:
            value = VALUE_LABEL;
            break;
        }
      } else {
        MQLWhereConditionModel whereCondition = (MQLWhereConditionModel)conditions.get(row - getFixedRowCount());
        switch (col) {
          case 0:
            if (row > getFixedHeaderRowCount()) {
              value = whereCondition.getOperator();
            }
            break;
          case 1:
            value = whereCondition.getField().getBusinessTable().getDisplayName(LOCALE);
            break;
          case 2:
            value = whereCondition.getField().getDisplayName(LOCALE);
            break;
          case 3:
            value = getConditionType(whereCondition); 
            break;
          case 4:
            value = getConditionValue(whereCondition);
            break;
        }
      }
    }
    return value == null ? "" : value; //$NON-NLS-1$
  }

  public KTableCellEditor doGetCellEditor(int col, int row) {
    KTableCellEditor editor = null;
    if (col == 0) {
      if (row > getFixedHeaderRowCount()) {
        editor = new KTableCellEditorCombo();
        ((KTableCellEditorCombo)editor).setItems(WhereCondition.operators);
      }
    } else if (col == 3) {
      editor = new ComparatorEditorCombo();
      ((KTableCellEditorCombo)editor).setItems(WhereCondition.comparators);
    } else if (col == 4) {
      String conditional = (String)doGetContentAt(col - 1, row);
      if ((conditional != null) && (!Character.isLetter(conditional.charAt(0)) || conditional.equals("IN"))) { //$NON-NLS-1$
        editor = valueCellEditor;
      }
    }
    return editor;
  }
  
  public void doSetContentAt(int col, int row, Object value) {
    MQLWhereConditionModel whereCondition = (MQLWhereConditionModel)conditions.get(row - getFixedHeaderRowCount());
    switch (col) {
      case 0:
        if (row > getFixedHeaderRowCount()) {
          whereCondition.setOperator(value.toString());
        }
        break;
      case 3:
        if (Arrays.asList(WhereCondition.comparators).contains(value.toString())) {
          String conditionString = value.toString().trim().toUpperCase();
          if (!Character.isLetter(conditionString.charAt(0)) || conditionString.equals("IN")) { //$NON-NLS-1$
            String conditionValue = getConditionValue(whereCondition);
            if (conditionValue != null) {
              conditionString = conditionString  + conditionValue;
            }
          }
          whereCondition.setCondition(conditionString);
        }
        break;
      case 4:
        if (value.toString().trim().length() > 0) {
          whereCondition.setCondition(getConditionType(whereCondition) + value.toString().trim());
        }
        break;
    }
  }

  public int doGetRowCount() {
    return getFixedRowCount() + conditions.size();
  }

  public int getFixedHeaderRowCount() {
    return 1;
  }

  public int doGetColumnCount() {
    return 5 + getFixedHeaderColumnCount();
  }

  public int getFixedHeaderColumnCount() {
    return 0;
  }

  public int getFixedSelectableRowCount() {
    return 0;
  }

  public int getFixedSelectableColumnCount() {
    return 0;
  }

  public boolean isColumnResizable(int col) {
    return true;
  }

  public boolean isRowResizable(int row) {
    return false;
  }

  public int getRowHeightMinimum() {
    return 18;
  }

  public KTableCellRenderer doGetCellRenderer(int col, int row) {
    if (isFixedCell(col, row))
      return m_fixedRenderer;

    return m_textRenderer;
  }

  public Point doBelongsToCell(int col, int row) {
    return null;
  }

  public int getInitialColumnWidth(int column) {
    int width = 100;
    switch (column) {
      case 0:
        width = 75;
        break;
      case 1: 
      case 2:
        width = 125;
        break;
    }
    return width;
  }

  public int getInitialRowHeight(int row) {
    if (row == 0)
      return 22;
    return 18;
  }
  
  MQLWhereConditionModel[] getConditions() {
    return (MQLWhereConditionModel[])conditions.toArray(new MQLWhereConditionModel[0]);
  }
  
  void setConditions(MQLWhereConditionModel[] conditions) {
    clear();
    this.conditions.addAll(Arrays.asList(conditions));
  }
  
  protected void remove(int row) {
    conditions.remove(row - getFixedHeaderRowCount());
  }
  
  protected void add(MQLWhereConditionModel whereCondition) {
    if (!conditions.contains(whereCondition)) {
      conditions.add(whereCondition);
    }
  }
  
  protected void add(int row, MQLWhereConditionModel whereCondition) {
    if (!conditions.contains(whereCondition)) {
      conditions.add(row - getFixedHeaderRowCount(), whereCondition);
    }
  }
  
  protected void clear() {
    conditions.clear();
  }
  
  protected void move(int fromRow, int toRow) {
    if ((fromRow < getFixedHeaderRowCount())
        || (fromRow >= getRowCount())
        || (toRow < getFixedHeaderRowCount())
        || (toRow >= getRowCount()))
    {
      throw new IndexOutOfBoundsException();
    }
    
    if (fromRow != toRow) {
      if (fromRow == getFixedHeaderRowCount()) {
        MQLWhereConditionModel whereCondition = (MQLWhereConditionModel)conditions.get(0);
        whereCondition.setOperator("AND"); //$NON-NLS-1$
        if (conditions.size() > 1) {
          whereCondition = (MQLWhereConditionModel)conditions.get(1);
          whereCondition.setOperator(null);
        }
      } else if (toRow == getFixedHeaderRowCount()) {
        MQLWhereConditionModel whereCondition = (MQLWhereConditionModel)conditions.get(0);
        whereCondition.setOperator("AND"); //$NON-NLS-1$
        whereCondition = (MQLWhereConditionModel)conditions.get(fromRow - getFixedHeaderRowCount());
        whereCondition.setOperator(null);
      }
      Object movedObject = conditions.remove(fromRow - getFixedHeaderRowCount());
      if (toRow == getRowCount()) {
        conditions.add(movedObject);
      } else {
        conditions.add(toRow  - getFixedHeaderRowCount(), movedObject);
      }
    }
  }
  
  protected MQLWhereConditionModel getCondition(int row) {
    return (MQLWhereConditionModel)conditions.get(row);
  }
}
