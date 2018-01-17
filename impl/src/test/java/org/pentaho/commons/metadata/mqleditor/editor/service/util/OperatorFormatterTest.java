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
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

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
