package org.pentaho.commons.mql.ui.mqldesigner;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.pentaho.pms.core.CWM;

public class CWMStartup {
  public static boolean loadMetadata(String metadataFileName, String baseSolution) {
    File metadataFile = new File(metadataFileName);
    if (metadataFile.exists()) {
      if (baseSolution != null) {
        CWM cwm = CWM.getInstance(baseSolution);
        if (cwm.getSchemas().length > 0) {
          return true;
        }
        InputStream xmiInputStream = null;
        try {
          xmiInputStream = new FileInputStream(metadataFile);
          if (xmiInputStream != null) {
            cwm.importFromXMI(xmiInputStream);
            return true;
          }
        } catch (Throwable t) {
        } finally {
          if (xmiInputStream != null) {
            try {
              xmiInputStream.close();
            } catch (Throwable t) {
            }
          }
        }
      }
    }
    return false;
  }

  public static void loadCWMInstance(String kettlePropsFileName, String cwmFileName) {
    try {
      Properties props = new Properties();
      InputStream is = null;
      File propsFile = new File(kettlePropsFileName);
      if (propsFile.exists()) {
        is = new FileInputStream(propsFile);
        props.load(is);
        InputStream xmiInputStream = null;
        try {
          File file = new File(cwmFileName);
          if (file.exists()) {
            xmiInputStream = new FileInputStream(file);
            CWM.getRepositoryInstance(props, xmiInputStream);
          }
        } catch (Throwable ex) {
          ex.printStackTrace();
        } finally {
          if (xmiInputStream != null) {
            xmiInputStream.close();
          }
        }
      } else {
      }
    } catch (Exception ex) {
    }
  }
  
}
