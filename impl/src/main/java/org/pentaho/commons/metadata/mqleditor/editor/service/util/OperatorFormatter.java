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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pentaho.commons.metadata.mqleditor.Operator;

/**
 * Formats an {@link Operator} into a {@code String} to be displayed in the MQL Editor dialog.
 */
public class OperatorFormatter {
  private static final String PIPE_EVAL = "[|]{0,1}"; //$NON-NLS-1$
  // Matches [test], [test|], [test |]
  private static final String IN_EVAL = "([^|]+)" + PIPE_EVAL; //$NON-NLS-1$
  // Matches: ["test"], ["test|ing"], ["test|" |]
  private static final String IN_EVAL_QUOTES = "\\s*\"([^\"]*)\"\\s*" + PIPE_EVAL; //$NON-NLS-1$
  private static final Pattern IN_EVAL_PAT = Pattern.compile( IN_EVAL_QUOTES + "|" + IN_EVAL ); //$NON-NLS-1$

  public String formatCondition( Operator op, String objectName, String value, boolean parameterized ) {
    if ( parameterized ) {
      value = "[param:" + value.replaceAll( "[\\{\\}]", "" ) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    } else if ( op == Operator.IN ) {
      Matcher m = IN_EVAL_PAT.matcher( value );
      StringBuilder sb = new StringBuilder();
      while ( m.find() ) {
        if ( sb.length() > 0 ) {
          sb.append( ";" ); //$NON-NLS-1$
        }
        sb.append( "\"" ); //$NON-NLS-1$
        sb.append( m.group( 1 ) != null ? m.group( 1 ) : m.group( 2 ) );
        sb.append( "\"" ); //$NON-NLS-1$
      }
      if ( sb.length() > 0 ) {
        value = sb.toString();
      }
    } else if ( op.getOperatorType() == 0 || op.getOperatorType() == 2 ) {
      value = "\"" + value + "\""; //$NON-NLS-1$ //$NON-NLS-2$ 
    }
    String retVal = ""; //$NON-NLS-1$

    switch ( op ) {
      case EXACTLY_MATCHES:
      case EQUAL:
        retVal += "EQUALS(" + objectName + ";" + value + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case CONTAINS:
        retVal += "CONTAINS(" + objectName + ";" + value + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case DOES_NOT_CONTAIN:
        retVal += "NOT(CONTAINS(" + objectName + ";" + value + "))"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case BEGINS_WITH:
        retVal += "BEGINSWITH(" + objectName + ";" + value + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case ENDS_WITH:
        retVal += "ENDSWITH(" + objectName + ";" + value + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      case IS_NULL:
        retVal += "ISNA(" + objectName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case IS_NOT_NULL:
        retVal += "NOT(ISNA(" + objectName + "))"; //$NON-NLS-1$ //$NON-NLS-2$
        break;
      case IN:
        retVal += "IN(" + objectName + ";" + value + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        break;
      default:
        retVal = objectName + " " + op.toString(); //$NON-NLS-1$
        if ( op.getRequiresValue() ) {
          retVal += value;
        }
        break;
    }
    return retVal;

  }
}
