package org.pentaho.commons.metadata.mqleditor.beans;

import java.io.Serializable;

import org.pentaho.metadata.model.concept.types.DataType;

public class TestObject implements ITestObject, Serializable{
  public TestObject() {
    super();
    // TODO Auto-generated constructor stub
  }

  public enum MyDataType {one, two};
  public String name;
  public String desc;
  public MyDataType type;
  public DataType dataType;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDesc() {
    return desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  
  public MyDataType getType() {
    return type;
  }
  public void setType(MyDataType type) {
    this.type = type;
  }
  public DataType getDataType() {
    return dataType;
  }
  public void setDataType(DataType dataType) {
    this.dataType = dataType;
  }
}
