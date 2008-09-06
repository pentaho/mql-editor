package org.pentaho.gwt.widgets.client.controls;

import java.util.Date;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Steven Barkdull
 *
 */

public class DateTimePicker extends FlowPanel implements IChangeHandler {

  private DatePickerEx datePicker = new DatePickerEx();
  private TimePicker timePicker = new TimePicker();
  private ICallback<IChangeHandler> onChangeHandler = null;
  
  public enum Layout {
    HORIZONTAL, VERTICAL
  }
  
  public DateTimePicker( Layout layout ) {
    super();
    Panel p = ( Layout.HORIZONTAL == layout )
      ? new HorizontalPanel()
      : new VerticalPanel();
    add( p );
    datePicker.setWidth( "12ex" ); //$NON-NLS-1$
    datePicker.setYoungestDate( new Date() );
    p.add( datePicker );
    //timePicker.setWidth( "100%" );
    p.add( timePicker );
    configureOnChangeHandler();
  }

  public void setOnChangeHandler( ICallback<IChangeHandler> handler ) {
    this.onChangeHandler = handler;
  }
  
  private void changeHandler() {
    if ( null != onChangeHandler ) {
      onChangeHandler.onHandle( this );
    }
  }
  
  private void configureOnChangeHandler() {
    final DateTimePicker localThis = this;
    
    ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
      public void onHandle(IChangeHandler o) {
        localThis.changeHandler();
      }
    };
    
    datePicker.setOnChangeHandler(handler);
    timePicker.setOnChangeHandler(handler);
  }
  
}
