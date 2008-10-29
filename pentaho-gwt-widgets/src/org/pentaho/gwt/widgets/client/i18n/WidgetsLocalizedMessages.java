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
package org.pentaho.gwt.widgets.client.i18n;


/**
 * Interface to represent the messages contained in resource bundle:
 * 	C:/development-2.0/pentaho-gwt-widgets/eclipse-bin/org/pentaho/gwt/widgets/client/i18n/WidgetsLocalizedMessages.properties'.
 */
public interface WidgetsLocalizedMessages extends com.google.gwt.i18n.client.Messages {
  
  /**
   * Translated "MMM dd, yyyy HH:mm:ss a".
   * 
   * @return translated "MMM dd, yyyy HH:mm:ss a"
  
   */
  @DefaultMessage("MMM dd, yyyy HH:mm:ss a")
  String dateFormatLongMedium();

  /**
   * Translated "getDayOfMonth() not valid for recurrence type: {0}.".
   * 
   * @return translated "getDayOfMonth() not valid for recurrence type: {0}."
  
   */
  @DefaultMessage("getDayOfMonth() not valid for recurrence type: {0}.")
  String invalidDayOfMonthForRecurrenceType(String arg0);

  /**
   * Translated "Suspend selected schedule(s)".
   * 
   * @return translated "Suspend selected schedule(s)"
  
   */
  @DefaultMessage("Suspend selected schedule(s)")
  String suspendSchedules();

  /**
   * Translated "Start Time".
   * 
   * @return translated "Start Time"
  
   */
  @DefaultMessage("Start Time")
  String startTime();

  /**
   * Translated "of".
   * 
   * @return translated "of"
  
   */
  @DefaultMessage("of")
  String of();

  /**
   * Translated "Hours token must be an integer between 0 and 23, but it is: {0}.".
   * 
   * @return translated "Hours token must be an integer between 0 and 23, but it is: {0}."
  
   */
  @DefaultMessage("Hours token must be an integer between 0 and 23, but it is: {0}.")
  String invalidHoursToken(String arg0);

  /**
   * Translated "Illegal state, must have either a cron string or a repeat time.".
   * 
   * @return translated "Illegal state, must have either a cron string or a repeat time."
  
   */
  @DefaultMessage("Illegal state, must have either a cron string or a repeat time.")
  String illegalStateMissingCronAndRepeat();

  /**
   * Translated "Search".
   * 
   * @return translated "Search"
  
   */
  @DefaultMessage("Search")
  String search();

  /**
   * Translated "Specify a name.".
   * 
   * @return translated "Specify a name."
  
   */
  @DefaultMessage("Specify a name.")
  String specifyName();

  /**
   * Translated "File".
   * 
   * @return translated "File"
  
   */
  @DefaultMessage("File")
  String file();

  /**
   * Translated "Invalid TemporalValue in getCronString(): {0}".
   * 
   * @return translated "Invalid TemporalValue in getCronString(): {0}"
  
   */
  @DefaultMessage("Invalid TemporalValue in getCronString(): {0}")
  String invalidTemporalValueInGetCronString(String arg0);

  /**
   * Translated "OK".
   * 
   * @return translated "OK"
  
   */
  @DefaultMessage("OK")
  String ok();

  /**
   * Translated "Run Once".
   * 
   * @return translated "Run Once"
  
   */
  @DefaultMessage("Run Once")
  String runOnce();

  /**
   * Translated "Loading...".
   * 
   * @return translated "Loading..."
  
   */
  @DefaultMessage("Loading...")
  String loading();

  /**
   * Translated "Hours".
   * 
   * @return translated "Hours"
  
   */
  @DefaultMessage("Hours")
  String hours();

  /**
   * Translated "All Groups".
   * 
   * @return translated "All Groups"
  
   */
  @DefaultMessage("All Groups")
  String allGroups();

  /**
   * Translated "Suspend scheduler".
   * 
   * @return translated "Suspend scheduler"
  
   */
  @DefaultMessage("Suspend scheduler")
  String suspendScheduler();

  /**
   * Translated "Monthly".
   * 
   * @return translated "Monthly"
  
   */
  @DefaultMessage("Monthly")
  String monthly();

  /**
   * Translated "third".
   * 
   * @return translated "third"
  
   */
  @DefaultMessage("third")
  String third();

  /**
   * Translated "Name:".
   * 
   * @return translated "Name:"
  
   */
  @DefaultMessage("Name:")
  String nameColon();

