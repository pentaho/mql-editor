package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;


/**
 * Represents a Metadata Category containing a collection of {@see MqlColumn}s
 *
 */
public interface MqlCategory extends Serializable {

  public String getId();

  public String getName();

  public List<? extends MqlColumn> getBusinessColumns();
  
}
