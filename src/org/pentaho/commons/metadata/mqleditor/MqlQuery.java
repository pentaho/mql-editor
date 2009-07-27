package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents an MQL Query. Contains a list of selected columns, conditions and order information. For convenience it
 * also has the serialized xml representation.
 *
 */
public interface MqlQuery extends Serializable{

  public List<? extends MqlColumn> getColumns();
  
  public List<? extends MqlCondition> getConditions();
  
  public List<? extends MqlOrder> getOrders();
  
  public MqlDomain getDomain();
  
  public MqlModel getModel();
  
  public String getMqlStr();
  
  public Map<String, String> getDefaultParameterMap();
  
}
