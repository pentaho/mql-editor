package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlModel;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;

public class Query implements MqlQuery {

  private List<Column> cols = new ArrayList<Column>();

  private List<Condition> conditions = new ArrayList<Condition>();

  private List<Order> orders = new ArrayList<Order>();

  private Domain domain;

  private Model model;

  private String query;
  
  /**
   * Keys are parameter names; values are defaults for those parameters.
   */
  private Map<String, String> defaultParameterMap;
  
  public Query() {
    super();
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

  public void setColumns(List<Column> cols) {

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

  public Map<String, String> getDefaultParameterMap() {  
    return defaultParameterMap;
  }

  public void setDefaultParameterMap(Map<String, String> defaultParameterMap) {
    this.defaultParameterMap = defaultParameterMap;
  }
  
}
