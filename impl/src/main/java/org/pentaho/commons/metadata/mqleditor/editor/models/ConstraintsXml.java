/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2026 by Pentaho Canada Inc. : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2030-06-15
 ******************************************************************************/
package org.pentaho.commons.metadata.mqleditor.editor.models;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

/**
 * Class to represent a list of constraints in XML to be converted into objects
 */
@XmlRootElement( name = "constraints" )
@XmlAccessorType( XmlAccessType.FIELD )
public class ConstraintsXml {

  @XmlElement( name = "constraint" )
  private List<ConstraintXml> constraintList;

  public ConstraintsXml( List<ConstraintXml> constraintList ) {
    this.constraintList = constraintList;
  }

  public ConstraintsXml() {
  }

  public List<ConstraintXml> getConstraintList() {
    return constraintList;
  }

  public void setConstraintList( List<ConstraintXml> constraintList ) {
    this.constraintList = constraintList;
  }
}
