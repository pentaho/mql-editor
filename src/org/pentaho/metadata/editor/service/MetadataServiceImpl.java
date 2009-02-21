package org.pentaho.metadata.editor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.pentaho.commons.mql.ui.mqldesigner.CWMStartup;
import org.pentaho.commons.mql.ui.mqldesigner.MQLWhereConditionModel;
import org.pentaho.metadata.ColumnType;
import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.IQuery;
import org.pentaho.metadata.beans.BusinessColumn;
import org.pentaho.metadata.beans.BusinessTable;
import org.pentaho.metadata.beans.Category;
import org.pentaho.metadata.beans.Domain;
import org.pentaho.metadata.beans.Model;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.mql.MQLQueryImpl;
import org.pentaho.pms.mql.Selection;
import org.pentaho.pms.mql.WhereCondition;
import org.pentaho.pms.schema.BusinessCategory;
import org.pentaho.pms.schema.BusinessModel;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.pms.schema.concept.types.datatype.DataTypeSettings;
import org.pentaho.pms.util.UniqueList;
import org.pentaho.ui.xul.XulServiceCallback;

public class MetadataServiceImpl implements MetadataService{

  MetadataServiceSyncImpl SERVICE;
  public MetadataServiceImpl(){

    CWMStartup.loadCWMInstance("src/org/pentaho/metadata/sampleMql/metadata/repository.properties", "src/org/pentaho/metadata/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWMStartup.loadMetadata("src/org/pentaho/metadata/sampleMql/metadata_steelwheels.xmi", "src/org/pentaho/metadata/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWM.getInstance("src/org/pentaho/metadata/sampleMql");

    CwmSchemaFactory factory = new CwmSchemaFactory();
    
    SERVICE = new MetadataServiceSyncImpl(cwm, factory);
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
  
  
  
}

  