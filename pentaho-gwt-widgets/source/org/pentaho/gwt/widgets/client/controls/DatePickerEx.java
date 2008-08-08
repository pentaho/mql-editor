/*
 * Copyright 2006-2008 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created May 19, 2008
 * 
 */
package org.pentaho.gwt.widgets.client.controls;

import java.util.Date;

import org.pentaho.gwt.widgets.client.utils.StringUtils;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

// TODO sbarkdull, should not extend DatePicker, should aggregate it

/**
 * @author Steven Barkdull
 *
 */

public class DatePickerEx extends org.zenika.widget.client.datePicker.DatePicker {

  public DatePickerEx() {
    super();
  }

  /**
   * Create a DatePicker which show a specific Date.
   * @param selectedDate Date to show
   */
  public DatePickerEx(Date selectedDate) {
    super( selectedDate );
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
