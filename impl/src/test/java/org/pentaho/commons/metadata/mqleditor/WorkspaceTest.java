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

package org.pentaho.commons.metadata.mqleditor;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.MqlOrder.Type;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumn;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumns;
import org.pentaho.commons.metadata.mqleditor.editor.models.UICondition;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIConditions;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIOrder;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIOrders;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceCWMDelegate;

public class WorkspaceTest {

  private Query mqlQuery;
  private MQLEditorServiceCWMDelegate service;
  Workspace workspace;

  @Before
  public void setUp() throws Exception {
    mqlQuery = new Query();

    Domain domain = new Domain();
    domain.setName( "mydomain" );

    List<Model> models = new ArrayList<Model>();
    Model model = new Model();
    model.setId( "mymodel" );
    model.setName( "mymodel" );

    List<Category> categories = new ArrayList<Category>();
    Category cat = new Category();
    cat.setId( "mycategory" );
    cat.setName( "mycategory" );

    List<Column> columns = new ArrayList<Column>();
    Column column = new Column();
    column.setId( "mycolumn" );
    column.setName( "mycolumn" );
    column.setType( ColumnType.TEXT );
    column.setPersistent( true );

    columns.add( column );
    cat.setBusinessColumns( columns );

    categories.add( cat );
    model.setCategories( categories );

    models.add( model );
    domain.setModels( models );

    mqlQuery.setDomain( domain );

    mqlQuery.setModel( model );

    mqlQuery.setColumns( columns );

    List<Condition> conditions = new ArrayList<Condition>();
    Condition cond = new Condition();
    cond.setColumn( column );
    cond.setCombinationType( null );
    cond.setOperator( Operator.BEGINS_WITH );
    cond.setValue( "myvalue1" );
    conditions.add( cond );

    Condition cond2 = new Condition();
    cond2.setColumn( column );
    cond2.setCombinationType( CombinationType.OR );
    cond2.setOperator( Operator.CONTAINS );
    cond2.setValue( "myvalue2" );
    conditions.add( cond2 );

    Condition cond3 = new Condition();
    cond3.setParameterized( true );
    cond3.setColumn( column );
    cond3.setCombinationType( CombinationType.OR );
    cond3.setOperator( Operator.EXACTLY_MATCHES );

    cond3.setValue( "myparameter" );
    conditions.add( cond3 );

    Condition cond4 = new Condition();
    cond4.setColumn( column );
    cond4.setCombinationType( CombinationType.OR );
    cond4.setOperator( Operator.IN );
    cond4.setValue( "testing;|\"test ing\"|\"Test|ing\"" ); //$NON-NLS-1$
    conditions.add( cond4 );

    mqlQuery.setConditions( conditions );

    List<Order> orders = new ArrayList<Order>();
    Order order = new Order();
    order.setColumn( column );
    order.setOrderType( Type.ASC );
    orders.add( order );

    mqlQuery.setOrders( orders );

    // service = new MQLEditorServiceCWMDelegate(cwms, factory);

    workspace = new Workspace();

    List<UIDomain> uiDomains = new ArrayList<UIDomain>();
    uiDomains.add( new UIDomain( domain ) );

    workspace.setDomains( uiDomains );

    workspace.wrap( mqlQuery );

  }

  @After
  public void tearDown() throws Exception {
    mqlQuery = null;
  }

  @Test
  public void testSelectedDomainAndModel() {

    Assert.assertEquals( mqlQuery.getDomain().getId(), workspace.getSelectedDomain().getId() );
    Assert.assertEquals( mqlQuery.getModel().getId(), workspace.getSelectedModel().getId() );

  }

  @Test
  public void testSelections() {

    // Test Conditions
    UIColumns outColumns = workspace.getSelections();

    Assert.assertEquals( outColumns.size(), mqlQuery.getColumns().size() );

    for ( int i = 0; i < outColumns.size(); i++ ) {
      UIColumn uiCol = outColumns.get( i );
      Column inCol = mqlQuery.getColumns().get( i );
      Assert.assertEquals( uiCol.getId(), inCol.getId() );
      Assert.assertEquals( uiCol.getSelectedAggType(), inCol.getSelectedAggType() );

      for ( int z = 0; z < uiCol.getAggTypes().size(); z++ ) {
        Assert.assertEquals( uiCol.getAggTypes().get( z ), inCol.getAggTypes().get( z ) );
      }

      Assert.assertEquals( uiCol.isPersistent(), inCol.isPersistent() );
    }

  }

  @Test
  public void testWorkspaceConditions() {
    UIConditions outConditions = workspace.getConditions();

    Assert.assertEquals( outConditions.size(), mqlQuery.getConditions().size() );

    for ( int i = 0; i < outConditions.size(); i++ ) {
      UICondition uiCond = outConditions.get( i );
      Condition inCond = mqlQuery.getConditions().get( i );
      Assert.assertEquals( uiCond.getColumn().getId(), inCond.getColumn().getId() );
      Assert.assertEquals( uiCond.getCombinationType(), inCond.getCombinationType() );

      Assert.assertEquals( uiCond.getValue(), inCond.getValue() );
      Assert.assertEquals( uiCond.getOperator(), inCond.getOperator() );
      Assert.assertEquals( uiCond.getSelectedAggType(), inCond.getSelectedAggType() );
    }

  }

  @Test
  public void testWorkspaceOrders() {

    UIOrders outOrders = workspace.getOrders();

    Assert.assertEquals( outOrders.size(), mqlQuery.getOrders().size() );

    for ( int i = 0; i < outOrders.size(); i++ ) {
      UIOrder uiOrd = outOrders.get( i );
      Order inOrd = mqlQuery.getOrders().get( i );
      Assert.assertEquals( uiOrd.getColumn().getId(), inOrd.getColumn().getId() );
      Assert.assertEquals( uiOrd.getOrderType(), inOrd.getOrderType() );
      Assert.assertEquals( uiOrd.getSelectedAggType(), inOrd.getSelectedAggType() );

    }

  }
}
