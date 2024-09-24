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

package org.pentaho.commons.metadata.mqleditor.editor.gwt;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.MQLEditorService;
import org.pentaho.commons.metadata.mqleditor.editor.MqlDialogListener;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConditionsController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.MainController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.OrderController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.PreviewController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.SelectedColumnController;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.messages.GwtMqlMessages;
import org.pentaho.gwt.widgets.client.utils.i18n.IResourceBundleLoadCallback;
import org.pentaho.gwt.widgets.client.utils.i18n.ResourceBundle;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.gwt.GwtXulDomContainer;
import org.pentaho.ui.xul.gwt.GwtXulLoader;
import org.pentaho.ui.xul.gwt.GwtXulRunner;
import org.pentaho.ui.xul.gwt.binding.GwtBindingFactory;
import org.pentaho.ui.xul.gwt.util.AsyncConstructorListener;
import org.pentaho.ui.xul.gwt.util.AsyncXulLoader;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.XMLParser;

public class GwtMqlEditor implements IResourceBundleLoadCallback {

  private ResourceBundle bundle;
  private MainController mainController = new MainController();
  private Workspace workspace = new Workspace();
  private GwtXulDomContainer container;
  private SelectedColumnController selectedColumnController = new SelectedColumnController();
  private ConditionsController constraintController = new ConditionsController();
  private OrderController orderController = new OrderController();
  private PreviewController previewController = new PreviewController();
  private AsyncConstructorListener constructorListener;
  private List<MqlDialogListener> listeners = new ArrayList<MqlDialogListener>();
  private MQLEditorService service;

  public GwtMqlEditor( final MQLEditorService service, final AsyncConstructorListener<GwtMqlEditor> constructorListener ) {
    mainController.setWorkspace( workspace );
    selectedColumnController.setWorkspace( workspace );
    constraintController.setWorkspace( workspace );
    orderController.setWorkspace( workspace );
    previewController.setWorkspace( workspace );
    this.constructorListener = constructorListener;
    setService( service );
  }

  /**
   * Set the selected domain using the domain ID.
   */
  public void setSelectedDomainId( String domainId ) {
    workspace.setSelectedDomainId( domainId );
  }

  public void setSelectedModelId( String modelId ) {
    workspace.setSelectedModelId( modelId );
  }

  public void setSavedQuery( Query savedQuery ) {
    mainController.setSavedQuery( savedQuery );
  }

  public String getMqlQuery() {
    return workspace.getMqlStr();
  }

  public void show() {
    workspace.setSelectedColumn( null );
    mainController.showDialog();
  }

  public void hide() {
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById( "mqlEditorDialog" );
    dialog.hide();
  }

  public void addMqlDialogListener( MqlDialogListener listener ) {
    if ( this.listeners.contains( listener ) == false ) {
      this.listeners.add( listener );
    }
    mainController.addMqlDialogListener( listener );
  }

  public void removeMqlDialogListener( MqlDialogListener listener ) {
    if ( this.listeners.contains( listener ) ) {
      this.listeners.remove( listener );
    }
    mainController.removeMqlDialogListener( listener );
  }

  private void loadContainer( String xul ) {
    try {

      if ( xul == null || xul.indexOf( AsyncXulLoader.NS_KEY ) < 0 ) {
        Window.alert( "Error loading XUL Application. Your session may have timed out." );
      }

      GwtXulLoader loader = new GwtXulLoader();

      com.google.gwt.xml.client.Document gwtDoc = XMLParser.parse( xul );

      if ( bundle != null ) {
        container = loader.loadXul( gwtDoc, bundle );
      } else {
        container = loader.loadXul( gwtDoc );
      }

      final GwtMqlMessages messages = new GwtMqlMessages( (ResourceBundle) container.getResourceBundles().get( 0 ) );
      Workspace.setMessages( messages );

      try {

        RequestBuilder builder =
            new RequestBuilder( RequestBuilder.GET, GWT.getModuleBaseURL() + "mainFrame-gwt-overlay.xul" );

        try {
          Request response = builder.sendRequest( null, new RequestCallback() {
            public void onError( Request request, Throwable exception ) {
              // Code omitted for clarity
            }

            public void onResponseReceived( Request request, Response response ) {

              loadOverlay( response.getText() );

            }
          } );
        } catch ( RequestException e ) {
          // Code omitted for clarity
        }
      } catch ( Exception e ) {
        e.printStackTrace();
      }

    } catch ( Exception e ) {
      Window.alert( "Error Loading MQLEditor Xul file: " + e.getMessage() );
      e.printStackTrace();

    }
  }

