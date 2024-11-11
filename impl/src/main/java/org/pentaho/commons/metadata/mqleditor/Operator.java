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


package org.pentaho.commons.metadata.mqleditor;

import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Operator is used in the definition of a @see MqlCondition
 */
public enum Operator implements Serializable {

  GREATER_THAN( ">", 1, true ), LESS_THAN( "<", 1, true ), EQUAL( "=", 1, true ), GREATOR_OR_EQUAL( ">=", 1, true ), LESS_OR_EQUAL(
      "<=", 1, true ),

  EXACTLY_MATCHES( "exactly matches", 0, true ), CONTAINS( "contains", 0, true ), DOES_NOT_CONTAIN( "does not contain",
      0, true ), BEGINS_WITH( "begins with", 0, true ), ENDS_WITH( "ends with", 0, true ),

  IS_NULL( "is null", 2, false ), IS_NOT_NULL( "is not null", 2, false ),

  IN( "in", 0, true ), NOT_EQUAL( "<>", 1, true );

  private static final String OPERATOR_PREFIX = "Operator.";

  private String strVal;
  // 0 = string
  // 1 = numeric
  // 2 = both
  private int operatorType;
  private boolean requiresValue;

  Operator( String str, int operatorType, boolean requiresValue ) {
    this.strVal = str;
    this.operatorType = operatorType;
    this.requiresValue = requiresValue;
  }

  public String toString() {
    if ( Workspace.getMessages() != null ) {
      return Workspace.getMessages().getString( OPERATOR_PREFIX + name(), strVal );
    } else {
      return strVal;
    }
  }

  public int getOperatorType() {
    return operatorType;
  }

  public boolean getRequiresValue() {
    return requiresValue;
  }

  public static Operator parse( String val ) {

    if ( val == null || val.equals( "" ) ) {
      return Operator.EQUAL;
    }
    // These are the UI equivalents that are re-resolved. Note this needs to be i18n
    // @TODO i18n
    if ( val.equals( ">" ) ) {
      return Operator.GREATER_THAN;
    } else if ( val.equals( ">=" ) ) {
      return Operator.GREATOR_OR_EQUAL;
    } else if ( val.equals( "=" ) ) {
      return Operator.EQUAL;
    } else if ( val.equals( "<" ) ) {
      return Operator.LESS_THAN;
    } else if ( val.equals( "<=" ) ) {
      return Operator.LESS_OR_EQUAL;
    } else if ( val.equals( "<>" ) ) {
      return Operator.NOT_EQUAL;
    } else if ( val.equals( "exactly matches" ) ) {
      return Operator.EXACTLY_MATCHES;
    } else if ( val.equals( "contains" ) ) {
      return Operator.CONTAINS;
    } else if ( val.equals( "does not contain" ) ) {
      return Operator.DOES_NOT_CONTAIN;
    } else if ( val.equals( "begins with" ) ) {
      return Operator.BEGINS_WITH;
    } else if ( val.equals( "ends with" ) ) {
      return Operator.ENDS_WITH;
    } else if ( val.equals( "is null" ) ) {
      return Operator.IS_NULL;
    } else if ( val.equals( "is not null" ) ) {
      return Operator.IS_NOT_NULL;
    } else if ( val.equals( "in" ) ) {
      return Operator.IN;
    }

    // Actual generated Open Formula formula name is passed in from deserialization routine. Try to match those here.
    if ( val.equals( "CONTAINS" ) ) {
      return Operator.CONTAINS;
    } else if ( val.equals( "BEGINSWITH" ) ) {
      return Operator.BEGINS_WITH;
    } else if ( val.equals( "ENDSWITH" ) ) {
      return Operator.ENDS_WITH;
    } else if ( val.equals( "ISNA" ) ) {
      return Operator.IS_NULL;
    } else if ( val.equals( "IN" ) ) {
      return Operator.IN;
    }

    return Operator.EQUAL;
  }

  public boolean requiresValue() {
    return requiresValue;
  }

  /**
   * Returns an array of types separated by whether or not they're string types
   * 
   * @param stringType
   * @return array of Operators
   */
  public static Operator[] values( boolean stringType ) {
    Operator[] vals = Operator.values();
    List<Operator> ops = new ArrayList<Operator>();
    for ( int i = 0; i < vals.length; i++ ) {
      if ( vals[i].operatorType == 2 ) {
        ops.add( vals[i] );
      } else if ( vals[i].operatorType == 0 && stringType ) {
        ops.add( vals[i] );
      } else if ( vals[i].operatorType == 1 && !stringType ) {
        ops.add( vals[i] );
      }
    }
    return ops.toArray( new Operator[] {} );
  }
}
