package org.pentaho.commons.metadata.mqleditor.editor.models;

import org.pentaho.commons.metadata.mqleditor.DatasourceType;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.beans.Datasource;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class DatasourceModel extends XulEventSourceAdapter{
  private String datasourceName;
  private DatasourceType datasourceType = DatasourceType.NONE;
  private RelationalModel relationalModel;
  private CsvModel csvModel;
  public DatasourceModel() {
    relationalModel = new RelationalModel();
    csvModel = new CsvModel();
  }

  public RelationalModel getRelationalModel() {
    return relationalModel;
  }

  public void setRelationalModel(RelationalModel relationalModel) {
    this.relationalModel = relationalModel;
  }

  public CsvModel getCsvModel() {
    return csvModel;
  }

  public void setCsvModel(CsvModel csvModel) {
    this.csvModel = csvModel;
  }


  public String getDatasourceName() {
    return datasourceName;
  }

  public void setDatasourceName(String datasourceName) {
    String previousVal = this.datasourceName;
    this.datasourceName = datasourceName;
    this.firePropertyChange("datasourcename", previousVal, datasourceName); //$NON-NLS-1$
    validate();
  }

  public DatasourceType getDatasourceType() {
    return this.datasourceType;
  }

  public void setDatasourceType(DatasourceType datasourceType) {
    DatasourceType previousVal = this.datasourceType;
    this.datasourceType = datasourceType;
    this.firePropertyChange("datasourceType", previousVal, datasourceType); //$NON-NLS-1$
    validate();
  }

  private boolean validate() {
    boolean value = false;
    if(DatasourceType.SQL == getDatasourceType()) {
      value = relationalModel.isValidated();
    } else if(DatasourceType.CSV == getDatasourceType()){
      value = csvModel.isValidated();
    }
    return (datasourceName != null && datasourceName.length() > 0 && value);
  }

  /*
   * Clears out the model
   */
  public void clearModel() {
    setDatasourceName("");
    setDatasourceType(DatasourceType.NONE);
    relationalModel.clearModel();
    csvModel.clearModel();
  }
  
  public IDatasource getDatasource() {
    IDatasource datasource = new Datasource();
    if(DatasourceType.SQL == getDatasourceType()) {
      datasource.setBusinessData(getRelationalModel().getBusinessData());
      datasource.setConnections(getRelationalModel().getConnections());
      datasource.setQuery(getRelationalModel().getQuery());
      datasource.setSelectedConnection(getRelationalModel().getSelectedConnection());
    } else {
      datasource.setBusinessData(new BusinessData(getCsvModel().getDomain(), null));
      datasource.setSelectedFile(getCsvModel().getSelectedFile());
      datasource.setHeadersPresent(getCsvModel().isHeadersPresent());
    }
    return datasource;
  }

}
