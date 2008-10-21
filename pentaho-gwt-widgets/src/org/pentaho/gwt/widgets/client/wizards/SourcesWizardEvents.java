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
 * Class that sources events for IWizardPanels
 */
public interface SourcesWizardEvents {
  /**
   * @param listener
   * 
   * Adds listener to list of Listeners monitoring this class
   */
  public void addWizardPanelListener(IWizardPanelListener listener);
  /**
   * @param listener
   * 
   * Removes listener from list of Listeners monitor this class
   */
  public void removeWizardPanelListener(IWizardPanelListener listener);
}
