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
 *
 * @created May 19, 2008
 * 
 */
package org.pentaho.gwt.widgets.client.controls;

import java.util.Date;

import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

// TODO sbarkdull, should not extend DatePicker, should aggregate it

/**
 * @author Steven Barkdull
 *
 */

public class DatePickerEx extends org.zenika.widget.client.datePicker.DatePicker implements IChangeHandler {

  private ICallback<IChangeHandler> onChangeHandler;
  
  public DatePickerEx() {
    super();
    configureOnChangeHandler();
  }

  /**
   * Create a DatePicker which show a specific Date.
   * @param selectedDate Date to show
   */
  public DatePickerEx(Date selectedDate) {
    super( selectedDate );
    configureOnChangeHandler();
  }
  
  /**
   * Get the selected date. Return null if control's textbox is empty.
   * 
   * NOTE: base class implementation sets the time to the current time, which is
   * not what we want. So, 0-out the time portion to midnight
   * 
   * @return Date the selected date.
   */
  public Date getSelectedDate() {
    Date d = super.getSelectedDate();
    String txt = super.getText();
    return ( d == null || StringUtils.isEmpty( txt ) ) 
      ? null
      : TimeUtil.zeroTimePart( d );
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
    // currently doesnt need to do anything. synchronizeFromDate() gets called
    // when the popup goes down, and populates the text box with the new
    // date. So synchronizeFromDate() activates the change handler
  }
  
  public void synchronizeFromDate() {
    super.synchronizeFromDate();
    changeHandler();
  }
  
//  /**
//   * Create a DatePicker which uses a specific theme.
//   * @param theme Theme name
//   */
//  public DatePickerEx(String theme) {
//    super( theme );
//  }
//  
//  /**
//   * Create a DatePicker which specifics date and theme.
//   * @param selectedDate Date to show
//   * @param theme Theme name
//   */
//  public DatePickerEx(Date selectedDate, String theme) {
//    super(selectedDate, theme);
//  }
}
