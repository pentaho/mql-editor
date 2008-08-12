/*
 * Copyright 2007 Pentaho Corporation.  All rights reserved. 
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
 * @created Aug 8, 2008 
 * @author wseyler
 */


package org.pentaho.gwt.widgets.client.wizards.panels;

import java.util.Map;

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;
import org.pentaho.gwt.widgets.client.utils.TimeUtil.DayOfWeek;
import org.pentaho.gwt.widgets.client.wizards.AbstractWizardPanel;
import org.pentaho.gwt.widgets.client.wizards.panels.validators.ScheduleEditorValidator;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author wseyler
 *
 */
public class ScheduleEditorWizardPanel extends AbstractWizardPanel implements KeyboardListener, ClickListener, ChangeListener {

  ScheduleEditor scheduleEditor = new ScheduleEditor();
  ScheduleEditorValidator scheduleEditorValidator;
  
  public ScheduleEditorWizardPanel() {
    super();
    scheduleEditorValidator = new ScheduleEditorValidator(scheduleEditor);
    init();
    layout();
  }

  /**
   * 
   */
  private void init() {
    scheduleEditor.getCronEditor().getCronTb().addKeyboardListener(this);
    scheduleEditor.getCronEditor().getDateRangeEditor().getEndDatePanel().getEndByRb().addClickListener(this);
    scheduleEditor.getDescriptionTb().addKeyboardListener(this);
    scheduleEditor.getGroupNameTb().addKeyboardListener(this);
    scheduleEditor.getNameTb().addKeyboardListener(this);
    scheduleEditor.getScheduleCombo().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getDailyEditor().getEveryNDaysRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getDailyEditor().getEveryWeekdayRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getDailyEditor().getRepeatValueTb().addKeyboardListener(this);
    scheduleEditor.getRecurrenceEditor().getDateRangeEditor().getEndDatePanel().getEndByRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getDateRangeEditor().getEndDatePanel().getEndDatePicker().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getDateRangeEditor().getEndDatePanel().getNoEndDateRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getDateRangeEditor().getStartDatePicker().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getHourlyEditor().getValueTb().addKeyboardListener(this);
    scheduleEditor.getRecurrenceEditor().getMinutelyEditor().getValueTb().addKeyboardListener(this);
    scheduleEditor.getRecurrenceEditor().getMonthlyEditor().getDayNOfMonthRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getMonthlyEditor().getDayOfMonthTb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getMonthlyEditor().getDayOfWeekLb().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getMonthlyEditor().getNthDayNameOfMonthRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getMinutelyEditor().getValueTb().addKeyboardListener(this);
    scheduleEditor.getRecurrenceEditor().getMonthlyEditor().getWhichWeekLb().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getSecondlyEditor().getValueTb().addKeyboardListener(this);
    Map<DayOfWeek, CheckBox> checkBoxMap = scheduleEditor.getRecurrenceEditor().getWeeklyEditor().getDayToCheckBox();
    for (DayOfWeek dayOfWeek : checkBoxMap.keySet()) {
      checkBoxMap.get(dayOfWeek).addClickListener(this);
    }
    scheduleEditor.getRecurrenceEditor().getYearlyEditor().getDayOfMonthTb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getYearlyEditor().getDayOfWeekLb().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getYearlyEditor().getEveryMonthOnNthDayRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getYearlyEditor().getMonthOfYearLb0().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getYearlyEditor().getMonthOfYearLb1().addChangeListener(this);
    scheduleEditor.getRecurrenceEditor().getYearlyEditor().getNthDayNameOfMonthNameRb().addClickListener(this);
    scheduleEditor.getRecurrenceEditor().getYearlyEditor().getWhichWeekLb().addChangeListener(this);
    scheduleEditor.getRunOnceEditor().getStartDatePicker().addChangeListener(this);
    scheduleEditor.getRunOnceEditor().getStartTimePicker().getHourLB().addChangeListener(this);
    scheduleEditor.getRunOnceEditor().getStartTimePicker().getMinuteLB().addChangeListener(this);
    scheduleEditor.getRunOnceEditor().getStartTimePicker().getTimeOfDayLB().addChangeListener(this);
  }

  /**
   * 
   */
  private void layout() {
    this.add(scheduleEditor, CENTER);
    panelWidgetChanged(null);
  }

  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.IWizardPanel#getName()
   */
  public String getName() {
    // TODO Auto-generated method stub
    return "Schedule Edit";
  }

  private void panelWidgetChanged(Widget changedWidget) {
    System.out.println("Widget Changed: " + changedWidget + " can continue: " + scheduleEditorValidator.isValid());
    this.setCanContinue(scheduleEditorValidator.isValid());
    this.setCanFinish(scheduleEditorValidator.isValid());
  }
  
  /**
   * @return
   */
  public String getCronString() {
    return scheduleEditor.getCronString();
  }
  
  public String getScheduleName() {
    return scheduleEditor.getName();
  }
  
  public String getDescription() {
    return scheduleEditor.getDescription();
  }

  // KeyboardListener Stuff
  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.KeyboardListener#onKeyDown(com.google.gwt.user.client.ui.Widget, char, int)
   */
  public void onKeyDown(Widget sender, char keyCode, int modifiers) {
    // noop   
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.KeyboardListener#onKeyPress(com.google.gwt.user.client.ui.Widget, char, int)
   */
  public void onKeyPress(Widget sender, char keyCode, int modifiers) {
    // noop
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.KeyboardListener#onKeyUp(com.google.gwt.user.client.ui.Widget, char, int)
   */
  public void onKeyUp(Widget sender, char keyCode, int modifiers) {
    panelWidgetChanged(sender);
  }

  // ClickListener Stuff
  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.ClickListener#onClick(com.google.gwt.user.client.ui.Widget)
   */
  public void onClick(Widget sender) {
    panelWidgetChanged(sender);
  }
  
  
  // ChangeListener Stuff
  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.ChangeListener#onChange(com.google.gwt.user.client.ui.Widget)
   */
  public void onChange(Widget sender) {
    panelWidgetChanged(sender);
  }
  
}
