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

  private static final String WIZARD_PANEL = "pentaho-wizard-panel"; //$NON-NLS-1$
  private boolean canContinue = false;
  private boolean canFinish = false;
  private Object userData;
  
  private WizardPanelListenerCollection wizardPanelListenerCollection = new WizardPanelListenerCollection();
  
  public AbstractWizardPanel() {
    super();
    this.addStyleName(WIZARD_PANEL);
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
