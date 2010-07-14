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

import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;
import org.pentaho.gwt.widgets.client.table.ColumnComparators.BaseColumnComparator;
import org.pentaho.gwt.widgets.client.table.ColumnComparators.ColumnComparatorTypes;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.FixedWidthGrid;
import com.google.gwt.widgetideas.table.client.ScrollTable;
import com.google.gwt.widgetideas.table.client.SortableGrid;
import com.google.gwt.widgetideas.table.client.SourceTableSelectionEvents;
import com.google.gwt.widgetideas.table.client.TableSelectionListener;
import com.google.gwt.widgetideas.table.client.ScrollTable.ResizePolicy;
import com.google.gwt.widgetideas.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.widgetideas.table.client.SortableGrid.ColumnSorter;
import com.google.gwt.widgetideas.table.client.SortableGrid.ColumnSorterCallback;
import com.google.gwt.widgetideas.table.client.TableModel.ColumnSortList;
import com.google.gwt.widgetideas.table.client.overrides.FlexTable.FlexCellFormatter;
import com.google.gwt.widgetideas.table.client.overrides.HTMLTable.CellFormatter;

/**
 * <p>
 * Core reusable, table widget for displaying tabular data that is
 * based on a composite of a ScrollTable and a FixedWidthGrid.
 * 
 * <p>
 * Usage Notes:
 * 
 * <p>
 * <ul>
 * <li>You must call the populateTable or populateTableWithSimpleMessage method
 * AFTER having instanciated it.
 * <li>It's always better to define the width and height right after
 * instanciation.
 * <li>Never set the resize policy to FILL_WIDTH or FireFox will experience an
 * ever growing table.
 * </ul>
 * </p>
 */
@SuppressWarnings("deprecation")
public class BaseTable extends Composite {
  
  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();
  
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
  private BaseColumnComparator[] columnComparators;

