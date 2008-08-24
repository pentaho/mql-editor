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
 * Listener interface for classes that monitor IWizardPanel implemtations.
 */
public interface IWizardPanelListener {
  
  /**
   * @param wizardPanel
   * This method is fired by the AbstractWizardPanel when an interesting
   * (canContinue or canFinish) event occurs.  This allows communication
   * between the AbstractWizardDialog and it's Panels so that when a panel
   * is manipulated and its state becomes ready to continue or finish the
   * AbstractWizardPanel can update it's GUI accordingly (ie enable/disable buttons).
   */
  public void panelUpdated(IWizardPanel wizardPanel);
}
