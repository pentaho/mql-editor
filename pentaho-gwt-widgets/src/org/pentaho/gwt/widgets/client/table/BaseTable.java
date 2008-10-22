/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.gwt.widgets.client.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.pentaho.gwt.widgets.client.table.ColumnComparators.BaseColumnComparator;
import org.pentaho.gwt.widgets.client.table.ColumnComparators.ColumnComparatorTypes;

import com.google.gwt.dom.client.Node;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.ScrollTable;
import com.google.gwt.widgetideas.table.client.SelectionGrid;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.SortableGrid;
import com.google.gwt.widgetideas.table.client.TableSelectionListener;
import com.google.gwt.widgetideas.table.client.ScrollTable.ResizePolicy;
import com.google.gwt.widgetideas.table.client.ScrollTable.ScrollPolicy;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.widgetideas.table.client.SortableGrid.ColumnSorter;
import com.google.gwt.widgetideas.table.client.SortableGrid.ColumnSorterCallback;
import com.google.gwt.widgetideas.table.client.TableModel.ColumnSortList;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable.FlexCellFormatter;
import com.google.gwt.widgetideas.table.client.overrides.HTMLTable.CellFormatter;

/**
 * Core reusable, feature-rich table widget for displaying tabular data that is based on a composite
 * of a ScrollTable and a FixedWidthGrid.  Instance-specific functionality and sizing/styling 
 * should be implemented in a subclass. 
 * 
 * Usage Notes:
 *   - You MUST call the populateTable or populateTableWithSimpleMessage method AFTER adding the widget
 *     to the DOM tree, otherwise the columns will not resize correctly.
 *   - You may need to set the height to a non-percentage value to get the scrollbars to appear.
 *   
 * TODO refactor to minimize size
 */
public class BaseTable extends Composite {
  
  private static final String LOADING_MESSAGE = "Loading";
  
  public static final BaseColumnComparator DEFAULT_COLUMN_COMPARATOR = BaseColumnComparator
      .getInstance(ColumnComparatorTypes.STRING_NOCASE);
  
  private Panel parentPanel = new VerticalPanel();
  private ScrollTable scrollTable;
  private FixedWidthFlexTable tableHeader;
  private FixedWidthGrid dataGrid;
  private String[] tableHeaderNames;
  private String scrollTableWidth;
  private String scrollTableHeight;
  private int[] columnWidths;
  private int numberOfColumns;
  private SelectionPolicy selectionPolicy;

  private List<TableListener> doubleClickListeners = new ArrayList<TableListener>();
  private List<TableListener> tableListeners = new ArrayList<TableListener>();
  private List<TableSelectionListener> tableSelectionListeners = new ArrayList<TableSelectionListener>();
  private BaseColumnComparator[] columnComparators;
  
  /**
   * Simple constructor. 
   */
  public BaseTable(String[] tableHeaderNames, int[] columnWidths){
    this(tableHeaderNames, columnWidths, null);
  }

  /**
   * Simple constructor. 
   */
  public BaseTable(String[] tableHeaderNames, int[] columnWidths, BaseColumnComparator[] columnComparators){
    this(tableHeaderNames, columnWidths, columnComparators, null);
  }
  
  /**
   * Main constructor.
   * 
   * Note: For column width values, use -1 to not specify a column width.
   * Note: For column comparators individually, a null value will disable sorting for that column.  If you set 
   *         the columnComparators array to null, all columns will be populated with the default column comparator.
   */
  public BaseTable(String[] tableHeaderNames, int[] columnWidths, BaseColumnComparator[] columnComparators, 
      SelectionPolicy selectionPolicy){
    
    if (tableHeaderNames != null){
      this.tableHeaderNames = tableHeaderNames;
      this.columnWidths = columnWidths;
      this.numberOfColumns = tableHeaderNames.length;

      if (selectionPolicy == null){
        this.selectionPolicy = SelectionPolicy.DISABLED;
      }else{
        this.selectionPolicy = selectionPolicy;
      }
      
      // Set column comparators to default if columnComparators is null 
      if (columnComparators == null){
        this.columnComparators = new BaseColumnComparator[tableHeaderNames.length];
      
        for (int i = 0; i < this.columnComparators.length; i++){
          this.columnComparators[i] = DEFAULT_COLUMN_COMPARATOR;
        }
      }else{
        this.columnComparators = columnComparators;  
      }
    
      initWidget(parentPanel);
    
      createTableWithShortMessage(LOADING_MESSAGE);

      parentPanel.setWidth("100%"); //$NON-NLS-1$
      this.parentPanel.add(scrollTable);
      scrollTable.fillWidth();
    }else{
      System.err.println("ERROR: Must specify table header names.");
    }
  }
  
