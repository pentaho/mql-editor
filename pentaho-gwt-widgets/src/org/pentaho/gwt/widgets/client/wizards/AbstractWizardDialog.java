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

import org.pentaho.gwt.widgets.client.buttons.RoundedButton;
import org.pentaho.gwt.widgets.client.dialogs.DialogBox;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessagesSingleton;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author wseyler
 *
 * Framework for creating Wizards
 */
public abstract class AbstractWizardDialog extends DialogBox implements IWizardPanelListener {
  
  private static final WidgetsLocalizedMessages MSGS = WidgetsLocalizedMessagesSingleton.getInstance().getMessages();
  
  private static final int STEPS_COUNT = 15;  // Defines the height of the steps ListBox

  private static final String WIZARD_DECK_PANEL = "pentaho-wizard-deck-panel"; //$NON-NLS-1$

  private static final String WIZARD_BUTTON_PANEL = "pentaho-wizard-button-panel"; //$NON-NLS-1$

  private static final String BACK_BTN_STYLE = "pentaho-wizard-back-button"; //$NON-NLS-1$

  private static final String NEXT_BTN_STYLE = "pentaho-wizard-next-button"; //$NON-NLS-1$

  private static final String FINISH_BTN_STYLE = "pentaho-wizard-finish-button"; //$NON-NLS-1$

  private static final String CANCEL_BTN_STYLE = "pentaho-wizard-cancel-button"; //$NON-NLS-1$
  
  // gui elements
  RoundedButton backButton = new RoundedButton(MSGS.back());
  RoundedButton nextButton = new RoundedButton(MSGS.next());
  RoundedButton cancelButton = new RoundedButton(MSGS.cancel());
  RoundedButton finishButton = new RoundedButton(MSGS.finish());

  ListBox steps = new ListBox();
  DeckPanel wizardDeckPanel = new DeckPanel();
  VerticalPanel stepsList = new VerticalPanel();

  private IWizardPanel[] wizardPanels;
  
  private boolean canceled = false;
  
  public AbstractWizardDialog(String title, IWizardPanel[] panels, boolean autohide, boolean modal) {
    super(autohide, modal);
    
    setText(title);
    
    init();
    layout();
    setWizardPanels(panels);
    show();
  }
  
