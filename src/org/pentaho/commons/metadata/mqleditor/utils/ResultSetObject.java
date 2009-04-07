package org.pentaho.commons.metadata.mqleditor.utils;

public class ResultSetObject implements java.io.Serializable{
  private static final long serialVersionUID = 8275330793662889379L;
  private String[] columns;// contains column names
  private String[] columnTypes;// contains column types
  private String[][] data;// 2 dimensional array

  public ResultSetObject(String[] columnTypes, String[] columns, String[][] data) {
    super();
    this.columnTypes = columnTypes;
    this.columns = columns;
    this.data = data;
  }
  
  public ResultSetObject()
  {
    
  }
  public String[] getColumns() {
    return columns;
  }
  public void setColumns(String[] columns) {
    this.columns = columns;
  }
  public String[] getColumnTypes() {
    return columnTypes;
  }
  public void setColumnTypes(String[] columnTypes) {
    this.columnTypes = columnTypes;
  }
  public String[][] getData() {
    return data;
  }
  public void setData(String[][] data) {
    this.data = data;
  }
}
