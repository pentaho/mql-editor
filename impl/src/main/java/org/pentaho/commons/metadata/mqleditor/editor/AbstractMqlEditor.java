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

import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.ConditionsController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.MainController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.OrderController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.PreviewController;
import org.pentaho.commons.metadata.mqleditor.editor.controllers.SelectedColumnController;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorServiceImpl;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceDelegate;
import org.pentaho.metadata.query.model.Query;
import org.pentaho.metadata.query.model.util.QueryXmlHelper;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.pms.core.exception.PentahoMetadataException;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulLoader;
import org.pentaho.ui.xul.XulOverlay;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.containers.XulDialog;

/**
 * Abstract implementation. This class forms the basis for the swt and swing implementations.
 */
public abstract class AbstractMqlEditor {

  protected static Log log = LogFactory.getLog( AbstractMqlEditor.class );
  protected MainController mainController = new MainController();
  protected SelectedColumnController selectedColumnController = new SelectedColumnController();
  protected ConditionsController constraintController = new ConditionsController();
  protected OrderController orderController = new OrderController();
  protected PreviewController previewController = new PreviewController();
  protected IMetadataDomainRepository repo;

  protected Workspace workspace = new Workspace();
  protected XulDomContainer container;
  protected XulLoader xulLoader;
  protected XulRunner xulRunner;
  protected MQLEditorServiceDelegate delegate;
  protected Window parentWindow;

  public AbstractMqlEditor( Window parent, IMetadataDomainRepository repo ) {
    parentWindow = parent;
    init();
    setService( new MQLEditorServiceImpl( repo ) );
  }

  public AbstractMqlEditor( IMetadataDomainRepository repo, MQLEditorService service, MQLEditorServiceDelegate delegate ) {
    init();
    this.repo = repo;
    setService( service );
    this.delegate = delegate;
  }

  public AbstractMqlEditor( IMetadataDomainRepository repo ) {
    init();
    this.repo = repo;
    setService( new MQLEditorServiceImpl( repo ) );
    delegate = new MQLEditorServiceDelegate( repo );
  }

  private void setService( MQLEditorService service ) {
    mainController.setService( service );
    previewController.setService( service );

    service.getMetadataDomains( new XulServiceCallback<List<MqlDomain>>() {

      public void error( String message, Throwable error ) {
        log.error( "Error loading Metadata Domain list", error );
      }

      public void success( List<MqlDomain> retVal ) {

        List<UIDomain> uiDomains = new ArrayList<UIDomain>();
        // Wrap Beans as UI peers
        for ( MqlDomain d : retVal ) {
          uiDomains.add( new UIDomain( (Domain) d ) );
        }
        workspace.setDomains( uiDomains );

        try {
          xulRunner.initialize();
        } catch ( XulException e ) {
          log.error( "error starting Xul application", e );
        }
      }

    } );
  }

  protected abstract XulLoader getLoader();

  protected abstract XulRunner getRunner();

  private void init() {
    try {

      XulLoader loader = getLoader();
      loader.setOuterContext( parentWindow );
      container = loader.loadXul( "org/pentaho/commons/metadata/mqleditor/editor/xul/mainFrame.xul" );

      getRunner().addContainer( container );

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

    } catch ( XulException e ) {
      log.error( "error loading Xul application", e );
    }
  }

  public void show() {
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById( "mqlEditorDialog" );
    dialog.show();
  }

  public void setQuery( String query ) throws PentahoMetadataException {
    if ( query == null ) {
      mainController.clearWorkspace();
    } else {
      QueryXmlHelper helper = new QueryXmlHelper();
      Query queryObject = helper.fromXML( repo, query );
      org.pentaho.commons.metadata.mqleditor.beans.Query thinQuery =
          (org.pentaho.commons.metadata.mqleditor.beans.Query) this.delegate.convertModelToThin( queryObject );
      mainController.setSavedQuery( thinQuery );
    }
  }

  public boolean getOkClicked() {
    return mainController.getOkClicked();
  }

  public String getQuery() {
    return delegate.saveQuery( workspace.getMqlQuery() );
  }

  public void hidePreview() {
    XulDialog dialog = (XulDialog) container.getDocumentRoot().getElementById( "mqlEditorDialog" );
    dialog.setButtons( "accept,cancel" );
  }

  public void addOverlay( XulOverlay overlay ) throws XulException {
    container.loadOverlay( overlay.getOverlayUri() );
  }

}
