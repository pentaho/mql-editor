package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;

public class UIColumn extends AbstractModelNode<UIColumn> implements MqlColumn {


  private ColumnType type;
  private String id, name;

  private List<AggType> aggTypes = new ArrayList<AggType>();
  private AggType defaultAggType;

  private AggType selectedAggType;
  
  public UIColumn() {

  }

  public UIColumn(MqlColumn col) {
    this.type = col.getType();
    this.id = col.getId();
    this.name = col.getName();
  }
  public String getId() {
    return id;
  }
  
  public void setId(String id){
    this.id = id;
  }

  public String getName() {
    return this.name;
  }
  
  public void setName(String name){
    this.name = name;
  }
  
  public void setTableName(String name){
    //TODO: Ignored! remove once Tree bindings respect one-way with editable="false"
  }
  
  public ColumnType getType() {

    return type;
  }
  
  public void setType(ColumnType type){
    this.type = type;
  }
  
  public String toString(){
    return id;
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
  
  public Vector getBindingAggTypes(){
    Vector v = new Vector();
    for(AggType t : this.aggTypes){
      v.add(t.toString());
    }
    return v;
  }
}
