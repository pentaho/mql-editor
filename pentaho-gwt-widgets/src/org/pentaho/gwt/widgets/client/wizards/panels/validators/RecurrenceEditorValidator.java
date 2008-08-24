/*
 * Copyright 2005-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * Created  
 * @author Steven Barkdull
 */
package org.pentaho.gwt.widgets.client.wizards.panels.validators;

import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.DailyRecurrenceEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.HourlyRecurrenceEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.MinutelyRecurrenceEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.MonthlyRecurrenceEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.SecondlyRecurrenceEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.WeeklyRecurrenceEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.RecurrenceEditor.YearlyRecurrenceEditor;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

/**
 * 
 * @author Steven Barkdull
 *
 */
public class RecurrenceEditorValidator implements IUiValidator {
  
  private RecurrenceEditor recurrenceEditor = null;
  private DateRangeEditorValidator dateRangeEditorValidator = null;
  
  public RecurrenceEditorValidator( RecurrenceEditor recurrenceEditor ) {
    this.recurrenceEditor = recurrenceEditor; 
    this.dateRangeEditorValidator = new DateRangeEditorValidator( recurrenceEditor.getDateRangeEditor() );
  }
  
  public boolean isValid() {
    boolean isValid = true;
    switch ( recurrenceEditor.getTemporalState() ) {
      case SECONDS:
        SecondlyRecurrenceEditor sEd = recurrenceEditor.getSecondlyEditor();
        String seconds = sEd.getValue();
        if ( !StringUtils.isPositiveInteger( seconds ) 
            || ( Integer.parseInt( seconds ) <= 0 ) ) {
          isValid = false;
        }
        if ( Integer.parseInt( seconds ) > TimeUtil.MAX_SECOND_BY_MILLISEC ) {
          isValid = false;
        }
        break;
      case MINUTES:
        MinutelyRecurrenceEditor mEd = recurrenceEditor.getMinutelyEditor();
        String minutes = mEd.getValue();
        if ( !StringUtils.isPositiveInteger( minutes ) 
            || ( Integer.parseInt( minutes ) <= 0 ) ) {
          isValid = false;
        }
        if ( Integer.parseInt( minutes ) > TimeUtil.MAX_MINUTE_BY_MILLISEC ) {
          isValid = false;
        }
        break;
      case HOURS:
        HourlyRecurrenceEditor hEd = recurrenceEditor.getHourlyEditor();
        String hours = hEd.getValue();
        if ( !StringUtils.isPositiveInteger( hours ) 
            || ( Integer.parseInt( hours ) <= 0 ) ) {
          isValid = false;
        }
        if ( Integer.parseInt( hours ) > TimeUtil.MAX_HOUR_BY_MILLISEC ) {
          isValid = false;
        }
        break;
      case DAILY:
        DailyRecurrenceEditor dEd = recurrenceEditor.getDailyEditor();
        if ( dEd.isEveryNDays() ) {
          String days = dEd.getRepeatValue();
          if ( !StringUtils.isPositiveInteger( days ) 
              || ( Integer.parseInt( days ) <= 0 ) ) {
            isValid = false;
          }
        }
        break;
      case WEEKLY:
        WeeklyRecurrenceEditor wEd = recurrenceEditor.getWeeklyEditor();
        if ( wEd.getNumCheckedDays() < 1 ) {
          isValid = false;
        }
        break;
      case MONTHLY:
        MonthlyRecurrenceEditor monthlyEd = recurrenceEditor.getMonthlyEditor();
        if ( monthlyEd.isDayNOfMonth() ) {
          String dayNOfMonth = monthlyEd.getDayOfMonth();
          if ( !StringUtils.isPositiveInteger( dayNOfMonth ) 
              || !TimeUtil.isDayOfMonth( Integer.parseInt( dayNOfMonth ) ) ) {
            isValid = false;
          }
        }
        break;
      case YEARLY:
        YearlyRecurrenceEditor yearlyEd = recurrenceEditor.getYearlyEditor();
        if ( yearlyEd.isEveryMonthOnNthDay() ) {
          String dayNOfMonth = yearlyEd.getDayOfMonth();
          if ( !StringUtils.isPositiveInteger( dayNOfMonth ) 
              || !TimeUtil.isDayOfMonth( Integer.parseInt( dayNOfMonth ) ) ) {
            isValid = false;
          }
        }
        break;
      default:
    }
    isValid &= dateRangeEditorValidator.isValid();
    return isValid;
  }

  public void clear() {
    recurrenceEditor.getSecondlyEditor().setValueError( null );
    recurrenceEditor.getMinutelyEditor().setValueError( null );
    recurrenceEditor.getHourlyEditor().setValueError( null );
    recurrenceEditor.getDailyEditor().setRepeatError( null );
    recurrenceEditor.getWeeklyEditor().setEveryDayOnError( null );
    recurrenceEditor.getMonthlyEditor().setDayNOfMonthError( null );
    recurrenceEditor.getYearlyEditor().setDayOfMonthError( null );
    dateRangeEditorValidator.clear();
  }
}
