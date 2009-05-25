package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.concept.types.DataType;
import org.pentaho.ui.xul.XulEventSourceAdapter;


public class CsvModelDataRow extends XulEventSourceAdapter{

  
  private String columnName;
  private List<DataType> dataTypes = new ArrayList<DataType>();
  private DataType selectedDataType;
  private String locale;
  //private List<DataFormatType> dataFormatTypes = new ArrayList<DataFormatType>();
  //private DataFormatType selectedDataFormatType;
  
  // Commenting out data format for now
 public CsvModelDataRow(LogicalColumn col,String locale) {
    this.selectedDataType = col.getDataType();
    //this.selectedDataFormatType = DataFormatType.CURRENCY;
    this.columnName = col.getName().getString(locale);
  }


  public String getColumnName() {
    return columnName;
  }


  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }


  public List<DataType> getDataTypes() {
    return dataTypes;
  }


  public void setDataTypes(List<DataType> dataTypes) {
    this.dataTypes = dataTypes;
  }

  public DataType getSelectedDataType() {
    return selectedDataType;
  }


  public void setSelectedDataType(DataType selectedDataType) {
    this.selectedDataType = selectedDataType;
  }


  public void setSelectedDataType(Object o){
    setSelectedDataType((DataType)  o);
  }
  
  public Vector getBindingDataTypes(){
    Vector v = new Vector();
    //for(DataType t : this.dataTypes){
    for(DataType t : DataType.values()){
      v.add(t);
    }
    return v;
  }

  
  //public List<DataFormatType> getDataFormatTypes() {
  //  return dataFormatTypes;
  //}


  //public void setDataFormatTypes(List<DataFormatType> dataFormatTypes) {
  //  this.dataFormatTypes = dataFormatTypes;
  //}


  //public DataFormatType getSelectedDataFormatType() {
   // return selectedDataFormatType;
  //}


  //public void setSelectedDataFormatType(DataFormatType selectedDataFormatType) {
  //  this.selectedDataFormatType = selectedDataFormatType;
 // }
  
  //public void setSelectedDataFormatType(Object o){
  //  setSelectedDataFormatType((DataFormatType)  o);
  //}
  
  //public Vector getBindingDataFormatTypes(){
  //  Vector v = new Vector();
    //for(DataFormatType t : this.dataFormatTypes){
   // for(DataFormatType t : DataFormatType.values()){
   //   v.add(t);
   // }
   // return v;
 // }

}
