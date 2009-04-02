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
import org.pentaho.commons.metadata.mqleditor.beans.ResultSetObject;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.utils.ResultSetConverter;

public class DatasourceServiceDelegate {

  private String locale = Locale.getDefault().toString();

  private List<IDatasource> datasources = new ArrayList<IDatasource>();
  
  public DatasourceServiceDelegate() {
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
        rso = new ResultSetObject(rsc.getColumnTypes(), rsc.getMetaData(), rsc.getResultSet());
      } else {
        throw new DatasourceServiceException("ERROR_0028_QUERY_NOT_VALID"); //$NON-NLS-1$
      }
    } catch (SQLException e) {
      throw new DatasourceServiceException("ERROR_0029_QUERY_VALIDATION_FAILED", e); //$NON-NLS-1$
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
        rso = new ResultSetObject(rsc.getColumnTypes(), rsc.getMetaData(), rsc.getResultSet());
      } else {
        throw new DatasourceServiceException("PacService.ERROR_0028_QUERY_NOT_VALID"); //$NON-NLS-1$
      }
    } catch (SQLException e) {
      throw new DatasourceServiceException("PacService.ERROR_0029_QUERY_VALIDATION_FAILED", e); //$NON-NLS-1$
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
      throw new DatasourceServiceException("ERROR_0024_CONNECTION_ATTEMPT_FAILED"); //$NON-NLS-1$  
    }
    Class<?> driverC = null;

    try {
      driverC = Class.forName(driverClass);
    } catch (ClassNotFoundException e) {
      throw new DatasourceServiceException("ERROR_0026_DRIVER_NOT_FOUND_IN_CLASSPATH", e); //$NON-NLS-1$
    }
    if (!Driver.class.isAssignableFrom(driverC)) {
      throw new DatasourceServiceException("ERROR_0026_DRIVER_NOT_FOUND_IN_CLASSPATH"); //$NON-NLS-1$    }
    }
    Driver driver = null;
    
    try {
      driver = driverC.asSubclass(Driver.class).newInstance();
    } catch (InstantiationException e) {
      throw new DatasourceServiceException("PacService.ERROR_0027_UNABLE_TO_INSTANCE_DRIVER", e); //$NON-NLS-1$
    } catch (IllegalAccessException e) {
      throw new DatasourceServiceException("PacService.ERROR_0027_UNABLE_TO_INSTANCE_DRIVER", e); //$NON-NLS-1$    }
    }
    try {
      DriverManager.registerDriver(driver);
      conn = DriverManager.getConnection(connection.getUrl(), connection.getUsername(), connection.getPassword());
      return conn;
    } catch (SQLException e) {
      throw new DatasourceServiceException("PacService.ERROR_0025_UNABLE_TO_CONNECT", e); //$NON-NLS-1$
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
  
  public ResultSetObject getBusinessData(IDatasource datasource) throws DatasourceServiceException {
    return doPreview(datasource);
  }
  public ResultSetObject getBusinessData(IConnection connection, String query, String previewLimit) throws DatasourceServiceException {
    return doPreview(connection, query, previewLimit);
  }

}
