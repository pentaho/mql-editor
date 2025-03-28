/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


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

  private int limit;

  /**
   * Keys are parameter names; values are defaults for those parameters.
   */
  private Map<String, Object> defaultParameterMap;

  private boolean disableDistinct;

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

  public void setColumns( List<UIColumn> cols ) {

    this.cols = cols;
  }

  public void setConditions( List<UICondition> conditions ) {

    this.conditions = conditions;
  }

  public void setOrders( List<UIOrder> orders ) {

    this.orders = orders;
  }

  public void setDomain( UIDomain domain ) {

    this.domain = domain;
  }

  public void setModel( UIModel model ) {

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

  @Override public boolean isDisableDistinct() {
    return this.disableDistinct;
  }

  public void setDisableDistinct( boolean disableDistinct ) {
    this.disableDistinct = disableDistinct;
  }
}