  /**
   * Translated "Schedule Editor".
   * 
   * @return translated "Schedule Editor"
  
   */
  @DefaultMessage("Schedule Editor")
  String scheduleEditor();

  /**
   * Translated "Cron string is invalid.".
   * 
   * @return translated "Cron string is invalid."
  
   */
  @DefaultMessage("Cron string is invalid.")
  String invalidCronString();

  /**
   * Translated "second(s)".
   * 
   * @return translated "second(s)"
  
   */
  @DefaultMessage("second(s)")
  String secondsLabel();

  /**
   * Translated "Resume scheduler".
   * 
   * @return translated "Resume scheduler"
  
   */
  @DefaultMessage("Resume scheduler")
  String resumeScheduler();

  /**
   * Translated "February".
   * 
   * @return translated "February"
  
   */
  @DefaultMessage("February")
  String february();

  /**
   * Translated "Resume selected schedule(s)".
   * 
   * @return translated "Resume selected schedule(s)"
  
   */
  @DefaultMessage("Resume selected schedule(s)")
  String resumeSchedules();

  /**
   * Translated "Sunday".
   * 
   * @return translated "Sunday"
  
   */
  @DefaultMessage("Sunday")
  String sunday();

  /**
   * Translated "Edit schedule".
   * 
   * @return translated "Edit schedule"
  
   */
  @DefaultMessage("Edit schedule")
  String editSchedule();

  /**
   * Translated "Schedule with name "{0}" already exists. Select another name.".
   * 
   * @return translated "Schedule with name "{0}" already exists. Select another name."
  
   */
  @DefaultMessage("Schedule with name \"{0}\" already exists. Select another name.")
  String scheduleNameAlreadyExists(String arg0);

  /**
   * Translated "PM".
   * 
   * @return translated "PM"
  
   */
  @DefaultMessage("PM")
  String pm();

  /**
   * Translated "Specify a start date.".
   * 
   * @return translated "Specify a start date."
  
   */
  @DefaultMessage("Specify a start date.")
  String specifyStartDate();

  /**
   * Translated "Group:".
   * 
   * @return translated "Group:"
  
   */
  @DefaultMessage("Group:")
  String groupColon();

  /**
   * Translated "Unrecognized ScheduleType in RecurrenceEditor.isValid(): {0}.".
   * 
   * @return translated "Unrecognized ScheduleType in RecurrenceEditor.isValid(): {0}."
  
   */
  @DefaultMessage("Unrecognized ScheduleType in RecurrenceEditor.isValid(): {0}.")
  String unrecognizedSchedType(String arg0);

  /**
   * Translated "Start Date".
   * 
   * @return translated "Start Date"
  
   */
  @DefaultMessage("Start Date")
  String startDate();

  /**
   * Translated "Up One Level".
   * 
   * @return translated "Up One Level"
  
   */
  @DefaultMessage("Up One Level")
  String upOneLevel();

  /**
   * Translated "Hours must be a number <= {0}.".
   * 
   * @return translated "Hours must be a number <= {0}."
  
   */
  @DefaultMessage("Hours must be a number <= {0}.")
  String mustBeHoursRange(String arg0);

  /**
   * Translated "Add Item".
   * 
   * @return translated "Add Item"
  
   */
  @DefaultMessage("Add Item")
  String addItem();

  /**
   * Translated "One or more days must be checked.".
   * 
   * @return translated "One or more days must be checked."
  
   */
  @DefaultMessage("One or more days must be checked.")
  String oneOrMoreMustBeChecked();

  /**
   * Translated "Invalid number of tokens.".
   * 
   * @return translated "Invalid number of tokens."
  
   */
  @DefaultMessage("Invalid number of tokens.")
  String invalidNumTokens();

  /**
   * Translated "Suspended".
   * 
   * @return translated "Suspended"
  
   */
  @DefaultMessage("Suspended")
  String stateSuspended();

  /**
   * Translated "Invalid token, must be an integer: {0}.".
   * 
   * @return translated "Invalid token, must be an integer: {0}."
  
   */
  @DefaultMessage("Invalid token, must be an integer: {0}.")
  String invalidIntegerToken(String arg0);

  /**
   * Translated "No end date".
   * 
   * @return translated "No end date"
  
   */
  @DefaultMessage("No end date")
  String noEndDateLabel();

