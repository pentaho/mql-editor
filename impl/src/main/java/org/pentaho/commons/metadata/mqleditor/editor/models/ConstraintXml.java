package org.pentaho.commons.metadata.mqleditor.editor.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * Class to represent a constraint in XML to be converted into an object
 */
@XmlAccessorType( XmlAccessType.FIELD )
public class ConstraintXml {
  @XmlElement( name = "operator" )
  private String operator;

  @XmlElement( name = "condition" )
  private String formula;

  public ConstraintXml( String operator, String formula ) {
    this.operator = operator;
    this.formula = formula;
  }

  public ConstraintXml() {
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator( String operator ) {
    this.operator = operator;
  }

  public String getFormula() {
    return formula;
  }

  public void setFormula( String formula ) {
    this.formula = formula;
  }
}
