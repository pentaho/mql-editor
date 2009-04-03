package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.List;

import org.pentaho.pms.schema.v3.model.Column;

public class BusinessData implements java.io.Serializable{
  private static final long serialVersionUID = 8275330793662889379L;
  private List<Column> columns;// contains column names
  private List<List<String>> data;// 2 dimensional array

  public BusinessData(List<Column> columns2, List<List<String>> data) {
    super();
    this.columns = columns2;
    this.data = data;
  }
  
  public List<Column> getColumns() {
    return columns;
  }
  public void setColumns(List<Column> columns) {
    this.columns = columns;
  }

  public List<List<String>> getData() {
    return data;
  }
  public void setData(List<List<String>> data) {
    this.data = data;
  }
}