  /**
   * Translated "Invalid token, must be a list of integers: {0}.".
   * 
   * @return translated "Invalid token, must be a list of integers: {0}."
  
   */
  @DefaultMessage("Invalid token, must be a list of integers: {0}.")
  String invalidIntegerListToken(String arg0);

  /**
   * Translated "Delete Items".
   * 
   * @return translated "Delete Items"
  
   */
  @DefaultMessage("Delete Items")
  String deleteItems();

  /**
   * Translated "Specify an end date.".
   * 
   * @return translated "Specify an end date."
  
   */
  @DefaultMessage("Specify an end date.")
  String specifyEndDate();

  /**
   * Translated "Tuesday".
   * 
   * @return translated "Tuesday"
  
   */
  @DefaultMessage("Tuesday")
  String tuesday();

  /**
   * Translated "Invalid day of month: {0}. ".
   * 
   * @return translated "Invalid day of month: {0}. "
  
   */
  @DefaultMessage("Invalid day of month: {0}. ")
  String invalidDayOfMonth(String arg0);

  /**
   * Translated "September".
   * 
   * @return translated "September"
  
   */
  @DefaultMessage("September")
  String september();

  /**
   * Translated "Weekly".
   * 
   * @return translated "Weekly"
  
   */
  @DefaultMessage("Weekly")
  String weekly();

  /**
   * Translated "May".
   * 
   * @return translated "May"
  
   */
  @DefaultMessage("May")
  String may();

  /**
   * Translated "Range of recurrence".
   * 
   * @return translated "Range of recurrence"
  
   */
  @DefaultMessage("Range of recurrence")
  String rangeOfRecurrence();

  /**
   * Translated "Invalid day of week: {0}. ".
   * 
   * @return translated "Invalid day of week: {0}. "
  
   */
  @DefaultMessage("Invalid day of week: {0}. ")
  String invalidDayOfWeek(String arg0);

  /**
   * Translated "Minute token must be an integer between 0 and 59, but it is: {0}. ".
   * 
   * @return translated "Minute token must be an integer between 0 and 59, but it is: {0}. "
  
   */
  @DefaultMessage("Minute token must be an integer between 0 and 59, but it is: {0}. ")
  String invalidMinutesToken(String arg0);

  /**
   * Translated "Folder".
   * 
   * @return translated "Folder"
  
   */
  @DefaultMessage("Folder")
  String folder();

  /**
   * Translated "fourth".
   * 
   * @return translated "fourth"
  
   */
  @DefaultMessage("fourth")
  String fourth();

  /**
   * Translated "Start:".
   * 
   * @return translated "Start:"
  
   */
  @DefaultMessage("Start:")
  String startLabel();

  /**
   * Translated "Invalid week of month: {0}. ".
   * 
   * @return translated "Invalid week of month: {0}. "
  
   */
  @DefaultMessage("Invalid week of month: {0}. ")
  String invalidWeekOfMonth(String arg0);

  /**
   * Translated "Days {0}.".
   * 
   * @return translated "Days {0}."
  
   */
  @DefaultMessage("Days {0}.")
  String days(String arg0);

  /**
   * Translated "Seconds".
   * 
   * @return translated "Seconds"
  
   */
  @DefaultMessage("Seconds")
  String seconds();

  /**
   * Translated "Invalid TemporalValue in getRepeatInSecs(): {0}".
   * 
   * @return translated "Invalid TemporalValue in getRepeatInSecs(): {0}"
  
   */
  @DefaultMessage("Invalid TemporalValue in getRepeatInSecs(): {0}")
  String invalidTemporalValueInGetRepeatInSecs(String arg0);

  /**
   * Translated "Minues must be a number <= {0}.".
   * 
   * @return translated "Minues must be a number <= {0}."
  
   */
  @DefaultMessage("Minues must be a number <= {0}.")
  String mustBeMinutesRange(String arg0);

  /**
   * Translated "October".
   * 
   * @return translated "October"
  
   */
  @DefaultMessage("October")
  String october();

  /**
   * Translated "Invalid recurrenceType: {0}.  ".
   * 
   * @return translated "Invalid recurrenceType: {0}.  "
  
   */
  @DefaultMessage("Invalid recurrenceType: {0}.  ")
  String invalidRecurrenceType(String arg0);

  /**
   * Translated "Refresh schedule list".
   * 
   * @return translated "Refresh schedule list"
  
   */
  @DefaultMessage("Refresh schedule list")
  String refreshScheduleList();

