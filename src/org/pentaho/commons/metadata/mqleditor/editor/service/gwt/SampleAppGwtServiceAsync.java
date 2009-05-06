package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import org.pentaho.commons.metadata.mqleditor.beans.BogoPojo;
import org.pentaho.commons.metadata.mqleditor.beans.ITestObject;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.SqlPhysicalModel;
import org.pentaho.metadata.model.SqlPhysicalTable;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SampleAppGwtServiceAsync {
  void generateModel(AsyncCallback<Domain> callback) throws DatasourceServiceException;
  void enumTest(AsyncCallback<ITestObject> callback) throws DatasourceServiceException;
  void generatePhysicalModel(AsyncCallback<SqlPhysicalModel> callback) throws DatasourceServiceException;
  void generatePhysicalTable(AsyncCallback<SqlPhysicalTable> callback)  throws DatasourceServiceException;
  void gwtWorkaround (BogoPojo pojo, AsyncCallback<BogoPojo> callback);
}

  