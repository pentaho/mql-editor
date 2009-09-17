package org.pentaho.gwt.widgets.login.client;

import java.io.Serializable;

  public class AuthenticationCanceledException extends Exception implements Serializable {

    private String msg = null;
    /**
     * 
     */
    private static final long serialVersionUID = 69L;

    public AuthenticationCanceledException() {
      super();
    }

    public AuthenticationCanceledException(String message) {
      super(message);
      msg = message;
    }

    public AuthenticationCanceledException(String message, Throwable cause) {
      super(message, cause);
      msg = message;
    }

    public AuthenticationCanceledException(Throwable cause) {
      super(cause);
      msg = cause.getMessage();
    }    
    
    public String getMessage() {
      return msg;
    }
}
