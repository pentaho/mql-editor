package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;

public interface IQuery extends Serializable{

  public List<? extends IBusinessColumn> getColumns();
  
  public List<? extends ICondition> getConditions();
  
  public List<? extends IOrder> getOrders();
  
  public IDomain getDomain();
  
  public IModel getModel();
  
  public String getMqlStr();
  
}
