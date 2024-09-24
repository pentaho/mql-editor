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

package org.pentaho.commons.metadata.mqleditor.utils;

import org.junit.Assert;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.FormulaParser;

public class FormulaParserTest {

  @Test
  public void parseEquals() {
    final String formula = "EQUALS([CATEGORY.COLUMN];\"Value 1;with semicolon\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.EQUAL, parser.getCondition().getOperator() );
    Assert.assertEquals( "Value 1;with semicolon", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void parseEqualsWithParenthesis_single() {
    final String formula = "EQUALS([CATEGORY.COLUMN];\"Sale Manager (EMEA)\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.EQUAL, parser.getCondition().getOperator() );
    Assert.assertEquals( "Sale Manager (EMEA)", parser.getCondition().getValue() ); //$NON-NLS-1$
    Assert.assertEquals( 1, parser.getValueAsArray().length );
    Assert.assertEquals( "Sale Manager (EMEA)", parser.getValueAsArray()[0] ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_WithParenthesis_multiple() {
    final String formula = "IN([CATEGORY.COLUMN];\"Sale Manager (EMEA)\";\"(Test) - Test (1)\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( 2, parser.getValueAsArray().length );
    Assert.assertEquals( "Sale Manager (EMEA)", parser.getValueAsArray()[0] ); //$NON-NLS-1$
    Assert.assertEquals( "(Test) - Test (1)", parser.getValueAsArray()[1] ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( "Value", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_numeric() {
    final String formula = "IN([CATEGORY.COLUMN];100)"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( "100", parser.getCondition().getValue() );
    Assert.assertEquals( 1, parser.getValueAsArray().length );
    Assert.assertEquals( "100", parser.getValueAsArray()[0] ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_with_space() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value 1\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    // Not quoted since it's a single value
    Assert.assertEquals( "Value 1", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_numeric_with_spaces() {
    final String formula = "IN([CATEGORY.COLUMN]; 100 )"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( "100", parser.getCondition().getValue() );
    Assert.assertEquals( 1, parser.getValueAsArray().length );
    Assert.assertEquals( "100", parser.getValueAsArray()[0] ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_with_semicolon() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value;1\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    // Not quoted since it's a single value
    Assert.assertEquals( "Value;1", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_with_pipe() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value|1\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    // Quoted since it contains the separator character (pipe)
    Assert.assertEquals( "\"Value|1\"", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_multiple() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value1\";\"Value 2\";\"Value;3\";\"Value|4\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( "Value1|Value 2|Value;3|\"Value|4\"", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_multiple_numeric() {
    final String formula = "IN([CATEGORY.COLUMN]; 100; 0; 30.3 )"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( 3, parser.getValueAsArray().length );
    Assert.assertEquals( "100", parser.getValueAsArray()[0] ); //$NON-NLS-1$
    Assert.assertEquals( "0", parser.getValueAsArray()[1] ); //$NON-NLS-1$
    Assert.assertEquals( "30.3", parser.getValueAsArray()[2] ); //$NON-NLS-1$
  }

  @Test
  public void parseIN_parameterized() {
    final String formula = "IN([CATEGORY.COLUMN];[param:test])"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( "[param:test]", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void getValuesAsArray_single() {
    final String formula = "EQUALS([CATEGORY.COLUMN];\"value\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.EQUAL, parser.getCondition().getOperator() );
    Assert.assertEquals( "value", parser.getCondition().getValue() ); //$NON-NLS-1$
    Assert.assertNotNull( parser.getValueAsArray() );
    Assert.assertEquals( 1, parser.getValueAsArray().length );
    Assert.assertEquals( "value", parser.getValueAsArray()[0] ); //$NON-NLS-1$
  }

  @Test
  public void getValuesAsArray_multiple() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value1\";\"Value 2\";\"Value;3\";\"Value|4\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( "Value1|Value 2|Value;3|\"Value|4\"", parser.getCondition().getValue() ); //$NON-NLS-1$
    Assert.assertNotNull( parser.getValueAsArray() );
    Assert.assertEquals( 4, parser.getValueAsArray().length );
    Assert.assertEquals( "Value1", parser.getValueAsArray()[0] ); //$NON-NLS-1$
    Assert.assertEquals( "Value 2", parser.getValueAsArray()[1] ); //$NON-NLS-1$
    Assert.assertEquals( "Value;3", parser.getValueAsArray()[2] ); //$NON-NLS-1$
    Assert.assertEquals( "Value|4", parser.getValueAsArray()[3] ); //$NON-NLS-1$
  }

  @Test
  public void getValuesAsArray_no_value() {
    final String formula = "ISNA([CATEGORY.COLUMN])"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IS_NULL, parser.getCondition().getOperator() );
    Assert.assertEquals( "", parser.getCondition().getValue() ); //$NON-NLS-1$
    Assert.assertNotNull( parser.getValueAsArray() );
    Assert.assertEquals( 1, parser.getValueAsArray().length );
    Assert.assertEquals( "", parser.getValueAsArray()[0] ); //$NON-NLS-1$
  }

  @Test
  public void getValuesAsArray_parameter() {
    final String formula = "IN([CATEGORY.COLUMN];[param:p])"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.IN, parser.getCondition().getOperator() );
    Assert.assertEquals( "[param:p]", parser.getCondition().getValue() ); //$NON-NLS-1$
    Assert.assertNotNull( parser.getValueAsArray() );
    Assert.assertEquals( 1, parser.getValueAsArray().length );
    Assert.assertEquals( "[param:p]", parser.getValueAsArray()[0] ); //$NON-NLS-1$
  }

  @Test
  public void parseEquals_withDateFunction() {
    String formula = "EQUALS([CATEGORY.COLUMN];DATEVALUE([param:DateParam]))";
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() ); //$NON-NLS-1$
    Assert.assertEquals( "COLUMN", parser.getColID() ); //$NON-NLS-1$
    Assert.assertEquals( Operator.EQUAL, parser.getCondition().getOperator() );
    Assert.assertEquals( "[param:DateParam]", parser.getCondition().getValue() ); //$NON-NLS-1$
  }

  @Test
  public void parseAggregationType() {
    String formula = "EQUALS([CATEGORY.COLUMN.AGG_TYPE];DATEVALUE([param:DateParam]))";
    final FormulaParser parser = new FormulaParser( formula );

    Assert.assertEquals( "CATEGORY", parser.getCatID() );
    Assert.assertEquals( "COLUMN", parser.getColID() );
    Assert.assertEquals( "AGG_TYPE", parser.getAggType() );
  }
}
