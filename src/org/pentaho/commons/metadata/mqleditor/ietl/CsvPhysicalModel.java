package org.pentaho.commons.metadata.mqleditor.ietl;

public class CsvPhysicalModel {
  
  String csvLocation;
  String delimiter = ",";
  private String enclosure= "\"";
  private boolean headerPresent;
  
  public String getCsvLocation() {
    return csvLocation;
  }
  
  public void setCsvLocation(String csvLocation) {
    this.csvLocation = csvLocation;
  }
  
  public String getDelimiter() {
    return delimiter;
  }
  
  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  public void setHeaderPresent(boolean headerPresent) {
    this.headerPresent = headerPresent;
  }

  public boolean isHeaderPresent() {
    return headerPresent;
  }

  void setEnclosure(String enclosure) {
    this.enclosure = enclosure;
  }

  String getEnclosure() {
    return enclosure;
  }
}
