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
 * @created Jul 31, 2008 
 * @author wseyler
 */


package org.pentaho.gwt.widgets.client.wizards;

import com.google.gwt.user.client.ui.DockPanel;

/**
 * @author wseyler
 *
 * Abstract class for implenenting a wizard panel (step).  Each step has it's own
 * instance of this class.  So a wizard with 5 steps will have 5 distince implementations
 * of this class to display for each step.
 */
public abstract class AbstractWizardPanel extends DockPanel implements IWizardPanel {

  private boolean canContinue = false;
  private boolean canFinish = false;
  private Object userData;
  
  private WizardPanelListenerCollection wizardPanelListenerCollection = new WizardPanelListenerCollection();
  
  public AbstractWizardPanel() {
    super();
    this.setSize("100%", "100%");
  }

  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.SourcesWizardEvents#addWizardPanelListener(org.pentaho.gwt.widgets.client.wizards.IWizardPanelListener)
   */
  public void addWizardPanelListener(IWizardPanelListener listener) {
    if (wizardPanelListenerCollection == null) {
      wizardPanelListenerCollection = new WizardPanelListenerCollection();
    }
    wizardPanelListenerCollection.add(listener);
  }

  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.SourcesWizardEvents#removeWizardPanelListener(org.pentaho.gwt.widgets.client.wizards.IWizardPanelListener)
   */
  public void removeWizardPanelListener(IWizardPanelListener listener) {
    if (wizardPanelListenerCollection != null) {
      wizardPanelListenerCollection.remove(listener);
    }
  }
  
  /**
   * @param canContinue
   */
  public void setCanContinue(boolean canContinue) {
    if (this.canContinue != canContinue) {
      this.canContinue = canContinue;
      wizardPanelListenerCollection.fireWizardPanelChanged(this);
    }
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.IWizardPanel#canContinue()
   */
  public boolean canContinue() {
    return canContinue;
  }
  
  /**
   * @param canFinish
   */
  public void setCanFinish(boolean canFinish) {
    if (this.canFinish != canFinish) {
      this.canFinish = canFinish;
      wizardPanelListenerCollection.fireWizardPanelChanged(this);
    }
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.IWizardPanel#canFinish()
   */
  public boolean canFinish() {
    return canFinish;
  }

  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.IWizardPanel#setUserData(java.lang.Object)
   */
  public void setUserData(Object userData) {
    this.userData = userData;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.IWizardPanel#getUserData()
   */
  public Object getUserData() {
    return userData;
  }

}
