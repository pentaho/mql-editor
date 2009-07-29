package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;

public class UIQuery implements MqlQuery {

  private List<UIColumn> cols;

  private List<UICondition> conditions;

  private List<UIOrder> orders;

  private UIDomain domain;

  private UIModel model;

  private String query;
  
  /**
   * Keys are parameter names; values are defaults for those parameters.
   */
  private Map<String, String> defaultParameterMap;
  
  public UIQuery() {
    super();
  }
  
  public List<UIColumn> getColumns() {
    return cols;
  }

  public List<UICondition> getConditions() {
    return conditions;
  }

  public UIDomain getDomain() {
    return domain;
  }

  public UIModel getModel() {
    return model;
  }

  public List<UIOrder> getOrders() {
    return orders;
  }

  public void setColumns(List<UIColumn> cols) {

    this.cols = cols;
  }

  public void setConditions(List<UICondition> conditions) {

    this.conditions = conditions;
  }

  public void setOrders(List<UIOrder> orders) {

    this.orders = orders;
  }

  public void setDomain(UIDomain domain) {

    this.domain = domain;
  }

  public void setModel(UIModel model) {

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
