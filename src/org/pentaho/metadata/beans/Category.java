package org.pentaho.metadata.beans;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.ICategory;

public class Category implements ICategory<BusinessColumn> {

  private String id, name;
  private List<BusinessColumn> columns = new ArrayList<BusinessColumn>();

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

  public List<BusinessColumn> getBusinessColumns() {
    return columns;
  }

  public void setBusinessColumns(List<BusinessColumn> columns) {
    this.columns = columns;
  }
  
  

}
