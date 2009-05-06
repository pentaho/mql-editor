package org.pentaho.commons.metadata.mqleditor.beans;

import java.io.Serializable;

import org.pentaho.metadata.model.concept.types.AggregationType;
import org.pentaho.metadata.model.concept.types.DataType;
import org.pentaho.metadata.model.concept.types.LocalizedString;
import org.pentaho.metadata.model.concept.types.TargetColumnType;
import org.pentaho.metadata.model.concept.types.TargetTableType;

public class BogoPojo implements Serializable{

  TargetTableType targetTableType;
  LocalizedString localizedString;
  DataType dataType;
  AggregationType aggType;
  TargetColumnType targetColumnType;
  public TargetTableType getTargetTableType() {
    return targetTableType;
  }
  public void setTargetTableType(TargetTableType targetTableType) {
    this.targetTableType = targetTableType;
  }
  public LocalizedString getLocalizedString() {
    return localizedString;
  }
  public void setLocalizedString(LocalizedString localizedString) {
    this.localizedString = localizedString;
  }
  public DataType getDataType() {
    return dataType;
  }
  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }
  public AggregationType getAggType() {
    return aggType;
  }
  public void setAggType(AggregationType aggType) {
    this.aggType = aggType;
  }
  public TargetColumnType getTargetColumnType() {
    return targetColumnType;
  }
  public void setTargetColumnType(TargetColumnType targetColumnType) {
    this.targetColumnType = targetColumnType;
  }
}
