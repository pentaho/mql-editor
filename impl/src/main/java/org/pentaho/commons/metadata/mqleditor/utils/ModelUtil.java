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


package org.pentaho.commons.metadata.mqleditor.utils;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;

public class ModelUtil {

  public static Query convertUIModelToBean( MqlQuery uiQuery ) {

    Query query = new Query();

    if ( uiQuery.getDomain() == null || uiQuery.getModel() == null ) {
      return null;
    }

    Domain domain = new Domain();
    domain.setName( uiQuery.getDomain().getName() );
    domain.setId( uiQuery.getDomain().getId() );
    query.setDomain( domain );

    Model model = new Model();
    model.setName( uiQuery.getModel().getName() );
    model.setId( uiQuery.getModel().getId() );

    query.setMqlStr( uiQuery.getMqlStr() );

    query.setModel( model );

    // must have columns selected
    List<Column> cols = new ArrayList<Column>();
    for ( MqlColumn q : uiQuery.getColumns() ) {
      Column col = new Column();
      col.setId( q.getId() );
      col.setName( q.getName() );
      col.setType( q.getType() );
      col.setAggTypes( q.getAggTypes() );
      col.setDefaultAggType( q.getDefaultAggType() );
      col.setSelectedAggType( q.getSelectedAggType() );
      col.setPersistent( q.isPersistent() );

      cols.add( col );
    }
    query.setColumns( cols );

    List<Order> orders = new ArrayList<Order>();
    // orders are optional
    if ( uiQuery.getOrders() != null ) {
      for ( MqlOrder order : uiQuery.getOrders() ) {
        Order ord = new Order();
        Column col = new Column();
        col.setId( order.getColumn().getId() );
        col.setName( order.getColumn().getName() );
        col.setType( order.getColumn().getType() );
        col.setAggTypes( order.getColumn().getAggTypes() );
        col.setDefaultAggType( order.getColumn().getDefaultAggType() );
        col.setSelectedAggType( order.getColumn().getSelectedAggType() );

        ord.setColumn( col );
        ord.setOrderType( order.getOrderType() );
        ord.setSelectedAggType( order.getSelectedAggType() );
        orders.add( ord );
      }
    }
    query.setOrders( orders );

    List<Condition> conditions = new ArrayList<Condition>();
    // conditions are optional
    if ( uiQuery.getConditions() != null ) {
      for ( MqlCondition condition : uiQuery.getConditions() ) {
        Condition con = new Condition();
        Column col = new Column();
        col.setId( condition.getColumn().getId() );
        col.setName( condition.getColumn().getName() );
        col.setType( condition.getColumn().getType() );
        col.setAggTypes( condition.getColumn().getAggTypes() );
        col.setDefaultAggType( condition.getColumn().getDefaultAggType() );
        col.setSelectedAggType( condition.getColumn().getSelectedAggType() );
        con.setColumn( col );

        con.setCombinationType( condition.getCombinationType() );
        con.setParameterized( condition.isParameterized() );
        con.setDefaultValue( condition.getDefaultValue() );
        con.setOperator( condition.getOperator() );
        con.setValue( condition.getValue() );
        con.setSelectedAggType( condition.getSelectedAggType() );

        conditions.add( con );
      }
    }

    // row limit
    query.setLimit( uiQuery.getLimit() );
    query.setDisableDistinct( uiQuery.isDisableDistinct() );

    query.setConditions( conditions );
    return query;
  }
}
