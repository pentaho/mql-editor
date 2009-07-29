package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlCategory;

public class Category implements MqlCategory {

  private String id, name;
  private List<Column> columns = new ArrayList<Column>();

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Column> getBusinessColumns() {
    return columns;
  }

  public void setBusinessColumns(List<Column> columns) {
    this.columns = columns;
  }
  
  

}
