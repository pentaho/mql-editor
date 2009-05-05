package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.util.SQLModelGenerator;
import org.pentaho.pms.service.ModelManagementServiceException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SampleAppGwtServlet extends RemoteServiceServlet implements SampleAppGwtService {


  public SampleAppGwtServlet() {

  }
  public Domain generateModel()
      throws DatasourceServiceException {
    
      try {
        String query = "select customername, customernumber, city from customers where customernumber < 171";
        Connection connection = null;
        try {
        connection = getDataSourceConnection("org.hsqldb.jdbcDriver","SampleData"
            ,"pentaho_user", "password"
              ,"jdbc:hsqldb:hsql://localhost:9001/sampledata");
        } catch(Exception e) {
          
        }
        SQLModelGenerator generator = new SQLModelGenerator();
        Domain domain = generator.generate("newdatasource", connection, query);
        return domain;
      } catch(Exception mmse) {
        throw new DatasourceServiceException(mmse.getLocalizedMessage(), mmse);
      }
    }
  
  private Connection getDataSourceConnection(String driverClass, String name, String username, String password, String url) throws Exception {
    Connection conn = null;

    if (StringUtils.isEmpty(driverClass)) {
      throw new Exception("Connection attempt failed"); //$NON-NLS-1$  
    }
    Class<?> driverC = null;

    try {
      driverC = Class.forName(driverClass);
    } catch (ClassNotFoundException e) {
      throw new Exception("Driver not found in the class path. Driver was " + driverClass, e); //$NON-NLS-1$
    }
    if (!Driver.class.isAssignableFrom(driverC)) {
      throw new Exception("Driver not found in the class path. Driver was " + driverClass); //$NON-NLS-1$    }
    }
    Driver driver = null;
    
    try {
      driver = driverC.asSubclass(Driver.class).newInstance();
    } catch (InstantiationException e) {
      throw new Exception("Unable to instance the driver", e); //$NON-NLS-1$
    } catch (IllegalAccessException e) {
      throw new Exception("Unable to instance the driver", e); //$NON-NLS-1$    }
    }
    try {
      DriverManager.registerDriver(driver);
      conn = DriverManager.getConnection(url, username, password);
      return conn;
    } catch (SQLException e) {
      throw new Exception("Unable to connect", e); //$NON-NLS-1$
    }
  }

  
}