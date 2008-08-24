package org.pentaho.gwt.widgets.client.controls;

import java.util.Date;

import org.pentaho.gwt.widgets.client.Widgets;
import org.pentaho.gwt.widgets.client.containers.SimpleGroupBox;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Steven Barkdull
 *
 */
public class DateRangeEditor extends SimpleGroupBox implements IChangeHandler {

  private static final WidgetsLocalizedMessages MSGS = Widgets.getLocalizedMessages();
  private static final String END_DATE_RB_GROUP = "end-date-group"; //$NON-NLS-1$

  private DatePickerEx startDatePicker = null;
  private EndDatePanel endDatePanel = null;
  
  private ErrorLabel startLabel = null;
  private ICallback<IChangeHandler> onChangeHandler = null;

  public DateRangeEditor( Date date ) {

    super( MSGS.rangeOfRecurrence() );

    HorizontalPanel outerHP = new HorizontalPanel();
    add( outerHP );
    
    HorizontalPanel hp = new HorizontalPanel();
    Label l = new Label( MSGS.startLabel() );
    l.setStyleName("startLabel"); //$NON-NLS-1$
    hp.add( l );
    startDatePicker = new DatePickerEx();
    hp.add(startDatePicker);
    startLabel = new ErrorLabel( hp );
    outerHP.add(startLabel);

    endDatePanel = new EndDatePanel( date );
    outerHP.add(endDatePanel);
    
    reset( date );
    configureOnChangeHandler();
  }
  
  public void setStartDateError( String errorMsg ) {
    startLabel.setErrorMsg( errorMsg );
  }
  
  public Date getStartDate() {
    return startDatePicker.getSelectedDate();
  }
  
  public void setStartDate( Date d ) {
    startDatePicker.setSelectedDate( d );
  }
  
  public Date getEndDate() {
    return endDatePanel.getDate();
  }
  
  public void setEndDate( Date d ) {
    endDatePanel.setDate( d );
  }
  
  public void reset( Date d ) {
    startDatePicker.setSelectedDate( d );
    startDatePicker.setYoungestDate( d );
    endDatePanel.reset( d );
  }
  
  public void setNoEndDate() {
    endDatePanel.setNoEndDate();
  }
  
  public boolean isEndBy() {
    return endDatePanel.isEndBy();
  }
  
  public void setEndBy() {
    endDatePanel.setEndBy();
  }
  
  public boolean isNoEndDate() {
    return endDatePanel.isNoEndDate();
  }
  
  public void setEndByError( String errorMsg ) {
    endDatePanel.setEndByError( errorMsg );
  }

  public void setOnChangeHandler( ICallback<IChangeHandler> handler ) {
    this.onChangeHandler = handler;
  }
  
  private void changeHandler() {
    if ( null != onChangeHandler ) {
      onChangeHandler.onHandle( this );
    }
  }
  
  private void configureOnChangeHandler() {
    final DateRangeEditor localThis = this;
    
    ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
      public void onHandle(IChangeHandler o) {
        localThis.changeHandler();
      }
    };

    startDatePicker.setOnChangeHandler(handler);
    endDatePanel.setOnChangeHandler(handler);
  }

  private class EndDatePanel extends VerticalPanel implements IChangeHandler {

    private DatePickerEx endDatePicker = null;
    private RadioButton noEndDateRb = null;
    private RadioButton endByRb = null;
    private ErrorLabel endByLabel = null;
    private ICallback<IChangeHandler> onChangeHandler = null;
    
    public EndDatePanel( Date date ) {
      final EndDatePanel localThis = this;
  
      noEndDateRb = new RadioButton(END_DATE_RB_GROUP, MSGS.noEndDateLabel() );
      noEndDateRb.setStyleName("recurrenceRadioButton"); //$NON-NLS-1$
      noEndDateRb.setChecked(true);
      add(noEndDateRb);
      HorizontalPanel hp = new HorizontalPanel();
      add(hp);
  
      HorizontalPanel endByPanel = new HorizontalPanel();
      endByRb = new RadioButton(END_DATE_RB_GROUP, MSGS.endByLabel() );
      endByRb.setStyleName("recurrenceRadioButton"); //$NON-NLS-1$
      endByPanel.add(endByRb);
      endDatePicker = new DatePickerEx();
      endDatePicker.setEnabled(false);
      endByPanel.add(endDatePicker);
      endByLabel = new ErrorLabel( endByPanel );
      hp.add( endByLabel );
  
      noEndDateRb.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          localThis.endDatePicker.setEnabled(false);
        }
      });
  
      endByRb.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          localThis.endDatePicker.setEnabled(true);
        }
      });
      reset( date );
      configureOnChangeHandler();
    }
    
    public void reset( Date d ) {
      setNoEndDate();
      endDatePicker.setSelectedDate( d );
      endDatePicker.setYoungestDate( d );
    }
    
    public DatePickerEx getEndDatePicker() {
      return endDatePicker;
    }
    
    public void setNoEndDate() {
      endByRb.setChecked( false );
      noEndDateRb.setChecked( true );
      endDatePicker.setEnabled( false );
    }
    
    public boolean isEndBy() {
      return endByRb.isChecked();
    }
    
    public void setEndBy() {
      noEndDateRb.setChecked( false );
      endByRb.setChecked( true );
      endDatePicker.setEnabled( true );
    }
    
    public boolean isNoEndDate() {
      return noEndDateRb.isChecked();
    }
    
    public Date getDate() {
      return isEndBy()
        ? endDatePicker.getSelectedDate()
        : null;
    }
    
    public void setDate( Date d ) {
      endDatePicker.setSelectedDate( d );
    }
    
    public void setEndByError( String errorMsg ) {
      endByLabel.setErrorMsg( errorMsg );
    }
    
    public void setOnChangeHandler( ICallback<IChangeHandler> handler ) {
      this.onChangeHandler = handler;
    }
    
    private void changeHandler() {
      if ( null != onChangeHandler ) {
        onChangeHandler.onHandle( this );
      }
    }
    
    private void configureOnChangeHandler() {
      final EndDatePanel localThis = this;
      
      ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
        public void onHandle(IChangeHandler o) {
          localThis.changeHandler();
        }
      };
      KeyboardListener keyboardListener = new KeyboardListener() {
        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        }
        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        }
        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
          localThis.changeHandler();
        }
      };
      
      ClickListener clickListener = new ClickListener() {
        public void onClick(Widget sender) {
          localThis.changeHandler();
        }
      };
      
      endDatePicker.setOnChangeHandler(handler);
      noEndDateRb.addClickListener( clickListener );
      noEndDateRb.addKeyboardListener( keyboardListener );
      endByRb.addClickListener( clickListener );
      endByRb.addKeyboardListener( keyboardListener );
    }
  } // end EndDatePanel
}