  /**
   * Translated "Date Modified".
   * 
   * @return translated "Date Modified"
  
   */
  @DefaultMessage("Date Modified")
  String dateModified();

  /**
   * Translated "Action sequence list cannot be empty.".
   * 
   * @return translated "Action sequence list cannot be empty."
  
   */
  @DefaultMessage("Action sequence list cannot be empty.")
  String actionSequenceCannotBeEmpty();

  /**
   * Translated "last".
   * 
   * @return translated "last"
  
   */
  @DefaultMessage("last")
  String last();

  /**
   * Translated "second".
   * 
   * @return translated "second"
  
   */
  @DefaultMessage("second")
  String second();

  /**
   * Translated "Location:".
   * 
   * @return translated "Location:"
  
   */
  @DefaultMessage("Location:")
  String location();

  /**
   * Translated "Monday".
   * 
   * @return translated "Monday"
  
   */
  @DefaultMessage("Monday")
  String monday();

  /**
   * Translated "Cancel".
   * 
   * @return translated "Cancel"
  
   */
  @DefaultMessage("Cancel")
  String cancel();

  /**
   * Translated "Finish".
   * 
   * @return translated "Finish"
  
   */
  @DefaultMessage("Finish")
  String finish();

  /**
   * Translated "Back".
   * 
   * @return translated "Back"
  
   */
  @DefaultMessage("Back")
  String back();

  /**
   * Translated "Next".
   * 
   * @return translated "Next"
  
   */
  @DefaultMessage("Next")
  String next();
  /**
   * Translated "Type".
   * 
   * @return translated "Type"
  
   */
  @DefaultMessage("Type")
  String type();

  /**
   * Translated "Invalid month of year: {0}.".
   * 
   * @return translated "Invalid month of year: {0}."
  
   */
  @DefaultMessage("Invalid month of year: {0}.")
  String invalidMonthOfYear(String arg0);

  /**
   * Translated "Every".
   * 
   * @return translated "Every"
  
   */
  @DefaultMessage("Every")
  String every();

  /**
   * Translated "of every month".
   * 
   * @return translated "of every month"
  
   */
  @DefaultMessage("of every month")
  String ofEveryMonth();

  /**
   * Translated "Delete schedule(s)".
   * 
   * @return translated "Delete schedule(s)"
  
   */
  @DefaultMessage("Delete schedule(s)")
  String deleteSchedules();

  /**
   * Translated "Cron".
   * 
   * @return translated "Cron"
  
   */
  @DefaultMessage("Cron")
  String cron();

  /**
   * Translated "March".
   * 
   * @return translated "March"
  
   */
  @DefaultMessage("March")
  String march();

  /**
   * Translated "Confirm Delete".
   * 
   * @return translated "Confirm Delete"
  
   */
  @DefaultMessage("Confirm Delete")
  String confirmDelete();

  /**
   * Translated "Recurrence:".
   * 
   * @return translated "Recurrence:"
  
   */
  @DefaultMessage("Recurrence:")
  String recurrenceColon();

  /**
   * Translated "January".
   * 
   * @return translated "January"
  
   */
  @DefaultMessage("January")
  String january();

  /**
   * Translated "Create schedule".
   * 
   * @return translated "Create schedule"
  
   */
  @DefaultMessage("Create schedule")
  String createSchedule();

  /**
   * Translated "Illegal to call recurrenceStringToCronStr with a RecurrenceType of Unknown.".
   * 
   * @return translated "Illegal to call recurrenceStringToCronStr with a RecurrenceType of Unknown."
  
   */
  @DefaultMessage("Illegal to call recurrenceStringToCronStr with a RecurrenceType of Unknown.")
  String illegalRecurrenceTypeUnknown();

  /**
   * Translated "day(s)".
   * 
   * @return translated "day(s)"
  
   */
  @DefaultMessage("day(s)")
  String daysLabel();

  /**
   * Translated "Saturday".
   * 
   * @return translated "Saturday"
  
   */
  @DefaultMessage("Saturday")
  String saturday();

  /**
   * Translated "July".
   * 
   * @return translated "July"
  
   */
  @DefaultMessage("July")
  String july();

  /**
   * Translated "Attempt to initialize the Recurrence Dialog with an invalid CRON string: {0}. Error details: {1}.".
   * 
   * @return translated "Attempt to initialize the Recurrence Dialog with an invalid CRON string: {0}. Error details: {1}."
  
   */
  @DefaultMessage("Attempt to initialize the Recurrence Dialog with an invalid CRON string: {0}. Error details: {1}.")
  String invalidCronInInitOfRecurrenceDialog(String arg0,  String arg1);

