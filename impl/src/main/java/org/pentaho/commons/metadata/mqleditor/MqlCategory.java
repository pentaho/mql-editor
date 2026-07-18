/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 - 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/



package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a Metadata Category containing a collection of {@see MqlColumn}s
 *
 */
public interface MqlCategory extends Serializable {

  public String getId();

  public String getName();

  public List<? extends MqlColumn> getBusinessColumns();

}
