package org.pentaho.gwt.widgets.client.controls.schededitor;

import java.util.Date;

import org.pentaho.gwt.widgets.client.Widgets;
import org.pentaho.gwt.widgets.client.controls.DatePickerEx;
import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.controls.TimePicker;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * @author Steven Barkdull
 *
 */

public class RunOnceEditor extends VerticalPanel{

  private static final WidgetsLocalizedMessages MSGS = Widgets.getLocalizedMessages();
  private TimePicker startTimePicker = new TimePicker();
  private DatePickerEx startDatePicker = new DatePickerEx();
  private Label startTimeLabel = null;
  private ErrorLabel startDateLabel = null;
  private static final String DEFAULT_START_HOUR = "12"; //$NON-NLS-1$
  private static final String DEFAULT_START_MINUTE = "00"; //$NON-NLS-1$
  private static final TimeUtil.TimeOfDay DEFAULT_TIME_OF_DAY = TimeUtil.TimeOfDay.AM;
  
  public RunOnceEditor() {
    startTimeLabel = new Label( MSGS.startTimeColon() );
    add( startTimeLabel );
    add( startTimePicker );
    startDateLabel = new ErrorLabel( new Label( MSGS.startDate() ) );
    add( startDateLabel );
    add( startDatePicker );
  }

  public Date getStartDate() {
    return startDatePicker.getSelectedDate();
  }
  
  public void setStartDate( Date d ) {
    startDatePicker.setSelectedDate( d );
  }

  public String getStartTime() {
    return startTimePicker.getTime();
  }
  
  public void setStartTime( String strTime ) {
    startTimePicker.setTime( strTime );
  }
  
  public void reset( Date d ) {
    startTimePicker.setHour( DEFAULT_START_HOUR );
    startTimePicker.setMinute( DEFAULT_START_MINUTE );
    startTimePicker.setTimeOfDay( DEFAULT_TIME_OF_DAY );
    startDatePicker.setSelectedDate( d );
    startDatePicker.setYoungestDate( d );
  }
  
  public void setStartDateError( String errorMsg ) {
    startDateLabel.setErrorMsg( errorMsg );
  }
}
