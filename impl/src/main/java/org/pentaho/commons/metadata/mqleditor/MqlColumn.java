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
import java.util.List;

/**
 * Represents a Metadata Column object
 *
 * @param <T>
 *          implementation of a MqlBusinessTable
 */
public interface MqlColumn extends Serializable {

  public ColumnType getType();

  public String getId();

  public String getName();

  public AggType getDefaultAggType();

  public List<AggType> getAggTypes();

  public AggType getSelectedAggType();

  public boolean isPersistent();
}
