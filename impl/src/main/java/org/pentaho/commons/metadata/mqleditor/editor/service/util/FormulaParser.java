/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;

import javax.swing.*;

public class FormulaParser {

  private static final String WRAPPED = "([^\\(]*)\\(\\[([^\\]]*)\\];(.*)(?=\\))"; //$NON-NLS-1$
  private static final String GENERIC = "\\[([^\\]]*)\\]\\s*([><=]+)\\s*(.*)";

  // Special case for Datevalues, DATEVALUE([param:foo]) and DATEVALUE("2010-01-01");
  private static final String DATEVALUE = "DATEVALUE\\(([^\\)]*)";

  private static final String VALUE_EVAL = ".*\"(.*)\"";
  // Matches the values of the IN function
  private static final String VALUE_EVAL_IN = "[^\"]*\"([^\"]*)\";{0,1}";
  private static final String VALUE_EVAL_IN_NUMERIC = "\\s*([^;\\s]+)\\s*;{0,1}";

  private static Pattern genericPat = Pattern.compile( GENERIC );
  private static Pattern wrappedPat = Pattern.compile( WRAPPED );
  private static Pattern valueEvalPat = Pattern.compile( VALUE_EVAL );
  private static Pattern valueEvalInPat = Pattern.compile( VALUE_EVAL_IN );
  private static Pattern valueEvalInNumericPat = Pattern.compile( VALUE_EVAL_IN_NUMERIC );
  private static Pattern datevaluePat = Pattern.compile( DATEVALUE );

  private String functionName = null;
  private String fieldName = null;
  private String value = null;
  private String catId = null;
  private String colId = null;
  private String aggType = null;
  private boolean notOperator = false;
  private String formula;
  private Condition c = new Condition();
  private String[] valueArray;
  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  public FormulaParser( String formula ) {
    this.formula = formula;
    parse();
  }

  private String checkValueAsDataTime( String value ) {
    Matcher m = datevaluePat.matcher( value );
    if ( m.find() ) {
      return m.group( 1 );
    }
    return value;
  }

  private void parse() {

    Matcher m = genericPat.matcher( formula );
    if ( m.find() ) {
      functionName = m.group( 2 );
      fieldName = m.group( 1 );
      value = m.group( 3 );
      value = checkValueAsDataTime( value );
    } else {
      // see if it's a logical NOT(), strip it out and parse inner function
      if ( formula.contains( "NOT(" ) ) {
        notOperator = true;
        formula = formula.substring( 4, formula.length() - 1 );
      }
      if ( formula.contains( "ISNA(" ) ) {
        formula = formula.substring( 5, formula.length() - 1 );
        fieldName = formula.substring( 1, formula.length() - 1 );
        functionName = "ISNA";
        value = "";
      }

      m = wrappedPat.matcher( formula );
      if ( m.find() ) {
        functionName = m.group( 1 );
        fieldName = m.group( 2 );
        value = m.group( 3 );
        value = checkValueAsDataTime( value );
      }
    }

    String errorMessage = "";
    if ( functionName == null) {
       errorMessage = "Query syntax not supported by PRD Editor: " + formula;
    }

    Operator op = null;
    try {
      op = Operator.parse( functionName.toUpperCase() );
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Dialog", JOptionPane.ERROR_MESSAGE);
      throw new RuntimeException( e );
    }

    // handle special NOT() wrapped functions
    if ( notOperator ) {
      switch ( op ) {
        case IS_NULL:
          op = Operator.IS_NOT_NULL;
          break;
        case CONTAINS:
          op = Operator.DOES_NOT_CONTAIN;
          break;
      }
    }
    c.setOperator( op );

    if ( value != null ) {
      if ( c.getOperator() == Operator.IN ) {
        Matcher matcher = null;
        if ( value.startsWith( "[param:" ) || value.contains( "\"" ) ) { // Poor attempt at determining if we're dealing
                                                                         // with parsing numerics or strings
          matcher = valueEvalInPat.matcher( value );
        } else {
          matcher = valueEvalInNumericPat.matcher( value );
        }
        StringBuilder parsedVal = new StringBuilder();
        List<String> values = new ArrayList<String>();
        while ( matcher.find() ) {
          if ( parsedVal.length() > 0 ) {
            parsedVal.append( "|" ); //$NON-NLS-1$
          }
          final String v = matcher.group( 1 );
          boolean quote = v.contains( "|" ); //$NON-NLS-1$
          if ( quote ) {
            parsedVal.append( "\"" ); //$NON-NLS-1$
          }
          parsedVal.append( v );
          values.add( v );
          if ( quote ) {
            parsedVal.append( "\"" ); //$NON-NLS-1$
          }
        }
        if ( parsedVal.length() != 0 ) {
          value = parsedVal.toString();
          valueArray = values.toArray( EMPTY_STRING_ARRAY );
        } else {
          valueArray = new String[] { value };
        }
      } else {
        // Extracts the value contained inside double quotes in the string
        Matcher matcher = valueEvalPat.matcher( value );
        if ( matcher.find() ) {
          value = matcher.group( 1 );
        }
        valueArray = new String[] { value };
      }
    }
    c.setValue( value );

    // Field is formatted as catId.colId.aggType
    if ( fieldName != null && fieldName.indexOf( "." ) > -1 ) {
      String[] pair = fieldName.split( "\\." );
      catId = pair[0];
      colId = pair[1];
      aggType = pair.length > 2 ? pair[2] : null;
    }
  }

  public Condition getCondition() {
    return c;
  }

  public String[] getValueAsArray() {
    return valueArray;
  }

  public String getColID() {
    return colId;
  }

  public String getCatID() {
    return catId;
  }

  public String getAggType() {
    return aggType;
  }
}
