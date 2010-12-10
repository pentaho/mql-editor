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
package org.pentaho.commons.metadata.mqleditor.editor.gwt.util;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.*;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.pentaho.pms.core.exception.PentahoMetadataException;

public interface MQLEditorGwtServiceAsync {

  void getMetadataDomains(AsyncCallback<List<MqlDomain>> callback);
  void refreshMetadataDomains(AsyncCallback<List<MqlDomain>> callback);
  void getDomainByName(String name, AsyncCallback<MqlDomain> callback);
  void saveQuery(MqlQuery model, AsyncCallback<String> callback);
  void serializeModel(MqlQuery query, AsyncCallback<String> callback);
  void deserializeModel(String serializedQuery, AsyncCallback<MqlQuery> callback);
  void getPreviewData(MqlQuery query, int page, int limit, AsyncCallback<String[][]> callback);
}

  