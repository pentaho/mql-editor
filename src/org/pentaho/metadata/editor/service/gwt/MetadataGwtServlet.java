package org.pentaho.metadata.editor.service.gwt;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.IQuery;
import org.pentaho.metadata.editor.service.CWMStartup;
import org.pentaho.metadata.editor.service.impl.MetadataServiceSyncImpl;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class MetadataGwtServlet extends RemoteServiceServlet implements MetadataGwtService {

  MetadataServiceSyncImpl SERVICE;

  public MetadataGwtServlet() {

    CWMStartup.loadCWMInstance("src/org/pentaho/metadata/sampleMql/metadata/repository.properties", "src/org/pentaho/metadata/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWMStartup.loadMetadata("src/org/pentaho/metadata/sampleMql/metadata_steelwheels.xmi", "src/org/pentaho/metadata/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWM.getInstance("src/org/pentaho/metadata/sampleMql");
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