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

package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

/**
 * Defines the logical relationship between two {@see MqlCondition}s
 *
 */
public enum CombinationType implements Serializable {
  AND( "AND" ), OR( "OR" ), AND_NOT( "AND NOT" ), OR_NOT( "OR NOT" );

  private String toStringVal;

  private CombinationType( String val ) {
    toStringVal = val;
  }

  @Override
  public String toString() {
    return toStringVal;
  }

  public static CombinationType getByName( String value ) {
    for ( CombinationType type : CombinationType.values() ) {
      if ( type.toStringVal.equals( value ) ) {
        return type;
      }
    }
    return null;
  }
}
