package org.pentaho.commons.metadata.mqleditor.beans;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;

import java.util.ArrayList;
import java.util.List;

public class Query implements MqlQuery {

  private List<Column> cols = new ArrayList<Column>();

  private List<Condition> conditions = new ArrayList<Condition>();

  private List<Order> orders = new ArrayList<Order>();

  private Domain domain;

  private Model model;

  private String query;
  
  public Query() {
    super();
    cols = new ArrayList<Column>();
    conditions = new ArrayList<Condition>();
    orders = new ArrayList<Order>();
  }
  
  public List<Column> getColumns() {
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

  public List<Column> getCols() {

    return cols;
  }

  public void setCols(List<Column> cols) {

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
