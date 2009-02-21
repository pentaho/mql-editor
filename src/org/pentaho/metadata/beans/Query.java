package org.pentaho.metadata.beans;

import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.IQuery;

public class Query implements IQuery {

  private List<BusinessColumn> cols;

  private List<Condition> conditions;

  private List<Order> orders;

  private Domain domain;

  private Model model;

  private String query;
  
  public List<? extends IBusinessColumn> getColumns() {
    return cols;
  }

  public List<? extends ICondition> getConditions() {
    return conditions;
  }

  public IDomain getDomain() {
    return domain;
  }

  public IModel getModel() {
    return model;
  }

  public List<? extends IOrder> getOrders() {
    return orders;
  }

  public List<BusinessColumn> getCols() {

    return cols;
  }

  public void setCols(List<BusinessColumn> cols) {

    this.cols = cols;
  }

  public void setConditions(List<Condition> conditions) {

    this.conditions = conditions;
  }

  public void setOrders(List<Order> orders) {

    this.orders = orders;
  }

  public void setDomain(Domain domain) {

    this.domain = domain;
  }

  public void setModel(Model model) {

    this.model = model;
  }

  public String getMqlStr() {
    return query;  
  }

  public void setMqlStr(String query){
    this.query = query;
  }
}
