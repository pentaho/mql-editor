/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2028-08-13
 ******************************************************************************/

package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.pentaho.pms.core.CWM;

public class CWMStartup {
  public static CWM loadMetadata( String metadataFileName, String baseSolution ) {
    if ( baseSolution != null ) {

      CWM cwm = CWM.getInstance( baseSolution );
      if ( cwm.getSchemas().length > 0 ) {
        return cwm;
      }
      InputStream xmiInputStream = null;
      try {
        xmiInputStream = CWMStartup.class.getResourceAsStream( metadataFileName );
        if ( xmiInputStream != null ) {
          cwm.importFromXMI( xmiInputStream );
          return cwm;
        }
        xmiInputStream = new FileInputStream( new File( metadataFileName ) );
        if ( xmiInputStream != null ) {
          cwm.importFromXMI( xmiInputStream );
          return cwm;
        }
      } catch ( Throwable t ) {
      } finally {
        if ( xmiInputStream != null ) {
          try {
            xmiInputStream.close();
          } catch ( Throwable t ) {
          }
        }
      }
    }
    return null;
  }

  public static void loadCWMInstance( String kettlePropsFileName, String cwmFileName ) {
    try {
      Properties props = new Properties();
      InputStream is = CWMStartup.class.getResourceAsStream( kettlePropsFileName );
      props.load( is );
      InputStream xmiInputStream = null;
      try {
        xmiInputStream = CWMStartup.class.getResourceAsStream( cwmFileName );
        CWM.getRepositoryInstance( props, xmiInputStream );
      } catch ( Throwable ex ) {
        ex.printStackTrace();
      } finally {
        if ( xmiInputStream != null ) {
          xmiInputStream.close();
        }
      }
    } catch ( Exception ex ) {
      ex.printStackTrace();
    }
  }

}
