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
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 */
package org.pentaho.gwt.widgets.client.controls;

import java.util.Date;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Steven Barkdull
 *
 */

public class DateTimePicker extends FlowPanel implements IChangeHandler {

  private DatePickerEx datePicker = new DatePickerEx();
  private TimePicker timePicker = new TimePicker();
  private ICallback<IChangeHandler> onChangeHandler = null;
  
  public enum Layout {
    HORIZONTAL, VERTICAL
  }
  
  public DateTimePicker( Layout layout ) {
    super();
    Panel p = ( Layout.HORIZONTAL == layout )
      ? new HorizontalPanel()
      : new VerticalPanel();
    add( p );
    datePicker.setWidth( "12ex" ); //$NON-NLS-1$
    datePicker.setYoungestDate( new Date() );
    p.add( datePicker );
    //timePicker.setWidth( "100%" );
    p.add( timePicker );
    configureOnChangeHandler();
  }

  public void setOnChangeHandler( ICallback<IChangeHandler> handler ) {
    this.onChangeHandler = handler;
  }
  
  private void changeHandler() {
    if ( null != onChangeHandler ) {
      onChangeHandler.onHandle( this );
    }
  }
  
  private void configureOnChangeHandler() {
    final DateTimePicker localThis = this;
    
    ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
      public void onHandle(IChangeHandler o) {
        localThis.changeHandler();
      }
    };
    
    datePicker.setOnChangeHandler(handler);
    timePicker.setOnChangeHandler(handler);
  }
  
}
