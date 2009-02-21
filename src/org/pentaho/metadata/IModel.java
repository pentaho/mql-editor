package org.pentaho.metadata;

import java.io.Serializable;
import java.util.List;

public interface IModel<T extends ICategory> extends Serializable{

  public String getId();

  public String getName();

  public List<T> getCategories();
  

}
