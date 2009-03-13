package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IQuery;

public class Query implements IQuery {

  private List<BusinessColumn> cols = new ArrayList<BusinessColumn>();

  private List<Condition> conditions = new ArrayList<Condition>();

  private List<Order> orders = new ArrayList<Order>();

  private Domain domain;

  private Model model;

  private String query;
  
  public Query() {
    super();
    cols = new ArrayList<BusinessColumn>();
    conditions = new ArrayList<Condition>();
    orders = new ArrayList<Order>();
  }
  
  public List<BusinessColumn> getColumns() {
    return cols;
  }

  public List<Condition> getConditions() {
    return conditions;
  }

  public Domain getDomain() {
    return domain;
  }

  public Model getModel() {
    return model;
  }

  public List<Order> getOrders() {
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