  /**
   * Translated "Friday ".
   * 
   * @return translated "Friday "
  
   */
  @DefaultMessage("Friday ")
  String friday();

  /**
   * Translated "Yearly".
   * 
   * @return translated "Yearly"
  
   */
  @DefaultMessage("Yearly")
  String yearly();

  /**
   * Translated "n/a".
   * 
   * @return translated "n/a"
  
   */
  @DefaultMessage("n/a")
  String notApplicable();

  /**
   * Translated "Number of {0} to repeat.".
   * 
   * @return translated "Number of {0} to repeat."
  
   */
  @DefaultMessage("Number of {0} to repeat.")
  String numberOfXToRepeat(String arg0);

  /**
   * Translated "There seems to not be any radio button selected, which is theoretically impossible.".
   * 
   * @return translated "There seems to not be any radio button selected, which is theoretically impossible."
  
   */
  @DefaultMessage("There seems to not be any radio button selected, which is theoretically impossible.")
  String noRadioBtnsSelected();

  /**
   * Translated "November".
   * 
   * @return translated "November"
  
   */
  @DefaultMessage("November")
  String november();

  /**
   * Translated "Specify a group name.".
   * 
   * @return translated "Specify a group name."
  
   */
  @DefaultMessage("Specify a group name.")
  String specifyGroupName();

  /**
   * Translated "minute(s)".
   * 
   * @return translated "minute(s)"
  
   */
  @DefaultMessage("minute(s)")
  String minutesLabel();

  /**
   * Translated "Thursday".
   * 
   * @return translated "Thursday"
  
   */
  @DefaultMessage("Thursday")
  String thursday();

  /**
   * Translated "Invalid String for RecurrenceType: {0}. ".
   * 
   * @return translated "Invalid String for RecurrenceType: {0}. "
  
   */
  @DefaultMessage("Invalid String for RecurrenceType: {0}. ")
  String invalidStringForRecurrenceType(String arg0);

  /**
   * Translated "Start Time:".
   * 
   * @return translated "Start Time:"
  
   */
  @DefaultMessage("Start Time:")
  String startTimeColon();

  /**
   * Translated "Recur every week on:".
   * 
   * @return translated "Recur every week on:"
  
   */
  @DefaultMessage("Recur every week on:")
  String recurEveryWeek();

  /**
   * Translated "Every weekday".
   * 
   * @return translated "Every weekday"
  
   */
  @DefaultMessage("Every weekday")
  String everyWeekDay();

  /**
   * Translated "Invalid Run Type: {0}.".
   * 
   * @return translated "Invalid Run Type: {0}."
  
   */
  @DefaultMessage("Invalid Run Type: {0}.")
  String invalidRunType(String arg0);

  /**
   * Translated "December".
   * 
   * @return translated "December"
  
   */
  @DefaultMessage("December")
  String december();

  /**
   * Translated "Comma separated list of action sequence paths:".
   * 
   * @return translated "Comma separated list of action sequence paths:"
  
   */
  @DefaultMessage("Comma separated list of action sequence paths:")
  String commaSeparatedList();

  /**
   * Translated "getWhichWeekOfMonth() not valid for recurrence type: {0}.".
   * 
   * @return translated "getWhichWeekOfMonth() not valid for recurrence type: {0}."
  
   */
  @DefaultMessage("getWhichWeekOfMonth() not valid for recurrence type: {0}.")
  String invalidWeekOfMonthForRecurrenceType(String arg0);

  /**
   * Translated "must be a number greater than 0 and less than 2147483647".
   * 
   * @return translated "must be a number greater than 0 and less than 2147483647"
  
   */
  @DefaultMessage("must be a number greater than 0 and less than 2147483647")
  String mustBeIntegerRange();

  /**
   * Translated "getWhichMonthOfYear() not valid for recurrence type: {0}. ".
   * 
   * @return translated "getWhichMonthOfYear() not valid for recurrence type: {0}. "
  
   */
  @DefaultMessage("getWhichMonthOfYear() not valid for recurrence type: {0}. ")
  String invalidMonthOfYearForRecurrenceType(String arg0);

