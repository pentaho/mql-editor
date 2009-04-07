package org.pentaho.commons.metadata.mqleditor.utils; 


import java.sql.ResultSet;
import java.sql.SQLException;
/** <p>Class that converts a Java ResultSetConverter to an Object[][] array.  Some advantages of doing this
 * are:</p><br>
 * 1) The data can be manipulated after the database connection is closed<br>
 * 2) The data can be cached<br>
 * 3) Array functions such as sort may be used on the data<br>
 * 4) Data Can be serialized<br>
 * 5) Data can be iterated over many times in any direction<br>
 * 6) You can get the number of rows within a ResultSetConverter (you can't do this with ResultSets)<br>
 * 
 *  */

public class ResultSetConverter extends java.lang.Object implements java.io.Serializable
{
   private String[] metaData;// contains column name of the ResultSetConverter (ResultSetConverter metadata)
   private int[] columnTypes;// contains column type of the ResultSetConverter (ResultSetConverter metadata)
   private String[] columnTypeNames;// contains column type of the ResultSetConverter (ResultSetConverter metadata)
   private String[][] resultSet;// 2 dimensional array version of the ResultSetConverter
   private ArrayHeaderLocator locator;

   /** Convert the ResultSetConverter to a ResultSetConverter.  Note the ResultSetConverter will be iterated through
    * in this constructor and so if the ResultSetConverter only supports one iteration an exception will be 
    * thrown if the calling code attempts iteration a second time.
    */
   public ResultSetConverter(ResultSet rs) throws SQLException {
            ResultSetHelper rsu=ResultSetHelper.createInstance();

            metaData  = rsu.getColumnNames(rs.getMetaData());
            columnTypes = rsu.getColumnTypes(rs.getMetaData());
            columnTypeNames = rsu.getColumnTypesNames(rs.getMetaData());
            resultSet = rsu.resultSetToStringArray(rs);
            locator   = new ArrayHeaderLocator(metaData, true);
   }

   /** A constructor that supports converting header and body arrays to a ResultSetConverter */
   public ResultSetConverter(String[] metaData, int[] columnTypes, String[][] resultSet) {
       this.metaData=metaData;
       this.columnTypes=columnTypes;
       this.resultSet=resultSet;
       // true means throw exceptions if column name doesn't exit in ArrayHeaderLocator
       this.locator = new ArrayHeaderLocator(metaData, true);

   }

   /** A constructor that supports converting header and body arrays to a ResultSetConverter */
   public ResultSetConverter(String[] metaData, String[] columnTypesNames, String[][] resultSet) {
       this.metaData=metaData;
       this.columnTypeNames=columnTypesNames;
       this.resultSet=resultSet;
       // true means throw exceptions if column name doesn't exit in ArrayHeaderLocator
       this.locator = new ArrayHeaderLocator(metaData, true);

   }
   /** Returns at 1 dimensional array of column names from the ResultSetConverter. */
   public String[] getMetaData() {
       return metaData;
   }

   /** Returns at 1 dimensional array of column names from the ResultSetConverter. */
   public int[] getColumnTypes() {
       return columnTypes;
   }

   /** Returns at 1 dimensional array of column names from the ResultSetConverter. */
   public String[] getColumnTypeNames() {
       return columnTypeNames;
   }
   
   /** Returns a 2 dimensional array containing the data in the ResultSetConverter */
   public String[][] getResultSet() {
       return resultSet;
   }

   /** Returns true if the ResultSetConverter has no rows */
   public boolean isEmpty() {
       return getRowCount()==0;
   }

   /** Returns the number of rows in the ResultSetConverter */
   public int getRowCount() {
       return (resultSet==null) ? 0 : resultSet.length;
   }

   /** Returns the number of columns in the ResultSetConverter */
   public int getColumnCount() {
     return (metaData==null) ? 0 : metaData.length;   
   }
   
   /** Returns the data at the specified row, col location.  Being as the data is an array
    * the row and column indices start at 0 and not 1.
    **/
   public Object getCellData(int row, int col) {
       return resultSet[row][col]; 
   }
   
   /** Returns the data at the specified row, col location with the col location corresponding
    * to the position of the passed in column name.  Being as the data is an array
    * the row index starts at 0 and not 1.
    **/
   public Object getCellData(int row, String colName) {
       return resultSet[row][locator.getColNum(colName)];
   }

}

