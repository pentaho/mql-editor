package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.model.Category;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class CsvModel extends XulEventSourceAdapter{
  private boolean validated;
  private Domain domain;
  private boolean headersPresent = false;
  private List<CsvModelDataRow> dataRows = new ArrayList<CsvModelDataRow>();
  private String selectedFile = null;

  public CsvModel() {
  }

  public Domain getDomain() {
    return domain;
  }

  public void setDomain(Domain domain) {
    this.domain = domain;
    setModelData(domain);  
  }

  public boolean isHeadersPresent() {
    return headersPresent;
  }

  public void setHeadersPresent(boolean headersPresent) {
    boolean previousVal = this.headersPresent;
    this.headersPresent = headersPresent;
    this.firePropertyChange("headersPresent", previousVal, headersPresent); //$NON-NLS-1$

  }


  public String getSelectedFile() {
    return selectedFile;
  }

  public void setSelectedFile(String selectedFile) {
    String previousVal = this.selectedFile;
    this.selectedFile = selectedFile;
    this.firePropertyChange("selectedFile", previousVal, selectedFile); //$NON-NLS-1$
  }

  public boolean isValidated() {
    return validated;
  }


  private void setValidated(boolean validated) {
    boolean prevVal = validated;
    this.validated = validated;
    this.firePropertyChange("validated", prevVal, validated);
  }

  public void validate() {
    if (getSelectedFile() != null && getSelectedFile().length() > 0) {
      this.setValidated(true);
    } else {
      this.setValidated(false);
    }
  }

  public void setModelData(Domain domain) {
    if (domain != null) {
      List<LogicalModel> logicalModels = domain.getLogicalModels();
      for (LogicalModel logicalModel : logicalModels) {
        List<Category> categories = logicalModel.getCategories();
        for (Category category : categories) {
          List<LogicalColumn> logicalColumns = category.getLogicalColumns();
          for (LogicalColumn logicalColumn : logicalColumns) {
            addCsvModelDataRow(logicalColumn, domain.getLocales().get(0).getCode());
          }
        }
      }
      firePropertyChange("dataRows", null, dataRows);
    } else {
      if (this.dataRows != null) {
        this.dataRows.removeAll(dataRows);
        List<CsvModelDataRow> previousValue = this.dataRows;
        firePropertyChange("dataRows", previousValue, null);
      }
    }
  }

  public void addCsvModelDataRow(LogicalColumn column, String locale) {
    if (dataRows == null) {
      dataRows = new ArrayList<CsvModelDataRow>();
    }
    this.dataRows.add(new CsvModelDataRow(column, locale));
  }


  public List<CsvModelDataRow> getDataRows() {
    return dataRows;
  }

  public void setDataRows(List<CsvModelDataRow> dataRows) {
    this.dataRows = dataRows;
  }

  /*
   * Clears out the model
   */
  public void clearModel() {
    setDomain(null);
    setDataRows(null);
    setSelectedFile(null);
  }

}
