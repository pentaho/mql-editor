package org.pentaho.commons.metadata.mqleditor.editor;

import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.SampleAppGwtService;
import org.pentaho.commons.metadata.mqleditor.editor.service.gwt.SampleAppGwtServiceAsync;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.SqlPhysicalTable;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * Default implementation usable by standard web applications or other GWT apps through
 * pure Javascript
 *
 */
public class SampleApp implements EntryPoint{
    SampleAppGwtServiceAsync service = null ;
    @SuppressWarnings("deprecation")
    public void onModuleLoad() {
      Button b = new Button("Click me", new ClickListener() {
        public void onClick(Widget sender) {
          try {
            getSampleAppGwtService().generateModel(new AsyncCallback<Domain>() {

              public void onFailure(Throwable arg0) {
                // TODO Auto-generated method stub
                
              }

              public void onSuccess(Domain arg0) {
                Window.alert("My Target Table Type" + ((SqlPhysicalTable)arg0.getLogicalModels().get(0).getLogicalTables().get(0).getPhysicalTable()).getTargetTableType().name()); //$NON-NLS-1$
              }
              
            });
          } catch (DatasourceServiceException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }        }
      });

      RootPanel.get().add(b);
    }
    
    public SampleAppGwtServiceAsync getSampleAppGwtService() {
      if (service == null) {
        service = (SampleAppGwtServiceAsync) GWT.create(SampleAppGwtService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "samplesvc"; //$NON-NLS-1$
        endpoint.setServiceEntryPoint(moduleRelativeURL);
      }
      return service;
    }
  }
