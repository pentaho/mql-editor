package org.pentaho.metadata;

import java.io.Serializable;
import java.util.List;

public interface IDomain<T extends IModel> extends Serializable{

  public String getId();

  public String getName();

  public List<T> getModels();

}
