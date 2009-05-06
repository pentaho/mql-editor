
package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BogoPojo;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;

import com.google.gwt.user.client.rpc.RemoteService;
     
public interface DatasourceGwtService extends RemoteService{
  public List<IDatasource> getDatasources();
  public IDatasource getDatasourceByName(String name);
  public Boolean addDatasource(IDatasource datasource);
  public Boolean deleteDatasource(IDatasource datasource);
  public Boolean updateDatasource(IDatasource datasource);
  public Boolean deleteDatasource(String name);
  public SerializedResultSet doPreview(IConnection connection, String query, String previewLimit) throws DatasourceServiceException;
  public SerializedResultSet doPreview(IDatasource datasource) throws DatasourceServiceException;
  public BusinessData generateModel(String modelName, IConnection connection, String query, String previewLimit) throws DatasourceServiceException;
  public Boolean saveModel(BusinessData businessData, Boolean overwrite)throws DatasourceServiceException;
  public BogoPojo gwtWorkaround(BogoPojo pojo);
  }

  