/*!
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
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

public interface MqlCondition extends Serializable{

  public boolean validate();

  public MqlColumn getColumn();

  public Operator getOperator();

  /**
   * @return if isParameterized() then the name of the parameter whose value will be substituted before query execution, else the literal value
   */
  public String getValue();

  public CombinationType getCombinationType() ;
  
  /**
   * Value in this condition is not static, but rather supplied for each execution of this query.
   * 
   * @return true if value denotes parameter name rather than a literal value
   */
  public boolean isParameterized();
  
  public String getDefaultValue();

  public AggType getSelectedAggType();
}
