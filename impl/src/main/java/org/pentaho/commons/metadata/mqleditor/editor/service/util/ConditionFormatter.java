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
