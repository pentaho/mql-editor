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
package org.pentaho.commons.metadata.mqleditor;

import org.pentaho.commons.metadata.mqleditor.editor.SwtMqlEditor;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorServiceCWMImpl;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.CWMStartup;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.schema.SchemaMeta;

public class DebugSwtMqlEditor {

  
  public static void main(String[] args) {
    

    CWMStartup.loadCWMInstance("/metadata/repository.properties", "/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWMStartup.loadMetadata("/metadata_steelwheels.xmi", "/metadata"); //$NON-NLS-1$ //$NON-NLS-2$

    CwmSchemaFactory factory = new CwmSchemaFactory();
    SchemaMeta meta = factory.getSchemaMeta(cwm);

    SwtMqlEditor editor = new SwtMqlEditor(new MQLEditorServiceCWMImpl(meta), null);
    editor.hidePreview();
    editor.show();
  }
  
}
