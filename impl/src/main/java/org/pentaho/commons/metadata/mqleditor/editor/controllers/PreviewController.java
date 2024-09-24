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

package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.editor.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulTreeCell;
import org.pentaho.ui.xul.components.XulTreeCol;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.containers.XulTreeItem;
import org.pentaho.ui.xul.containers.XulTreeRow;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.ui.xul.stereotype.Bindable;

public class PreviewController extends AbstractXulEventHandler {

  private Workspace workspace;

  private BindingFactory bf;

  private XulTree previewTree;

  private XulDialog previewDialog;

  private MQLEditorService service;

  private String query;

  private int page = 1;

  private String[][] previewData;

  private int previewLimit = 10;

  @Bindable
  public void init() {
    previewTree = (XulTree) document.getElementById( "previewTree" );
    previewDialog = (XulDialog) document.getElementById( "previewDialog" );
    bf.createBinding( document.getElementById( "previewLimit" ), "value", this, "previewLimit",
        new BindingConvertor<String, Integer>() {

          @Override
          public Integer sourceToTarget( String value ) {
            return Integer.parseInt( value );
          }

          @Override
          public String targetToSource( Integer value ) {
            return "" + value;
          }

        } );
  }

  public void setBindingFactory( BindingFactory bf ) {
    this.bf = bf;
  }

  public void setWorkspace( Workspace workspace ) {
    this.workspace = workspace;
  }

  public String getName() {
    return "previewController";

  }

  public MQLEditorService getService() {

    return service;
  }

  public void setService( MQLEditorService service ) {

    this.service = service;
  }

  @Bindable
  public void showPreview() {

    MqlQuery q = workspace.getMqlQuery();

    service.getPreviewData( workspace.getMqlQuery(), page, previewLimit, new XulServiceCallback<String[][]>() {

      public void error( String message, Throwable error ) {
        setPreviewData( new String[][] {} );
        MainController.showErrorDialog( cleanUpErrorMessage( error.getLocalizedMessage() ) );
      }

      public void success( String[][] retVal ) {
        setPreviewData( retVal );
        openDialog();
      }
    } );
  }

  @Bindable
  private void openDialog() {
    previewDialog.show();
  }

  @Bindable
  public String[][] getPreviewData() {
    return previewData;
  }

  @Bindable
  public void updateQuery() {

    service.getPreviewData( workspace.getMqlQuery(), page, previewLimit, new XulServiceCallback<String[][]>() {

      public void error( String message, Throwable error ) {
        MainController.showErrorDialog( cleanUpErrorMessage( error.getLocalizedMessage() ) );
      }

      public void success( String[][] retVal ) {
        setPreviewData( retVal );
      }
    } );
  }

  public void setPreviewData( String[][] previewData ) {
    this.previewData = previewData;
    if ( previewData == null || previewData.length == 0 ) {
      previewTree.setElements( null );
      return;
    }

    // Adjust number of columns as needed.
    int colCount = previewData[0].length;
    int curTreeColCount = previewTree.getColumns().getColumnCount();
    try {
      if ( colCount > curTreeColCount ) { // Add new Columns
        for ( int i = ( colCount - curTreeColCount ); i > 0; i-- ) {
          XulTreeCol col = (XulTreeCol) document.createElement( "treecol" );
          col.setFlex( 1 );
          previewTree.getColumns().addColumn( col );
        }
      } else if ( colCount < curTreeColCount ) { // Remove un-needed exiting columns
        List<XulComponent> cols = previewTree.getColumns().getChildNodes();

        for ( int i = ( curTreeColCount - colCount ); i < cols.size(); i++ ) {
          previewTree.getColumns().removeChild( cols.get( i ) );
        }
      }
    } catch ( XulException e ) {
      // TODO: add logging!!
      System.out.println( e.getMessage() );
      e.printStackTrace();
    }

    for ( int i = 0; i < previewTree.getColumns().getColumnCount(); i++ ) {
      previewTree.getColumns().getColumn( i ).setLabel( workspace.getSelections().get( i ).getPreviewName() );
    }

    previewTree.getRootChildren().removeAll();
    try {
      for ( int i = 0; i < previewData.length; i++ ) {
        XulTreeItem item = (XulTreeItem) document.createElement( "treeitem" );
        XulTreeRow row = (XulTreeRow) document.createElement( "treerow" );

        String[] r = previewData[i];
        for ( int y = 0; y < r.length; y++ ) {
          XulTreeCell cell = (XulTreeCell) document.createElement( "treecell" );
          cell.setLabel( ( r[y] != null ) ? r[y] : "" );
          row.addCell( cell );
        }

        item.addChild( row );
        previewTree.getRootChildren().addChild( item );
      }
      previewTree.update();
    } catch ( XulException e ) {
      // TODO: add logging
      System.out.println( e.getMessage() );
      e.printStackTrace();
    }

  }

  @Bindable
  public void closeDialog() {
    previewDialog.hide();
  }

  @Bindable
  public int getPreviewLimit() {

    return previewLimit;
  }

  @Bindable
  public void setPreviewLimit( int previewLimit ) {

    this.previewLimit = previewLimit;
  }

  private String cleanUpErrorMessage( String errorMessage ) {
    if ( errorMessage.startsWith( "SqlOpenFormula.ERROR_" ) ) {
      return errorMessage.substring( "SqlOpenFormula.ERROR_XXXX - ".length() );
    } else {
      return errorMessage;
    }
  }
}
