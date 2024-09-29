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

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MQLEditorGwtServiceAsync {

  void getMetadataDomains( AsyncCallback<List<MqlDomain>> callback );

  void refreshMetadataDomains( AsyncCallback<List<MqlDomain>> callback );

  void getDomainByName( String name, AsyncCallback<MqlDomain> callback );

  void saveQuery( MqlQuery model, AsyncCallback<String> callback );

  void serializeModel( MqlQuery query, AsyncCallback<String> callback );

  void deserializeModel( String serializedQuery, AsyncCallback<MqlQuery> callback );

  void getPreviewData( MqlQuery query, int page, int limit, AsyncCallback<String[][]> callback );
}
