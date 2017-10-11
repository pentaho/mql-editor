/*
 * This program is free software; you can redistribute it and/or modify it under the
 *  terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 *  Foundation.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this
 *  program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *  or from the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  Copyright (c) 2006 - 2017 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import org.junit.Assert;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.metadata.model.concept.types.AggregationType;

public class MQLEditorServiceDelegateTest {
  @Test
  public void convertNewThinAggregationTypeTest() {
    MQLEditorServiceDelegate mqlESD = new MQLEditorServiceDelegate();

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( null ), AggType.NONE );

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( AggregationType.COUNT ), AggType.COUNT );

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( AggregationType.COUNT_DISTINCT ), AggType.COUNT_DISTINCT );

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( AggregationType.AVERAGE ), AggType.AVERAGE );

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( AggregationType.MINIMUM ), AggType.MIN );

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( AggregationType.MAXIMUM ), AggType.MAX );

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( AggregationType.SUM ), AggType.SUM );

    Assert.assertEquals( mqlESD.convertNewThinAggregationType( AggregationType.NONE ), AggType.NONE );
  }
}
