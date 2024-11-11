/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/


package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;

public class Query implements MqlQuery {

  private List<Column> cols = new ArrayList<Column>();

  private List<Condition> conditions = new ArrayList<Condition>();

  private List<Order> orders = new ArrayList<Order>();

  private int limit = -1;

  private Domain domain;

  private Model model;

  private String query;

  /**
   * Keys are parameter names; values are defaults for those parameters.
   */
  private Map<String, Object> defaultParameterMap;

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

  public void setColumns( List<Column> cols ) {

    this.cols = cols;
  }

  public void setConditions( List<Condition> conditions ) {

    this.conditions = conditions;
  }

  public void setOrders( List<Order> orders ) {

    this.orders = orders;
  }

  public void setDomain( Domain domain ) {

    this.domain = domain;
  }

  public void setModel( Model model ) {

    this.model = model;
  }

  public String getMqlStr() {
    return query;
  }

  public void setMqlStr( String query ) {
    this.query = query;
  }

  public Map<String, Object> getDefaultParameterMap() {
    return defaultParameterMap;
  }

  public void setDefaultParameterMap( Map<String, Object> defaultParameterMap ) {
    this.defaultParameterMap = defaultParameterMap;
  }

  public void setLimit( int limit ) {
    this.limit = limit;
  }

  public int getLimit() {
    return limit;
  }
}
