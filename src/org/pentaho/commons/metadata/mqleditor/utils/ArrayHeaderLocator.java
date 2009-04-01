package org.pentaho.commons.metadata.mqleditor.utils; 


import java.util.HashMap;
import java.util.Map;


/**
 *
 * <p>This class is used internal to the ArrayFilter, and ArrayComparator classes to allow them
 * to locate array entries by a column lable as opposed to an index.  The contsructor takes an
 * array of header names, and one method converts the name to an index based on the position
 * the name has in the array.  Another method allows you to associate a column name with an
 * index position even if you don't want/need to provide the whole header array.  Note column
 * names are case insensitive (i.e. fname is the same as FName)</p>
 *
 * <br><br><a href="http://www.fdsapi.com/javadocs/com/fdsapi/arrays/ArrayHeaderLocator.htm">View Code</a>
 * 
 */
public class ArrayHeaderLocator implements Cloneable {
    
    /** Creates a new instance of ArrayHeaderLocator */
    // Contains logic column names.  Arrays are typically index based not name based.
    private Map headerFromNum=null;
    private Map headerFromName=null;
    private boolean throwExceptions=false;
    public final int NOT_IN_HEADER=-3467651;// Really just any number to represent data doesn't exit
    
    public ArrayHeaderLocator() {
        initMaps();
    }
    
    public ArrayHeaderLocator(boolean throwExceptions) {
        initMaps();
        this.throwExceptions=throwExceptions;
    }
    
    public ArrayHeaderLocator(String[] header) {
        initMaps();
        // Note Integer object is used to map a logical name back to the index.
        for (int i=0;i<header.length;i++) {
            setColName(header[i], i);
        }
    }
    
    public ArrayHeaderLocator(String[] header, boolean throwExceptions) {
        this(header);
        this.throwExceptions=throwExceptions;
    }
    
    private void initMaps() {
        headerFromNum= new HashMap();
        headerFromName= new HashMap();
    }
    
    // Used to clone
    private ArrayHeaderLocator(Map headerFromNum, Map headerFromName, boolean throwExceptions) {
        this.headerFromNum=headerFromNum;
        this.headerFromName=headerFromName;
        this.throwExceptions=throwExceptions;
    }
    
        /** Returns the column number when passed a column name that matches the header array passed into the Constructor */
    public int getColNum(String columnName) {
        Integer col=(Integer) headerFromName.get(columnName);
        if (col!=null)
          return col.intValue();
        else if (throwExceptions)
          throw new IllegalArgumentException("The following column name was not in the header: "+columnName);
        else
          return NOT_IN_HEADER;

    }
    
    public void setThrowExceptions(boolean throwExceptions) {
        this.throwExceptions=throwExceptions;
    }
    
    public boolean getThrowExceptions() {
        return throwExceptions;
    }
    
        
    /** Return the column name associated with the passed in index (i.e. starts at 0) */
    public String getColName(int index) {
        String col=(String) headerFromNum.get(new Integer(index));
        if (col!=null)
          return col;
        else if (throwExceptions)
          throw new IllegalArgumentException("The following column index was not in the header: "+index);
        else 
          return null;
    }
    
    /** Returns true if the this column number is valid */
    public boolean containsColNum(int index) {
        return headerFromNum.containsKey(new Integer(index));
    }

    /** Returns true if the this column name is valid */
    public boolean containsColName(String colName) {
        return headerFromNum.containsKey(colName);
    }
    
    
    
    /** Allows you to explicitly associate a named label with a column index for the array (starting at 0).
     ** This is useful if you don't want to provide a whole header in the contructor, but would like to refer
     ** to a column or 2 by name.  Note after calling this method you will be able to get a column index
     ** if passed a column name, or vice versa.
     **/
    public void setColName(String columnName, int index) {
        Integer columnNum=new Integer(index);
        headerFromName.put(columnName, columnNum);
        headerFromNum.put(columnNum, columnName);
    }
    
        /** Clone this factory */
    public Object clone() {
        // Note because the Map interface and Object base class don't support cloning
        // the maps must be casted to an AppMap which inherits its ability to do a shallow
        // clone through HashMap.  
        Map num=(Map) ((HashMap) headerFromNum).clone();
        Map name=(Map) ((HashMap) headerFromName).clone();
        return new ArrayHeaderLocator(num, name,throwExceptions);
      
    }
    
    /** Clones the object and then casts it to the appropriate type */
    public ArrayHeaderLocator copy() {
        return (ArrayHeaderLocator) clone();
    }
    
    public String toString() {
        return headerFromNum.toString();
    }
    
    public static void main(String[] args) {
        String[] header={"fname","lname","salary"};
        ArrayHeaderLocator l=new ArrayHeaderLocator(header);
        System.out.println(l);
        l.setColName("ssn",3);
        System.out.println(l);
        System.out.println(l.getColName(2)+"="+l.getColNum("salary"));
        
    }
     
}
