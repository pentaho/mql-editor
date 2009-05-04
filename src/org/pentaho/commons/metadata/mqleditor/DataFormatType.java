package org.pentaho.commons.metadata.mqleditor;

import java.io.Serializable;

/**
 * DataFormatType represents the data format type
 *
 */
public enum DataFormatType implements Serializable{
  CURRENCY("$XXX,XXX.XX"), MMDDYYYY("MM-DD-YYYY"), DDMMYYYY("DD-MM-YYYY"); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$

  private String formatString;
  DataFormatType(String formatString) {
    this.formatString = formatString;
  }

  public String toString() {
    return formatString;
  }
}