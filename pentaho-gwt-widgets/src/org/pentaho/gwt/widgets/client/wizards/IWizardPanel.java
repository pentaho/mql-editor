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

/**
 * @author wseyler
 * 
 * Interface for wizard panels that are displayed by classes implenting the
 * AbstractWizard Dialog.  Most users should subclass the class AbstractWizardPanel
 * and implement the abstract methods.
 */

public interface IWizardPanel extends SourcesWizardEvents {
  /**
   * @param userData
   * 
   * Allows setting of arbitrary user data for this WizardPanel.
   */
  public void setUserData(Object userData);
  /**
   * @return object that contains userdate
   */
  public Object getUserData();
  /**
   * @return String that represents the name of this IWizardPanel
   * 
   * This name will be used to populate the Steps List in the dialog so it
   * should be human readable.
   */
  public String getName();
  /**
   * @return boolean 
   * Represents the state of the current panels ability to continue.  When this
   * becomes true the "Next" button on the AbstractWizardDialog will become
   * enabled.  Concrete classes should determined when the panel is completed
   * enough for the user to "Continue".
   */
  public boolean canContinue();
  /**
   * @return boolean 
   * Represents the state of the current panels ability to finish the wizard operation.  When this
   * becomes true the "Finish" button on the AbstractWizardDialog will become
   * enabled.  Concrete classes should determined when the panel is completed
   * enough for the user to "Finish" the operation.
   */
  public boolean canFinish();
}
