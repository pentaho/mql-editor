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

import org.pentaho.commons.metadata.mqleditor.MqlDomain;

public class Domain implements MqlDomain {

  private String id = "default";
  private String name;

  private List<Model> models = new ArrayList<Model>();

  public String getId() {
    return id;
  }

  public List<Model> getModels() {
    return models;
  }

  public String getName() {
    if ( name == null ) {
      name = "default";
    }
    return name;
  }

  public void setId( String id ) {
    this.id = id;
  }

  public void setModels( List<Model> models ) {
    this.models = models;
  }

  public void setName( String name ) {
    this.name = name;
  }

}
