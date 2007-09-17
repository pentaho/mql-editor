package org.pentaho.commons.mql.ui.mqldesigner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.pms.schema.BusinessColumn;

import de.kupzog.ktable.KTable;
import de.kupzog.ktable.SWTX;


public class MQLColumnsTable extends KTable {

  public MQLColumnsTable(Composite parent) {
    this(parent, "en_US"); //$NON-NLS-1$
  }
  
  public MQLColumnsTable(Composite parent, String locale) {
    super(parent, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL | SWTX.FILL_WITH_LASTCOL | SWT.MULTI);
    setModel(new MQLColumnsTableModel(locale));
    if (SWT.getPlatform().equals("win32")) { //$NON-NLS-1$

      // Cross

      Image crossCursor = SWTX.loadImageResource(getDisplay(), "/icons/cross_win32.gif"); //$NON-NLS-1$

      // Row Resize

      Image row_resizeCursor = SWTX.loadImageResource(getDisplay(), "/icons/row_resize_win32.gif"); //$NON-NLS-1$

      // Column Resize

      Image column_resizeCursor = SWTX.loadImageResource(getDisplay(), "/icons/column_resize_win32.gif"); //$NON-NLS-1$

      // we set the hotspot to the center, so calculate the number of pixels from hotspot to lower border:

      Rectangle crossBound = crossCursor.getBounds();
      Rectangle rowresizeBound = row_resizeCursor.getBounds();
      Rectangle columnresizeBound = column_resizeCursor.getBounds();

      Point crossSize = new Point(crossBound.width / 2, crossBound.height / 2);
      Point rowresizeSize = new Point(rowresizeBound.width / 2, rowresizeBound.height / 2);
      Point columnresizeSize = new Point(columnresizeBound.width / 2, columnresizeBound.height / 2);

      setDefaultCursor(new Cursor(getDisplay(), crossCursor.getImageData(), crossSize.x, crossSize.y), crossSize);
      setDefaultRowResizeCursor(new Cursor(getDisplay(), row_resizeCursor.getImageData(), rowresizeSize.x, rowresizeSize.y));
      setDefaultColumnResizeCursor(new Cursor(getDisplay(), column_resizeCursor.getImageData(), columnresizeSize.x, columnresizeSize.y));

    } else {

      // Cross

      Image crossCursor = SWTX.loadImageResource(getDisplay(), "/icons/cross.gif"); //$NON-NLS-1$
      Image crossCursor_mask = SWTX.loadImageResource(getDisplay(), "/icons/cross_mask.gif"); //$NON-NLS-1$

      // Row Resize

      Image row_resizeCursor = SWTX.loadImageResource(getDisplay(), "/icons/row_resize.gif"); //$NON-NLS-1$
      Image row_resizeCursor_mask = SWTX.loadImageResource(getDisplay(), "/icons/row_resize_mask.gif"); //$NON-NLS-1$

      // Column Resize

      Image column_resizeCursor = SWTX.loadImageResource(getDisplay(), "/icons/column_resize.gif"); //$NON-NLS-1$
      Image column_resizeCursor_mask = SWTX.loadImageResource(getDisplay(), "/icons/column_resize_mask.gif"); //$NON-NLS-1$

      // we set the hotspot to the center, so calculate the number of pixels from hotspot to lower border:

      Rectangle crossBound = crossCursor.getBounds();
      Rectangle rowresizeBound = row_resizeCursor.getBounds();
      Rectangle columnresizeBound = column_resizeCursor.getBounds();

      Point crossSize = new Point(crossBound.width / 2, crossBound.height / 2);
      Point rowresizeSize = new Point(rowresizeBound.width / 2, rowresizeBound.height / 2);
      Point columnresizeSize = new Point(columnresizeBound.width / 2, columnresizeBound.height / 2);

      setDefaultCursor(new Cursor(getDisplay(), crossCursor_mask.getImageData(), crossCursor.getImageData(), crossSize.x, crossSize.y), crossSize);
      setDefaultRowResizeCursor(new Cursor(getDisplay(), row_resizeCursor_mask.getImageData(), row_resizeCursor.getImageData(), rowresizeSize.x, rowresizeSize.y));
      setDefaultColumnResizeCursor(new Cursor(getDisplay(), column_resizeCursor_mask.getImageData(), column_resizeCursor.getImageData(), columnresizeSize.x, columnresizeSize.y));

    }
  }
  
  public void add(BusinessColumn businessColumn) {
    MQLColumnsTableModel mqlColumnsTableModel = (MQLColumnsTableModel)getModel();
    mqlColumnsTableModel.add(businessColumn);
    redraw();
  }
  
  public void add(int row, BusinessColumn businessColumn) {
    MQLColumnsTableModel mqlColumnsTableModel = (MQLColumnsTableModel)getModel();
    mqlColumnsTableModel.add(row, businessColumn);
    redraw();
  }
  
  public void remove(int row) {
    MQLColumnsTableModel mqlColumnsTableModel = (MQLColumnsTableModel)getModel();
    mqlColumnsTableModel.remove(row);
    redraw();
  }
  
  public void clear() {
    super.clearSelection();
    MQLColumnsTableModel mqlColumnsTableModel = (MQLColumnsTableModel)getModel();
    mqlColumnsTableModel.clear();
    redraw();
  }
  
  public void move(int fromRow, int toRow) {
    MQLColumnsTableModel mqlColumnsTableModel = (MQLColumnsTableModel)getModel();
    mqlColumnsTableModel.move(fromRow, toRow);
    redraw();
  }
  
  public BusinessColumn[] getBusinessColumns() {
    MQLColumnsTableModel mqlColumnsTableModel = (MQLColumnsTableModel)getModel();
    return mqlColumnsTableModel.getBusinessColumns();
  }
  
  public void setBusinessColumns(BusinessColumn[] businessColumns) {
    MQLColumnsTableModel mqlColumnsTableModel = (MQLColumnsTableModel)getModel();
    mqlColumnsTableModel.setBusinessColumns(businessColumns);
    redraw();
  }
  
  protected void removeSelectedRows() {
    int[] rows = getRowSelection();
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
  }
}
