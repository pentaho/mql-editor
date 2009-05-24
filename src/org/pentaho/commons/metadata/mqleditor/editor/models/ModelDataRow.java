package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.pentaho.commons.metadata.mqleditor.DataFormatType;
import org.pentaho.metadata.model.IPhysicalColumn;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.concept.types.DataType;
import org.pentaho.metadata.model.concept.types.LocalizedString;
import org.pentaho.ui.xul.XulEventSourceAdapter;


public class ModelDataRow extends XulEventSourceAdapter{

  
  private String columnName, sampleData;
  private List<DataType> dataTypes = new ArrayList<DataType>();
  private List<String> sampleDataList;
  private DataType selectedDataType;
  private String locale;
  //private List<DataFormatType> dataFormatTypes = new ArrayList<DataFormatType>();
  //private DataFormatType selectedDataFormatType;
  
  // Commenting out data format for now
 public ModelDataRow(LogicalColumn col, List<String> columnData, String locale) {
    this.selectedDataType = col.getDataType();
    //this.selectedDataFormatType = DataFormatType.CURRENCY;
    this.columnName = col.getName().getString(locale);
    if(columnData.size() > 0) {
      this.sampleData = columnData.get(0);
      this.sampleDataList = columnData;
    }
  }


  public String getColumnName() {
    return columnName;
  }


  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }


  public String getSampleData() {
    return sampleData;
  }


  public void setSampleData(String sampleData) {
    this.sampleData = sampleData;
  }


  public List<DataType> getDataTypes() {
    return dataTypes;
  }


  public void setDataTypes(List<DataType> dataTypes) {
    this.dataTypes = dataTypes;
  }


  public List<String> getSampleDataList() {
    return sampleDataList;
  }


  public void setSampleDataList(List<String> sampleDataList) {
    this.sampleDataList = sampleDataList;
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
