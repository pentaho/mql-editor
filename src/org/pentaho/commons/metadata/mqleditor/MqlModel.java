package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;


/**
 *
 * Represents a Metadata Model object containing one or more {@see MqlCategory}s
 *
 * @param <T> implementation of an MqlCategory
 */
public interface MqlModel<T extends MqlCategory> extends Serializable{

  public String getId();

  public String getName();

  public List<T> getCategories();
  

}
