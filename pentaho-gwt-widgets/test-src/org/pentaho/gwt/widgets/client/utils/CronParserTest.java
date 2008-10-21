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
package org.pentaho.gwt.widgets.client.utils;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class CronParserTest extends GWTTestCase {

  /**
   * Must refer to a valid module that sources this class.
   */
  public String getModuleName() {
    return "org.pentaho.gwt.widgets.Widgets"; //$NON-NLS-1$
  }

  /**
   * Add as many tests as you like.
   */
  public void testSimple() {
    assertTrue(true);
  }
  
  public void testCronStringIsValidRecurrence() {
    String[] invalidCronSamples = {
        "0 59 23 ? *", // invalid # tokens //$NON-NLS-1$
        "0 59 23 ? * 2#4 2008", // invalid # tokens //$NON-NLS-1$
        "60 59 23 ? * 2#4 2008", // invalid seconds tokens //$NON-NLS-1$
        "1 60 23 ? * 2#4 2008", // invalid minutes tokens //$NON-NLS-1$
        "1 59 24 ? * 2#4 2008", // invalid hours tokens //$NON-NLS-1$
        "1 59 23 ? * ? 2008", // can't have two ? //$NON-NLS-1$
        "1 59 23 * 1 * 2008", // 4th and 6th components can't both be * //$NON-NLS-1$
        "0 59 23 ? * 2#8",          // NthDayNameOfMonth, should fail, there is no 8th //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5,6,7,8",   //WeeklyOn, must fail, too many days of week //$NON-NLS-1$
        "" //$NON-NLS-1$
    };

    String[] validCronSamples = {
        "0 22 4 0/3 * ?", // EveryNthDayOfMonth Fires at 4:22am on the 1,4,7...  //$NON-NLS-1$
        "0 14 21 ? * 2-6", // EveryWeekday Fires at 2:21pm every weekday //$NON-NLS-1$

        "0 33 6 ? * 1",         //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2",       //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3",       //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4",     //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5",     //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5,6",   //WeeklyOn //$NON-NLS-1$
        "0 33 6 ? * 1,2,3,4,5,6,7",   //WeeklyOn //$NON-NLS-1$
        "0 59 23 ? * 5#4",          // NthDayNameOfMonth, 4th friday //$NON-NLS-1$
        
        "0 5 5 13 * ?",           // DayNOfMonth //$NON-NLS-1$

        "0 59 23 ? * 2#4",          // NthDayNameOfMonth //$NON-NLS-1$
        
        "0 59 23 ? * 2#4",          // NthDayNameOfMonth Fires at 11:59pm on the 2nd Wed. //$NON-NLS-1$
        "0 33 5 ? * 3L",          // LastDayNameOfMonth Fires at 5:33am on the last Tues. of the month //$NON-NLS-1$
        "0 23 4 ? * 7L",          // LastDayNameOfMonth Fires at 4:23am on the last Sat. of the month //$NON-NLS-1$
        
        "0 01 02 28 2 ?",         //EveryMonthNameN //$NON-NLS-1$
        "1 1 1 8 4 ?",            // EveryMonthNameN //$NON-NLS-1$
        
        "0 3 5 ? 4 2#1",          //NthDayNameOfMonthName 1st mon of april //$NON-NLS-1$
        "0 3 5 ? 4 7#4",          //NthDayNameOfMonthName 4th sat of april //$NON-NLS-1$
        "0 3 5 ? 4 1#1",          //NthDayNameOfMonthName 1st sun of april //$NON-NLS-1$
        
        "0 3 8 ? 6 5L",           //LastDayNameOfMonthName //$NON-NLS-1$
        "0 59 12 ? 1 1L"         //LastDayNameOfMonthName //$NON-NLS-1$
    };
    
    boolean failed = false;
    String eMsg = null;
    for ( int ii=0; ii<invalidCronSamples.length; ++ii ) {
      String cronStr = invalidCronSamples[ii];
      
      try {
        CronParser cp = new CronParser( cronStr );
        String recStr = cp.parseToRecurrenceString();
        failed = recStr == null;  // if recStr is null, it may be a valid cron string, but it doesn't generated a valid recurrence string
        eMsg = ""; //$NON-NLS-1$
      } catch (CronParseException e) {
        failed = true;
        eMsg = e.getMessage();
      }
      assertTrue( "should be INVALID cron str: " + cronStr + ". " + eMsg, failed ); //$NON-NLS-1$  //$NON-NLS-2$
    }
    
    for ( int ii=0; ii<validCronSamples.length; ++ii ) {
      String cronStr = validCronSamples[ii];
      
      try {
        CronParser cp = new CronParser( cronStr );
        String recStr = cp.parseToRecurrenceString();
        failed = recStr != null;  // if recStr is null, it may be a valid cron string, but it doesn't generated a valid recurrence string
        failed = false;
        eMsg = ""; //$NON-NLS-1$
      } catch (CronParseException e) {
        failed = true;
        eMsg = e.getMessage();
      }
      assertFalse( "should be valid cron str: " + cronStr + ". " + eMsg, failed ); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

}
