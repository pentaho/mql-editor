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
package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.ui.xul.stereotype.Bindable;

public class UIDomain extends AbstractModelNode<UIModel> implements MqlDomain {
  
  private String id = "default";
  private String name;
  
  public UIDomain(){
  }
  
  public UIDomain(Domain domain){
    if(domain.getId() != null){
      this.id = domain.getId();
    }
    this.name = domain.getName();
    
    for(Model model : domain.getModels()){
      this.children.add(new UIModel(model));
    }
  }

  @Bindable
  public String getId() {
    return id;
  }
  
  @Bindable
  public void setId(String id){
    this.id = id;
  }

  @Bindable
  public String getName() {
    return this.name;
  }
  
  @Bindable
  public void setName(String name){
    this.name = name;
  }
  
  @Bindable
  public List<UIModel> getModels() {
    return this.getChildren();
  }

  @Bindable
  public void setModels(List<UIModel> models) {
    this.children = models;
  }
  
}

  