package org.pentaho.gwt.widgets.client.controls.schededitor;

import java.util.Date;

import org.pentaho.gwt.widgets.client.Widgets;
import org.pentaho.gwt.widgets.client.controls.DateRangeEditor;
import org.pentaho.gwt.widgets.client.controls.ErrorLabel;
import org.pentaho.gwt.widgets.client.i18n.WidgetsLocalizedMessages;
import org.pentaho.gwt.widgets.client.ui.ICallback;
import org.pentaho.gwt.widgets.client.ui.IChangeHandler;
import org.pentaho.gwt.widgets.client.utils.TimeUtil;

import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Steven Barkdull
 *
 */
public class CronEditor extends VerticalPanel implements IChangeHandler {
  
  private TextBox cronTb = new TextBox();
  private DateRangeEditor dateRangeEditor = null;
  private ErrorLabel cronLabel = null;
  private ICallback<IChangeHandler> onChangeHandler;
  private static final WidgetsLocalizedMessages MSGS = Widgets.getLocalizedMessages();
  
  public CronEditor() {
    Label l = new Label( MSGS.cronLabel() );
    cronLabel = new ErrorLabel( l );
    add( cronLabel );
    add( cronTb );
    
    dateRangeEditor = new DateRangeEditor( new Date() );
    add( dateRangeEditor );
    configureOnChangeHandler();
  }
  
  public void reset( Date d ) {
    cronTb.setText( "" ); //$NON-NLS-1$
    dateRangeEditor.reset( d );
  }

  public String getCronString() {
    return cronTb.getText();
  }

  public void setCronString( String cronStr) {
    this.cronTb.setText( cronStr );
  }
  
  public Date getStartDate() {
    return dateRangeEditor.getStartDate();
  }
  
  public void setStartDate( Date d ) {
    dateRangeEditor.setStartDate( d );
  }
  
  public Date getEndDate() {
    return dateRangeEditor.getEndDate();
  }
  
  public void setEndDate( Date d ) {
    dateRangeEditor.setEndDate( d );
  }
  
  public void setNoEndDate() {
    dateRangeEditor.setNoEndDate();
  }
  
  public boolean isEndBy() {
    return dateRangeEditor.isEndBy();
  }
  
  public void setEndBy() {
    dateRangeEditor.setEndBy();
  }
  
  public boolean isNoEndDate() {
    return dateRangeEditor.isNoEndDate();
  }
  
  public String getStartTime() {
    return TimeUtil.get0thTime();
  }
  
  /**
   * NOTE: should only ever be used by validators. This is a backdoor
   * into this class that shouldn't be here, do not use this method
   * unless you are validating.
   * 
   * @return DateRangeEditor
   */
  public DateRangeEditor getDateRangeEditor() {
    return dateRangeEditor;
  }
  
  public void setCronError( String errorMsg ) {
    cronLabel.setErrorMsg( errorMsg );
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
    final CronEditor localThis = this;
    KeyboardListener keyboardListener = new KeyboardListener() {
      public void onKeyDown(Widget sender, char keyCode, int modifiers) {
      }
      public void onKeyPress(Widget sender, char keyCode, int modifiers) {
      }
      public void onKeyUp(Widget sender, char keyCode, int modifiers) {
        localThis.changeHandler();
      }
    };
    ICallback<IChangeHandler> handler = new ICallback<IChangeHandler>() {
      public void onHandle(IChangeHandler o) {
        localThis.changeHandler();
      }
    };
    cronTb.addKeyboardListener( keyboardListener );
    dateRangeEditor.setOnChangeHandler( handler );
  }
}
