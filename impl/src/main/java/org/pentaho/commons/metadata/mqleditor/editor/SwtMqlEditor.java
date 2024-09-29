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


package org.pentaho.commons.metadata.mqleditor.editor;

import java.awt.Window;

import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceDelegate;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulLoader;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.swt.SwtXulLoader;
import org.pentaho.ui.xul.swt.SwtXulRunner;

/**
 * Default Swt implementation. This class requires a concrete Service implementation
 */
public class SwtMqlEditor extends AbstractMqlEditor {

  public SwtMqlEditor( Window parent, IMetadataDomainRepository repo ) {
    super( parent, repo );
  }

  public SwtMqlEditor( IMetadataDomainRepository repo, MQLEditorService service, MQLEditorServiceDelegate delegate ) {
    super( repo, service, delegate );
  }

  public SwtMqlEditor( IMetadataDomainRepository repo ) {
    super( repo );
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.commons.metadata.mqleditor.editor.AbstractMqlEditor#getLoader()
   */
  @Override
  protected XulLoader getLoader() {
    if ( xulLoader == null ) {
      try {
        xulLoader = new SwtXulLoader();
      } catch ( XulException e ) {
        log.error( "error loading Xul application", e );
      }
    }
    return xulLoader;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.pentaho.commons.metadata.mqleditor.editor.AbstractMqlEditor#getRunner()
   */
  @Override
  protected XulRunner getRunner() {
    if ( xulRunner == null ) {
      xulRunner = new SwtXulRunner();
    }
    return xulRunner;
  }

}
