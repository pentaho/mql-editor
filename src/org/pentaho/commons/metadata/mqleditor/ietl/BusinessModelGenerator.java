package org.pentaho.commons.metadata.mqleditor.ietl;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.di.core.vfs.KettleVFS;

public class BusinessModelGenerator {
  
  // eventually use V3 model
  public Model generateModel(CsvPhysicalModel model) throws Exception {
    
    // use code within Kettle to gen CSV model
    
    InputStream inputStream = KettleVFS.getInputStream(model.getCsvLocation());
    
    InputStreamReader reader = new InputStreamReader(inputStream);
    return null;
    // Read a line of data to determine the number of rows...
    //
    //String line = TextFileInput.getLine(null, reader, TextFileInputMeta.FILE_FORMAT_MIXED, new StringBuilder(1000));
    
    // Split the string, header or data into parts...
    //
   // String[] fieldNames = Const.splitString(line, model.getDelimiter()); 
    
 /*   if (!model.isHeaderPresent()) {
      // Don't use field names from the header...
      // Generate field names F1 ... F10
      //
      DecimalFormat df = new DecimalFormat("000"); // $NON-NLS-1$
      for (int i=0;i<fieldNames.length;i++) {
        fieldNames[i] = "Field_"+df.format(i); // $NON-NLS-1$
      }
    }
    else
    {
      if (!Const.isEmpty(model.getEnclosure())) {
          for (int i=0;i<fieldNames.length;i++) {
            if (fieldNames[i].startsWith(model.getEnclosure()) && fieldNames[i].endsWith(model.getEnclosure()) && fieldNames[i].length()>1) fieldNames[i] = fieldNames[i].substring(1, fieldNames[i].length()-1);
          }
      }
    }

    Model bizModel = new Model();
    List<Category> categories = new ArrayList<Category>();
    Category cat = new Category();
    cat.setId("CAT1");
    cat.setName("Columns");
    categories.add(cat);
    
    BusinessTable bt = new BusinessTable();
    bt.setId("BT1");
    bt.setName("CSV");

    List<Column> cols = new ArrayList<Column>();
    
    // Trim the names to make sure...
    //
    for (int i=0;i<fieldNames.length;i++) {
      fieldNames[i] = Const.trim(fieldNames[i]);
      Column col = new Column();
      col.setTable(bt);
      col.setId("BC" + i);
      col.setName(fieldNames[i]);
      col.setType(ColumnType.TEXT);
      cols.add(col);
    }
    
    cat.setBusinessColumns(cols);
    bizModel.setCategories(categories);
    return bizModel;*/
  }
  
}
