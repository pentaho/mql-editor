/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
 */

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
