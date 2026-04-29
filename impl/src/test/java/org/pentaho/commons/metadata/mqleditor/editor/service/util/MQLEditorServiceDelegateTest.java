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


package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.editor.models.UICategory;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumn;
import org.pentaho.commons.metadata.mqleditor.editor.models.UICondition;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIConditions;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.concept.types.AggregationType;
import org.pentaho.metadata.model.concept.types.LocalizedString;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.metadata.repository.InMemoryMetadataDomainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MQLEditorServiceDelegateTest {
  @Test
  public void convertNewThinAggregationTypeTest() {
    MQLEditorServiceDelegate mqlESD = new MQLEditorServiceDelegate();

    Assert.assertEquals( AggType.NONE, mqlESD.convertNewThinAggregationType( null ) );

    Assert.assertEquals( AggType.COUNT, mqlESD.convertNewThinAggregationType( AggregationType.COUNT ) );

    Assert.assertEquals( AggType.COUNT_DISTINCT,
      mqlESD.convertNewThinAggregationType( AggregationType.COUNT_DISTINCT ) );

    Assert.assertEquals( AggType.AVERAGE, mqlESD.convertNewThinAggregationType( AggregationType.AVERAGE ) );

    Assert.assertEquals( AggType.MIN, mqlESD.convertNewThinAggregationType( AggregationType.MINIMUM ) );

    Assert.assertEquals( AggType.MAX, mqlESD.convertNewThinAggregationType( AggregationType.MAXIMUM ) );

    Assert.assertEquals( AggType.SUM, mqlESD.convertNewThinAggregationType( AggregationType.SUM ) );

    Assert.assertEquals( AggType.NONE, mqlESD.convertNewThinAggregationType( AggregationType.NONE ) );
  }

  // Timeout is set in order to prevent test "hanging" it's not test expected scenario
  @Test( timeout = 10000 )
  public void testConcurrency() throws Exception {
    String domainIdPrefix = "id";
    int repoSize = 100;
    //init repo
    IMetadataDomainRepository repo = new InMemoryMetadataDomainRepository();
    for ( int i = 0; i < repoSize; i++ ) {
      Domain domain = new Domain();
      LocalizedString name = new LocalizedString();
      name.setString( "US", String.valueOf( i + 1 ) );
      domain.setId( domainIdPrefix + String.valueOf( i + 1 ) );
      domain.setName( name );
      repo.storeDomain( domain, false );
    }

    MQLEditorServiceDelegate service = new MQLEditorServiceDelegate( repo );

    int poolSize = repoSize / 2;
    ExecutorService executorService = Executors.newFixedThreadPool( poolSize );

    List<Future<Boolean>> results = new ArrayList<>();
    for ( int i = 0; i < poolSize; i++ ) {
      results.add( executorService.submit( new Callable<Boolean>() {
        public Boolean call() throws Exception {
          for ( int i = 0; i < repoSize; i++ ) {
            try {
              String id = domainIdPrefix + String.valueOf( i + 1 );
              service.getDomainByName( id );
              service.addThinDomain( id );
            } catch ( Exception e ) {
              return false;
            }
          }
          return true;
        }
      } ) );
    }
    for ( Future<Boolean> result : results ) {
      Assert.assertTrue( result.get() );
    }
    executorService.shutdown();
  }

  @Test
  public void testConvertConditionsIntoComplexConstraints() {
    // GIVEN
    MQLEditorServiceDelegate delegate = new MQLEditorServiceDelegate();
    String expectedConstraints =
      "<constraints><constraint><operator>AND</operator><condition>EQUALS([CAT_ORDERS.BC_ORDERDETAILS_PRICEEACH];100)"
        + "</condition></constraint></constraints>";
    UICategory category = new UICategory();
    category.setId( "CAT_ORDERS" );
    UIColumn column = new UIColumn();
    column.setId( "BC_ORDERDETAILS_PRICEEACH" );
    category.setBusinessColumns( List.of( column ) );

    UIConditions conditions = new UIConditions();
    UICondition condition = new UICondition();
    condition.setOperator( Operator.EQUAL );
    condition.setValue( "100" );
    condition.setColumn( column );
    conditions.add( condition );

    // WHEN
    String constraints = delegate.convertConditionsIntoComplexConstraints( conditions, List.of( category ) );

    // THEN
    Assert.assertEquals( expectedConstraints, constraints );
  }

  @Test
  public void testConvertComplexConstraintsIntoConditions() {
    // GIVEN
    MQLEditorServiceDelegate delegate = new MQLEditorServiceDelegate();
    String complexConstraints =
      "<constraints><constraint><operator>AND</operator><condition>[CAT_ORDERS.BC_ORDERDETAILS_PRICEEACH] &lt;"
        + "200</condition></constraint><constraint><operator>OR</operator><condition>[CAT_ORDERS"
        + ".BC_ORDERDETAILS_TOTAL] &gt;500</condition></constraint></constraints>";
    UICategory category = new UICategory();
    UIColumn column1 = new UIColumn();
    column1.setId( "BC_ORDERDETAILS_PRICEEACH" );
    UIColumn column2 = new UIColumn();
    column2.setId( "BC_ORDERDETAILS_TOTAL" );
    category.setBusinessColumns( List.of( column1, column2 ) );

    // WHEN
    List<UICondition> conditions =
      delegate.convertComplexConstraintsIntoConditions( complexConstraints, List.of( category ) );

    // THEN
    Assert.assertEquals( 2, conditions.size() );

    Assert.assertEquals( "200", conditions.get( 0 ).getValue() );
    Assert.assertEquals( "<", conditions.get( 0 ).getOperator().toString() );
    Assert.assertEquals( "AND", conditions.get( 0 ).getCombinationType().toString() );
    Assert.assertEquals( "BC_ORDERDETAILS_PRICEEACH", conditions.get( 0 ).getColumn().getId() );

    Assert.assertEquals( "500", conditions.get( 1 ).getValue() );
    Assert.assertEquals( ">", conditions.get( 1 ).getOperator().toString() );
    Assert.assertEquals( "OR", conditions.get( 1 ).getCombinationType().toString() );
    Assert.assertEquals( "BC_ORDERDETAILS_TOTAL", conditions.get( 1 ).getColumn().getId() );
  }
}
