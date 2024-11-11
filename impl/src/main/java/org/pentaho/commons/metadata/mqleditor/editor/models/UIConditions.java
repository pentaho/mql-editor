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
