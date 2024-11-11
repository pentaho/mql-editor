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


package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlCategory;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UICategory extends AbstractModelNode<UIColumn> implements MqlCategory {

  private String id, name;

  public UICategory( Category category ) {
    this.id = category.getId();
    this.name = category.getName();

    for ( Column col : category.getBusinessColumns() ) {
      UIColumn c = new UIColumn( col );
      this.children.add( c );
    }
  }

  public String getId() {
    return id;
  }

  public void setId( String id ) {
    this.id = id;
  }

  @Bindable
  public String getName() {
    return this.name;
  }

  @Bindable
  public void setName( String name ) {
    this.name = name;
  }

  public UICategory() {

  }

  public List<UIColumn> getBusinessColumns() {
    return this.getChildren();
  }

  public void setBusinessColumns( List<UIColumn> cols ) {
    this.children = cols;
  }

}
