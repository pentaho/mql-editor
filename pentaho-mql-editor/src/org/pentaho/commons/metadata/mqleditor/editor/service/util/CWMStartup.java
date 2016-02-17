/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
 */

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
