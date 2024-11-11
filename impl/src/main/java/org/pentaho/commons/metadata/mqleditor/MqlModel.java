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
 *
 * Represents a Metadata Model object containing one or more {@see MqlCategory}s
 *
 */
public interface MqlModel extends Serializable {

  public String getId();

  public String getName();

  public List<? extends MqlCategory> getCategories();

}
