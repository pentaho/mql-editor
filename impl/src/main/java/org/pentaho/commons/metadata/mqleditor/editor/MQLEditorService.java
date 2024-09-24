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

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.ui.xul.XulServiceCallback;

/**
 * Interface to the service that the MQL Editor uses for operation. Due to the fact that the MQL Editor may be run in
 * GWT, all service calls use an asynchronous pattern.
 *
 */
public interface MQLEditorService {
  void refreshMetadataDomains( XulServiceCallback<List<MqlDomain>> callback );

  void getMetadataDomains( XulServiceCallback<List<MqlDomain>> callback );

  void getDomainByName( String name, XulServiceCallback<MqlDomain> callback );

  void saveQuery( MqlQuery model, XulServiceCallback<String> callback );

  void serializeModel( MqlQuery query, XulServiceCallback<String> callback );

  void deserializeModel( String serializedQuery, XulServiceCallback<MqlQuery> callback );

  void getPreviewData( MqlQuery query, int page, int limit, XulServiceCallback<String[][]> callback );
}
