package org.pentaho.gwt.widgets.client.controls;

import org.pentaho.gwt.widgets.client.utils.StringUtils;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * @author Steven Barkdull
 *
 */

public class ErrorLabel extends VerticalPanel {

    private Label errorLabel = null;
    
    public ErrorLabel( Widget w ) {
      errorLabel = new Label();
      errorLabel.setStyleName( "errorLabel" ); //$NON-NLS-1$
      errorLabel.setVisible( false );
      add( errorLabel );
      
      add( w );
    }
    
    /**
     * Set an error message to be associated with the label
     * 
     * @param msg String if null, clear the error message, else set
     * the error message to <param>mgs</param>.
     */
    public void setErrorMsg( String msg ) {
      if ( !StringUtils.isEmpty( msg ) ) {
        errorLabel.setText( msg );
        errorLabel.setVisible( true );
      } else {
        errorLabel.setText( "" ); //$NON-NLS-1$
        errorLabel.setVisible( false );
      }
    }
}
