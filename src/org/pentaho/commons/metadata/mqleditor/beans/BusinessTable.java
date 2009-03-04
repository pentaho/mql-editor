package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IBusinessColumn;
import org.pentaho.commons.metadata.mqleditor.IBusinessTable;

public class BusinessTable implements IBusinessTable<BusinessColumn> {

  private String id, name;
  private List<BusinessColumn> columns = new ArrayList<BusinessColumn>();
  
  public String getId() {
    return this.id;  
  }

  public String getName() {
    return this.id;  
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
