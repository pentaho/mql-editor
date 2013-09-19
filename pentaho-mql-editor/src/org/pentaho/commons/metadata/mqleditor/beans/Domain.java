/*!
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
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

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
    if(name == null) {
        name = "default";
    }
    return name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setModels(List<Model> models) {
    this.models = models;
  }

  public void setName(String name) {
    this.name = name;
  }

}
