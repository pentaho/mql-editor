package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;


/**
 *
 * Represents a Metadata Model object containing one or more {@see MqlCategory}s
 *
 */
public interface MqlModel extends Serializable{

  public String getId();

  public String getName();

  public List<? extends MqlCategory> getCategories();
  

}
