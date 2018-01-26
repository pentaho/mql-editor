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
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.List;

public class UIConditions extends AbstractModelNode<UICondition> {

  public UIConditions() {
  }

  public UIConditions( List<UICondition> conditions ) {
    super( conditions );
  }

  @Override
  protected void fireCollectionChanged() {
    markTopMostCondition();
    this.changeSupport.firePropertyChange( "children", null, this.getChildren() );
  }

  private void markTopMostCondition() {
    for ( int index = 0; index < children.size(); index++ ) {
      if ( index == 0 ) {
        children.get( index ).setTopMost( true );
      } else {
        children.get( index ).setTopMost( false );
      }
    }
  }

}