  private final TableListener internalDoubleClickListener = new TableListener() {
		public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
			for (TableListener listener : doubleClickListeners) {
				listener.onCellClicked(sender, row, cell);
			}
		}
	  };
	  private List<TableListener> doubleClickListeners = new ArrayList<TableListener>();
	  
	  private final TableListener internalTableListener = new TableListener() {
		public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
			for (TableListener listener : tableListeners) {
				listener.onCellClicked(sender, row, cell);
			}
		}
	  };
	  private List<TableListener> tableListeners = new ArrayList<TableListener>();
	 
	  private final TableSelectionListener internalSelectionListener = new TableSelectionListener() {
		public void onRowsSelected(SourceTableSelectionEvents sender, int firstRow, int numRows) {
			for (TableSelectionListener listener : tableSelectionListeners) {
				listener.onRowsSelected(sender, firstRow, numRows);
			}
		}
		public void onRowUnhover(SourceTableSelectionEvents sender, int row) {
			for (TableSelectionListener listener : tableSelectionListeners) {
				listener.onRowUnhover(sender, row);
			}
		}
		public void onRowHover(SourceTableSelectionEvents sender, int row) {
			for (TableSelectionListener listener : tableSelectionListeners) {
				listener.onRowHover(sender, row);
			}
		}
		public void onRowDeselected(SourceTableSelectionEvents sender, int row) {
			for (TableSelectionListener listener : tableSelectionListeners) {
				listener.onRowDeselected(sender, row);
			}
		}
		public void onCellUnhover(SourceTableSelectionEvents sender, int row, int cell) {
			for (TableSelectionListener listener : tableSelectionListeners) {
				listener.onCellUnhover(sender, row, cell);
			}
		}
		public void onCellHover(SourceTableSelectionEvents sender, int row, int cell) {
			for (TableSelectionListener listener : tableSelectionListeners) {
				listener.onCellHover(sender, row, cell);
			}
		}
		public void onAllRowsDeselected(SourceTableSelectionEvents sender) {
			for (TableSelectionListener listener : tableSelectionListeners) {
				listener.onAllRowsDeselected(sender);
			}
		}
	  };
	  private List<TableSelectionListener> tableSelectionListeners = new ArrayList<TableSelectionListener>();
	  
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
    
      createTable(
    		  tableHeaderNames, 
    		  columnWidths, 
    		  new Object[0][0], 
    		  ResizePolicy.FIXED_WIDTH, 
    		  selectionPolicy);

      this.parentPanel.add(scrollTable);

      initWidget(parentPanel);

    }else{
      System.err.println(MSGS.tableHeaderInputError());
    }
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

	// Set header values and disable text selection
    final FlexCellFormatter cellFormatter = tableHeader.getFlexCellFormatter();
    for (int i = 0; i < tableHeaderNames.length; i++){
      tableHeader.setHTML(0, i, tableHeaderNames[i]);
      cellFormatter.setHorizontalAlignment(0, i, HasHorizontalAlignment.ALIGN_LEFT);
    }
    
    if (this.selectionPolicy == null){
      tableHeader.setStylePrimaryName("disabled"); //$NON-NLS-1$
    }
    
    // Figure out the correct widths
    // Defer it so the DOM has finished the layout.
    DeferredCommand.addCommand(new Command() {
        public void execute() {
        	if (scrollTableWidth != null) {
        		tableHeader.setWidth(scrollTableWidth);
        	}
        	if (columnWidths != null
        			&& columnWidths.length > 0) 
        	{
	        	for (int i = 0; i < columnWidths.length; i++) {
	        		tableHeader.setColumnWidth(i, columnWidths[i]);
	        	}
	        }
        }
    });
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
        int row = DOM.getChildIndex(body, tr) - 1;
        int column = DOM.getChildIndex(tr, td);
        
        switch (DOM.eventGetType(event)) {
          case Event.ONDBLCLICK: {
        	  internalDoubleClickListener.onCellClicked(dataGrid, row, column);
          }
          default: {
            break;
          }
        }
        
        super.onBrowserEvent(event);
      }
    };
      
    // Set style
    if (selectionPolicy == null) {
      dataGrid.setSelectionPolicy(SelectionPolicy.ONE_ROW);
    } else {
      dataGrid.setSelectionPolicy(selectionPolicy);
    }

    // Add table listeners
    dataGrid.addTableListener(internalTableListener);
    
    // Add table selection listeners
    dataGrid.addTableSelectionListener(internalSelectionListener);
    
    dataGrid.sinkEvents(Event.ONDBLCLICK);    
    dataGrid.setColumnSorter(new BaseTableColumnSorter());
    
    if (this.selectionPolicy == null){
      dataGrid.setStylePrimaryName("disabled"); //$NON-NLS-1$
    }
    
    // Figure out the correct widths
    // Defer it so the DOM has finished the layout.
    DeferredCommand.addCommand(new Command() {
        public void execute() {
        	if (scrollTableWidth != null) {
        		dataGrid.setWidth(scrollTableWidth);
        	}
        	if (columnWidths != null
        			&& columnWidths.length > 0) 
        	{
	        	for (int i = 0; i < columnWidths.length; i++) {
	        		dataGrid.setColumnWidth(i, columnWidths[i]);
	        	}
	        }
        }
    });
    
    DeferredCommand.addCommand(new Command() {
        public void execute() {
        	internalSelectionListener.onAllRowsDeselected(dataGrid);
        }
    });
  }
  
  /**
   * Creates and initializes the scroll table. 
   */
  private void createScrollTable(ResizePolicy resizePolicy){
    
	  scrollTable = 
		  new ScrollTable(
			  dataGrid, 
			  tableHeader, 
			  (BaseTableImages)GWT.create(
				  BaseTableImages.class));

    scrollTable.setResizePolicy(resizePolicy);
    scrollTable.setCellPadding(0);
    scrollTable.setCellSpacing(0);

    // Set column comparators
    if (columnComparators != null){
      for (int i = 0; i < columnComparators.length; i++){
        if (columnComparators[i] != null){
          scrollTable.setColumnSortable(i, true);
        }else{
          scrollTable.setColumnSortable(i, false);
        }
      }
    }
    if (this.scrollTableWidth != null) {
    	this.setWidth(scrollTableWidth);
    }
    if (this.scrollTableHeight != null) {
    	this.setHeight(scrollTableHeight);    	
    }
  }
  
  /**
   * Populates the data grid with data then sets the column widths. 
   */
  private void populateDataGrid(int[] columnWidths, Object[][] rowAndColumnValues){
   
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
            System.err.print(MSGS.invalidDataGridTypeSet());
            Window.alert(MSGS.invalidDataGridTypeSet());
			return;
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
    
    
    // Set cell styles/tooltip for data grid cells
    final CellFormatter cellFormatter = dataGrid.getCellFormatter();
    for (int i = 0; i < rowAndColumnValues.length; i++){
      for (int j = 0; j < rowAndColumnValues[i].length; j++){
        Object value = rowAndColumnValues[i][j];
        Element element = null;
        
        try{
          element = cellFormatter.getElement(i, j);
        }catch(Exception e){}
        
        if (element != null){
          
          if (value != null && value instanceof String && !value.equals("&nbsp;")){ //$NON-NLS-1$
            element.setTitle(value.toString());
          }
        }
      }
    }
    
    DeferredCommand.addCommand(new Command() {
        public void execute() {
        	internalSelectionListener.onAllRowsDeselected(dataGrid);
        }
    });
  }
  
  
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
  
  /**
   * Adds a listener to fire when a user double-clicks on a table row. 
   */
  public void addDoubleClickListener(TableListener listener){
    doubleClickListeners.add(listener);
  }
  
  
  /**
   * Gets the text within the specified cell.
   */
  public String getText(int row, int column){
    return dataGrid.getText(row, column);
  }
  
  /**
   * Returns the number of columns in the data table.
   */
  public int getNumberOfColumns(){
    return numberOfColumns;
  }

  /**
   * Select a row in the data table.
   */
  public void selectRow(int row){
    dataGrid.selectRow(row, false);
  }
  
  /**
   * Returns the set of selected row indexes.
   */
  public Set<Integer> getSelectedRows(){
	return dataGrid.getSelectedRows();		
  }
  
  
  /**
   * Deselect all selected rows in the data table.
   */
  public void deselectRows(){
    dataGrid.deselectAllRows();
  }
  
  /**
   * Default column sorter for this class.
   */
  final class BaseTableColumnSorter extends ColumnSorter {
    
	public void onSortColumn(
			SortableGrid grid,
			ColumnSortList sortList, 
			ColumnSorterCallback callback)
	{
      
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
  
  	/*
  	 * (non-Javadoc)
  	 * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
  	 */
    @Override
	public void setWidth(final String width) {
		super.setWidth(width);
		this.scrollTableWidth = width;
		// It sounds silly to resize this way but we need to let the browser
		// update the DOM before we recompute sizes. IE doesn't return
		// correct values when you perform too many dynamically computed resizes.
		DeferredCommand.addCommand(new Command() {
            public void execute() {
            	if (scrollTable != null) {
            		scrollTable.setWidth(width);
            		scrollTable.fillWidth();
            		scrollTable.redraw();
            	}
            }
        });
	}
	
    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.client.ui.UIObject#setHeight(java.lang.String)
     */
	@Override
	public void setHeight(final String height) {
		super.setHeight(height);
		this.scrollTableHeight = height;
		// It sounds silly to resize this way but we need to let the browser
		// update the DOM before we recompute sizes. IE doesn't return
		// correct values when you perform too many dynamically computed resizes.
		DeferredCommand.addCommand(new Command() {
            public void execute() {
            	if (scrollTable != null) {
            		scrollTable.setHeight(height);
            		scrollTable.redraw();
            	}
            }
        });
	}
}
