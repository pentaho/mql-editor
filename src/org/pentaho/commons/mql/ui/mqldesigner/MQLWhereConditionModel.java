package org.pentaho.commons.mql.ui.mqldesigner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pentaho.pms.mql.WhereCondition;
import org.pentaho.pms.schema.BusinessCategory;
import org.pentaho.pms.schema.BusinessColumn;
import org.pentaho.pms.schema.BusinessModel;

public class MQLWhereConditionModel {
  
  private String operator;       // AND
  private BusinessColumn field;  // customer_name
  private String condition;      // = 'Casters'
  
  public MQLWhereConditionModel(BusinessModel model, WhereCondition whereCondition) {
    Pattern p = Pattern.compile("\\[([^\\]]*)\\.([^\\]]*)\\] (.*)"); //$NON-NLS-1$
    Matcher m = p.matcher(whereCondition.getCondition());
    if (m.find()) {
      String cat = m.group(1);
      String col = m.group(2);
      BusinessCategory bizCat = model.getRootCategory().findBusinessCategory(cat);
      field = bizCat.findBusinessColumn(col);
      condition = m.group(3);
      operator = whereCondition.getOperator();
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
