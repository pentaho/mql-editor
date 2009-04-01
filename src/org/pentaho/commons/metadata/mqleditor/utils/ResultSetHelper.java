package org.pentaho.commons.metadata.mqleditor.utils; 
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Provides various methods for manipulating ResultSets such as converting them 
 * to Lists, and 2 dimensional arrays.  </p>
 *
 *
 */


public class ResultSetHelper extends java.lang.Object
{

    static ResultSetHelper resultSetHelper = new ResultSetHelper();

    protected ResultSetHelper() {
    }

    public static ResultSetHelper createInstance() {
        return resultSetHelper;
    }
  


  /**
   * The following method returns a List containing the ResultSetObject data.
   * Rows contain ResultSetObject rows as object arrays. The method getRowData() converts ResultSetObject
   * row data into an object array.
   */
  public void resultSetToList(List list, ResultSet resultSet) throws SQLException {
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    int columnCount = resultSetMetaData.getColumnCount();

    //Loop through the ResultSetObject data rows and add each one to the List
    while(resultSet.next()) {
      list.add(getRowData(resultSet, columnCount));
    }
  }


  /**
   * The following method returns a List containing the ResultSetMetaData column names.
   * There is only 1 row in the List and it will always contain a string array of the column names.
   */
   public void resultSetMetaDataToList(List list, ResultSet resultSet) throws SQLException {
    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
    int columnCount = resultSetMetaData.getColumnCount();

    //Add the String array of column names to the List
    list.add(getColumnNames(resultSetMetaData));

  }


 /**
   * <p>The following method converts ResultSetObject data to a Map of object keys and values using an instance
   * of HashMap.</p>
   *
   * <p>The first column in the ResultSetObject supplies the key, the second column the value. If the ResultSetObject
   * contains more than two columns then subsequent columns are ignored. If the ResultSetObject supplies
   * only one column then an SQLException is thrown. The ResultSetObject values are converted to objects
   * using the getRowData() method which converts a ResultSetObject row into an array of objects.</p>
   */
  public void resultSetToMap(Map map, ResultSet resultSet) throws SQLException {
    int columnCount = resultSet.getMetaData().getColumnCount();
    Object[] rowData;

    if (columnCount < 2) {
      throw new SQLException("resultSetToMap: At least two columns needed for conversion.");
    }

    //Loop through the ResultSetObject data rows and adding to the HashMap the first column as the key,
    //and the second column as the value.
    while(resultSet.next()) {
      rowData = getRowData(resultSet, columnCount);
      map.put(rowData[0], rowData[1]);
    }

  }
  
  
 /**
   * <p>The following method converts an Object[][] to a Map of object keys and values using an instance
   * of Map.</p>
   *
   * <p>The first column in the array supplies the key, the second column the value. If the array
   * contains more than two columns then subsequent columns are ignored. If the array supplies
   * only one column then a RuntimeException is thrown. This method may be used to convert the
   * data in a ResultSetObject to a map.</p>
   */
  public void objectArrayToMap(Map map, Object[][] data) {
    int rowCount = (data==null) ? 0 : data.length;
    int columnCount = (data==null || data.length==0) ? 0 : data[0].length;

    //Loop through the array data rows and adding to the HashMap the first column as the key,
    //and the second column as the value.
    for (int i=0; i<rowCount; i++) {
      map.put(data[i][0], data[i][1]);
    }

}

 /**
   * <p>The following method converts an Object[][] to a List where each entry in the list is a row (i.e. a
   * one dimensional array Object[])</p>
   *
   * <p>This list add/concatenate multiple arrays by making multiple calls while passing in the same list</p>
   */
  public void objectArrayToList(List list, Object[][] data) {
    int rowCount = (data==null) ? 0 : data.length;
    
    //Loop through the array data rows and add each row to the List.
    for (int i=0; i<rowCount; i++) {
      list.add(data[i]);
    }

}



  /**
   * <p>The following method converts an List, into a two dimensional string array. 
   * This array must be be allocated before it can be passed as an argument to the List 
   * toArray() method. The method assumes the list contains one dimensional arrays.</p>
   *
   * <p>The first dimension is the row data.</p>
   *
   * <p>The second dimension is the columns. The number of coluns is obtained by 
   * length of the string array in the first element of the List since:</p>
   *    1. The length of all arrays in the List must be the same.<br>
   */
  public Object[][] listToObjectArray(List list) {
        if (list==null || list.isEmpty())
          return null;
        else {
            Object[][] resultArr = new Object[list.size()][];
            list.toArray(resultArr);
            return resultArr;
        }
  }
  

