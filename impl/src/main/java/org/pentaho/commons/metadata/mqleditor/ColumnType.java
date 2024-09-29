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
import java.sql.Date;

/**
 * ColumnType represents the data type of a {@see MqlColumn}
 *
 */
public enum ColumnType implements Serializable {
  NUMERIC( Integer.class ), TEXT( String.class ), FLOAT( Float.class ), BOOLEAN( Boolean.class ), DATE( Date.class );

  private final Class clazz;

  ColumnType( Class cls ) {
    this.clazz = cls;
  }

  boolean validate( Object val ) {
    return clazz.getClass().equals( clazz );
  }
}
