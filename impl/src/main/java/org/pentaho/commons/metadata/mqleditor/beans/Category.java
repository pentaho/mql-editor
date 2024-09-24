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

package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlCategory;

public class Category implements MqlCategory {

  private String id, name;
  private List<Column> columns = new ArrayList<Column>();

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setId( String id ) {
    this.id = id;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public List<Column> getBusinessColumns() {
    return columns;
  }

  public void setBusinessColumns( List<Column> columns ) {
    this.columns = columns;
  }

}
