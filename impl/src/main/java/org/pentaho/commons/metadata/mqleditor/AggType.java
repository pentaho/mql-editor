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


package org.pentaho.commons.metadata.mqleditor;

import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;

public enum AggType {
  SUM, COUNT, COUNT_DISTINCT, TOTAL, MIN, MAX, AVERAGE, NONE;

  private static final String AGG_TYPE_PREFIX = "AggType.";

  @Override
  public String toString() {
    return Workspace.getMessages() == null ? name() : Workspace.getMessages().getString( AGG_TYPE_PREFIX + name(),
        name() );
  }
}
