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
 * Copyright (c) 2002-2017 Hitachi Vantara..  All rights reserved.
 */

package org.pentaho.commons.metadata.mqleditor.editor.service;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.editor.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceCWMDelegate;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.ui.xul.XulServiceCallback;

public class MQLEditorServiceCWMImpl implements MQLEditorService {

  MQLEditorServiceCWMDelegate delegate;

  public MQLEditorServiceCWMImpl( SchemaMeta meta ) {

    delegate = new MQLEditorServiceCWMDelegate( meta );

  }

  public void getDomainByName( String name, XulServiceCallback<MqlDomain> callback ) {
    callback.success( delegate.getDomainByName( name ) );
  }

  public void refreshMetadataDomains( XulServiceCallback<List<MqlDomain>> callback ) {
    callback.success( delegate.refreshMetadataDomains() );

  }

  public void getMetadataDomains( XulServiceCallback<List<MqlDomain>> callback ) {
    callback.success( delegate.getMetadataDomains() );
  }

  public void saveQuery( MqlQuery model, XulServiceCallback<String> callback ) {
    callback.success( delegate.saveQuery( model ) );
  }

  public void serializeModel( MqlQuery query, XulServiceCallback<String> callback ) {
    callback.success( delegate.serializeModel( query ) );
  }

  public void getPreviewData( MqlQuery query, int page, int limit, XulServiceCallback<String[][]> callback ) {
    try {
      String[][] previewData = delegate.getPreviewData( query, page, limit );
      callback.success( previewData );
    } catch ( Exception e ) {
      callback.error( "error fetching results", e );
    }
  }

  public void deserializeModel( String serializedQuery, XulServiceCallback<MqlQuery> callback ) {
    callback.success( delegate.deserializeModel( serializedQuery ) );
  }

}
