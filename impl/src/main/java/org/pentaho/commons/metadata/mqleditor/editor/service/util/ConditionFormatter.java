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

import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;

/**
 * Formats a {@link MqlCondition} into a {@code String} to be displayed in the MQL Editor dialog.
 */
public class ConditionFormatter {

  private OperatorFormatter opFormatter;

  public ConditionFormatter( OperatorFormatter of ) {
    opFormatter = of;
  }

  /**
   * Returns the comparision plus value, i.e "= 'Atlanta'"
   * 
   * @return a string formatted to support parameters
   */
  public String getCondition( final MqlCondition c, final String objName ) {
    return getCondition( c, objName, true );
  }

  /**
   * Returns the comparision plus value, i.e "= 'Atlanta'"
   * 
   * @param enforceParam
   *          flag to format the Condition for parameters
   * @return a string formatted to support parameters based on the enforceParams flag
   */
  public String getCondition( final MqlCondition c, final String objName, final boolean enforceParameters ) {
    String val = c.getValue();

    // Date is a special case where we craft a formula function.
    if ( c.getColumn().getType() == ColumnType.DATE ) {
      if ( c.isParameterized() && enforceParameters ) {
        // Due to the fact that the value of a Date is a forumula function, the tokenizing of
        // the value needs to happen here instead of letting the Operator class handle it.
        val = "DATEVALUE(" + "[param:" + c.getValue().replaceAll( "[\\{\\}]", "" ) + "]" + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        return opFormatter.formatCondition( c.getOperator(), objName, val, false );
      } else {
        val = "DATEVALUE(\"" + val + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    return opFormatter.formatCondition( c.getOperator(), objName, val, c.isParameterized() && enforceParameters );
  }
}
