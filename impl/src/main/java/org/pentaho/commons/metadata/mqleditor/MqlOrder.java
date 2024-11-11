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

import java.io.Serializable;

public interface MqlOrder extends Serializable {

  public enum Type {
    ASC, DESC
  }

  public MqlColumn getColumn();

  public Type getOrderType();

  public AggType getSelectedAggType();

}
