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
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.gwt.widgets.client.controls;

import org.pentaho.gwt.widgets.client.buttons.ImageButton;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.utils.ListBoxUtils;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TableEditor extends VerticalPanel {
  
  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();
  
  private ImageButton addBtn = new ImageButton("style/images/add.png", "style/images/add_disabled.png", MSGS.addItem(), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$
  private ImageButton deleteBtn = new ImageButton("style/images/remove.png", "style/images/remove_disabled.png", MSGS.deleteItems(), 15, 15); //$NON-NLS-1$ //$NON-NLS-2$
  private ListBox actionLb = new ListBox();
  private ErrorLabel errorLabel = null;
  private ICallback<TableEditor> onSelectHandler = null;
  private static int DEFAULT_NUM_VISIBLE_ITEMS = 10;
  private ICallback<TableEditor> onAddHandler = null;
  private ICallback<TableEditor> onDeleteHandler = null;
  
  public TableEditor( String labelText ) {

    DockPanel buttonPanel = new DockPanel();
    
    buttonPanel.add(deleteBtn, DockPanel.EAST);
    VerticalPanel spacer = new VerticalPanel();
    spacer.setWidth("2"); //$NON-NLS-1$
    buttonPanel.add(spacer, DockPanel.EAST);
    buttonPanel.add(addBtn, DockPanel.EAST);
    
    errorLabel = new ErrorLabel( new Label( labelText ) );
    buttonPanel.add(errorLabel, DockPanel.WEST);
    buttonPanel.setCellWidth(errorLabel, "100%"); //$NON-NLS-1$
    
    add( buttonPanel );
    
    actionLb.setWidth( "100%" ); //$NON-NLS-1$
    actionLb.setHeight( "100%" ); //$NON-NLS-1$
    actionLb.setVisibleItemCount( DEFAULT_NUM_VISIBLE_ITEMS );
    actionLb.setMultipleSelect( true );
    final TableEditor localThis = this;
    
    actionLb.addClickListener( new ClickListener() {
      public void onClick(Widget arg0) {
        if ( null != onSelectHandler ) {
          onSelectHandler.onHandle( localThis );
        }
      }
    });
    add( actionLb );
    setCellHeight( actionLb, "100%" ); //$NON-NLS-1$
    addBtn.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        localThis.handleAdd();
      }
    });
    
    deleteBtn.addClickListener( new ClickListener() {
      public void onClick(Widget sender) {
        localThis.handleDeleteSelectedItems();
      }
    });
  }
  
  private void handleAdd() {
    if ( null != onAddHandler ) {
      onAddHandler.onHandle( this );
    }
  }
  
  private void handleDeleteSelectedItems() {
    removeSelectedItems();
    if ( null != onDeleteHandler ) {
      onDeleteHandler.onHandle( this );
    }
  }
  
  public void removeSelectedItems() {
    for ( int ii=getItemCount()-1; ii>=0; --ii ) {
      if ( actionLb.isItemSelected( ii ) ) {
        actionLb.removeItem( ii );
      }
    }
  }
  
  public void setVisibleItemCount( int numVisibleItems) {
    actionLb.setVisibleItemCount( numVisibleItems );
  }
  
  public int getNumSelectedItems() {
    int count = 0;
    for ( int ii=getItemCount()-1; ii>=0; --ii ) {
      if ( actionLb.isItemSelected( ii ) ) {
        count++;
      }
    }
    return count;
  }
  
  public int getItemCount() {
    return actionLb.getItemCount();
  }
  
  public void removeAll() {
    ListBoxUtils.removeAll( actionLb );
  }
  
  private boolean isExist(String item) {
    boolean returnValue = false;
    for ( int ii=0; ii<getItemCount(); ii++ ) {
      if ( actionLb.getItemText(ii) != null) {
        if(!actionLb.getItemText(ii).equals(item)) {
          continue;
        }  else {
          returnValue = true;
          break;
        }
      }
    }
    return returnValue;
  }
  public void addItem( String item, String value ) {
    if(!isExist(item)) {
      actionLb.addItem( item );
      actionLb.setValue( actionLb.getItemCount()-1, value );
    }
  }
  
  public String getItemText( int idx ) {
    return actionLb.getItemText( idx );
  }
  
  public String getItemValue( int idx ) {
    return actionLb.getValue( idx );
  }
  
  public void setOnAddClickedHandler( ICallback<TableEditor> handler ) {
    onAddHandler = handler;
  }
  
  public void setOnDeleteClickedHandler( ICallback<TableEditor> handler ) {
    onDeleteHandler = handler;
  }
  
  public void setOnSelectHandler( ICallback<TableEditor> onSelectHandler ) {
    this.onSelectHandler = onSelectHandler;
  }

  public void setErrorMsg( String errorMsg ) {
    errorLabel.setErrorMsg( errorMsg );
  }

  /**
   * No longer available.
   */
  @Deprecated
  public void setFocus() {}
  
  public void setAddBtnEnabled( boolean enabled ) {
    addBtn.setEnabled( enabled );
  }
  
  public void setDeleteBtnEnabled( boolean enabled ) {
    deleteBtn.setEnabled( enabled );
  }
  
  private String message = null;
  public void setMessage( String message ) {
    actionLb.addItem( message );
    this.message = message;
  }
  
  public void clearMessage() {
    if ( null != message ) {
      for ( int ii=0; ii<actionLb.getItemCount(); ++ii ) {
        String item = actionLb.getItemText( ii );
        if ( message.equals( item ) ) {
          actionLb.removeItem( ii );
          return;
        }
      }
    }
    message = null;
  }
}