  /**
   * Creates a table with blank headers and a single row with a short message in it. 
   */
  private void createTableWithShortMessage(String message){
    String[] simpleMessageHeaderValues = new String[]{"&nbsp;", "&nbsp;"}; //$NON-NLS-1$ //$NON-NLS-2$
    int[] simpleMessageColumnWidths = new int[]{45, -1};
    String[][] simpleMessageRowAndColumnValues = new String[][]{{message, "&nbsp;"}}; //$NON-NLS-1$
    
    createTable(simpleMessageHeaderValues, simpleMessageColumnWidths, simpleMessageRowAndColumnValues, 
        ScrollTable.ResizePolicy.FILL_WIDTH, SelectionGrid.SelectionPolicy.DISABLED);
    
    scrollTable.setSortingEnabled(false);
    scrollTable.setScrollPolicy(ScrollPolicy.DISABLED);

    tableHeader.setStylePrimaryName("disabled"); //$NON-NLS-1$
    dataGrid.setStylePrimaryName("disabled"); //$NON-NLS-1$
  }
  
  /**
   * Creates a table with the given headers, column widths, and row/column values using the default
   * resize policy of RESIZE_POLICY_FIXED_WIDTH. 
   */
  private void createTable(String[] tableHeaderNames, int[] columnWidths, Object[][] rowAndColumnValues){
    createTable(tableHeaderNames, columnWidths, rowAndColumnValues, ScrollTable.ResizePolicy.FIXED_WIDTH, selectionPolicy);
  }
  
  /**
   * Creates a table with the given headers, column widths, row/column values, and resize policy. 
   */
  private void createTable(String[] tableHeaderNames, int[] columnWidths, Object[][] rowAndColumnValues, 
      ResizePolicy resizePolicy, SelectionPolicy selectionPolicy){
    
    createTableHeader(tableHeaderNames);
    createDataGrid(selectionPolicy);
    createScrollTable(resizePolicy);
    populateDataGrid(columnWidths, rowAndColumnValues);
  }
  
  /**
   * Creates and initializes the header for the table.
   */
  private void createTableHeader(String[] tableHeaderNames){
    tableHeader = new FixedWidthFlexTable();
    FlexCellFormatter cellFormatter = tableHeader.getFlexCellFormatter();
    
    // Set header values and disable text selection
    for (int i = 0; i < tableHeaderNames.length; i++){
      tableHeader.setHTML(0, i, tableHeaderNames[i]);
      
      cellFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_LEFT);
      
      disableTextSelectionForCell(cellFormatter.getElement(0, i));
    }
    
    tableHeader.setCellPadding(2);
    tableHeader.setCellSpacing(0);
    
