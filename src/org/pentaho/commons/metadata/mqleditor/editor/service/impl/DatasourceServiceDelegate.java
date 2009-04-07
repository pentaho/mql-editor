package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.ResultSetConverter;
import org.pentaho.commons.metadata.mqleditor.utils.ResultSetObject;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.pms.schema.v3.model.Column;
import org.pentaho.pms.schema.v3.physical.IDataSource;
import org.pentaho.pms.schema.v3.physical.SQLDataSource;
import org.pentaho.pms.service.IModelManagementService;
import org.pentaho.pms.service.IModelQueryService;
import org.pentaho.pms.service.JDBCModelManagementService;

public class DatasourceServiceDelegate {

  private String locale = Locale.getDefault().toString();

  private List<IDatasource> datasources = new ArrayList<IDatasource>();
  private IModelManagementService modelManagementService;
  private IModelQueryService modelQueryService;
  
  public DatasourceServiceDelegate() {
    modelManagementService =  new JDBCModelManagementService();
  }
  
  public List<IDatasource> getDatasources() {
    return datasources;
  }
  public IDatasource getDatasourceByName(String name) {
    for(IDatasource datasource:datasources) {
      if(datasource.getDatasourceName().equals(name)) {
        return datasource;
      }
    }
    return null;
  }
  public Boolean addDatasource(IDatasource datasource) {
    datasources.add(datasource);
    return true;
  }
  public Boolean updateDatasource(IDatasource datasource) {
    for(IDatasource datasrc:datasources) {
      if(datasrc.getDatasourceName().equals(datasource.getDatasourceName())) {
        datasources.remove(datasrc);
        datasources.add(datasource);
      }
    }
    return true;
  }
  public Boolean deleteDatasource(IDatasource datasource) {
    datasources.remove(datasources.indexOf(datasource));
    return true;
  }
  public Boolean deleteDatasource(String name) {
    for(IDatasource datasource:datasources) {
      if(datasource.getDatasourceName().equals(name)) {
        return deleteDatasource(datasource);
      }
    }
    return false;
  }
  
  public ResultSetObject doPreview(IConnection connection, String query, String previewLimit) throws DatasourceServiceException{
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    ResultSetConverter rsc  = null;
    ResultSetObject rso = null;
    int limit = (previewLimit != null && previewLimit.length() > 0) ? Integer.parseInt(previewLimit): -1;
    try {
      conn = getDataSourceConnection(connection);

      if (!StringUtils.isEmpty(query)) {
        stmt = conn.createStatement();
        if(limit >=0) {
          stmt.setMaxRows(limit);
        }        
        rs = stmt.executeQuery(query);
        rsc =  new ResultSetConverter(rs);
        rso = new ResultSetObject(rsc.getColumnTypeNames(), rsc.getMetaData(), rsc.getResultSet());
      } else {
        throw new DatasourceServiceException("Query not valid"); //$NON-NLS-1$
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new DatasourceServiceException("Query validation failed", e); //$NON-NLS-1$
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        throw new DatasourceServiceException(e);
      }
    }
    return rso;

  }
  
