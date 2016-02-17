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

import org.pentaho.commons.metadata.mqleditor.MqlModel;

public class Model implements MqlModel {

  private List<Category> categories = new ArrayList<Category>();

  private String id, name;

  public List<Category> getCategories() {
    return categories;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setCategories( List<Category> categories ) {
    this.categories = categories;
  }

  public void setId( String id ) {
    this.id = id;
  }

  public void setName( String name ) {
    this.name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( categories == null ) ? 0 : categories.hashCode() );
    result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
    result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if ( this == obj ) {
      return true;
    }
    if ( obj == null ) {
      return false;
    }
    if ( getClass() != obj.getClass() ) {
      return false;
    }
    Model other = (Model) obj;
    if ( categories == null ) {
      if ( other.categories != null ) {
        return false;
      }
    } else if ( !categories.equals( other.categories ) ) {
      return false;
    }
    if ( id == null ) {
      if ( other.id != null ) {
        return false;
      }
    } else if ( !id.equals( other.id ) ) {
      return false;
    }
    if ( name == null ) {
      if ( other.name != null ) {
        return false;
      }
    } else if ( !name.equals( other.name ) ) {
      return false;
    }
    return true;
  }

}
