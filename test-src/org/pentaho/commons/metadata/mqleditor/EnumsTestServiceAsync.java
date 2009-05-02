package org.pentaho.commons.metadata.mqleditor;

import org.pentaho.commons.metadata.mqleditor.EnumsTestService.Basic;
import org.pentaho.commons.metadata.mqleditor.EnumsTestService.Complex;
import org.pentaho.commons.metadata.mqleditor.EnumsTestService.Subclassing;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 *
 */
public interface EnumsTestServiceAsync {
  void echo(Basic value, AsyncCallback<Basic> callback);

  void echo(Complex value, AsyncCallback<Complex> callback);

  void echo(Subclassing value, AsyncCallback<Subclassing> callback);
}