  public ResultSetObject doPreview(IConnection connection, String query) throws DatasourceServiceException{
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    ResultSetConverter rsc  = null;
    ResultSetObject rso = null;
    try {
      conn = getDataSourceConnection(connection);

      if (!StringUtils.isEmpty(query)) {
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);
        rsc =  new ResultSetConverter(rs);
        rso = new ResultSetObject(rsc.getColumnTypeNames(), rsc.getMetaData(), rsc.getResultSet());
      } else {
        throw new DatasourceServiceException("Query is not valid"); //$NON-NLS-1$
      }
    } catch (SQLException e) {
      throw new DatasourceServiceException("Query validation failed", e); //$NON-NLS-1$
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        throw new DatasourceServiceException(e);
      }
    }
    return rso;

  }
  public ResultSetObject doPreview(IDatasource datasource) throws DatasourceServiceException {
    String limit = datasource.getPreviewLimit();
    if(limit != null && limit.length() > 0) {
      return doPreview(datasource.getSelectedConnection(), datasource.getQuery(), limit);
    } else {
      return doPreview(datasource.getSelectedConnection(), datasource.getQuery());  
    }
    
  }
  
  /**
   * NOTE: caller is responsible for closing connection
   * 
   * @param ds
   * @return
   * @throws DataSourceManagementException
   */
  private static Connection getDataSourceConnection(IConnection connection) throws DatasourceServiceException {
    Connection conn = null;

    String driverClass = connection.getDriverClass();
    if (StringUtils.isEmpty(driverClass)) {
      throw new DatasourceServiceException("Connection attempt failed"); //$NON-NLS-1$  
    }
    Class<?> driverC = null;

    try {
      driverC = Class.forName(driverClass);
    } catch (ClassNotFoundException e) {
      throw new DatasourceServiceException("Driver not found in the class path. Driver was " + driverClass, e); //$NON-NLS-1$
    }
    if (!Driver.class.isAssignableFrom(driverC)) {
      throw new DatasourceServiceException("Driver not found in the class path. Driver was " + driverClass); //$NON-NLS-1$    }
    }
    Driver driver = null;
    
    try {
      driver = driverC.asSubclass(Driver.class).newInstance();
    } catch (InstantiationException e) {
      throw new DatasourceServiceException("Unable to instance the driver", e); //$NON-NLS-1$
    } catch (IllegalAccessException e) {
      throw new DatasourceServiceException("Unable to instance the driver", e); //$NON-NLS-1$    }
    }
    try {
      DriverManager.registerDriver(driver);
      conn = DriverManager.getConnection(connection.getUrl(), connection.getUsername(), connection.getPassword());
      return conn;
    } catch (SQLException e) {
      throw new DatasourceServiceException("Unable to connect", e); //$NON-NLS-1$
    }
  }

  public boolean testDataSourceConnection(IConnection connection) throws DatasourceServiceException {
    Connection conn = null;
    try {
      conn = getDataSourceConnection(connection);
    } catch (DatasourceServiceException dme) {
      throw new DatasourceServiceException(dme.getMessage(), dme);
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (SQLException e) {
        throw new DatasourceServiceException(e);
      }
    }
    return true;
  }

  private IDataSource constructIDataSource(IConnection connection, String query) throws DatasourceServiceException{
    final String SLASH = "/"; //$NON-NLS-1$
    final String DOUBLE_SLASH = "//";//$NON-NLS-1$
    final String COLON = ":";//$NON-NLS-1$
    String databaseType = null;
    String databaseName = null;
    String hostname = null;
    String port = null;
    String url = connection.getUrl();
    try {
    int lastIndexOfSlash = url.lastIndexOf(SLASH); 
    if((lastIndexOfSlash >= 0) &&( lastIndexOfSlash +SLASH.length() <=url.length())) {
      databaseName = url.substring(lastIndexOfSlash+SLASH.length() ,url.length());
    }
    int lastIndexOfDoubleSlash =  url.lastIndexOf(DOUBLE_SLASH);
    int indexOfColonFromDoubleSlash = url.indexOf(COLON,lastIndexOfDoubleSlash);
    if(lastIndexOfDoubleSlash >=  0 && lastIndexOfDoubleSlash+DOUBLE_SLASH.length() <= url.length()) {
      hostname = url.substring(lastIndexOfDoubleSlash+DOUBLE_SLASH.length(), indexOfColonFromDoubleSlash);
    }
    if(indexOfColonFromDoubleSlash >=0 && indexOfColonFromDoubleSlash + SLASH.length() <= url.length() &&  lastIndexOfSlash >=0 && lastIndexOfSlash <= url.length()) {
      port = url.substring(indexOfColonFromDoubleSlash + SLASH.length(), lastIndexOfSlash);
    }
    if(connection.getDriverClass().equals("org.hsqldb.jdbcDriver")) {//$NON-NLS-1$
      databaseType = "Hypersonic";//$NON-NLS-1$
    } else if(connection.getDriverClass().equals("com.mysql.jdbc.Driver") || connection.getDriverClass().equals("org.git.mm.mysql.Driver")){ //$NON-NLS-1$ //$NON-NLS-2$ 
      databaseType="MySql"; //$NON-NLS-1$
    }
    DatabaseMeta dbMeta = new DatabaseMeta(databaseName, databaseType, "JDBC", hostname, databaseName, port, connection.getUsername(), connection.getPassword()); //$NON-NLS-1$
    return new SQLDataSource(dbMeta, query);
    } catch(Exception e) {
      throw new DatasourceServiceException(e);
    }
  }
  public BusinessData getBusinessData(IDatasource datasource) throws DatasourceServiceException {
    return getBusinessData(datasource.getSelectedConnection(), datasource.getQuery(), datasource.getPreviewLimit());  }
 
  public BusinessData getBusinessData(IConnection connection, String query, String previewLimit) throws DatasourceServiceException {
      IDataSource dataSource = constructIDataSource(connection, query);
      List<Column> columns = getModelManagementService().getColumns(dataSource);
      List<List<String>> data = getModelManagementService().getDataSample(dataSource, Integer.parseInt(previewLimit));
      return new BusinessData(columns, data);
  }

  
  public Boolean createCategory(String categoryName, IConnection connection, String query, BusinessData businessData) throws DatasourceServiceException{
    IDataSource dataSource = constructIDataSource(connection, query);
    getModelManagementService().createCategory(dataSource, categoryName, businessData.getColumns());
    return true;
  }
  
  public void setModelManagementService(IModelManagementService modelManagementService) {
    this.modelManagementService = modelManagementService;
  }

  public IModelManagementService getModelManagementService() {
    return modelManagementService;
  }

  public void setModelQueryService(IModelQueryService modelQueryService) {
    this.modelQueryService = modelQueryService;
  }

  public IModelQueryService getModelQueryService() {
    return modelQueryService;
  }

  
}
