package org.pentaho.commons.metadata.mqleditor.beans;

public class ResultSetObject implements java.io.Serializable{
  private static final long serialVersionUID = 8275330793662889379L;
  private Object[] columns;// contains column names
  private Object[] columnTypes;// contains column types
  private Object[][] data;// 2 dimensional array

  public ResultSetObject(Object[] columnTypes, Object[] columns, Object[][] data) {
    super();
    this.columnTypes = columnTypes;
    this.columns = columns;
    this.data = data;
  }
  
  public Object[] getColumns() {
    return columns;
  }
  public void setColumns(Object[] columns) {
    this.columns = columns;
  }
  public Object[] getColumnTypes() {
    return columnTypes;
  }
  public void setColumnTypes(Object[] columnTypes) {
    this.columnTypes = columnTypes;
  }
  public Object[][] getData() {
    return data;
  }
  public void setData(Object[][] data) {
    this.data = data;
  }
}