  private void loadOverlay( String xul ) {

    com.google.gwt.xml.client.Document gwtDoc = XMLParser.parse( xul );
    try {
      container.loadOverlay( gwtDoc );

    } catch ( Exception e ) {
      e.printStackTrace();
    }
    displayXulDialog();
  }

  private void displayXulDialog() {

    try {
      final GwtXulRunner runner = new GwtXulRunner();

      GwtBindingFactory bf = new GwtBindingFactory( container.getDocumentRoot() );

      mainController.setBindingFactory( bf );
      container.addEventHandler( mainController );

      constraintController.setBindingFactory( bf );
      container.addEventHandler( constraintController );

      selectedColumnController.setBindingFactory( bf );
      container.addEventHandler( selectedColumnController );

      orderController.setBindingFactory( bf );
      container.addEventHandler( orderController );

      previewController.setBindingFactory( bf );
      container.addEventHandler( previewController );

      runner.addContainer( container );

      runner.initialize();
      runner.start();

      // RootPanel.get().add(runner.getRootPanel());
      if ( constructorListener != null ) {
        constructorListener.asyncConstructorDone( this );
      }

    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  /**
   * @deprecated this causes all models to be downloaded to the client, an expensive operation. it only exists to be
   *             compatible with trunk of dashboards until it is merged with changes from the 3.7 branch. It will be
   *             removed once that happens.
   */
  @Deprecated
  public void updateDomainList() {
    service.refreshMetadataDomains( new XulServiceCallback<List<MqlDomain>>() {

      public void error( String message, Throwable error ) {
        Window.alert( "could not get list of metadata domains" );
      }

      public void success( List<MqlDomain> domains ) {
        updateDomains( domains );
        for ( MqlDialogListener listener : listeners ) {
          listener.onDialogReady();
        }
      }

    } );
  }

  public void loadDomainById( final String domainId ) {
    service.getDomainByName( domainId, new XulServiceCallback<MqlDomain>() {

      @Override
      public void success( MqlDomain mqlDomain ) {
        if ( mqlDomain != null ) {
          List<MqlDomain> domains = new ArrayList<MqlDomain>();
          domains.add( mqlDomain );
          updateDomains( domains );
          for ( MqlDialogListener listener : listeners ) {
            listener.onDialogReady();
          }
        } else {
          Window.alert( "could not find the requested domain - " + domainId );
        }
      }

      @Override
      public void error( String s, Throwable throwable ) {
        Window.alert( "could not find the requested domain - " + domainId );
      }
    } );
  }

  private void setService( MQLEditorService service ) {
    this.service = service;
    previewController.setService( service );
    mainController.setService( service );

    for ( MqlDialogListener listener : listeners ) {
      listener.onDialogReady();
    }

    try {
      bundle = new ResourceBundle( GWT.getModuleBaseURL(), "mainFrame", true, GwtMqlEditor.this );
    } catch ( Exception e ) {
      e.printStackTrace();
    }

  }

  public void updateDomains( List<MqlDomain> domains ) {
    List<UIDomain> uiDomains = new ArrayList<UIDomain>();
    for ( MqlDomain domain : domains ) {
      uiDomains.add( new UIDomain( (Domain) domain ) );
    }
    workspace.setDomains( uiDomains );

  }

  public void bundleLoaded( String bundleName ) {
    try {

      RequestBuilder builder = new RequestBuilder( RequestBuilder.GET, GWT.getModuleBaseURL() + "mainFrame.xul" );

      try {
        Request response = builder.sendRequest( null, new RequestCallback() {
          public void onError( Request request, Throwable exception ) {
            Window.alert( "could not find main Xul file" );
          }

          public void onResponseReceived( Request request, Response response ) {
            loadContainer( response.getText() );
          }
        } );
      } catch ( RequestException e ) {
        Window.alert( "could not find main Xul file" );
      }
    } catch ( Exception e ) {
      e.printStackTrace();
      Window.alert( "Unknown error loading main Xul file" );
    }
  }

  public Workspace getWorkspace() {
    return workspace;
  }

}