  /**
   * The following method simply takes the ResultSetObject and converts it to a two dimensional
   * array of Objects containing data.
   */
   public Object[][] resultSetToObjectArray(ResultSet resultSet) throws SQLException {
        List list = new ArrayList(); 
        resultSetToList(list, resultSet);
        Object[][] arr=listToObjectArray(list); 
        return arr;
  }
  
 

  /**
   * The following method returns an array of strings containing the column names for
   * a given ResultSetMetaData object.
   */
  public String[] getColumnNames(ResultSetMetaData resultSetMetaData) throws SQLException {
    int columnCount = resultSetMetaData.getColumnCount();
    String columnNames[] = new String[columnCount];

    for(int colIndex=1; colIndex<=columnCount; colIndex++){
      columnNames[colIndex-1] = resultSetMetaData.getColumnName(colIndex);
    }

    return columnNames;
  }


  /**
   * The following method returns an array of int(java.sql.Types) containing the column types for
   * a given ResultSetMetaData object.
   */
  public int[] getColumnTypes(ResultSetMetaData resultSetMetaData) throws SQLException {
    int columnCount = resultSetMetaData.getColumnCount();
    int[] columnTypes = new int[columnCount];

    for(int colIndex=1; colIndex<=columnCount; colIndex++){
      columnTypes[colIndex-1] = resultSetMetaData.getColumnType(colIndex);
    }

    return columnTypes;
  }

  

  /**
   * The following method returns an array of strings containing the column names for
   * a given ResultSetMetaData object.  The returnCols array is the columns to return.
   */
  public String[] getColumnNames(ResultSetMetaData resultSetMetaData, int[] returnCols) throws SQLException {
    int columnCount = returnCols.length; //resultSetMetaData.getColumnCount();
    String columnNames[] = new String[returnCols.length];

    for(int colIndex=0; colIndex<columnCount; colIndex++){
      columnNames[colIndex] = resultSetMetaData.getColumnName(returnCols[colIndex]);
    }

    return columnNames;
  }


  /**
   * The following method returns an array of objects containing the data values for
   * each column for the current row in a given ResultSetObject. The columnCount parameter
   * saves the method having to address the ResultSetMetaData to obtain the number of
   * columns.
   */
 private Object[] getRowData(ResultSet resultSet, int columnCount) throws SQLException {
    Object rowData[] = new Object[columnCount];

    for(int colIndex=1; colIndex<=columnCount; colIndex++) {
       rowData[colIndex-1] = resultSet.getObject(colIndex);
    }
    return rowData;
  }
  
 
   /** Calls toString() on all elements of an Object[][] array and converts all elements to a String[][] array. */
    public String[][] convert(Object[][] arr) {
        if (arr==null)
            return null;
        else {
            int rows=arr.length;
            int cols=arr[0].length;
            String[][] strArr=new String[rows][cols];
            for (int i=0; i<rows; i++)
                for (int j=0; j<cols; j++)
                    strArr[i][j] = (arr[i][j]==null) ? null : arr[i][j].toString();
 
            return strArr;
        }
    }
 
       /** Calls toString() on all elements of an Object[] array and converts all elements to a String[] array. */
    public String[] convert(Object[] arr) {
        if (arr==null)
            return null;
        else {
            int rows=arr.length;
            String[] strArr=new String[rows];
            for (int i=0; i<rows; i++)
               strArr[i] = (arr[i]==null) ? null : arr[i].toString();
 
            return strArr;
        }
    }
    
 public static void main(String[] args) {
     Object[][] data={{"key1","value1","ignored"},{"key2","value2", "ignored"},};
     Object[] header={"fname","lname"};
     String[] headerStrArr=resultSetHelper.convert(header);
     
     Map map=new HashMap();
     List list=new ArrayList();
     resultSetHelper.objectArrayToMap(map, data);
     System.out.println(map);
     resultSetHelper.objectArrayToList(list, data); // add data to list
     resultSetHelper.objectArrayToList(list, data); // add data to list again
     data=resultSetHelper.listToObjectArray(list);  // get both arrays out of list
     resultSetHelper.objectArrayToList(list, data); // add data to list again
     data=resultSetHelper.listToObjectArray(list);  // get both arrays out of list
 }
}