    if (this.selectionPolicy == SelectionPolicy.DISABLED){
      tableHeader.setStylePrimaryName("disabled");
    }
  }
  
  /**
   * Creates and initializes the data grid. 
   */
  private void createDataGrid(SelectionPolicy selectionPolicy){
    dataGrid = new FixedWidthGrid(){
      @Override
      public void onBrowserEvent(Event event) {
        Element td = this.getEventTargetCell(event);
        if (td == null) return;
        Element tr = DOM.getParent(td);
        Element body = DOM.getParent(tr);
        int row = DOM.getChildIndex(body, tr);
        int column = DOM.getChildIndex(tr, td);
        
        switch (DOM.eventGetType(event)) {
          case Event.ONCLICK: {
            for (TableSelectionListener tableSelectionListener : tableSelectionListeners){
              tableSelectionListener.onRowsSelected(this, row, column);
            }
            break;
          }
          case Event.ONDBLCLICK: {
            for (TableListener doubleClickListener : doubleClickListeners){
              doubleClickListener.onCellClicked(this, row, column);
            }
            break;
          }
          default: {
            break;
          }
        }
        
        super.onBrowserEvent(event);
      }
    };
    
    // Set style
    dataGrid.setCellPadding(2);
    dataGrid.setCellSpacing(0);
    dataGrid.setSelectionPolicy(selectionPolicy);

    // Add table listeners
    for (TableListener listener : tableListeners){
      dataGrid.addTableListener(listener);
    }
    
    // Add table selection listeners
    for (TableSelectionListener listener : tableSelectionListeners){
      dataGrid.addTableSelectionListener(listener);
    }
    
    dataGrid.sinkEvents(Event.ONDBLCLICK | Event.ONCLICK);
    
    dataGrid.setColumnSorter(new BaseTableColumnSorter());
    
    if (this.selectionPolicy == SelectionPolicy.DISABLED){
      dataGrid.setStylePrimaryName("disabled");
    }
  }
  
  /**
   * Creates and initializes the scroll table. 
   */
  private void createScrollTable(ResizePolicy resizePolicy){
    scrollTable = new ScrollTable(dataGrid, tableHeader, (BaseTableImages)GWT.create(BaseTableImages.class));

    scrollTable.setResizePolicy(resizePolicy);

    // Set style
    if (scrollTableWidth != null){
      scrollTable.setWidth(scrollTableWidth);
    }else{
      scrollTable.setWidth("100%"); //$NON-NLS-1$  
    }
    if (scrollTableHeight != null){
      scrollTable.setHeight(scrollTableHeight);
    }
    
    // Set column comparators
    for (int i = 0; i < columnComparators.length; i++){
      if (columnComparators[i] != null){
        scrollTable.setColumnSortable(i, true);
      }else{
        scrollTable.setColumnSortable(i, false);
      }
    }
  }
  
  /**
   * Populates the data grid with data then sets the column widths. 
   */
  private void populateDataGrid(int[] columnWidths, Object[][] rowAndColumnValues){
    CellFormatter cellFormatter;
    
    // If values are invalid, create a blank/disabled row so that table headers will resize properly
    // then set the style for this case
    if (!isRowAndColumnValuesValid(rowAndColumnValues)){
      rowAndColumnValues = new String[1][tableHeaderNames.length];
      
      for (int i = 0; i < tableHeaderNames.length; i++){
        rowAndColumnValues[0][i] = "&nbsp;"; //$NON-NLS-1$
      }
      
      dataGrid.setSelectionPolicy(SelectionGrid.SelectionPolicy.DISABLED);
      dataGrid.setStylePrimaryName("disabled"); //$NON-NLS-1$
    }
    
    // Set table values
    for (int i = 0; i < rowAndColumnValues.length; i++){
      for (int j = 0; j < rowAndColumnValues[i].length; j++){
        Object value = rowAndColumnValues[i][j];
        
        if (value != null){
          if (value instanceof String){
            dataGrid.setHTML(i, j, value.toString());
          } else if (value instanceof Widget){
            dataGrid.setWidget(i, j, (Widget)value);
          } else {
            System.err.print("Invalid type set in data grid");
          }
        }
      }
    }
    
    // Set column widths
    if (columnWidths != null){
      for (int i = 0; i < columnWidths.length; i++){
        if (columnWidths[i] >= 0){
          dataGrid.setColumnWidth(i, columnWidths[i]);
          scrollTable.setColumnWidth(i, columnWidths[i]);
        }
      }
    }
    
    cellFormatter = dataGrid.getCellFormatter();
    
    // Set cell styles/tooltip for data grid cells
    for (int i = 0; i < rowAndColumnValues.length; i++){
      for (int j = 0; j < rowAndColumnValues[i].length; j++){
        Object value = rowAndColumnValues[i][j];
        Element element = null;
        
        try{
          element = cellFormatter.getElement(i, j);
        }catch(Exception e){}
        
        if (element != null){
          disableTextSelectionForCell(element);
        
          if (value != null && value instanceof String && !value.equals("&nbsp;")){ //$NON-NLS-1$
            element.setTitle(value.toString());
          }
        }
      }
    }
  }
  
  /**
   * Checks to see that row and column values are populated.
   */
  private boolean isRowAndColumnValuesValid(Object[][] rowAndColumnValues){
    if (rowAndColumnValues == null || rowAndColumnValues.length == 0 || rowAndColumnValues[0].length == 0){
      return false;
    }
    
    for (int i = 0; i < rowAndColumnValues.length; i++){
      for (int j = 0; j < rowAndColumnValues[i].length; j++){
        if (rowAndColumnValues[i][j] != null){
          return true;
        }
      }
    }
    
    return false;
  }
  
  /**
   * Disables text selection for a table cell containing a child element (typically a span).
   */
  private void disableTextSelectionForCell(Element cellElement){
    if (cellElement != null){
      Node node = cellElement.getChildNodes().getItem(0);
      
      disableTextSelection(cellElement);
      
      if (node != null){
        disableTextSelection(node);
        disableTextSelection(node.getFirstChild());
      }
    }
  }
  
  /**
   * Disables the ability to select text on the given object.
   * 
   * TODO use ElementUtils common method instead
   */
  private static native void disableTextSelection(JavaScriptObject target) /*-{
    if (target){
      if (typeof target.onselectstart != "undefined"){
        target.onselectstart = function(){
          return false;
        }
      }else if (target.style && typeof target.style.MozUserSelect != "undefined"){ // Firefox only
        target.style.MozUserSelect = "none";
      }
    }
  }-*/;

  /**
   * Creates the table using the default values specified in the constructor but with new data for
   * the rows. 
   */
  public void populateTable(Object[][] rowAndColumnValues){
    parentPanel.clear();

    createTable(tableHeaderNames, columnWidths, rowAndColumnValues);  
    
    parentPanel.add(scrollTable);
    
    scrollTable.fillWidth();
  }
  
  /**
   * Creates a table with a short message for the user.  Message may contain HTML.
   */
  public void populateTableWithSimpleMessage(String simpleMessage){
    parentPanel.clear();
    
    createTableWithShortMessage(simpleMessage);
    
    parentPanel.add(scrollTable);
    
    scrollTable.fillWidth();    
  }

  /**
   * Adds an additional table listener in addition to the default listener. 
   */
  public void addTableListener(TableListener listener){
    tableListeners.add(listener);
  }
  
  /**
   * Adds an additional table selection listener in addition to the default listener. 
   */
  public void addTableSelectionListener(TableSelectionListener listener){
    tableSelectionListeners.add(listener);
  }
  
  public void addDoubleClickListener(TableListener listener){
    doubleClickListeners.add(listener);
  }
  
  /**
   * Use setTableHeight instead.
   */
  @Deprecated
  public void setHeight(String height){
    this.scrollTableHeight = height;
  }

  public void setTableWidth(String width){
    this.scrollTableWidth = width;
  }
  
  public void setTableHeight(String height){
    this.scrollTableHeight = height;
  }
  
  public void setGridWidth(String width){
    this.dataGrid.setWidth(width);
  }
  
  public String getText(int row, int column){
    return dataGrid.getText(row, column);
  }
  
  public int getNumberOfColumns(){
    return numberOfColumns;
  }

  public void selectRow(int row){
    dataGrid.selectRow(row, false);
  }
  
  public Set<Integer> getSelectedRows(){
    return dataGrid.getSelectedRows();
  }
  
  public void fillWidth(){
    scrollTable.fillWidth();
  }
  
  public void deselectRows(){
    dataGrid.deselectRows();
  }
  
  /**
   * Default column sorter for this class.
   */
  final class BaseTableColumnSorter extends ColumnSorter {
    public void onSortColumn(SortableGrid grid,
        ColumnSortList sortList, ColumnSorterCallback callback){
      
      // Get the primary column and sort order
      int column = sortList.getPrimaryColumn();
      boolean ascending = sortList.isPrimaryAscending();

      // Apply the default quicksort algorithm
      //Element[] tdElems = new Element[grid.getRowCount()];
      List<Element> tdElems = new ArrayList<Element>(); 
      for (int i = 0; i < grid.getRowCount(); i++) {
        tdElems.add(grid.getCellFormatter().getElement(i, column));
      }
      
      if(grid.getColumnCount() > column){
        Collections.sort(tdElems, columnComparators != null && columnComparators[column] != null ? columnComparators[column] 
            : DEFAULT_COLUMN_COMPARATOR);
      }

      // Convert tdElems to trElems, reversing if needed
      Element[] trElems = new Element[tdElems.size()];
      if (ascending) {
        for (int i = 0; i < tdElems.size(); i++) {
          trElems[i] = DOM.getParent(tdElems.get(i));
        }
      } else {
        int maxElem = tdElems.size() - 1;
        for (int i = 0; i <= maxElem; i++) {
          trElems[i] = DOM.getParent(tdElems.get(maxElem - i));
        }
      }

      callback.onSortingComplete(trElems);
    }
  };
  
}
