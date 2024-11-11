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

public interface MqlCondition extends Serializable {

  public boolean validate();

  public MqlColumn getColumn();

  public Operator getOperator();

  /**
   * @return if isParameterized() then the name of the parameter whose value will be substituted before query execution,
   *         else the literal value
   */
  public String getValue();

  public CombinationType getCombinationType();

  /**
   * Value in this condition is not static, but rather supplied for each execution of this query.
   * 
   * @return true if value denotes parameter name rather than a literal value
   */
  public boolean isParameterized();

  public String getDefaultValue();

  public AggType getSelectedAggType();
}
