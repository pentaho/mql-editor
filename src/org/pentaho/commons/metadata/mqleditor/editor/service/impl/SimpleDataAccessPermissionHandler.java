package org.pentaho.commons.metadata.mqleditor.editor.service.impl;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.IPluginResourceLoader;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.security.SecurityHelper;

public class SimpleDataAccessPermissionHandler implements IDataAccessPermissionHandler {

  public boolean hasDataAccessPermission(IPentahoSession session) {

    Authentication auth = SecurityHelper.getAuthentication(session, true);
    
    IPluginResourceLoader resLoader = PentahoSystem.get(IPluginResourceLoader.class, null);
    String roles = null;
    String users = null;
    
    // TODO: delete this try catch once data access becomes a fully fledged plugin
    
    try {
      roles = resLoader.getPluginSetting(getClass(), "settings/data-access-roles" ); //$NON-NLS-1$
      users = resLoader.getPluginSetting(getClass(), "settings/data-access-users" ); //$NON-NLS-1$
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    // TODO: delete hardcoded admin this is a fully fledged plugin
    
    if (roles == null) {
      roles = "Admin";
    }
    
    if (roles != null) {
      String roleArr[] = roles.split(","); //$NON-NLS-1$
  
      for (String role : roleArr) {
        for (GrantedAuthority userRole : auth.getAuthorities()) {
          if (role != null && role.trim().equals(userRole.getAuthority())) {
            return true;
          }
        }
      }
    }
    
    if (users != null) {
      String userArr[] = users.split(","); //$NON-NLS-1$
      for (String user : userArr) {
        if (user != null && user.trim().equals(auth.getName())) {
          return true;
        }
      }
    }
    
    return false;
  }

}
