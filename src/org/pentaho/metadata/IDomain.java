package org.pentaho.metadata;

import java.io.Serializable;
import java.util.List;

public interface IDomain<T extends IModel> extends Serializable{

  public String getId();

  public void setId(String id);

  public String getName();

  public void setName(String name);

  public List<T> getModels();

  public void setModels(List<T> models);
}
