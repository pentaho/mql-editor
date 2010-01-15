/*
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
 * Copyright (c) 2009 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.commons.metadata.mqleditor.editor;

import java.awt.Window;

import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorServiceImpl;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceDelegate;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulLoader;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.swt.SwtXulLoader;
import org.pentaho.ui.xul.swt.SwtXulRunner;
import org.pentaho.ui.xul.swt.tags.SwtDialog;

/**
 * Default Swt implementation. This class requires a concrete Service
 * implementation
 */
public class SwtMqlEditor extends AbstractMqlEditor {
  
  public SwtMqlEditor(Window parent, IMetadataDomainRepository repo) {
    super(parent, repo);
  }
  
  public SwtMqlEditor(IMetadataDomainRepository repo, MQLEditorServiceImpl service, MQLEditorServiceDelegate delegate) {
    super(repo, service, delegate);
  }
  
  public SwtMqlEditor(IMetadataDomainRepository repo) {
    super(repo);
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.commons.metadata.mqleditor.editor.AbstractMqlEditor#getLoader()
   */
  @Override
  protected XulLoader getLoader() {
    if (xulLoader == null) {
      try {
        xulLoader = new SwtXulLoader();
      } catch (XulException e) {
        log.error("error loading Xul application", e);
      }
    }
    return xulLoader;
  }

  /* (non-Javadoc)
   * @see org.pentaho.commons.metadata.mqleditor.editor.AbstractMqlEditor#getRunner()
   */
  @Override
  protected XulRunner getRunner() {
    if (xulRunner == null) {
      xulRunner = new SwtXulRunner();
    }
    return xulRunner;
  }


}
