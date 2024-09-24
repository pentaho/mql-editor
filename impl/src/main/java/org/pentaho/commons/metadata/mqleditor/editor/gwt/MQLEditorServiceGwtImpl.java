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
    SERVICE =
        (org.pentaho.commons.metadata.mqleditor.editor.gwt.util.MQLEditorGwtServiceAsync) GWT
            .create( org.pentaho.commons.metadata.mqleditor.editor.gwt.util.MQLEditorGwtService.class );
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    endpoint.setServiceEntryPoint( getContextPath() + "gwtrpc/MqlService" );
  }

  public static String getContextPath() {
    return readContextPath();
  }

  public static native String readContextPath()/*-{
                                               if($wnd.CONTEXT_PATH){
                                               return $wnd.CONTEXT_PATH;
                                               }
                                               return "";
                                               }-*/;

  public static String getFullyQualifiedUrl() {
    return readFullQualifiedUrl();
  }

  public static native String readFullQualifiedUrl()/*-{
                                                    if($wnd.FULLY_QUALIFIED_SERVER_URL){
                                                    return $wnd.FULLY_QUALIFIED_SERVER_URL;
                                                    }
                                                    return "";
                                                    }-*/;

  public MQLEditorServiceGwtImpl() {
  }

  public void getDomainByName( final String name, final XulServiceCallback<MqlDomain> callback ) {

    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand<MqlDomain>() {
      public void execute( AsyncCallback<MqlDomain> callback ) {
        SERVICE.getDomainByName( name, callback );
      }
    }, new AsyncCallback<MqlDomain>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( "error loading metadata domains: ", arg0 );
      }

      public void onSuccess( MqlDomain arg0 ) {
        callback.success( arg0 );
      }
    } );

  }

  public void getMetadataDomains( final XulServiceCallback<List<MqlDomain>> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand<List<MqlDomain>>() {
      public void execute( AsyncCallback<List<MqlDomain>> callback ) {
        SERVICE.getMetadataDomains( callback );
      }
    }, new AsyncCallback<List<MqlDomain>>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( "error loading metadata domains: ", arg0 );
      }

      public void onSuccess( List<MqlDomain> arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  public void refreshMetadataDomains( final XulServiceCallback<List<MqlDomain>> callback ) {

    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand<List<MqlDomain>>() {
      public void execute( AsyncCallback<List<MqlDomain>> callback ) {
        SERVICE.refreshMetadataDomains( callback );
      }
    }, new AsyncCallback<List<MqlDomain>>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( "error loading metadata domains: ", arg0 );
      }

      public void onSuccess( List<MqlDomain> arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  public void saveQuery( final MqlQuery model, final XulServiceCallback<String> callback ) {

    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand<String>() {
      public void execute( AsyncCallback<String> callback ) {
        SERVICE.saveQuery( model, callback );
      }
    }, new AsyncCallback<String>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( "error loading metadata domains: ", arg0 );
      }

      public void onSuccess( String arg0 ) {
        callback.success( arg0 );
      }
    } );

  }

  public void serializeModel( final MqlQuery query, final XulServiceCallback<String> callback ) {

    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand<String>() {
      public void execute( AsyncCallback<String> callback ) {
        SERVICE.serializeModel( query, callback );
      }
    }, new AsyncCallback<String>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( "error loading metadata domains: ", arg0 );
      }

      public void onSuccess( String arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  public void getPreviewData( final MqlQuery query, final int page, final int limit,
      final XulServiceCallback<String[][]> callback ) {

    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand<String[][]>() {
      public void execute( AsyncCallback<String[][]> callback ) {
        SERVICE.getPreviewData( query, page, limit, callback );
      }
    }, new AsyncCallback<String[][]>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( "error previewing data: ", arg0 );
      }

      public void onSuccess( String[][] arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  public void deserializeModel( final String serializedQuery, final XulServiceCallback<MqlQuery> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand<MqlQuery>() {
      public void execute( AsyncCallback<MqlQuery> callback ) {
        SERVICE.deserializeModel( serializedQuery, callback );
      }
    }, new AsyncCallback<MqlQuery>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( "error loading metadata domains: ", arg0 );
      }

      public void onSuccess( MqlQuery arg0 ) {
        callback.success( arg0 );
      }
    } );

  }

}
