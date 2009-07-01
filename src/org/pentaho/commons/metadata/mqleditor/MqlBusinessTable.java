package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;

public interface MqlBusinessTable extends Serializable {

  public String getId();

  public String getName();
  
  public List<? extends MqlColumn>getBusinessColumns();
  
}
