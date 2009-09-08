package org.pentaho.gwt.widgets.client.datepicker;

/*
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.datepicker.client.MonthSelector;

/**
 * A simple {@link MonthSelector} used for the default date picker. Not extensible as we wish to evolve it freely over time.
 */

public final class PentahoMonthSelector extends MonthSelector {

  private PushButton backMonth;
  private PushButton backYear;
  private PushButton forwardMonth;
  private PushButton forwardYear;
  private Grid grid;
  private PentahoDatePicker picker;

  /**
   * Constructor.
   */
  public PentahoMonthSelector() {
  }

  public void setMyDatePicker(PentahoDatePicker picker) {
    this.picker = picker;
  }

  protected void refresh() {
    String formattedMonth = getModel().formatCurrentMonth();
    grid.setText(0, 2, formattedMonth);
  }

  protected void setup() {
    // Set up backwards.
    backMonth = new PushButton();
    backMonth.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        picker.addMonths(-1);
      }
    });

    backMonth.getUpFace().setHTML("&#139;");
    backMonth.setStyleName("datePickerPreviousButton");

    backYear = new PushButton();
    backYear.setStyleName("datePickerPreviousButton");
    backYear.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        picker.addMonths(-12);
      }
    });
    backYear.getUpFace().setHTML("&laquo;");

    forwardMonth = new PushButton();
    forwardMonth.getUpFace().setHTML("&#155;");
    forwardMonth.setStyleName("datePickerNextButton");
    forwardMonth.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        picker.addMonths(+1);
      }
    });

    forwardYear = new PushButton();
    forwardYear.getUpFace().setHTML("&raquo;");
    forwardYear.setStyleName("datePickerNextButton");
    forwardYear.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        picker.addMonths(+12);
      }
    });

    // Set up grid.
    grid = new Grid(1, 5) {
      public void setWidget(int row, int column, Widget widget) {
        super.setWidget(row, column, widget);
      }
    };
    grid.setWidget(0, 0, backYear);
    grid.setWidget(0, 1, backMonth);
    grid.setWidget(0, 3, forwardMonth);
    grid.setWidget(0, 4, forwardYear);

    CellFormatter formatter = grid.getCellFormatter();
    formatter.setStyleName(0, 2, "datePickerMonth");
    formatter.setWidth(0, 0, "1");
    formatter.setWidth(0, 1, "1");
    formatter.setWidth(0, 2, "100%");
    formatter.setWidth(0, 3, "1");
    formatter.setWidth(0, 4, "1");
    grid.setStyleName("datePickerMonthSelector");

    initWidget(grid);
  }

}
