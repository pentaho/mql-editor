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
 * Copyright (c) 2009 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;

public class FormulaParser {

  private static final String WRAPPED = "([^\\(]*)\\(\\[([^\\]]*)\\];([^\\)]*)\\)";
  private static final String GENERIC = "\\[([^\\]]*)\\]\\s*([><=]*)\\s*(.*)";
  private static final String VALUE_EVAL = ".*\"(.*)\"";

  private static Pattern genericPat = Pattern.compile(GENERIC); //$NON-NLS-1$
  private static Pattern wrappedPat = Pattern.compile(WRAPPED); //$NON-NLS-1$
  private static Pattern valueEvalPat = Pattern.compile(VALUE_EVAL);

  private String functionName = null;
  private String fieldName = null;
  private String value = null;
  private String catId = null;
  private String colId = null;
  private boolean notOperator = false;
  private String formula;
  private Condition c = new Condition();
  
  public FormulaParser(String formula){
    this.formula = formula;
    parse();
  }
  
  private void parse(){

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
    
    if(value != null) {
       //Extracts the value contained inside double quotes in the string
       Matcher matcher = valueEvalPat.matcher(value);
       if(matcher.find()) {
           value = matcher.group(1);
       }
    }
    c.setValue(value);
    
    // Field is formatted as catId.colId
    if(fieldName != null && fieldName.indexOf(".") > -1){
      String[] pair = fieldName.split("\\.");
      catId = pair[0];
      colId = pair[1];
    }
  }
  
  public Condition getCondition(){
    return c;
  }
  
  public String getColID(){
    return colId;
  }
  public String getCatID(){
    return catId;
  }
}
