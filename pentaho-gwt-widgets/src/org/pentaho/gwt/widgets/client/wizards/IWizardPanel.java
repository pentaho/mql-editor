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
