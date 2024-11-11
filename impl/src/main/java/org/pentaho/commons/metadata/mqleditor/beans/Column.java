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

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;

public class Column implements MqlColumn {

  private String id, name;
  private ColumnType type;
  private List<AggType> aggTypes = new ArrayList<AggType>();
  private AggType defaultAggType;
  private AggType selectedAggType;
  private boolean persistent;

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public ColumnType getType() {
    return this.type;
  }

  public void setId( String id ) {
    this.id = id;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public void setType( ColumnType type ) {
    this.type = type;
  }

  public AggType getDefaultAggType() {
    return defaultAggType;
  }

  public List<AggType> getAggTypes() {
    return aggTypes;
  }

  public void setAggTypes( List<AggType> aggTypes ) {
    this.aggTypes = aggTypes;
  }

  public void setDefaultAggType( AggType defaultAggType ) {
    this.defaultAggType = defaultAggType;
  }

  public void setSelectedAggType( AggType aggType ) {
    this.selectedAggType = aggType;
  }

  public AggType getSelectedAggType() {
    return this.selectedAggType;
  }

  public Column clone() {
    Column c = new Column();
    c.setType( this.type );
    c.setAggTypes( this.aggTypes );
    c.setId( this.id );
    c.setName( this.name );
    c.setDefaultAggType( this.defaultAggType );
    c.setSelectedAggType( this.selectedAggType );
    c.setPersistent( this.persistent );
    return c;
  }

  public boolean isPersistent() {
    return persistent;
  }

  public void setPersistent( boolean persistent ) {
    this.persistent = persistent;
  }
}
