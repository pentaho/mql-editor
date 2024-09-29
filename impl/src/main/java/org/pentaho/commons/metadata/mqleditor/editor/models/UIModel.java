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


package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlModel;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UIModel extends AbstractModelNode<UICategory> implements MqlModel {

  private List<UICategory> categories = new ArrayList<UICategory>();

  private String id, name;

  public UIModel( Model model ) {
    this.id = model.getId();
    this.name = model.getName();

    for ( Category cat : model.getCategories() ) {
      this.children.add( new UICategory( cat ) );
    }
  }

  public UIModel() {

  }

  @Bindable
  public String getId() {
    return id;
  }

  @Bindable
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

  @Bindable
  public List<UICategory> getCategories() {
    return this.getChildren();
  }

  @Bindable
  public void setCategories( List<UICategory> cats ) {
    this.children = cats;
  }

}
