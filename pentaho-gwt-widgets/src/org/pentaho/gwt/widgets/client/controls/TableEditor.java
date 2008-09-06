package org.pentaho.gwt.widgets.client.controls;

import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.utils.ListBoxUtils;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TableEditor extends VerticalPanel {
  
  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();
  private Button deleteBtn = new Button( "-" ); //$NON-NLS-1$
  private Button addBtn = new Button( "+" ); //$NON-NLS-1$
  private ListBox actionLb = new ListBox();
  private ErrorLabel errorLabel = null;
  private ICallback<TableEditor> onSelectHandler = null;
  private static int DEFAULT_NUM_VISIBLE_ITEMS = 10;
  private ICallback<TableEditor> onAddHandler = null;
  private ICallback<TableEditor> onDeleteHandler = null;
  
  public TableEditor( String labelText ) {

    DockPanel buttonPanel = new DockPanel();
    deleteBtn.addStyleName( "deleteBtn" ); //$NON-NLS-1$
    addBtn.addStyleName( "addBtn" ); //$NON-NLS-1$
    
    addBtn.setTitle(MSGS.addItem());
    deleteBtn.setTitle(MSGS.deleteItems());
    
    buttonPanel.add(deleteBtn, DockPanel.EAST);
    buttonPanel.add(addBtn, DockPanel.EAST);
    
    errorLabel = new ErrorLabel( new Label( labelText ) );
    buttonPanel.add(errorLabel, DockPanel.WEST);
    buttonPanel.setCellWidth(errorLabel, "100%"); //$NON-NLS-1$
    
    add( buttonPanel );
    
    actionLb.setWidth( "100%" ); //$NON-NLS-1$  // TODO sbarkdull, move to css
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
    setCellHeight( actionLb, "100%" );
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
  
  public void addItem( String item, String value ) {
    actionLb.addItem( item );
    actionLb.setValue( actionLb.getItemCount()-1, value );
  }
  
  public String getItemText( int idx ) {
    return actionLb.getItemText( idx );
  }
  
  public String getItemValue( int idx ) {
    return actionLb.getValue( idx );
  }
  
//  public void setValue( int idx, String value ) {
//    actionLb.setValue( idx, value );
//  }
  
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
  
  public void setFocus() {
    addBtn.setFocus( true );
  }
  
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


//FixedWidthFlexTable
// http://code.google.com/p/google-web-toolkit-incubator/wiki/ScrollTable
// http://code.google.com/p/google-web-toolkit-incubator/wiki/ScrollTable