  /**
   * Translated "Delete Job".
   * 
   * @return translated "Delete Job"
  
   */
  @DefaultMessage("Delete Job")
  String deleteJob();

  /**
   * Translated "End by:".
   * 
   * @return translated "End by:"
  
   */
  @DefaultMessage("End by:")
  String endByLabel();

  /**
   * Translated "Day".
   * 
   * @return translated "Day"
  
   */
  @DefaultMessage("Day")
  String day();

  /**
   * Translated "Number of days to repeat.".
   * 
   * @return translated "Number of days to repeat."
  
   */
  @DefaultMessage("Number of days to repeat.")
  String numDaysToRepeat();

  /**
   * Translated "Only allowed to specify one action sequence.".
   * 
   * @return translated "Only allowed to specify one action sequence."
  
   */
  @DefaultMessage("Only allowed to specify one action sequence.")
  String onlyOneActionSequence();

  /**
   * Translated "Invalid recurrence string: {0}".
   * 
   * @return translated "Invalid recurrence string: {0}"
  
   */
  @DefaultMessage("Invalid recurrence string: {0}")
  String invalidRecurrenceString(String arg0);

  /**
   * Translated "Schedule Creator".
   * 
   * @return translated "Schedule Creator"
  
   */
  @DefaultMessage("Schedule Creator")
  String scheduleCreator();

  /**
   * Translated "June".
   * 
   * @return translated "June"
  
   */
  @DefaultMessage("June")
  String june();

  /**
   * Translated "Invalid String for week of month: {0}. ".
   * 
   * @return translated "Invalid String for week of month: {0}. "
  
   */
  @DefaultMessage("Invalid String for week of month: {0}. ")
  String invalidStringForWeekOfMonth(String arg0);

  /**
   * Translated "Invalid String for time of day value: {0}. ".
   * 
   * @return translated "Invalid String for time of day value: {0}. "
  
   */
  @DefaultMessage("Invalid String for time of day value: {0}. ")
  String invalidStringForTimeOfDay(String arg0);

  /**
   * Translated "Description:".
   * 
   * @return translated "Description:"
  
   */
  @DefaultMessage("Description:")
  String descriptionColon();

  /**
   * Translated "Recurrence pattern".
   * 
   * @return translated "Recurrence pattern"
  
   */
  @DefaultMessage("Recurrence pattern")
  String recurrencePattern();

  /**
   * Translated "Seconds must be a number <= {0}.".
   * 
   * @return translated "Seconds must be a number <= {0}."
  
   */
  @DefaultMessage("Seconds must be a number <= {0}.")
  String mustBeSecondsRange(String arg0);

  /**
   * Translated "Name".
   * 
   * @return translated "Name"
  
   */
  @DefaultMessage("Name")
  String name();

  /**
   * Translated "Day of month must be a number between 1 and 31.".
   * 
   * @return translated "Day of month must be a number between 1 and 31."
  
   */
  @DefaultMessage("Day of month must be a number between 1 and 31.")
  String dayOfMonthMustBeBetween();

  /**
   * Translated "Check to select all rows".
   * 
   * @return translated "Check to select all rows"
  
   */
  @DefaultMessage("Check to select all rows")
  String checkToSelectAll();

  /**
   * Translated "August".
   * 
   * @return translated "August"
  
   */
  @DefaultMessage("August")
  String august();

  /**
   * Translated "Are you sure you want to delete all checked schedules? There are {0} users subscribed to this schedule.".
   * 
   * @return translated "Are you sure you want to delete all checked schedules? There are {0} users subscribed to this schedule."
  
   */
  @DefaultMessage("Are you sure you want to delete all checked schedules? There are {0} users subscribed to this schedule.")
  String confirmDeleteQuestion(String arg0);

  /**
   * Translated "Unrecognized ScheduleType in ScheduleEditorValidator.isValid(): {0}.".
   * 
   * @return translated "Unrecognized ScheduleType in ScheduleEditorValidator.isValid(): {0}."
  
   */
  @DefaultMessage("Unrecognized ScheduleType in ScheduleEditorValidator.isValid(): {0}.")
  String unrecognizedSchedTypeInValidator(String arg0);

  /**
   * Translated "Minutes".
   * 
   * @return translated "Minutes"
  
   */
  @DefaultMessage("Minutes")
  String minutes();

  /**
   * Translated "April".
   * 
   * @return translated "April"
  
   */
  @DefaultMessage("April")
  String april();

