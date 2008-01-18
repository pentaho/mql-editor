package org.pentaho.commons.mql.ui.mqldesigner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import org.eclipse.swt.graphics.Point;
import org.pentaho.pms.mql.OrderBy;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.editors.KTableCellEditorCombo;
import de.kupzog.ktable.editors.KTableCellEditorText;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

public class MQLOrderTableModel extends KTableDefaultModel {

  static final String ORDER_LABEL = Messages.getString("MQLOrderTableModel.SORT_ORDER"); //$NON-NLS-1$
  static final String COLUMN_LABEL = Messages.getString("MQLOrderTableModel.SORT_COLUMN"); //$NON-NLS-1$
  static final String TABLE_LABEL = Messages.getString("MQLOrderTableModel.SORT_TABLE"); //$NON-NLS-1$
  
  static final String ASCENDING_ORDER = Messages.getString("MQLOrderTableModel.ASCENDING_SORT_ORDER"); //$NON-NLS-1$
  static final String DESCENDING_ORDER = Messages.getString("MQLOrderTableModel.DESCENDING_SORT_ORDER"); //$NON-NLS-1$
  static final String[] ORDER = new String[]{ASCENDING_ORDER, DESCENDING_ORDER};
  
  private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT);
  private final TextCellRenderer m_textRenderer = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS);
  ArrayList orderList = new ArrayList();
  final KTableCellEditorText valueCellEditor = new KTableCellEditorText();
  static final String LOCALE = Locale.getDefault().toString();
  
  public MQLOrderTableModel() {
    initialize();
  }

  public Object doGetContentAt(int col, int row) {
    String value = null;
    if ((row >= 0) && (row < getRowCount()) && (col >= 0) && (col < getColumnCount())) {
      if (row == 0) {
        switch (col) {
          case 0:
            value = TABLE_LABEL;
            break;
          case 1:
            value = COLUMN_LABEL;
            break;
          case 2:
            value = ORDER_LABEL;
            break;
        }
      } else {
        OrderBy orderBy = (OrderBy)orderList.get(row - getFixedRowCount());
        switch (col) {
          case 0:
            value = orderBy.getBusinessColumn().getBusinessTable().getDisplayName(LOCALE);
            break;
          case 1:
            value = orderBy.getBusinessColumn().getDisplayName(LOCALE);
            break;
          case 2:
            value = orderBy.isAscending() ? ASCENDING_ORDER : DESCENDING_ORDER;
            break;
        }
      }
    }
    return value == null ? "" : value; //$NON-NLS-1$
  }

  public KTableCellEditor doGetCellEditor(int col, int row) {
    KTableCellEditor editor = null;
    if (col == 2) {
      editor = new KTableCellEditorCombo();
      ((KTableCellEditorCombo)editor).setItems(ORDER);
    }
    return editor;
  }
  
  public void doSetContentAt(int col, int row, Object value) {
    OrderBy orderBy = (OrderBy)orderList.get(row - getFixedHeaderRowCount());
    if ((col == 2) && (row >= getFixedHeaderRowCount())) {
      value = value.toString().trim().toUpperCase();
      orderBy.setAscending(!value.equals(DESCENDING_ORDER));
    }
  }

  public int doGetRowCount() {
    return getFixedRowCount() + orderList.size();
  }

  public int getFixedHeaderRowCount() {
    return 1;
  }

  public int doGetColumnCount() {
    return 3 + getFixedHeaderColumnCount();
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
      case 1: 
        width = 125;
        break;
      case 2:
        width = 100;
        break;
    }
    return width;
  }

  public int getInitialRowHeight(int row) {
    if (row == 0)
      return 22;
    return 18;
  }
  
  OrderBy[] getOrderList() {
    return (OrderBy[])orderList.toArray(new OrderBy[0]);
  }
  
  void setOrderList(OrderBy[] order) {
    clear();
    this.orderList.addAll(Arrays.asList(order));
  }
  
  protected void remove(int row) {
    orderList.remove(row - getFixedHeaderRowCount());
  }
  
  protected void add(OrderBy orderBy) {
    if (!orderList.contains(orderBy)) {
      orderList.add(orderBy);
    }
  }
  
  protected void add(int row, OrderBy orderBy) {
    if (!orderList.contains(orderBy)) {
      orderList.add(row - getFixedHeaderRowCount(), orderBy);
    }
  }
  
  protected void clear() {
    orderList.clear();
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
      Object movedObject = orderList.remove(fromRow - getFixedHeaderRowCount());
      if (toRow == getRowCount()) {
        orderList.add(movedObject);
      } else {
        orderList.add(toRow  - getFixedHeaderRowCount(), movedObject);
      }
    }
  }
  
  protected OrderBy getOrderBy(int row) {
    return (OrderBy)orderList.get(row);
  }
}
