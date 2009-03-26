package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;

public interface MqlBusinessTable<T extends MqlColumn> extends Serializable {

  public String getId();

  public String getName();
  
  public List<T> getBusinessColumns();
  
}