  /**
   * Init()
   * 
   * Initialize the GUI Elements by setting up the required listeners and state
   * NOTE:  This method can be overridden to provided new/additional functionality
   * but should NEVER be called more than once during the lifecycle of the object
   */
  protected void init() {
 
    nextButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        int oldIndex = steps.getSelectedIndex();  // The panel currently being displayed
        int newIndex = oldIndex + 1;              // The panel that is going to be displayed
        // Get the actors (next and previous panels)
        IWizardPanel nextPanel = (IWizardPanel)wizardDeckPanel.getWidget(newIndex);
        IWizardPanel previousPanel = (IWizardPanel)wizardDeckPanel.getWidget(oldIndex);
        if (!onNext(nextPanel, previousPanel)) {
          return;
        }
        // Update the Listeners
        previousPanel.removeWizardPanelListener(AbstractWizardDialog.this);
        nextPanel.addWizardPanelListener(AbstractWizardDialog.this);
        // Update the GUI with the current widget index;
        updateGUI(newIndex);
      }
    });
    
    backButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        int oldIndex = wizardDeckPanel.getVisibleWidget();
        int newIndex = oldIndex - 1;              // The panel that is going to be displayed  
        // Get the actors (next and previous panels)
        IWizardPanel previousPanel = (IWizardPanel)wizardDeckPanel.getWidget(newIndex);
        IWizardPanel currentPanel = (IWizardPanel)wizardDeckPanel.getWidget(oldIndex);
        if (!onPrevious(previousPanel, currentPanel)) {
          return;
        }
        // Update the Listeners
        currentPanel.removeWizardPanelListener(AbstractWizardDialog.this);
        previousPanel.addWizardPanelListener(AbstractWizardDialog.this);
        // Update the GUI with the current widget index;
        updateGUI(newIndex);
      }
    });
    
    cancelButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        canceled = true;
        AbstractWizardDialog.this.hide();
      }     
    });
    
    finishButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        if (onFinish()) {
          AbstractWizardDialog.this.hide();
        }
      }
    });
    
    steps.setEnabled(false);
    
  }

  
  /**
   * @param index of the widget that will be shown.
   * 
   * updateGUI(int index) sets up the panels and buttons based on the state of the widget
   * (IWizardPanel) that will be shown (index).
   */
  protected void updateGUI(int index) {
    stepsList.setVisible(wizardDeckPanel.getWidgetCount() > 1);
    backButton.setVisible(wizardDeckPanel.getWidgetCount() > 1);
    nextButton.setVisible(wizardDeckPanel.getWidgetCount() > 1);
    // Updates the selected step
    steps.setSelectedIndex(index);
    // Shows the current IWizardPanel
    wizardDeckPanel.showWidget(index);
    // Enables the next button if the current IWizardPanel can continue and we're not at the last IWizardPanel
    nextButton.setEnabled(((IWizardPanel)wizardDeckPanel.getWidget(index)).canContinue() && index < wizardDeckPanel.getWidgetCount() -1);
    // Back button always enabled unless we're on the first IWizardPanel
    backButton.setEnabled(index > 0);
    // Current IWizardPanel can finish at any step.
    finishButton.setEnabled(((IWizardPanel)wizardDeckPanel.getWidget(index)).canFinish());
    
  }
  
  /**
   * layout()
   * 
   * Lays out the GUI elements.  Should only be called ONCE during the objects lifecycle
   */
  protected void layout() {    
    // Create the overall container to be displayed in the dialog
    DockPanel content = new DockPanel();
    
    // Create the Steps and add it to the content
    stepsList = new VerticalPanel();
    stepsList.add(new Label("Steps:"));
    steps.setVisibleItemCount(STEPS_COUNT);
    stepsList.add(steps);
//    steps.setSize("30%", "100%");
    content.add(stepsList, DockPanel.WEST);
    
    // Add the wizardPanels to the Deck and add the deck to the content
//    wizardDeckPanel.setSize("70%", "100%");
    content.add(wizardDeckPanel, DockPanel.CENTER);
    wizardDeckPanel.addStyleName(WIZARD_DECK_PANEL);
    
    // Add the control buttons
    HorizontalPanel wizardButtonPanel = new HorizontalPanel();
    wizardButtonPanel.setSpacing(2);
    // If we have only one button then we dont need to show the back and next button.
    wizardButtonPanel.add(backButton);
    backButton.addStyleName(BACK_BTN_STYLE);
    wizardButtonPanel.add(nextButton);
    nextButton.addStyleName(NEXT_BTN_STYLE);
    wizardButtonPanel.add(finishButton);
    finishButton.addStyleName(FINISH_BTN_STYLE);
    wizardButtonPanel.add(cancelButton);
    cancelButton.addStyleName(CANCEL_BTN_STYLE);
    wizardButtonPanel.addStyleName(WIZARD_BUTTON_PANEL);

    HorizontalPanel wizardButtonPanelWrapper = new HorizontalPanel();
    wizardButtonPanelWrapper.setWidth("100%"); //$NON-NLS-1$
    wizardButtonPanelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    wizardButtonPanelWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
    wizardButtonPanelWrapper.add(wizardButtonPanel);
    
    content.add(wizardButtonPanelWrapper, DockPanel.SOUTH);
    content.setCellVerticalAlignment(wizardButtonPanelWrapper, HasVerticalAlignment.ALIGN_BOTTOM);
    
    // Add the content to the dialog
    add(content);
    content.setWidth("100%"); //$NON-NLS-1$
    content.setHeight("100%"); //$NON-NLS-1$
  }

  /**
   * @param wizardPanels - IWizardPanel[]
   * 
   * Creates a wizardDeckPanel with the contents of wizardPanels respecting the order.
   * Creates a step panel populated with the step names from the wizardPanels and then
   * sets the current wizard panel to the first panel in the list and updates the GUI.
   */
  public void setWizardPanels(IWizardPanel[] wizardPanels) {
    this.wizardPanels = wizardPanels;
    
    steps.clear();      
    wizardDeckPanel.clear();
    if (wizardPanels != null  && wizardPanels.length > 0) { // Add new wizardPanels
      for (IWizardPanel panel : wizardPanels) {
        steps.addItem(panel.getName());
        wizardDeckPanel.add((Widget) panel);
      }
      
      ((IWizardPanel) wizardDeckPanel.getWidget(0)).addWizardPanelListener(this);
      if (wizardPanels.length == 1) { // We only have one item so change the Finish button to ok.
        finishButton.setText("OK");
      }
      
      updateGUI(0);
    }
  }
  
  /**
   * @return the current list if IWizardPanel in an array.
   */
  public IWizardPanel[] getWizardPanels() {
    return wizardPanels;
  }
  
  /* (non-Javadoc)
   * @see org.pentaho.gwt.widgets.client.wizards.IWizardPanelListener#panelChanged(org.pentaho.gwt.widgets.client.wizards.IWizardPanel)
   */
  public void panelUpdated(IWizardPanel wizardPanel) {
    int index = wizardDeckPanel.getVisibleWidget();
    int lastPanelIndex = wizardDeckPanel.getWidgetCount() -1;
    
    nextButton.setEnabled(wizardPanel.canContinue() && index < lastPanelIndex);
    finishButton.setEnabled(wizardPanel.canFinish());
  }
  
  public boolean wasCancelled() {
    return canceled;
  }
  
  /**
   * abstract onFinish()
   * 
   * Override for action to take when user presses the finish button.  Return true if the wizard
   * dialog should close after the finish() method completes.
   */
  protected abstract boolean onFinish();
  
  /**
   * @param nextPanel
   * @param previousPanel
   * @return boolean if the "Next" operation should complete.
   * 
   * Users should return true if the Wizard should proceed to the next panel.  This would be a
   * good spot to retrieve/update state information between the two panels.  This method is call
   * before the next method executes (ie. next panel is displayed).  If nothing needs to be done
   * simply return true
   */
  protected abstract boolean onNext(IWizardPanel nextPanel, IWizardPanel previousPanel);
  
  /**
   * @param previousPanel
   * @param currentPanel
   * @return boolean if the "Back" operation should complete
   *   
   * Users should return true if the Wizard should proceed to the next panel.  This would be a
   * good spot to retrieve/update state information between the two panels.  This method is call
   * before the back method executes (ie. previous panel is displayed).  If nothing needs to be
   * done simply return true;
   */
  protected abstract boolean onPrevious(IWizardPanel previousPanel, IWizardPanel currentPanel);
}
