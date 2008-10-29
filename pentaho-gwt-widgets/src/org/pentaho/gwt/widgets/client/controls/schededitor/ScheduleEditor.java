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
 *
 * @created May 19, 2008
 * 
 */
package org.pentaho.gwt.widgets.client.controls.schededitor;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.TemporalValue;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;
import org.pentaho.gwt.widgets.client.utils.CronParseException;
import org.pentaho.gwt.widgets.client.utils.CronParser;
import org.pentaho.gwt.widgets.client.utils.EnumException;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class ScheduleEditor extends VerticalPanel implements IChangeHandler {

  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();

  private static final String SCHEDULE_LABEL = "schedule-label"; //$NON-NLS-1$
  
  public enum ScheduleType {
    RUN_ONCE(0, MSGS.runOnce()), 
    SECONDS(1, MSGS.seconds()), 
    MINUTES(2, MSGS.minutes()), 
    HOURS(3, MSGS.hours()), 
    DAILY(4, MSGS.daily()), 
    WEEKLY(5, MSGS.weekly()), 
    MONTHLY(6, MSGS.monthly()), 
    YEARLY(7, MSGS.yearly()),
    CRON(8, MSGS.cron());

    private ScheduleType(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static ScheduleType[] scheduleValue = {
      RUN_ONCE,
      SECONDS, 
      MINUTES, 
      HOURS,
      DAILY, 
      WEEKLY, 
      MONTHLY, 
      YEARLY,
      CRON
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static ScheduleType get(int idx) {
      return scheduleValue[idx];
    }

    public static int length() {
      return scheduleValue.length;
    }
    
    public static ScheduleType stringToScheduleType( String strSchedule ) throws EnumException {
      for (ScheduleType v : EnumSet.range(ScheduleType.RUN_ONCE, ScheduleType.CRON)) {
        if ( v.toString().equals( strSchedule ) ) {
          return v;
        }
      }
      throw new EnumException( MSGS.invalidTemporalValue( scheduleValue.toString() ) );
    }
  } /* end enum */

  private RunOnceEditor runOnceEditor = null;
  private RecurrenceEditor recurrenceEditor = null;
  private CronEditor cronEditor = null;
  // TODO sbarkdull, can this be static?
  private Map<ScheduleType, Panel> scheduleTypeMap = new HashMap<ScheduleType, Panel>();
  private static Map<TemporalValue, ScheduleType> temporalValueToScheduleTypeMap = createTemporalValueToScheduleTypeMap();
  private static Map<ScheduleType, TemporalValue> scheduleTypeToTemporalValueMap = createScheduleTypeMapToTemporalValue();
  
  private TextBox nameTb = new TextBox();
  private TextBox groupNameTb = new TextBox();
  private TextBox descriptionTb = new TextBox();
  private ListBox scheduleCombo = null;
  
  // validation labels
  private ErrorLabel nameLabel;
  private ErrorLabel groupNameLabel;

//  private String cronStr = null;
//  private String repeatInSecs = null;
  
  private static final String DEFAULT_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_GROUP_NAME = ""; //$NON-NLS-1$
  private static final String DEFAULT_DESCRIPTION = ""; //$NON-NLS-1$
  private ICallback<IChangeHandler> onChangeHandler = null;

  public ScheduleEditor() {
    super();
    
    setStylePrimaryName( "scheduleEditor" ); //$NON-NLS-1$
    int rowNum = 0;
    nameLabel = new ErrorLabel( new Label( MSGS.nameColon() ) );
    nameLabel.setStyleName(SCHEDULE_LABEL);
    add( nameLabel );
    add( nameTb );
    nameTb.setWidth("70%"); //$NON-NLS-1$
    
    rowNum++;
    groupNameLabel = new ErrorLabel( new Label( MSGS.groupColon() ) );
    groupNameLabel.setStyleName(SCHEDULE_LABEL);
    add( groupNameLabel );
    add( groupNameTb );
    groupNameTb.setWidth("70%"); //$NON-NLS-1$

    rowNum++;
    Label l = new Label( MSGS.descriptionColon() );
    l.setStyleName(SCHEDULE_LABEL);
    add( l );
    descriptionTb.setStyleName( "scheduleDescription" ); //$NON-NLS-1$
    add( descriptionTb );

    rowNum++;
    scheduleCombo = createScheduleCombo();
    l = new Label( MSGS.recurrenceColon() );
    l.setStyleName(SCHEDULE_LABEL);
    add( l );
    add( scheduleCombo );

    rowNum++;
    VerticalPanel vp = new VerticalPanel();
    vp.setWidth("100%"); //$NON-NLS-1$
    add( vp );
    setCellHeight( vp, "100%" ); //$NON-NLS-1$

    runOnceEditor = new RunOnceEditor();
    vp.add( runOnceEditor );
    scheduleTypeMap.put( ScheduleType.RUN_ONCE, runOnceEditor );
    runOnceEditor.setVisible( true );
    
    recurrenceEditor = new RecurrenceEditor();
    vp.add( recurrenceEditor );
    scheduleTypeMap.put( ScheduleType.SECONDS, recurrenceEditor );
    scheduleTypeMap.put( ScheduleType.MINUTES, recurrenceEditor );
    scheduleTypeMap.put( ScheduleType.HOURS, recurrenceEditor );
    scheduleTypeMap.put( ScheduleType.DAILY, recurrenceEditor );
    scheduleTypeMap.put( ScheduleType.WEEKLY, recurrenceEditor );
    scheduleTypeMap.put( ScheduleType.MONTHLY, recurrenceEditor );
    scheduleTypeMap.put( ScheduleType.YEARLY, recurrenceEditor );
    recurrenceEditor.setVisible( false );
    
    cronEditor = new CronEditor();
    vp.add( cronEditor );
    scheduleTypeMap.put( ScheduleType.CRON, cronEditor );
    cronEditor.setVisible( false );
    
    configureOnChangeHandler();
  }

  public void reset( Date now ) {
    setName( DEFAULT_NAME );
    setGroupName( DEFAULT_GROUP_NAME );
    setDescription( DEFAULT_DESCRIPTION );
    
    runOnceEditor.reset( now );
    recurrenceEditor.reset( now );
    cronEditor.reset( now );
    
    setScheduleType( ScheduleType.RUN_ONCE );
  }
  
  public String getName() {
    return nameTb.getText();
  }
  
  public void setName( String name ) {
    nameTb.setText( name );
  }
  
  public String getGroupName() {
    return groupNameTb.getText();
  }
  
  public void setGroupName( String groupName ) {
    groupNameTb.setText( groupName );
  }
  
  public String getDescription() {
    return descriptionTb.getText();
  }
  
  public void setDescription( String description ) {
    descriptionTb.setText( description );
  }

  public String getCronString() {
    switch ( getScheduleType() ) {
      case RUN_ONCE:
        return null;
      case SECONDS: // fall through
      case MINUTES: // fall through
      case HOURS: // fall through
      case DAILY: // fall through
      case WEEKLY: // fall through
      case MONTHLY: // fall through
      case YEARLY:
        return recurrenceEditor.getCronString();
      case CRON:
        return cronEditor.getCronString();
      default:
        throw new RuntimeException( MSGS.invalidRunType( getScheduleType().toString() ) );
    }
  }
  /**
   * 
   * @param cronStr
   * @throws CronParseException if cronStr is not a valid CRON string.
   */
  public void setCronString( String cronStr ) throws CronParseException {

    CronParser cp = new CronParser( cronStr );
    String recurrenceStr = null;
    try {
      recurrenceStr = cp.parseToRecurrenceString(); // throws CronParseException
    } catch( CronParseException e ) {
      if ( !CronParser.isValidCronString( cronStr ) ) {
        throw e;
      }
      recurrenceStr = null; // valid cronstring, not parse-able to recurrence string
    }

    if ( null != recurrenceStr ) {
      recurrenceEditor.inititalizeWithRecurrenceString( recurrenceStr );
      TemporalValue tv = recurrenceEditor.getTemporalState();
      ScheduleType rt = temporalValueToScheduleType( tv );
      setScheduleType( rt );
    } else {
      // its a cron string that cannot be parsed into a recurrence string, switch to cron string editor.
      setScheduleType( ScheduleType.CRON );
    }
    
    cronEditor.setCronString( cronStr );
  }

  
  /**
   * 
   * @return null if the selected schedule does not support repeat-in-seconds, otherwise
   * return the number of seconds between schedule execution.
   * @throws RuntimeException if the temporal value is invalid. This
   * condition occurs as a result of programmer error.
   */
  public Integer getRepeatInSecs() throws RuntimeException {
    return recurrenceEditor.getRepeatInSecs();
  }

  public void setRepeatInSecs( Integer repeatInSecs ) {
    recurrenceEditor.inititalizeWithRepeatInSecs( repeatInSecs );
    TemporalValue tv = recurrenceEditor.getTemporalState();
    ScheduleType rt = temporalValueToScheduleType( tv );
    setScheduleType( rt );
  }
  
  private ListBox createScheduleCombo() {
    final ScheduleEditor localThis = this;
    ListBox lb = new ListBox();
    lb.setVisibleItemCount( 1 );
    //lb.setStyleName("scheduleCombo"); //$NON-NLS-1$
    lb.addChangeListener( new ChangeListener() {
      public void onChange(Widget sender) {
        localThis.handleScheduleChange();
      }
    });
    // add all schedule types to the combobox
    for (ScheduleType schedType : EnumSet.range(ScheduleType.RUN_ONCE, ScheduleType.CRON)) {
      lb.addItem( schedType.toString() );
    }
    lb.setItemSelected( 0, true );

    return lb;
  }
  
  public ScheduleType getScheduleType() {
    String selectedValue = scheduleCombo.getValue( scheduleCombo.getSelectedIndex() );
    return ScheduleType.stringToScheduleType( selectedValue );
  }
  
  public void setScheduleType( ScheduleType scheduleType ) {
    scheduleCombo.setSelectedIndex( scheduleType.value() );
    selectScheduleTypeEditor( scheduleType );
  }
  
  /**
   * NOTE: should only ever be used by validators. This is a backdoor
   * into this class that shouldn't be here, do not use this method
   * unless you are validating.
   * 
   * @return DateRangeEditor
   */
  public RecurrenceEditor getRecurrenceEditor() {
    return recurrenceEditor;
  }
  
  /**
   * NOTE: should only ever be used by validators. This is a backdoor
   * into this class that shouldn't be here, do not use this method
   * unless you are validating.
   * 
   * @return DateRangeEditor
   */
  public CronEditor getCronEditor() {
    return cronEditor;
  }
  
  /**
   * NOTE: should only ever be used by validators. This is a backdoor
   * into this class that shouldn't be here, do not use this method
   * unless you are validating.
   * 
   * @return DateRangeEditor
   */
  
  public RunOnceEditor getRunOnceEditor() {
    return runOnceEditor;
  }
  
  public void setStartTime( String startTime ) {
    runOnceEditor.setStartTime( startTime );
    recurrenceEditor.setStartTime( startTime );
  }
  
  public String getStartTime() {
    switch ( getScheduleType() ) {
      case RUN_ONCE:
        return runOnceEditor.getStartTime();
      case SECONDS: // fall through
      case MINUTES: // fall through
      case HOURS: // fall through
      case DAILY: // fall through
      case WEEKLY: // fall through
      case MONTHLY: // fall through
      case YEARLY:
        return recurrenceEditor.getStartTime();
      case CRON:
        return cronEditor.getStartTime();
      default:
        throw new RuntimeException( MSGS.invalidRunType( getScheduleType().toString() ) );
    }
  }

  public void setStartDate( Date startDate ) {
    runOnceEditor.setStartDate( startDate );
    recurrenceEditor.setStartDate( startDate );
    cronEditor.setStartDate( startDate );
  }
  
  public Date getStartDate() {
    switch ( getScheduleType() ) {
      case RUN_ONCE:
        Date startDate = runOnceEditor.getStartDate();
        String startTime  = runOnceEditor.getStartTime();
        Date startDateTime = TimeUtil.getDateTime( startTime, startDate );
        return startDateTime;
      case SECONDS: // fall through
      case MINUTES: // fall through
      case HOURS: // fall through
      case DAILY: // fall through
      case WEEKLY: // fall through
      case MONTHLY: // fall through
      case YEARLY:
        return recurrenceEditor.getStartDate();
      case CRON:
        return cronEditor.getStartDate();
      default:
        throw new RuntimeException( MSGS.invalidRunType( getScheduleType().toString() ) );
    }
  }

  public void setEndDate( Date endDate ) {
    recurrenceEditor.setEndDate( endDate );
    cronEditor.setEndDate( endDate );
  }

  public Date getEndDate() {
    switch ( getScheduleType() ) {
      case RUN_ONCE:
        return null;
      case SECONDS: // fall through
      case MINUTES: // fall through
      case HOURS: // fall through
      case DAILY: // fall through
      case WEEKLY: // fall through
      case MONTHLY: // fall through
      case YEARLY:
        return recurrenceEditor.getEndDate();
      case CRON:
        return cronEditor.getEndDate();
      default:
        throw new RuntimeException( MSGS.invalidRunType( getScheduleType().toString() ) );
    }
  }

  public void setNoEndDate() {
    recurrenceEditor.setNoEndDate();
    cronEditor.setNoEndDate();
  }

  public void setEndBy() {
    recurrenceEditor.setEndBy();
    cronEditor.setEndBy();
  }

  private void handleScheduleChange() throws EnumException {
    ScheduleType schedType = getScheduleType();
    selectScheduleTypeEditor( schedType );
  }


  private void selectScheduleTypeEditor( ScheduleType scheduleType ) {
    // hide all panels
    for ( Map.Entry<ScheduleType, Panel> me : scheduleTypeMap.entrySet() ) {
      me.getValue().setVisible( false );
    }
    // show the selected panel
    Panel p = scheduleTypeMap.get( scheduleType );
    p.setVisible( true );
    
    TemporalValue tv = scheduleTypeToTemporalValue( scheduleType );
    if ( null != tv ) {
      // force the recurrence editor to display the appropriate ui
      recurrenceEditor.setTemporalState( tv );
    }
  }
  
  /**
   * @param errorMsg String null or "" to clear the error msg, else
   * set the error msg to <param>errorMsg</param>.
   */
  public void setNameError( String errorMsg ) {
    nameLabel.setErrorMsg( errorMsg );
  }
  
  public void setGroupNameError( String errorMsg ) {
    groupNameLabel.setErrorMsg( errorMsg );
  }
  
  private static Map<TemporalValue, ScheduleType> createTemporalValueToScheduleTypeMap() {
    Map<TemporalValue, ScheduleType> m = new HashMap<TemporalValue, ScheduleType>();

    m.put( TemporalValue.SECONDS, ScheduleType.SECONDS );
    m.put( TemporalValue.MINUTES, ScheduleType.MINUTES );
    m.put( TemporalValue.HOURS, ScheduleType.HOURS );
    m.put( TemporalValue.DAILY, ScheduleType.DAILY );
    m.put( TemporalValue.WEEKLY, ScheduleType.WEEKLY );
    m.put( TemporalValue.MONTHLY, ScheduleType.MONTHLY );
    m.put( TemporalValue.YEARLY, ScheduleType.YEARLY );

    return m;
  }
  
  private static Map<ScheduleType, TemporalValue> createScheduleTypeMapToTemporalValue() {
    Map<ScheduleType, TemporalValue> m = new HashMap<ScheduleType, TemporalValue>();

    m.put( ScheduleType.SECONDS, TemporalValue.SECONDS );
    m.put( ScheduleType.MINUTES, TemporalValue.MINUTES );
    m.put( ScheduleType.HOURS, TemporalValue.HOURS );
    m.put( ScheduleType.DAILY, TemporalValue.DAILY );
    m.put( ScheduleType.WEEKLY, TemporalValue.WEEKLY );
    m.put( ScheduleType.MONTHLY, TemporalValue.MONTHLY );
    m.put( ScheduleType.YEARLY, TemporalValue.YEARLY );

    return m;
  }
  
  private static ScheduleType temporalValueToScheduleType( TemporalValue tv ) {
    return temporalValueToScheduleTypeMap.get( tv );
  }
  
  private static TemporalValue scheduleTypeToTemporalValue( ScheduleType st ) {
    return scheduleTypeToTemporalValueMap.get( st );
  }
  
  public void setFocus() {
    nameTb.setFocus( true );
    nameTb.setSelectionRange( 0, nameTb.getText().length() );
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
    final ScheduleEditor localThis = this;
    KeyboardListener keyboardListener = new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
      }
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
      }
      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        localThis.changeHandler();
      }
    };
    ChangeListener changeListener = new ChangeListener() {
      public void onChange(Widget sender) {
        localThis.changeHandler();
      }
    };
    ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
      public void onHandle(IChangeHandler o) {
        localThis.changeHandler();
      }
    };
    nameTb.addKeyboardListener( keyboardListener );
    groupNameTb.addKeyboardListener( keyboardListener );
    descriptionTb.addKeyboardListener( keyboardListener );
    scheduleCombo.addChangeListener( changeListener );
    runOnceEditor.setOnChangeHandler( handler );
    recurrenceEditor.setOnChangeHandler( handler );
    cronEditor.setOnChangeHandler( handler );
  }
}