  /**
   * Translated "The".
   * 
   * @return translated "The"
  
   */
  @DefaultMessage("The")
  String the();

  /**
   * Translated "Daily".
   * 
   * @return translated "Daily"
  
   */
  @DefaultMessage("Daily")
  String daily();

  /**
   * Translated "Seconds token must be an integer between 0 and 59, but it is: {0}. ".
   * 
   * @return translated "Seconds token must be an integer between 0 and 59, but it is: {0}. "
  
   */
  @DefaultMessage("Seconds token must be an integer between 0 and 59, but it is: {0}. ")
  String invalidSecondsToken(String arg0);

  /**
   * Translated "Schedule".
   * 
   * @return translated "Schedule"
  
   */
  @DefaultMessage("Schedule")
  String schedule();

  /**
   * Translated "Cron String:".
   * 
   * @return translated "Cron String:"
  
   */
  @DefaultMessage("Cron String:")
  String cronLabel();

  /**
   * Translated "Wednesday".
   * 
   * @return translated "Wednesday"
  
   */
  @DefaultMessage("Wednesday")
  String wednesday();

  /**
   * Translated "None".
   * 
   * @return translated "None"
  
   */
  @DefaultMessage("None")
  String XmlSerializer_stateNone();

  /**
   * Translated "Search Results".
   * 
   * @return translated "Search Results"
  
   */
  @DefaultMessage("Search Results")
  String searchResults();

  /**
   * Translated "AM".
   * 
   * @return translated "AM"
  
   */
  @DefaultMessage("AM")
  String am();

  /**
   * Translated "Run selected schedule(s) now".
   * 
   * @return translated "Run selected schedule(s) now"
  
   */
  @DefaultMessage("Run selected schedule(s) now")
  String runSchedules();

  /**
   * Translated "getDaysOfWeek() not valid for recurrence type: {0}. ".
   * 
   * @return translated "getDaysOfWeek() not valid for recurrence type: {0}. "
  
   */
  @DefaultMessage("getDaysOfWeek() not valid for recurrence type: {0}. ")
  String invalidDayOfWeekForRecurrenceType(String arg0);

  /**
   * Translated "Filename:".
   * 
   * @return translated "Filename:"
  
   */
  @DefaultMessage("Filename:")
  String filename();

  /**
   * Translated "hour(s)".
   * 
   * @return translated "hour(s)"
  
   */
  @DefaultMessage("hour(s)")
  String hoursLabel();

  /**
   * Translated "Unable to translate Cron string "{0}" into recurrence string.".
   * 
   * @return translated "Unable to translate Cron string "{0}" into recurrence string."
  
   */
  @DefaultMessage("Unable to translate Cron string \"{0}\" into recurrence string.")
  String cronStringCannotTransformToRecurrenceString(String arg0);

  /**
   * Translated "Invalid String for temporal value: {0}".
   * 
   * @return translated "Invalid String for temporal value: {0}"
  
   */
  @DefaultMessage("Invalid String for temporal value: {0}")
  String invalidTemporalValue(String arg0);

  /**
   * Translated "first".
   * 
   * @return translated "first"
  
   */
  @DefaultMessage("first")
  String first();
  
  /**
   * Translated "tableHeaderInputError".
   * 
   * @return translated "tableHeaderInputError"
  
   */
  @DefaultMessage("Must specify table header names.")
  String tableHeaderInputError();  
 
  /**
   * Translated "invalidDataGridTypeSet".
   * 
   * @return translated "invalidDataGridTypeSet"
  
   */
  @DefaultMessage("Invalid type set in data grid")
  String invalidDataGridTypeSet();

  /**
   * Translated "Color Chooser".
   * 
   * @return translated "Color Chooser"
  
   */
  @DefaultMessage("Color Chooser")
  String colorChooser();

  /**
   * Translated "Error".
   * 
   * @return translated "Error"
  
   */
  @DefaultMessage("Error")
  String error();

  /**
   * Translated "No filename has been entered.".
   * 
   * @return translated "No filename has been entered."
  
   */
  @DefaultMessage("No filename has been entered.")
  String noFilenameEntered();

  /**
   * Translated "Schedule Edit".
   * 
   * @return translated "Schedule Edit"
  
   */
  @DefaultMessage("Schedule Edit")  
  String scheduleEdit();

  /**
   * Translated "Steps:".
   * 
   * @return translated "Steps:"
  
   */
  @DefaultMessage("Steps:")  
  String steps();
}
