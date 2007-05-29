package org.pentaho.commons.mql.ui.mqldesigner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pentaho.pms.schema.BusinessColumn;
import org.pentaho.pms.schema.BusinessModel;
import org.pentaho.pms.schema.BusinessTable;
import org.pentaho.pms.schema.WhereCondition;

public class MQLWhereConditionModel {
  
  private String operator;       // AND
  private BusinessColumn field;  // customer_name
  private String condition;      // = 'Casters'
  
  public MQLWhereConditionModel(BusinessModel model, WhereCondition whereCondition) {
    Pattern p = Pattern.compile("\\[([^\\]]*)\\.([^\\]]*)\\] (.*)"); //$NON-NLS-1$
    Matcher m = p.matcher(whereCondition.getCondition());
    if (m.find()) {
      String tbl = m.group(1);
      String col = m.group(2);
      BusinessTable biztbl = model.findBusinessTable(tbl);
      field = biztbl.findBusinessColumn(col);
      condition = m.group(3);
    } else {
      // log error?
    }
  }
  
  public MQLWhereConditionModel(String operator, BusinessColumn field,  String condition) {
    this.operator  = operator;
    this.field     = field;
    this.condition = condition;
  }
  
  public String getOperator() {
    return operator;
  }
  
  public BusinessColumn getField() {
    return field;
  }
    
  public String getCondition() {
    return condition;
  }
  
  public void setCondition(String condition) {
      this.condition = condition;
  }
  
  public void setOperator(String operator) {
    this.operator = operator;
  }
  
}
