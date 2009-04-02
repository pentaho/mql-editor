package org.pentaho.commons.metadata.mqleditor.beans;

public class ResultSetObject implements java.io.Serializable{
  private static final long serialVersionUID = 8275330793662889379L;
  private String[] columns;// contains column names
  private int[] columnTypes;// contains column types
  private Object[][] data;// 2 dimensional array

  public ResultSetObject(int[] columnTypes, String[] columns, Object[][] data) {
    super();
    this.columnTypes = columnTypes;
    this.columns = columns;
    this.data = data;
  }
  
  public String[] getColumns() {
    return columns;
  }
  public void setColumns(String[] columns) {
    this.columns = columns;
  }
  public int[] getColumnTypes() {
    return columnTypes;
  }
  public void setColumnTypes(int[] columnTypes) {
    this.columnTypes = columnTypes;
  }
  public Object[][] getData() {
    return data;
  }
  public void setData(Object[][] data) {
    this.data = data;
  }
}
