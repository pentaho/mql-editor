package org.pentaho.commons.metadata.mqleditor.editor.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.editor.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceDelegate;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.ui.xul.XulServiceCallback;

public class MQLEditorServiceImpl implements MQLEditorService{

  private MQLEditorServiceDelegate delegate;
  private static Log log = LogFactory.getLog(MQLEditorServiceImpl.class);

  public MQLEditorServiceImpl(IMetadataDomainRepository repo){
    delegate = new MQLEditorServiceDelegate(repo);
  }

  public void getDomainByName(String name, XulServiceCallback<MqlDomain> callback) {
    callback.success(delegate.getDomainByName(name));
  }

  public void refreshMetadataDomains(XulServiceCallback<List<MqlDomain>> callback) {
    callback.success(delegate.refreshMetadataDomains());
  }
  
  public void getMetadataDomains(XulServiceCallback<List<MqlDomain>> callback) {
    callback.success(delegate.getMetadataDomains());
  }

  public void saveQuery(MqlQuery model, XulServiceCallback<String> callback) {
    callback.success(delegate.saveQuery(model));
  }

  public void serializeModel(MqlQuery query, XulServiceCallback<String> callback) {
    callback.success(delegate.serializeModel(query));
  }

  public void getPreviewData(MqlQuery query, int page, int limit, XulServiceCallback<String[][]> callback) {
    throw new UnsupportedOperationException();
  }

  public void deserializeModel(String serializedQuery, XulServiceCallback<MqlQuery> callback) {
    callback.success(delegate.deserializeModel(serializedQuery));
  }
  
}
