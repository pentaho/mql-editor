package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pentaho.commons.metadata.mqleditor.MqlCategory;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.MqlModel;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;

public class FormulaParser {

  private static final String WRAPPED = "([^\\(]*)\\(\\[([^\\]]*)\\];([^\\)]*)\\)";
  private static final String GENERIC = "\\[([^\\]]*)\\]\\s+([^\\s]*)\\s+(.*)";
  
  
  private static Pattern genericPat = Pattern.compile(GENERIC); //$NON-NLS-1$
  private static Pattern wrappedPat = Pattern.compile(WRAPPED); //$NON-NLS-1$
  
  public static Condition parse(MqlModel model, String formula){
    Condition c = new Condition();
    String functionName = null;
    String fieldName = null;
    String value = null;
    String catId = null;
    String colId = null;
    boolean notOperator = false;
    
    Matcher m = genericPat.matcher(formula);
    if(m.find()){
      functionName = m.group(2);
      fieldName = m.group(1);
      value = m.group(3);
    } else {
      // see if it's a logical NOT(), strip it out and parse inner function
      if(formula.contains("NOT(")){
        notOperator = true;
        formula = formula.substring(4, formula.length()-1);
      }
      
      m = wrappedPat.matcher(formula);
      if(m.find()){
        functionName = m.group(1);
        fieldName = m.group(2);
        value = m.group(3);
      }
    }
    Operator op = Operator.parse(functionName.toUpperCase());
    // handle special NOT() wrapped functions
    if(notOperator){
      switch(op){
        case IS_NULL:
          op = Operator.IS_NOT_NULL;
          break;
        case IS_EMPTY:
          op = Operator.IS_NOT_EMPTY;
          break;
        case CONTAINS:
          op = Operator.DOES_NOT_CONTAIN;
          break;
      }
    }
    c.setOperator(op);
    if(value != null && value.charAt(0) == '\"' && value.charAt(value.length()-1) == '\"'){
      value = value.substring(1, value.length()-1);
    }
    c.setValue(value);
    
    // Field is formatted as catId.colId
    if(fieldName != null && fieldName.indexOf(".") > -1){
      String[] pair = fieldName.split("\\.");
      catId = pair[0];
      colId = pair[1];
    } 
    
    if(model != null && colId != null){
      Outter:
      for(MqlCategory cat : model.getCategories()){
        for(MqlColumn col : cat.getBusinessColumns()){
          if(col.getId().equals(colId)){
            c.setColumn((Column) col);
            break Outter;
          }
        }
      }
    }
    return c;
  }
}
