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

package org.pentaho.commons.metadata.mqleditor.editor.gwt.util;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;

import com.google.gwt.user.client.rpc.RemoteService;

public interface MQLEditorGwtService extends RemoteService {

  List<MqlDomain> refreshMetadataDomains();

  List<MqlDomain> getMetadataDomains();

  MqlDomain getDomainByName( String name );

  String saveQuery( MqlQuery model );

  String serializeModel( MqlQuery query );

  MqlQuery deserializeModel( String serializedQuery );

  String[][] getPreviewData( MqlQuery query, int page, int limit ) throws Exception;

}
