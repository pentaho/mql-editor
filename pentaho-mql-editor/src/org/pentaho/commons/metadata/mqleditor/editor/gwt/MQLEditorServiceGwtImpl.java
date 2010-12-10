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
package org.pentaho.commons.metadata.mqleditor.editor.gwt;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.editor.MQLEditorService;
import org.pentaho.gwt.widgets.login.client.AuthenticatedGwtServiceUtil;
import org.pentaho.gwt.widgets.login.client.IAuthenticatedGwtCommand;
import org.pentaho.ui.xul.XulServiceCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class MQLEditorServiceGwtImpl implements MQLEditorService {

  static org.pentaho.commons.metadata.mqleditor.editor.gwt.util.MQLEditorGwtServiceAsync SERVICE;

  static {

    SERVICE = (org.pentaho.commons.metadata.mqleditor.editor.gwt.util.MQLEditorGwtServiceAsync) GWT
        .create(org.pentaho.commons.metadata.mqleditor.editor.gwt.util.MQLEditorGwtService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    endpoint.setServiceEntryPoint(getBaseUrl() + "gwtrpc/MqlService");
  }

  private static String getBaseUrl() {
    String baseUrl = GWT.getModuleBaseURL();

    //
    // Set the base url appropriately based on the context in which we are running this client
    //
    if (baseUrl.indexOf("content") > -1) {
      // we are running the client in the context of a BI Server plugin, so
      // point the request to the GWT rpc proxy servlet
      baseUrl = baseUrl.substring(0, baseUrl.indexOf("content"));
    }
    return baseUrl;
  }

  public MQLEditorServiceGwtImpl() {

  }

  public void getDomainByName(final String name, final XulServiceCallback<MqlDomain> callback) {

    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand<MqlDomain>() {
      public void execute(AsyncCallback<MqlDomain> callback) {
        SERVICE.getDomainByName(name, callback);
      }
    }, new AsyncCallback<MqlDomain>() {
      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(MqlDomain arg0) {
        callback.success(arg0);
      }
    });

  }

  public void getMetadataDomains(final XulServiceCallback<List<MqlDomain>> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand<List<MqlDomain>>() {
      public void execute(AsyncCallback<List<MqlDomain>> callback) {
        SERVICE.getMetadataDomains(callback);
      }
    }, new AsyncCallback<List<MqlDomain>>() {
      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(List<MqlDomain> arg0) {
        callback.success(arg0);
      }
    });
  }

  public void refreshMetadataDomains(final XulServiceCallback<List<MqlDomain>> callback) {

    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand<List<MqlDomain>>() {
      public void execute(AsyncCallback<List<MqlDomain>> callback) {
        SERVICE.refreshMetadataDomains(callback);
      }
    }, new AsyncCallback<List<MqlDomain>>() {
      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(List<MqlDomain> arg0) {
        callback.success(arg0);
      }
    });
  }

  public void saveQuery(final MqlQuery model, final XulServiceCallback<String> callback) {
    
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand<String>() {
      public void execute(AsyncCallback<String> callback) {
        SERVICE.saveQuery(model, callback);
      }
    }, new AsyncCallback<String>() {
      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String arg0) {
        callback.success(arg0);
      }
    });

  }

  public void serializeModel(final MqlQuery query, final XulServiceCallback<String> callback) {

    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand<String>() {
      public void execute(AsyncCallback<String> callback) {
        SERVICE.serializeModel(query, callback);
      }
    }, new AsyncCallback<String>() {
      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(String arg0) {
        callback.success(arg0);
      }
    });
  }

  public void getPreviewData(final MqlQuery query, final int page, final int limit, final XulServiceCallback<String[][]> callback) {

    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand<String[][]>() {
      public void execute(AsyncCallback<String[][]> callback) {
        SERVICE.getPreviewData(query, page, limit, callback);
      }
    }, new AsyncCallback<String[][]>() {
      public void onFailure(Throwable arg0) {
        callback.error("error previewing data: ", arg0);
      }

      public void onSuccess(String[][] arg0) {
        callback.success(arg0);
      }
    });
  }

  public void deserializeModel(final String serializedQuery, final XulServiceCallback<MqlQuery> callback) {
    AuthenticatedGwtServiceUtil.invokeCommand(new IAuthenticatedGwtCommand<MqlQuery>() {
      public void execute(AsyncCallback<MqlQuery> callback) {
        SERVICE.deserializeModel(serializedQuery, callback);
      }
    }, new AsyncCallback<MqlQuery>() {
      public void onFailure(Throwable arg0) {
        callback.error("error loading metadata domains: ", arg0);
      }

      public void onSuccess(MqlQuery arg0) {
        callback.success(arg0);
      }
    });

  }

}
