
package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.metadata.model.Domain;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface SampleAppGwtService extends RemoteService{
  public Domain generateModel() throws DatasourceServiceException;
  }

  