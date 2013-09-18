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

package org.pentaho.commons.metadata.mqleditor.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.FormulaParser;

import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormulaParserTest {

  @Test
  public void parseEquals() {
    final String formula = "EQUALS([CATEGORY.COLUMN];\"Value 1;with semicolon\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.EQUAL, parser.getCondition().getOperator());
    assertEquals("Value 1;with semicolon", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void parseEqualsWithParenthesis_single() {
    final String formula = "EQUALS([CATEGORY.COLUMN];\"Sale Manager (EMEA)\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.EQUAL, parser.getCondition().getOperator());
    assertEquals("Sale Manager (EMEA)", parser.getCondition().getValue()); //$NON-NLS-1$
    assertEquals(1, parser.getValueAsArray().length);
    assertEquals("Sale Manager (EMEA)", parser.getValueAsArray()[0]); //$NON-NLS-1$
  }

  @Test
  public void parseIN_WithParenthesis_multiple() {
    final String formula = "IN([CATEGORY.COLUMN];\"Sale Manager (EMEA)\";\"(Test) - Test (1)\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals(2, parser.getValueAsArray().length);
    assertEquals("Sale Manager (EMEA)", parser.getValueAsArray()[0]); //$NON-NLS-1$
    assertEquals("(Test) - Test (1)", parser.getValueAsArray()[1]); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals("Value", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_numeric() {
    final String formula = "IN([CATEGORY.COLUMN];100)"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals("100", parser.getCondition().getValue());
    assertEquals(1, parser.getValueAsArray().length);
    assertEquals("100", parser.getValueAsArray()[0]); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_with_space() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value 1\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    // Not quoted since it's a single value
    assertEquals("Value 1", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_numeric_with_spaces() {
    final String formula = "IN([CATEGORY.COLUMN]; 100 )"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals("100", parser.getCondition().getValue());
    assertEquals(1, parser.getValueAsArray().length);
    assertEquals("100", parser.getValueAsArray()[0]); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_with_semicolon() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value;1\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    // Not quoted since it's a single value
    assertEquals("Value;1", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void parseIN_single_with_pipe() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value|1\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    // Quoted since it contains the separator character (pipe)
    assertEquals("\"Value|1\"", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void parseIN_multiple() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value1\";\"Value 2\";\"Value;3\";\"Value|4\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals("Value1|Value 2|Value;3|\"Value|4\"", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void parseIN_multiple_numeric() {
    final String formula = "IN([CATEGORY.COLUMN]; 100; 0; 30.3 )"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals(3, parser.getValueAsArray().length);
    assertEquals("100", parser.getValueAsArray()[0]); //$NON-NLS-1$
    assertEquals("0", parser.getValueAsArray()[1]); //$NON-NLS-1$
    assertEquals("30.3", parser.getValueAsArray()[2]); //$NON-NLS-1$
  }
  
  @Test
  public void parseIN_parameterized() {
    final String formula = "IN([CATEGORY.COLUMN];[param:test])"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals("[param:test]", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void getValuesAsArray_single() {
    final String formula = "EQUALS([CATEGORY.COLUMN];\"value\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.EQUAL, parser.getCondition().getOperator());
    assertEquals("value", parser.getCondition().getValue()); //$NON-NLS-1$
    assertNotNull(parser.getValueAsArray());
    assertEquals(1, parser.getValueAsArray().length);
    assertEquals("value", parser.getValueAsArray()[0]); //$NON-NLS-1$
  }

  @Test
  public void getValuesAsArray_multiple() {
    final String formula = "IN([CATEGORY.COLUMN];\"Value1\";\"Value 2\";\"Value;3\";\"Value|4\")"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals("Value1|Value 2|Value;3|\"Value|4\"", parser.getCondition().getValue()); //$NON-NLS-1$
    assertNotNull(parser.getValueAsArray());
    assertEquals(4, parser.getValueAsArray().length);
    assertEquals("Value1", parser.getValueAsArray()[0]); //$NON-NLS-1$
    assertEquals("Value 2", parser.getValueAsArray()[1]); //$NON-NLS-1$
    assertEquals("Value;3", parser.getValueAsArray()[2]); //$NON-NLS-1$
    assertEquals("Value|4", parser.getValueAsArray()[3]); //$NON-NLS-1$
  }

  @Test
  public void getValuesAsArray_no_value() {
    final String formula = "ISNA([CATEGORY.COLUMN])"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IS_NULL, parser.getCondition().getOperator());
    assertEquals("", parser.getCondition().getValue()); //$NON-NLS-1$
    assertNotNull(parser.getValueAsArray());
    assertEquals(1, parser.getValueAsArray().length);
    assertEquals("", parser.getValueAsArray()[0]); //$NON-NLS-1$
  }
  
  @Test
  public void getValuesAsArray_parameter() {
    final String formula = "IN([CATEGORY.COLUMN];[param:p])"; //$NON-NLS-1$
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.IN, parser.getCondition().getOperator());
    assertEquals("[param:p]", parser.getCondition().getValue()); //$NON-NLS-1$
    assertNotNull(parser.getValueAsArray());
    assertEquals(1, parser.getValueAsArray().length);
    assertEquals("[param:p]", parser.getValueAsArray()[0]); //$NON-NLS-1$
  }
  
  @Test
  public void parseEquals_withDateFunction() {
    String formula = "EQUALS([CATEGORY.COLUMN];DATEVALUE([param:DateParam]))";
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID()); //$NON-NLS-1$
    assertEquals("COLUMN", parser.getColID()); //$NON-NLS-1$
    assertEquals(Operator.EQUAL, parser.getCondition().getOperator());
    assertEquals("[param:DateParam]", parser.getCondition().getValue()); //$NON-NLS-1$
  }

  @Test
  public void parseAggregationType() {
    String formula = "EQUALS([CATEGORY.COLUMN.AGG_TYPE];DATEVALUE([param:DateParam]))";
    final FormulaParser parser = new FormulaParser(formula);

    assertEquals("CATEGORY", parser.getCatID());
    assertEquals("COLUMN", parser.getColID());
    assertEquals("AGG_TYPE", parser.getAggType());
  }
}
