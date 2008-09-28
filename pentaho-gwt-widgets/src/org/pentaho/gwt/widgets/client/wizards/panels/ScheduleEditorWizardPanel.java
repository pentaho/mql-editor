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

import java.util.Date;

import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor;
import org.pentaho.gwt.widgets.client.controls.schededitor.ScheduleEditor.ScheduleType;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;
import org.pentaho.gwt.widgets.client.wizards.AbstractWizardPanel;
import org.pentaho.gwt.widgets.client.wizards.panels.validators.ScheduleEditorValidator;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author wseyler
 *
 */
public class ScheduleEditorWizardPanel extends AbstractWizardPanel {

  private static final String PENTAHO_SCHEDULE = "pentaho-schedule-create"; //$NON-NLS-1$
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
    ICallback<IChangeHandler> chHandler = new ICallback<IChangeHandler>() {
      public void onHandle(IChangeHandler se ) {
        panelWidgetChanged(ScheduleEditorWizardPanel.this);
      }
    };
    scheduleEditor.setOnChangeHandler( chHandler );
  }

  /**
   * 
   */
  private void layout() {
    this.addStyleName(PENTAHO_SCHEDULE);
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

  protected void panelWidgetChanged(Widget changedWidget) {
//    System.out.println("Widget Changed: " + changedWidget + " can continue: " + scheduleEditorValidator.isValid());
    this.setCanContinue(scheduleEditorValidator.isValid());
    this.setCanFinish(scheduleEditorValidator.isValid());
  }
  
  public ScheduleType getScheduleType() {
    return scheduleEditor.getScheduleType();
  }
  
  /**
   * @return
   */
  public String getCronString() {
    return scheduleEditor.getCronString();
  }
  
  public String getTriggerName() {
    return scheduleEditor.getName();
  }
  
  public String getTriggerGroup() {
    return scheduleEditor.getGroupName();
  }
  
  public String getDescription() {
    return scheduleEditor.getDescription();
  }

  public Date getStartDate() {
    return scheduleEditor.getStartDate();
  }
  
  public String getStartTime() {
    return scheduleEditor.getStartTime();
  }
  
  public Date getEndDate() {
    return scheduleEditor.getEndDate();
  }
  
  public int getRepeatCount() {
    // Repeate forever
    return -1;
  }
  public String getRepeatInterval() {
    return scheduleEditor.getRepeatInSecs().toString();
  }
}
