package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.*;
import org.pentaho.commons.metadata.mqleditor.editor.service.CWMStartup;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MQLEditorDebugGwtServlet extends RemoteServiceServlet implements MQLEditorGwtService {

  org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceDeligate deligate;

  public MQLEditorDebugGwtServlet() {

    CWMStartup.loadCWMInstance("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWMStartup.loadMetadata("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata_steelwheels.xmi", "/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$

    List<CWM> cwms = new ArrayList<CWM>();
    cwms.add(cwm);
    CwmSchemaFactory factory = new CwmSchemaFactory();
    deligate = new org.pentaho.commons.metadata.mqleditor.editor.service.impl.MQLEditorServiceDeligate(cwms, factory);
  }

  public MqlDomain getDomainByName(String name) {
    return deligate.getDomainByName(name);
  }

  public List<MqlDomain> getMetadataDomains() {
    return deligate.getMetadataDomains();
  }

  public String saveQuery(MqlQuery model) {
    return deligate.saveQuery(model);
  }

  public String serializeModel(MqlQuery query) {
    return deligate.serializeModel(query);
  }

  public String[][] getPreviewData(String query, int page, int limit) {
    return deligate.getPreviewData(query, page, limit);
  }

  public MqlQuery deserializeModel(String serializedQuery) {
    return deligate.deserializeModel(serializedQuery);
  }

}