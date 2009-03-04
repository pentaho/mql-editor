package org.pentaho.metadata.editor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.IQuery;
import org.pentaho.metadata.editor.service.CWMStartup;
import org.pentaho.metadata.editor.service.MetadataService;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.ui.xul.XulServiceCallback;

public class MetadataServiceImpl implements MetadataService{

  MetadataServiceSyncImpl SERVICE;
  public MetadataServiceImpl(){

    CWMStartup.loadCWMInstance("src/org/pentaho/metadata/sampleMql/metadata/repository.properties", "src/org/pentaho/metadata/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWMStartup.loadMetadata("src/org/pentaho/metadata/sampleMql/metadata_steelwheels.xmi", "src/org/pentaho/metadata/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWM.getInstance("src/org/pentaho/metadata/sampleMql"); //$NON-NLS-1$

    CwmSchemaFactory factory = new CwmSchemaFactory();
    
    List<CWM> cwms = new ArrayList<CWM>();
    cwms.add(cwm);
    
    SERVICE = new MetadataServiceSyncImpl(cwms, factory);
  }

  public void getDomainByName(String name, XulServiceCallback<IDomain> callback) {
    callback.success(SERVICE.getDomainByName(name));
  }

  public void getMetadataDomains(XulServiceCallback<List<IDomain>> callback) {
    callback.success(SERVICE.getMetadataDomains());  
  }

  public void saveQuery(IModel model, List<? extends IBusinessColumn> cols, List<? extends ICondition> conditions,
      List<? extends IOrder> orders, XulServiceCallback<String> callback) {
    callback.success(SERVICE.saveQuery(model, cols, conditions, orders)); 
  }

  public void serializeModel(IQuery query, XulServiceCallback<String> callback) {
    callback.success(SERVICE.serializeModel(query));
  }

  public void getPreviewData(String query, int page, int limit, XulServiceCallback<String[][]> callback) {
    callback.error("Operation not supported", new NotImplementedException("Implement in a subclass of this service"));
  }
  
  
  
}

  