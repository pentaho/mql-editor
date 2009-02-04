package org.pentaho.metadata.beans;

import org.pentaho.metadata.ColumnType;
import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.IBusinessTable;
import org.pentaho.metadata.editor.models.UIBusinessTable;

public class BusinessColumn implements IBusinessColumn<BusinessTable>{

  private String id, name;
  private BusinessTable table;
  private ColumnType type;
  
  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;   
  }

  public BusinessTable getTable() {
    return this.table;   
  }

  public ColumnType getType() {
    return this.type;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setTable(BusinessTable table) {
    this.table = table;   
  }

  public void setType(ColumnType type) {
    this.type = type;
  }

  
  
}
