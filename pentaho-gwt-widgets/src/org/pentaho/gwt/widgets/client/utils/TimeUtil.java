package org.pentaho.gwt.widgets.client.utils;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;

import com.google.gwt.i18n.client.DateTimeFormat;

public class TimeUtil {

  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();
  public static final int HOURS_IN_DAY = 24;

  public static final int MINUTES_IN_HOUR = 60;

  public static final int SECONDS_IN_MINUTE = 60;

  public static final int MILLISECS_IN_SECONDS = 1000;

  public static final int MIN_HOUR = 0;
  public static final int MAX_HOUR = HOURS_IN_DAY/2;

  public static final int MAX_MINUTE = 60;
  
  public static final int MAX_SECOND_BY_MILLISEC = Integer.MAX_VALUE / MILLISECS_IN_SECONDS;
  public static final int MAX_MINUTE_BY_MILLISEC = MAX_SECOND_BY_MILLISEC / SECONDS_IN_MINUTE;
  public static final int MAX_HOUR_BY_MILLISEC = MAX_MINUTE_BY_MILLISEC / MINUTES_IN_HOUR;

  public enum TimeOfDay {
    AM(0, MSGS.am() ),
    PM(1, MSGS.pm() );
    
    TimeOfDay(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static TimeOfDay[] timeOfDay = { 
      AM, PM 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static TimeOfDay get(int idx) {
      return timeOfDay[idx];
    }

    public static int length() {
      return timeOfDay.length;
    }    
    
    public static TimeOfDay stringToTimeOfDay( String timeOfDay ) throws EnumException {
      for (TimeOfDay v : EnumSet.range(TimeOfDay.AM, TimeOfDay.PM)) {
        if ( v.toString().equals( timeOfDay ) ) {
          return v;
        }
      }
      throw new EnumException( MSGS.invalidStringForTimeOfDay( timeOfDay ) );
    }
  } // end enum TimeOfDay
  
  public enum DayOfWeek {
    SUN(0, MSGS.sunday() ),
    MON(1, MSGS.monday() ),
    TUES(2, MSGS.tuesday() ),
    WED(3, MSGS.wednesday() ),
    THUR(4, MSGS.thursday() ),
    FRI(5, MSGS.friday() ), 
    SAT(6, MSGS.saturday() );

    DayOfWeek(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static DayOfWeek[] week = { 
      SUN, MON, TUES, WED, THUR, FRI, SAT 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static DayOfWeek get(int idx) {
      return week[idx];
    }

    public static int length() {
      return week.length;
    }
  } /* end enum */

  public enum MonthOfYear {
    JAN(0, MSGS.january()), 
    FEB(1, MSGS.february()), 
    MAR(2, MSGS.march()), 
    APR(3, MSGS.april()), 
    MAY(4, MSGS.may()), 
    JUN(5, MSGS.june()), 
    JUL(6, MSGS.july()), 
    AUG(7, MSGS.august()), 
    SEPT(8, MSGS.september()), 
    OCT(9, MSGS.october()), 
    NOV(10, MSGS.november()), 
    DEC(11, MSGS.december());

    MonthOfYear(int value, String name) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static MonthOfYear[] year = { 
      JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEPT, OCT, NOV, DEC 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static MonthOfYear get(int idx) {
      return year[idx];
    }

    public static int length() {
      return year.length;
    }
  } /* end enum */

  public enum WeekOfMonth {
    FIRST( 0, MSGS.first() ),
    SECOND( 1, MSGS.second() ),
    THIRD( 2, MSGS.third() ),
    FOURTH( 3, MSGS.fourth() ),
    LAST( 4, MSGS.last() );
    
    WeekOfMonth( int value, String name ) {
      this.value = value;
      this.name = name;
    }

    private final int value;

    private final String name;

    private static WeekOfMonth[] week = { 
      FIRST, SECOND, THIRD, FOURTH, LAST 
    };

    public int value() {
      return value;
    }

    public String toString() {
      return name;
    }

    public static WeekOfMonth get(int idx) {
      return week[idx];
    }

    public static int length() {
      return week.length;
    }    
    
    public static WeekOfMonth stringToWeekOfMonth( String weekOfMonth ) throws EnumException {
      for (WeekOfMonth v : EnumSet.range(WeekOfMonth.FIRST, WeekOfMonth.LAST)) {
        if ( v.toString().equals( weekOfMonth ) ) {
          return v;
        }
      }
      throw new EnumException( MSGS.invalidStringForWeekOfMonth( weekOfMonth ) );
    }
  } // end enum WeekOfMonth

  private static Map<MonthOfYear, Integer> validNumDaysOfMonth = new HashMap<MonthOfYear, Integer>();
  static {
    validNumDaysOfMonth.put(MonthOfYear.JAN, 31);
    validNumDaysOfMonth.put(MonthOfYear.FEB, 29);
    validNumDaysOfMonth.put(MonthOfYear.MAR, 31);
    validNumDaysOfMonth.put(MonthOfYear.APR, 30);
    validNumDaysOfMonth.put(MonthOfYear.MAY, 31);
    validNumDaysOfMonth.put(MonthOfYear.JUN, 30);
    validNumDaysOfMonth.put(MonthOfYear.JUL, 31);
    validNumDaysOfMonth.put(MonthOfYear.AUG, 31);
    validNumDaysOfMonth.put(MonthOfYear.SEPT, 30);
    validNumDaysOfMonth.put(MonthOfYear.OCT, 31);
    validNumDaysOfMonth.put(MonthOfYear.NOV, 30);
    validNumDaysOfMonth.put(MonthOfYear.DEC, 31);
  }
  
  private TimeUtil() {
  } // cannot create instance, static class

  public static int daysToSecs(int days) {
    return hoursToSecs(days * HOURS_IN_DAY);
  }

  public static int hoursToSecs(int hours) {
    return minutesToSecs(hours * MINUTES_IN_HOUR);
  }

  public static int minutesToSecs(int minutes) {
    return minutes * SECONDS_IN_MINUTE;
  }

  public static int secsToMillisecs(int secs) {
    return secs * MILLISECS_IN_SECONDS;
  }

  public static int secsToDays(int secs) {
    return secs / HOURS_IN_DAY / MINUTES_IN_HOUR / SECONDS_IN_MINUTE;
  }

  public static int secsToHours(int secs) {
    return secs / MINUTES_IN_HOUR / SECONDS_IN_MINUTE;
  }

  public static int secsToMinutes(int secs) {
    return secs / SECONDS_IN_MINUTE;
  }

  public static boolean isSecondsWholeDay(int secs) {
    return ( daysToSecs( secsToDays( secs )) ) == secs;
  }

  public static boolean isSecondsWholeHour(int secs) {
    return ( hoursToSecs( secsToHours( secs ) ) ) == secs;
  }

  public static boolean isSecondsWholeMinute(int secs) {
    return ( minutesToSecs( secsToMinutes( secs ) ) ) == secs;
  }
  
  /**
   * Time of day is element of {AM, PM}
   * @param hour
   * @return
   */
  public static TimeOfDay getTimeOfDayBy0To23Hour( String hour ) {
    return Integer.parseInt( hour ) < MAX_HOUR ? TimeOfDay.AM : TimeOfDay.PM;
  }
  
  /**
   * Convert a 24 hour time, where hours are 0-23, to a twelve hour time,
   * where 0-23 maps to 0...11 AM and 0...11 PM
   * @param int0To23hour int integer in the range 0..23
   * @return int integer in the range 0..11
   */
  public static int to12HourClock( int int0To23hour ) {
    assert int0To23hour >=0 && int0To23hour<=23 : "int0To23hour is out of range"; //$NON-NLS-1$
    
    int int0To11 = int0To23hour < MAX_HOUR
      ? int0To23hour
      : int0To23hour - MAX_HOUR;
    return int0To11;
  }
  /**
   * @param time String it will look like: '17:17:00 PM' for 5:17 PM
   */
  public static String to12HourClock( String time ) {
    String[] parts = time.split( ":" ); //$NON-NLS-1$
    int hour = Integer.parseInt( parts[0] );
    if ( hour > MAX_HOUR ) {
      hour -= MAX_HOUR;
    }
    return Integer.toString( hour ) + ":" + parts[1] + ":" + parts[2];  //$NON-NLS-1$//$NON-NLS-2$
  }
  
  /**
   * map 0..11 to 12,1..11
   */
  public static int map0Through11To12Through11( int int0To11 ) {
    return int0To11 == 0 ? 12 : int0To11;
  }
  
  // NOTE: this method will produce rounding errors, since it doesn't round, it truncates
  public static int millsecondsToSecs( int milliseconds ) {
    return milliseconds / MILLISECS_IN_SECONDS;
  }
  /**
   * TODO sbarkdull, gwt 1.5 has a DateTimeFormatter, change this method to use it.
   * 
   * sample output: May 21, 2008 8:29:21 PM
   * This is consistent with the formatter constructed like this:
   * DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, Locale.getDefault());
   */
  public static String getDateTimeString( String month, String dayInMonth, String year, 
      String hour, String minute, String second, TimeOfDay timeOfDay ) {
    return new StringBuilder()
      .append( getDateString( month, dayInMonth, year ) )
      .append( " " ) //$NON-NLS-1$
      .append( hour )
      .append( ":" ) //$NON-NLS-1$
      .append( minute )
      .append( ":" ) //$NON-NLS-1$
      .append( second )
      .append( " " ) //$NON-NLS-1$
      .append( timeOfDay.toString() ).toString();
  }

  // this code runs in a single thread, so it is ok to share these two DateTimeFormats
  private static DateTimeFormat dateFormatter = DateTimeFormat.getLongDateFormat();
  private static DateTimeFormat dateTimeFormatter = DateTimeFormat.getFormat( MSGS.dateFormatLongMedium() );
  
  public static Date getDateTime( String time, Date date ) {
    String strDate = dateFormatter.format( date );
    return dateTimeFormatter.parse( strDate + " " + time ); //$NON-NLS-1$
  }
  
  /**
   * 
   * @param strDate String representing the date, in this format: MMM dd, yyyy HH:mm:ss a
   * @return
   */
  public static Date getDate( String strDate ) {
    return dateTimeFormatter.parse( strDate );
  }
  
  /**
   * Get the time part of a date string.
   * 
   * @param dateTime String in this format: MMM dd, yyyy HH:mm:ss a
   * @return String HH:mm:ss a
   */
  public static String getTimePart( String dateTime ) {
    String[] parts = dateTime.split( "\\s" ); //$NON-NLS-1$

    //TODO sbarkdull, use StringBuilder
    return parts[3] + " " + parts[4]; //$NON-NLS-1$
  }

  /**
   * Get the time part of a date string.
   * 
   * @param dateTime Date
   * @return String HH:mm:ss a
   */
  public static String getTimePart( Date dateTime ) {
    String strDateTime = dateTimeFormatter.format( dateTime );
    return to12HourClock( getTimePart( strDateTime ) );
  }
  /**
   * Get the time part of a date string.
   * 
   * @param dateTime String in this format: MMM dd, yyyy HH:mm:ss a
   * @return String HH:mm:ss a
   */
  public static String getDatePart( String dateTime ) {
    String[] parts = dateTime.split( "\\s" ); //$NON-NLS-1$
    //TODO sbarkdull, use StringBuilder
    return parts[0] + " " + parts[1] + " " + parts[2]; //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public static String get0thTime() {
    //TODO sbarkdull, use StringBuilder
    return "12:00:00 " + TimeOfDay.AM.toString(); //$NON-NLS-1$
  }
  
  public static String zeroTimePart( String dateTime ) {
    //TODO sbarkdull, use StringBuilder
    return getDatePart( dateTime ) + " " + get0thTime(); //$NON-NLS-1$
  }
  
  public static Date zeroTimePart( Date dateTime ) {
    //TODO sbarkdull, use StringBuilder
    return getDateTime( get0thTime(), dateTime );
  }
  
  /**
   * TODO sbarkdull, gwt 1.5 has a DateTimeFormatter, change this method to use it.
   * 
   * sample output: May 21, 2008
   * This is consistent with the formatter constructed like this:
   * DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
   */
  public static String getDateString( String month, String dayInMonth, String year ) {
    return new StringBuilder()
      .append( month )
      .append( " " ) //$NON-NLS-1$
      .append( dayInMonth )
      .append( ", " ) //$NON-NLS-1$
      .append( year ).toString();
  }

  public static boolean isValidNumOfDaysForMonth(int numDays, MonthOfYear month) {
    if (numDays < 1) {
      return false;
    } else {
      return validNumDaysOfMonth.get(month) <= numDays;
    }
  }
  
  /**
   * Is <param>num</param> between <param>low</param> and <param>high</param>, inclusive.
   * @param low
   * @param num
   * @param high
   * @return boolean true if <param>num</param> between <param>low</param> 
   * and <param>high</param>, inclusive, else false. 
   */
  private static boolean isNumBetween( int low, int num, int high ) {
    return num >= low && num <= high;
  }
  
  public static boolean isDayOfMonth( int num ) {
    return isNumBetween( 1, num, 31 );
  }
  
  public static boolean isDayOfWeek( int num ) {
    return isNumBetween( 1, num, 7 );
  }
  
  public static boolean isWeekOfMonth( int num ) {
    return isNumBetween( 1, num, 4 );
  }
  
  public static boolean isMonthOfYear( int num ) {
    return isNumBetween( 1, num, 12 );
  }
  
  public static boolean isSecond( int num ) {
    return isNumBetween( 0, num, 59 );
  }
  
  public static boolean isMinute( int num ) {
    return isNumBetween( 0, num, 59 );
  }
  
  public static boolean isHour( int num ) {
    return isNumBetween( 0, num, 23 );
  }
  
//  private static final String MATCH_DATE_STRING_RE = "^[0-9]{1,2}$";
//  public boolean isDateStr( String strInt ) {
//    return MATCH_DATE_STRING_RE.matches( strInt );
//  }
  
  public static void main( String[] args ) {
    assert daysToSecs( 13 ) == 1123200: ""; //$NON-NLS-1$
    assert daysToSecs( 13 ) != 1123201 : ""; //$NON-NLS-1$
    assert daysToSecs( 13 ) != 1123199 : ""; //$NON-NLS-1$
    
    assert hoursToSecs( 13 ) == 46800: ""; //$NON-NLS-1$
    assert hoursToSecs( 13 ) != 46801 : ""; //$NON-NLS-1$
    assert hoursToSecs( 13 ) != 46799 : ""; //$NON-NLS-1$
    
    assert minutesToSecs( 13 ) == 780: ""; //$NON-NLS-1$
    assert minutesToSecs( 13 ) != 781 : ""; //$NON-NLS-1$
    assert minutesToSecs( 13 ) != 779 : ""; //$NON-NLS-1$
    
    assert secsToDays( 1123200 ) == 13 : ""; //$NON-NLS-1$
    assert secsToDays( 1123201 ) != 13 : ""; //$NON-NLS-1$
    assert secsToDays( 1123199 ) != 13 : ""; //$NON-NLS-1$
    
    assert secsToHours( 46800 ) == 13 : ""; //$NON-NLS-1$
    assert secsToHours( 46801 ) != 13 : ""; //$NON-NLS-1$
    assert secsToHours( 46799 ) != 13 : ""; //$NON-NLS-1$
    
    assert secsToMinutes( 780 ) == 13 : ""; //$NON-NLS-1$
    assert secsToMinutes( 781 ) != 13 : ""; //$NON-NLS-1$
    assert secsToMinutes( 779 ) != 13 : ""; //$NON-NLS-1$
    
    assert isSecondsWholeDay( 1123200 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeDay( 1123201 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeDay( 1123199 ) : ""; //$NON-NLS-1$
    
    assert isSecondsWholeHour( 46800 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeHour( 46801 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeHour( 46799 ) : ""; //$NON-NLS-1$
    
    assert isSecondsWholeMinute( 780 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeMinute( 781 ) : ""; //$NON-NLS-1$
    assert !isSecondsWholeMinute( 779 ) : ""; //$NON-NLS-1$

    assert getTimeOfDayBy0To23Hour( "0" ) == TimeOfDay.AM : "hour 0 is AM"; //$NON-NLS-1$ //$NON-NLS-2$
    assert getTimeOfDayBy0To23Hour( "11" ) == TimeOfDay.AM : "hour 11 is AM"; //$NON-NLS-1$ //$NON-NLS-2$
    assert getTimeOfDayBy0To23Hour( "12" ) == TimeOfDay.PM: "hour 12 is PM"; //$NON-NLS-1$ //$NON-NLS-2$
    assert getTimeOfDayBy0To23Hour( "13" ) == TimeOfDay.PM : "hour 13 is PM"; //$NON-NLS-1$ //$NON-NLS-2$
    assert getTimeOfDayBy0To23Hour( "23" ) == TimeOfDay.PM : "hour 23 is PM"; //$NON-NLS-1$ //$NON-NLS-2$

    assert to12HourClock( 0 )==( 1 ) : "0 is 1"; //$NON-NLS-1$
    assert to12HourClock( 11 )==( 12 ) : "11 is 12"; //$NON-NLS-1$
    assert to12HourClock( 12 )==( 1 ) : "12 is 1"; //$NON-NLS-1$
    assert to12HourClock( 23 )==( 11 ) : "23 is 11"; //$NON-NLS-1$
    
    System.out.println( "done" ); //$NON-NLS-1$
  }
}
