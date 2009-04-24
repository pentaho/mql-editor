package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;

public class Column implements MqlColumn {

  private String id, name;
  private ColumnType type;
  private List<AggType> aggTypes;
  private AggType defaultAggType;
  private AggType selectedAggType;
  
  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;   
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

  public void setType(ColumnType type) {
    this.type = type;
  }

  public AggType getDefaultAggType() {
    return defaultAggType;
  }

  public List<AggType> getAggTypes() {
    return aggTypes;
  }

  public void setAggTypes(List<AggType> aggTypes) {
    this.aggTypes = aggTypes;
  }

  public void setDefaultAggType(AggType defaultAggType) {
    this.defaultAggType = defaultAggType;
  }

  public void setSelectedAggType(AggType aggType){
    this.selectedAggType = aggType;
  }
  
  public AggType getSelectedAggType(){
    return this.selectedAggType;
  }
  
  
}
