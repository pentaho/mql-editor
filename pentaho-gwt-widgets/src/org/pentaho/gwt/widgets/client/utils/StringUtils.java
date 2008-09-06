package org.pentaho.gwt.widgets.client.utils;

// see: http://commons.apache.org/lang/apidocs/org/apache/commons/lang/StringUtils.html

public class StringUtils {

  public static boolean isEmpty( String str ) {
    return null == str || "".equals( str ); //$NON-NLS-1$
  }
  
  public static String defaultString( String str, String xdefault )
  {
    return StringUtils.isEmpty( str ) ? xdefault : str;
  }
  
  public static String defaultString( String str )
  {
    return StringUtils.isEmpty( str ) ? "" : str; //$NON-NLS-1$
  }
  
  public static String defaultIfEmpty( String str, String xdefault )
  {
    return StringUtils.isEmpty( str ) ? xdefault : str;
  }
  
  public static String addStringToInt( String strAddend, int addend ) {
    return Integer.toString( Integer.parseInt( strAddend ) + addend );
  }
  
  public static String multiplyStringWithInt( String strMultiplicand, int multiplier ) {
    return Integer.toString( Integer.parseInt( strMultiplicand ) * multiplier );
  }
  
  public static String divideStringWithInt( String strDividend, int divisor ) {
    return Integer.toString( Integer.parseInt( strDividend ) / divisor );
  }

  // MAX_INT = 2147483647
  private static final String MATCH_POSITIVE_INTEGER_RE = "^\\s*[0-9]{1,10}\\s*$"; //$NON-NLS-1$
  public static boolean isPositiveInteger( String strInt ) {
    int length = strInt.length();
    if ( length > 10 ) {
      return false; // MAX_INT has 10 digits
    } else if ( length == 10 ) {
      // This is an execution-expensive path, since it relies on exception
      // processing to determine if strInt is an Integer or not. Minimize
      // the number of times this else-if is entered
      if ( strInt.matches( MATCH_POSITIVE_INTEGER_RE ) ) {
        try {
          Integer.parseInt( strInt );
          return true;
        } catch( NumberFormatException ignore ) {
        }
      }
      return false;
    } else {
      return strInt.matches( MATCH_POSITIVE_INTEGER_RE );
    }
  }
}
