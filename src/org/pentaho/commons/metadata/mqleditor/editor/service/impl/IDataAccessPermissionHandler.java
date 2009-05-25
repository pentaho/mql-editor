package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import org.pentaho.platform.api.engine.IPentahoSession;

/**
 * Implement this interface to override the permissions behavior of
 * data access.
 * 
 * This interface may be implemented and then the implementation class specified 
 * in the data-access settings.xml file, within the
 * settings/dataaccess-permission-handler
 * 
 * @author Will Gorman (wgorman@pentaho.com)
 */
public interface IDataAccessPermissionHandler {

  /**
   * This method returns true if the session has permission to
   * execute arbitrary sql from the client. 
   * 
   * @param session pentaho session
   * @return true if allowed
   */
  boolean hasDataAccessPermission(IPentahoSession session);
}
