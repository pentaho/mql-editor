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

import org.pentaho.gwt.widgets.client.datepicker.PentahoDatePicker;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;
import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

// TODO sbarkdull, should not extend DatePicker, should aggregate it

/**
 * @author Steven Barkdull
 *
 */

@SuppressWarnings("deprecation")
public class DatePickerEx implements IChangeHandler {

  private DefaultFormat format = null;
  private DateBox datePicker = null;
  private ICallback<IChangeHandler> onChangeHandler;
  
  public DatePickerEx() {
    this(new DefaultFormat(DateTimeFormat.getShortDateFormat()));
  }

  
  public DatePickerEx(DefaultFormat format) {
    datePicker = new DateBox(new PentahoDatePicker(), new Date(), format);
    datePicker.addValueChangeHandler(new ValueChangeHandler<Date>(){

      public void onValueChange(ValueChangeEvent<Date> event) {
        changeHandler();
      }
    });
    configureOnChangeHandler();
  }
  

  public DateBox getDatePicker() {
    return datePicker;
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
    Date d = datePicker.getValue();
    if(d != null) {
      return TimeUtil.zeroTimePart( d );
    } else {
      return null;
    }
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
    changeHandler();
  }
  
  public DefaultFormat getFormat() {
    return format;
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
