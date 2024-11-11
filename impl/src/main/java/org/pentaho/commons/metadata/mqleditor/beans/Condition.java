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


package org.pentaho.commons.metadata.mqleditor.beans;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.Operator;

public class Condition implements MqlCondition {

  private Column column;
  private Operator operator = Operator.EQUAL;
  private String value;
  private CombinationType comboType = CombinationType.AND;
  private boolean parameterized;
  private String defaultValue;
  private AggType selectedAggType;

  public Condition() {

  }

  public Column getColumn() {
    return this.column;
  }

  public CombinationType getCombinationType() {
    return this.comboType;
  }

  public Operator getOperator() {
    return this.operator;
  }

  public String getValue() {
    return this.value;
  }

  public void setColumn( Column column ) {
    this.column = column;

    if ( column.getType() == ColumnType.TEXT && operator == Operator.EQUAL ) {
      operator = Operator.EXACTLY_MATCHES;
    }
  }

  public void setCombinationType( CombinationType combinationType ) {
    this.comboType = combinationType;
  }

  public void setOperator( Operator operator ) {
    this.operator = operator;
  }

  public void setValue( String value ) {
    this.value = value;
  }

  public boolean validate() {
    return true;
  }

  public boolean isParameterized() {
    return parameterized;
  }

  public void setParameterized( boolean parameterized ) {
    this.parameterized = parameterized;
  }

  public void setDefaultValue( String val ) {
    this.defaultValue = val;
  }

  public String getDefaultValue() {
    return this.defaultValue;
  }

  public void setSelectedAggType( AggType aggType ) {
    this.selectedAggType = aggType;
  }

  public AggType getSelectedAggType() {
    return this.selectedAggType;
  }

}
