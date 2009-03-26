package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;


/**
 * Represents a Metadata Category containing a collection of {@see MqlColumn}s
 *
 * @param <T> implementation of the MqlColumn interface
 */
public interface MqlCategory<T extends MqlColumn> extends Serializable {

  public String getId();

  public String getName();

  public List<T> getBusinessColumns();
  
}
