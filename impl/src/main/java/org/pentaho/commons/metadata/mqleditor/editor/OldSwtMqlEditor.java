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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.widgets.Composite;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConditionsController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.MainController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.OrderController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.PreviewController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.SelectedColumnController;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceCWMDelegate;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.swt.SwtXulLoader;
import org.pentaho.ui.xul.swt.SwtXulRunner;
import org.pentaho.ui.xul.swt.tags.SwtDialog;

/**
 * Default Swt implementation. This class requires a concrete Service implementation
 */
public class OldSwtMqlEditor {

  private static Log log = LogFactory.getLog( SwingMqlEditor.class );

  private MainController mainController = new MainController();
  private SelectedColumnController selectedColumnController = new SelectedColumnController();
  private ConditionsController constraintController = new ConditionsController();
  private OrderController orderController = new OrderController();
  private PreviewController previewController = new PreviewController();

  private Workspace workspace = new Workspace();
  private XulDomContainer container;
  private MQLEditorServiceCWMDelegate delegate;

  public OldSwtMqlEditor( MQLEditorService service, SchemaMeta meta ) {
    try {
      if ( meta != null ) {
        this.delegate = new MQLEditorServiceCWMDelegate( meta );
      }

      container = new SwtXulLoader().loadXul( "org/pentaho/commons/metadata/mqleditor/editor/xul/mainFrame.xul" );
      loadOverlays();

      final XulRunner runner = new SwtXulRunner();
      runner.addContainer( container );

      BindingFactory bf = new DefaultBindingFactory();
      bf.setDocument( container.getDocumentRoot() );

      mainController.setBindingFactory( bf );
      selectedColumnController.setBindingFactory( bf );
      constraintController.setBindingFactory( bf );
      orderController.setBindingFactory( bf );
      previewController.setBindingFactory( bf );

      container.addEventHandler( mainController );
      container.addEventHandler( selectedColumnController );
      container.addEventHandler( constraintController );
      container.addEventHandler( orderController );
      container.addEventHandler( previewController );

      mainController.setWorkspace( workspace );
      selectedColumnController.setWorkspace( workspace );
      constraintController.setWorkspace( workspace );
      orderController.setWorkspace( workspace );
      previewController.setWorkspace( workspace );

      mainController.setService( service );

      service.getMetadataDomains( new XulServiceCallback<List<MqlDomain>>() {

        public void error( String message, Throwable error ) {
          log.error( "error getting list of Domains", error );
        }

        public void success( List<MqlDomain> retVal ) {

          List<UIDomain> uiDomains = new ArrayList<UIDomain>();
          for ( MqlDomain d : retVal ) {
            uiDomains.add( new UIDomain( (Domain) d ) );
          }

          workspace.setDomains( uiDomains );

          try {
            runner.initialize();
          } catch ( XulException e ) {
            log.error( "error starting Xul application", e );
          }
        }

      } );

    } catch ( XulException e ) {
      log.error( "error loading Xul application", e );
    }
  }

  public Composite getDialogArea() {
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById( "mqlEditorDialog" );
    return (Composite) dialog.getManagedObject();
  }

  public void show() {
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById( "mqlEditorDialog" );
    dialog.show();

  }

  private void loadOverlays() {
    // Load the overlay to remove the "dynamic" behavior of the "combine" column in the "conditions" table
    // SWT table cannot accommodate more then one widget type in a given column yet
    try {
      container.loadOverlay( "org/pentaho/commons/metadata/mqleditor/editor/xul/mainFrame-swt-overlay.xul" ); //$NON-NLS-1$
    } catch ( XulException e ) {
      log.error( "Error loading Xul overlay: mainFrame-swt-overlay.xul" );
      e.printStackTrace();
    }
  }

  public void setMqlQuery( MQLQuery query ) {
    if ( query == null || query.equals( "" ) ) {
      mainController.clearWorkspace();
    } else {
      mainController.setSavedQuery( (Query) this.delegate.convertModelToThin( query ) );
    }
  }

  public MQLQuery getMqlQuery() {
    MqlQuery q = workspace.getMqlQuery();
    if ( q == null ) {
      return null;
    }
    return delegate.convertModel( q );

  }

  public void hidePreview() {
    SwtDialog dialog = (SwtDialog) container.getDocumentRoot().getElementById( "mqlEditorDialog" );
    dialog.setButtons( "accept,cancel" );
  }
}
