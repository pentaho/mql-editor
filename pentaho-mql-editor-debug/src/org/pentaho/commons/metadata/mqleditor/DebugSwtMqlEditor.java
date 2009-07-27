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
