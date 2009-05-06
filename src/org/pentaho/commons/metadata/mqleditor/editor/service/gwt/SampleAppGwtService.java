
package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import org.pentaho.commons.metadata.mqleditor.beans.BogoPojo;
import org.pentaho.commons.metadata.mqleditor.beans.ITestObject;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.SqlPhysicalModel;
import org.pentaho.metadata.model.SqlPhysicalTable;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface SampleAppGwtService extends RemoteService{
  public Domain generateModel() throws DatasourceServiceException;
  public ITestObject enumTest() throws DatasourceServiceException;
  public SqlPhysicalModel generatePhysicalModel() throws DatasourceServiceException;
  public SqlPhysicalTable generatePhysicalTable() throws DatasourceServiceException;
  public BogoPojo gwtWorkaround(BogoPojo pojo);
  }

  