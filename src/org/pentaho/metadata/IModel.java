package org.pentaho.metadata;

import java.io.Serializable;
import java.util.List;

public interface IModel<T extends ICategory> extends Serializable{

  public String getId();

  public void setId(String id);

  public String getName();

  public void setName(String name);

  public List<T> getCategories();
  
  public void setCategories(List<T> categories);

}
