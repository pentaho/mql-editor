package org.pentaho.gwt.widgets.client.controls;

import java.util.Date;

import org.zenika.widget.client.datePicker.DatePicker;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * @author Steven Barkdull
 *
 */

public class DateTimePicker extends FlowPanel {

  private DatePicker datePicker = new DatePicker();
  private TimePicker timePicker = new TimePicker();
  
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
  }
  
}
