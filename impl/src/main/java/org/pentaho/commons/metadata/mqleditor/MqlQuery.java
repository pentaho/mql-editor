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
import java.util.Map;

/**
 * Represents an MQL Query. Contains a list of selected columns, conditions and order information. For convenience it
 * also has the serialized xml representation.
 *
 */
public interface MqlQuery extends Serializable {

  public List<? extends MqlColumn> getColumns();

  public List<? extends MqlCondition> getConditions();

  public List<? extends MqlOrder> getOrders();

  public int getLimit();

  public MqlDomain getDomain();

  public MqlModel getModel();

  public String getMqlStr();

  public Map<String, Object> getDefaultParameterMap();

}
