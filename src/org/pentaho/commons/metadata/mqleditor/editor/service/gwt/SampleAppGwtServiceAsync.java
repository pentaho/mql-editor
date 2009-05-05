package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.metadata.model.Domain;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SampleAppGwtServiceAsync {
  void generateModel(AsyncCallback<Domain> callback) throws DatasourceServiceException;
}

  