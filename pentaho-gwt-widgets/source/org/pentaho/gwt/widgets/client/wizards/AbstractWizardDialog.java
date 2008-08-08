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

import org.pentaho.gwt.widgets.client.dialogs.DialogBox;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
  
  private static final int STEPS_COUNT = 15;  // Defines the height of the steps ListBox
  
  // gui elements
  Button backButton = new Button("Back");
  Button nextButton = new Button("Next");
  Button cancelButton = new Button("Cancel");
  Button finishButton = new Button("Finish");

  ListBox steps = new ListBox();
  DeckPanel wizardDeckPanel = new DeckPanel();

  private IWizardPanel[] wizardPanels;
  
  private boolean canceled = false;
  
  public AbstractWizardDialog(String title, IWizardPanel[] panels, boolean autohide, boolean modal) {
    super(autohide, modal);
    
    this.wizardPanels = panels;
    
    setText(title);
    
    init();
    layout();
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
    
    finishButton.setEnabled(false);
    steps.setEnabled(false);
    
    setWizardPanels(wizardPanels);
  }

  
  /**
   * @param index of the widget that will be shown.
   * 
   * updateGUI(int index) sets up the panels and buttons based on the state of the widget
   * (IWizardPanel) that will be shown (index).
   */
  protected void updateGUI(int index) {
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
    VerticalPanel stepsList = new VerticalPanel();
    stepsList.add(new Label("Steps:"));
    steps.setVisibleItemCount(STEPS_COUNT);
    stepsList.add(steps);
//    steps.setSize("30%", "100%");
    content.add(stepsList, DockPanel.WEST);
    
    // Add the wizardPanels to the Deck and add the deck to the content
//    wizardDeckPanel.setSize("70%", "100%");
    content.add(wizardDeckPanel, DockPanel.CENTER);
    
    // Add the control buttons
    HorizontalPanel buttonPanel = new HorizontalPanel();
    buttonPanel.add(backButton);
    buttonPanel.add(nextButton);
    buttonPanel.add(finishButton);
    buttonPanel.add(cancelButton);
    content.add(buttonPanel, DockPanel.SOUTH);
    content.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);
    
    // Add the content to the dialog
    add(content);

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
