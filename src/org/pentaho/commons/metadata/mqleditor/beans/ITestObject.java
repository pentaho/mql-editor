package org.pentaho.commons.metadata.mqleditor.beans;

import org.pentaho.commons.metadata.mqleditor.beans.TestObject.MyDataType;
import org.pentaho.metadata.model.concept.types.DataType;

public interface ITestObject {

  public String getName();
  public void setName(String name);
  public String getDesc();
  public void setDesc(String desc);
  
  public MyDataType getType();
  public void setType(MyDataType type);
  public DataType getDataType();
  public void setDataType(DataType dataType);
}
