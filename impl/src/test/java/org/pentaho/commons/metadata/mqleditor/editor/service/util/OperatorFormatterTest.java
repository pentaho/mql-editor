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


package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.Operator;

public class OperatorFormatterTest {

  @Test
  public void formatIn_single() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "value", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_single_with_space() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "value 1", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value 1\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_single_with_semicolon() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "value;1", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value;1\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_single_with_quotes() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "\"value\"", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_single_with_pipe() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "\"value|1\"", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value|1\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_two() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "value|2", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value\";\"2\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_two_quotes() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "\"value\"|2", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value\";\"2\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_two_quotes_with_whitespace_1() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "  \"value\"  |2", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"value\";\"2\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_two_quotes_with_whitespace_2() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "2|  \"value\"  ", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"2\";\"value\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_two_whitepace() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "2|value 2", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"2\";\"value 2\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_multiple() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "2|value 2|  \"whitespace&\" |test|\"||\"", false ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];\"2\";\"value 2\";\"whitespace&\";\"test\";\"||\")", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatIn_parameterized() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.IN, objectName, "test", true ); //$NON-NLS-1$
    Assert.assertEquals( "IN([CAT.COL];[param:test])", formatted ); //$NON-NLS-1$
  }

  @Test
  public void formatGreaterThan() {
    final OperatorFormatter f = new OperatorFormatter();
    final String objectName = "[CAT.COL]"; //$NON-NLS-1$
    String formatted = f.formatCondition( Operator.GREATER_THAN, objectName, "100", false ); //$NON-NLS-1$
    Assert.assertEquals( "[CAT.COL] >100", formatted ); //$NON-NLS-1$
  }
}
