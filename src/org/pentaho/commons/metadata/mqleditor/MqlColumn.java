package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a Metadata Column object
 *
 * @param <T> implementation of a MqlBusinessTable
 */
public interface MqlColumn extends Serializable {

  public ColumnType getType();

  public String getId();

  public String getName();
  
  public AggType getDefaultAggType();
  
  public List<AggType> getAggTypes();
  
  public AggType getSelectedAggType();

}
