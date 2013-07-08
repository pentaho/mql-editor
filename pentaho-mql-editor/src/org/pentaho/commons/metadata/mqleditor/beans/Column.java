/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2009 Pentaho Corporation.  All rights reserved.
 */
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
  
  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;   
  }

  public ColumnType getType() {
    return this.type;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(ColumnType type) {
    this.type = type;
  }

  public AggType getDefaultAggType() {
    return defaultAggType;
  }

  public List<AggType> getAggTypes() {
    return aggTypes;
  }

  public void setAggTypes(List<AggType> aggTypes) {
    this.aggTypes = aggTypes;
  }

  public void setDefaultAggType(AggType defaultAggType) {
    this.defaultAggType = defaultAggType;
  }

  public void setSelectedAggType(AggType aggType){
    this.selectedAggType = aggType;
  }
  
  public AggType getSelectedAggType(){
    return this.selectedAggType;
  }

  public Column clone() {
    Column c = new Column();
    c.setType(this.type);
    c.setAggTypes(this.aggTypes);
    c.setId(this.id);
    c.setName(this.name);
    c.setDefaultAggType(this.defaultAggType);
    c.setSelectedAggType(this.selectedAggType);
    return c;
  }
}
