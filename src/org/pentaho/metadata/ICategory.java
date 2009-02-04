package org.pentaho.metadata;

import java.io.Serializable;
import java.util.List;

public interface ICategory<T extends IBusinessColumn> extends Serializable {

  public String getId();

  public void setId(String id);

  public String getName();

  public void setName(String name);

  public List<T> getBusinessColumns();
  
  public void setBusinessColumns(List<T> columns);
}
