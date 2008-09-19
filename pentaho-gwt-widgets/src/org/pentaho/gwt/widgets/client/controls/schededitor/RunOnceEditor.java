package org.pentaho.gwt.widgets.client.controls.schededitor;

import java.util.Date;

import org.pentaho.gwt.widgets.client.containers.SimpleGroupBox;
import org.pentaho.gwt.widgets.client.controls.DatePickerEx;
import org.pentaho.gwt.widgets.client.controls.TimePicker;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * @author Steven Barkdull
 *
 */

public class RunOnceEditor extends VerticalPanel implements IChangeHandler {

  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();
  private TimePicker startTimePicker = new TimePicker();
  private DatePickerEx startDatePicker = new DatePickerEx();
  private static final String DEFAULT_START_HOUR = "12"; //$NON-NLS-1$
  private static final String DEFAULT_START_MINUTE = "00"; //$NON-NLS-1$
  private static final TimeUtil.TimeOfDay DEFAULT_TIME_OF_DAY = TimeUtil.TimeOfDay.AM;
  private ICallback<IChangeHandler> onChangeHandler = null;
  
  public RunOnceEditor() {
    setWidth("100%");

    SimpleGroupBox startTimeCaptionPanel = new SimpleGroupBox(MSGS.startTime());
    startTimeCaptionPanel.add(startTimePicker);
    add( startTimeCaptionPanel );
    
    SimpleGroupBox startDateCaptionPanel = new SimpleGroupBox(MSGS.startDate());
    startDateCaptionPanel.add(startDatePicker);
    add( startDateCaptionPanel );
    
    configureOnChangeHandler();
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
//    startDateLabel.setErrorMsg( errorMsg );
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
    final RunOnceEditor localThis = this;
    
    ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
      public void onHandle(IChangeHandler o) {
        localThis.changeHandler();
      }
    };
    startTimePicker.setOnChangeHandler(handler);
    startDatePicker.setOnChangeHandler(handler);
  }
}
