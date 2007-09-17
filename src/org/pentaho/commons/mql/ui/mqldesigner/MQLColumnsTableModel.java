package org.pentaho.commons.mql.ui.mqldesigner;

import java.util.ArrayList;
import java.util.Arrays;
import org.eclipse.swt.graphics.Point;
import org.pentaho.pms.schema.BusinessColumn;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;

public class MQLColumnsTableModel extends KTableDefaultModel {

  static final String COLUMN_LABEL = Messages.getString("MQLColumnsTableModel.DB_COLUMN"); //$NON-NLS-1$
  static final String TABLE_LABEL = Messages.getString("MQLColumnsTableModel.DB_TABLE"); //$NON-NLS-1$
  static final int BUSINESS_COLUMN_INDEX = 1;
  static final int BUSINESS_TABLE_INDEX = 0;
  
  private final FixedCellRenderer m_fixedRenderer = new FixedCellRenderer(FixedCellRenderer.STYLE_FLAT);
  private final TextCellRenderer m_textRenderer = new TextCellRenderer(TextCellRenderer.INDICATION_FOCUS);
  ArrayList businessColumns = new ArrayList();
  String locale;
  
  public MQLColumnsTableModel() {
    this("en_US");
  } 
  
  public MQLColumnsTableModel(String locale) {
    this.locale = locale;
    initialize();
  }

  public Object doGetContentAt(int col, int row) {
    Object value = ""; //$NON-NLS-1$
    if ((row >= 0) && (row < getRowCount()) && (col >= 0) && (col < getColumnCount())) {
      if (row == 0) {
        if (col == BUSINESS_COLUMN_INDEX) {
          value = COLUMN_LABEL;
        } else if (col == BUSINESS_TABLE_INDEX) {
          value = TABLE_LABEL;
        }
      } else {
        BusinessColumn businessColumn = (BusinessColumn)businessColumns.get(row - getFixedRowCount());
        if (col == BUSINESS_COLUMN_INDEX) {
          value = businessColumn.getDisplayName(locale); 
        } else if (col == BUSINESS_TABLE_INDEX) {
          value = businessColumn.getBusinessTable().getDisplayName(locale); 
        }
      }
    }
    return value == null ? "" : value; //$NON-NLS-1$
  }

  public KTableCellEditor doGetCellEditor(int col, int row) {
    return null;
  }

  public void doSetContentAt(int col, int row, Object value) {
  }

  public int doGetRowCount() {
    return getFixedRowCount() + businessColumns.size();
  }

  public int getFixedHeaderRowCount() {
    return 1;
  }

  public int doGetColumnCount() {
    return 2 + getFixedHeaderColumnCount();
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
    return 100;
  }

  public int getInitialRowHeight(int row) {
    if (row == 0)
      return 22;
    return 18;
  }
  
  BusinessColumn[] getBusinessColumns() {
    return (BusinessColumn[])businessColumns.toArray(new BusinessColumn[0]);
  }
  
  void setBusinessColumns(BusinessColumn[] businessColumns) {
    clear();
    this.businessColumns.addAll(Arrays.asList(businessColumns));
  }
  
  protected void remove(int row) {
    if (businessColumns.size() > 0) {
      businessColumns.remove(row - getFixedHeaderRowCount());
    }
  }
  
  protected void add(BusinessColumn businessColumn) {
    if (!businessColumns.contains(businessColumn)) {
      businessColumns.add(businessColumn);
    }
  }
  
  protected void add(int row, BusinessColumn businessColumn) {
    if (!businessColumns.contains(businessColumn)) {
      businessColumns.add(row - getFixedHeaderRowCount(), businessColumn);
    }
  }
  
  protected void clear() {
    businessColumns.clear();
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
      Object movedObject = businessColumns.remove(fromRow - getFixedHeaderRowCount());
      if (toRow == getRowCount()) {
        businessColumns.add(movedObject);
      } else {
        businessColumns.add(toRow  - getFixedHeaderRowCount(), movedObject);
      }
    }
  }
  
  protected BusinessColumn getBusinessColumn(int row) {
    return (BusinessColumn)businessColumns.get(row);
  }
}
