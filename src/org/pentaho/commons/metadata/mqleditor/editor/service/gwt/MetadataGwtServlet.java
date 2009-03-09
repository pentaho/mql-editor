package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IBusinessColumn;
import org.pentaho.commons.metadata.mqleditor.ICondition;
import org.pentaho.commons.metadata.mqleditor.IDomain;
import org.pentaho.commons.metadata.mqleditor.IModel;
import org.pentaho.commons.metadata.mqleditor.IOrder;
import org.pentaho.commons.metadata.mqleditor.IQuery;
import org.pentaho.commons.metadata.mqleditor.editor.service.CWMStartup;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.MetadataServiceSyncImpl;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MetadataGwtServlet extends RemoteServiceServlet implements MetadataGwtService {

  MetadataServiceSyncImpl SERVICE;

  public MetadataGwtServlet() {

    CWMStartup.loadCWMInstance("src/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "src/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWMStartup.loadMetadata("src/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata_steelwheels.xmi", "src/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWM.getInstance("src/org/pentaho/commons/metadata/mqleditor/sampleMql");
    List<CWM> cwms = new ArrayList<CWM>();
    cwms.add(cwm);
    CwmSchemaFactory factory = new CwmSchemaFactory();
    SERVICE = new MetadataServiceSyncImpl(cwms, factory);
  }

  public IDomain getDomainByName(String name) {
    return SERVICE.getDomainByName(name);
  }

  public List<IDomain> getMetadataDomains() {
    return SERVICE.getMetadataDomains();
  }

  public String saveQuery(IModel model, List<IBusinessColumn> cols, List<ICondition> conditions, List<IOrder> orders) {
    return SERVICE.saveQuery(model, cols, conditions, orders);
  }

  public String serializeModel(IQuery query) {
    return SERVICE.serializeModel(query);  
  }

  public String[][] getPreviewData(String query, int page, int limit) {
    return SERVICE.getPreviewData(query, page, limit);
  }

}