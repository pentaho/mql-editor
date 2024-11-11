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


package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.ui.xul.stereotype.Bindable;

public class SelectedColumnController extends AbstractXulEventHandler {

  private XulTree columnTree;
  private Workspace workspace;
  private BindingFactory bf;
  private static XulDialog denyRemoveColumnDialog;

  @Bindable
  public void init() {
    columnTree = (XulTree) document.getElementById( "selectedColumnTree" );

    BindingConvertor<int[], Boolean> buttonConvertor = new BindingConvertor<int[], Boolean>() {

      @Override
      public Boolean sourceToTarget( int[] value ) {
        return ( value == null || value.length == 0 );
      }

      @Override
      public int[] targetToSource( Boolean value ) {
        return null;
      }

    };

    bf.setBindingType( Binding.Type.ONE_WAY );
    bf.createBinding( columnTree, "selectedRows", "colUp", "disabled", buttonConvertor );
    bf.createBinding( columnTree, "selectedRows", "colDown", "disabled", buttonConvertor );
    bf.createBinding( columnTree, "selectedRows", "colRemove", "disabled", buttonConvertor );

    denyRemoveColumnDialog = (XulDialog) document.getElementById( "denyRemoveColumnDialog" );
  }

  public int getSelectedIndex() {
    int[] rows = this.columnTree.getAbsoluteSelectedRows();
    return ( rows != null && rows.length == 0 ) ? -1 : rows[0];
  }

  @Bindable
  public void moveUp() {
    try {
      int prevIndex = getSelectedIndex();
      workspace.getSelections().moveChildUp( getSelectedIndex() );
      columnTree.clearSelection();
      columnTree.setSelectedRows( new int[] { prevIndex - 1 } );
    } catch ( IllegalArgumentException e ) {
      // out of bounds
    }
  }

  @Bindable
  public void moveDown() {
    int prevIndex = getSelectedIndex();
    try {
      workspace.getSelections().moveChildDown( getSelectedIndex() );
      columnTree.clearSelection();
      columnTree.setSelectedRows( new int[] { prevIndex + 1 } );
    } catch ( IllegalArgumentException e ) {
      // out of bounds
    }
  }

  @Bindable
  public void remove() {
    if ( getSelectedIndex() < 0 ) {
      return;
    }
    if ( workspace.getSelections().get( getSelectedIndex() ).isPersistent() ) {
      if ( denyRemoveColumnDialog == null ) {
        throw new IllegalStateException( "Error dialog has not been loaded yet" );
      } else {
        denyRemoveColumnDialog.show();
      }
      return;
    }
    workspace.getSelections().remove( getSelectedIndex() );
    columnTree.clearSelection();
  }

  public void setWorkspace( Workspace workspace ) {
    this.workspace = workspace;
  }

  public String getName() {
    return "selectedColumns";
  }

  public void setBindingFactory( BindingFactory bf ) {
    this.bf = bf;
  }

  @Bindable
  public static void closeDenyRemoveColumnDialog() {
    if ( denyRemoveColumnDialog == null ) {
      throw new IllegalStateException( "Error dialog has not been loaded yet" );
    } else {
      denyRemoveColumnDialog.hide();
    }
  }
}
