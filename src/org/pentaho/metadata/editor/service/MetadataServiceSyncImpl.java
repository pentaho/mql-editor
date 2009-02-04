package org.pentaho.metadata.editor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.pentaho.commons.mql.ui.mqldesigner.CWMStartup;
import org.pentaho.commons.mql.ui.mqldesigner.MQLWhereConditionModel;
import org.pentaho.metadata.ColumnType;
import org.pentaho.metadata.IBusinessColumn;
import org.pentaho.metadata.ICondition;
import org.pentaho.metadata.IDomain;
import org.pentaho.metadata.IModel;
import org.pentaho.metadata.IOrder;
import org.pentaho.metadata.beans.BusinessColumn;
import org.pentaho.metadata.beans.BusinessTable;
import org.pentaho.metadata.beans.Category;
import org.pentaho.metadata.beans.Domain;
import org.pentaho.metadata.beans.Model;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.mql.MQLQueryImpl;
import org.pentaho.pms.mql.OrderBy;
import org.pentaho.pms.mql.Selection;
import org.pentaho.pms.mql.WhereCondition;
import org.pentaho.pms.schema.BusinessCategory;
import org.pentaho.pms.schema.BusinessModel;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.pms.schema.concept.types.datatype.DataTypeSettings;
import org.pentaho.pms.util.UniqueList;
import org.pentaho.ui.xul.XulServiceCallback;

public class MetadataServiceSyncImpl {

  private String locale = Locale.getDefault().toString();

  private Domain domain;

  private CwmSchemaFactory factory;

  private SchemaMeta meta;

  public MetadataServiceSyncImpl(CWM cwm, CwmSchemaFactory factory) {
    this.factory = factory;
    meta = factory.getSchemaMeta(cwm);

    domain = new Domain();
    domain.setName(meta.getDomainName());

    UniqueList<BusinessModel> models = meta.getBusinessModels();
    for (BusinessModel model : models) {
      domain.getModels().add(createModel(model));
    }

  }

  private Model createModel(BusinessModel m) {
    Model model = new Model();
    model.setName(m.getName(locale));
    model.setId(m.getId());

    UniqueList<BusinessCategory> cats = m.getRootCategory().getBusinessCategories();
    for (BusinessCategory cat : cats) {
      model.getCategories().add(createCategory(cat));
    }

    return model;
  }

  private Category createCategory(BusinessCategory c) {
    Category cat = new Category();
    cat.setName(c.getName(locale));
    cat.setId(c.getId());
    UniqueList<org.pentaho.pms.schema.BusinessColumn> cols = c.getBusinessColumns();
    for (org.pentaho.pms.schema.BusinessColumn col : cols) {
      cat.getBusinessColumns().add(createColumn(col));
    }

    return cat;
  }

  private BusinessColumn createColumn(org.pentaho.pms.schema.BusinessColumn c) {
    BusinessColumn col = new BusinessColumn();
    col.setName(c.getName(locale));
    col.setId(c.getId());
    col.setTable(createTable(c.getBusinessTable()));

    int type = c.getPhysicalColumn().getDataType().getType();
    ColumnType ourType = null;
    switch (type) {
      case DataTypeSettings.DATA_TYPE_BOOLEAN:
        ourType = ColumnType.BOOLEAN;
        break;
      case DataTypeSettings.DATA_TYPE_STRING:
        ourType = ColumnType.TEXT;
        break;
      case DataTypeSettings.DATA_TYPE_NUMERIC:
        ourType = ColumnType.FLOAT;
        break;
      case DataTypeSettings.DATA_TYPE_DATE:
        ourType = ColumnType.DATE;
        break;
    }

    col.setType(ourType);
    return col;
  }

  private BusinessTable createTable(org.pentaho.pms.schema.BusinessTable t) {
    BusinessTable table = new BusinessTable();
    table.setName(t.getName(locale));
    table.setId(t.getId());
    return table;
  }

  public String[] getMetadataDomains() {
    return new String[] { domain.getName() };
  }

  public IDomain getDomainByName(String name) {
    return domain;
  }

  private org.pentaho.pms.schema.BusinessColumn[] getColumns(BusinessModel model,
      List<? extends IBusinessColumn> thincols) {
    org.pentaho.pms.schema.BusinessColumn[] cols = new org.pentaho.pms.schema.BusinessColumn[thincols.size()];

    int i = 0;
    for (IBusinessColumn thincol : thincols) {
      UniqueList list = model.getAllBusinessColumns();
      for (Object col : list.getList()) {
        if (((org.pentaho.pms.schema.BusinessColumn) col).getName(locale).equals(thincol.getName())) {
          cols[i++] = (org.pentaho.pms.schema.BusinessColumn) col;
        }
      }
    }
    return cols;
  }

  private org.pentaho.pms.schema.BusinessColumn getColumn(BusinessModel model, IBusinessColumn thinCol) {
    UniqueList list = model.getAllBusinessColumns();
    for (Object col : list.getList()) {
      if (((org.pentaho.pms.schema.BusinessColumn) col).getName(locale).equals(thinCol.getName())) {
        return (org.pentaho.pms.schema.BusinessColumn) col;
      }
    }
    return null;
  }

  private MQLWhereConditionModel[] getConditions(BusinessModel model, List<? extends ICondition> thinConditions) {
    MQLWhereConditionModel[] conditions = new MQLWhereConditionModel[thinConditions.size()];
    int i = 0;
    for (ICondition thinCondition : thinConditions) {
      MQLWhereConditionModel where = new MQLWhereConditionModel(thinCondition.getOperator().toString(), getColumn(
          model, thinCondition.getColumn()), thinCondition.getValue());
      conditions[i++] = where;
    }
    return conditions;
  }
  
  private List<OrderBy> getOrders(BusinessModel model, List<? extends IOrder> thinOrders){
    List<OrderBy> ord = new ArrayList<OrderBy>();
    
    for(IOrder thinOrder : thinOrders){
      Selection selection = new Selection(getColumn(model , thinOrder.getColumn()));
      ord.add(new OrderBy(selection, (thinOrder.getOrderType() == IOrder.Type.ASC)));
    }
    return ord;
  }

  public String saveQuery(IModel model, List<? extends IBusinessColumn> cols, List<? extends ICondition> conditions,
      List<? extends IOrder> orders) {
    model.getName();

    UniqueList<BusinessModel> models = meta.getBusinessModels();
    BusinessModel realModel = null;
    for (BusinessModel m : models) {
      if (m.getName(locale).equals(model.getName())) {
        realModel = m;
        break;
      }
    }
    if (realModel != null) {

      MQLQuery mqlQuery = null;
      try {

        org.pentaho.pms.schema.BusinessColumn[] businessColumns = getColumns(realModel, cols);
        if (businessColumns.length > 0) {
          BusinessModel businessModel = realModel;
          mqlQuery = new MQLQueryImpl(this.meta, businessModel, null, meta.getActiveLocale()); //$NON-NLS-1$
          List<Selection> selections = new ArrayList<Selection>();
          for (int i = 0; i < businessColumns.length; i++) {
            selections.add(new Selection(businessColumns[i]));
          }

          mqlQuery.setSelections(selections);
          MQLWhereConditionModel wherelist[] = getConditions(realModel, conditions);
          ArrayList<WhereCondition> constraints = new ArrayList<WhereCondition>();
          BusinessCategory rootCat = businessModel.getRootCategory();
          //mqlQuery.setDisableDistinct(!this.distinctSelections.getSelection());
          for (int i = 0; i < wherelist.length; i++) {
            BusinessCategory businessCategory = rootCat.findBusinessCategoryForBusinessColumn(wherelist[i].getField());

            constraints.add(new WhereCondition(businessModel, wherelist[i].getOperator(),
                "[" + businessCategory.getId() + "." + //$NON-NLS-1$ //$NON-NLS-2$
                    wherelist[i].getField().getId() + "] " + wherelist[i].getCondition()) //$NON-NLS-1$
                );
          }
          mqlQuery.setConstraints(constraints);
          mqlQuery.setOrder(getOrders(realModel, orders));

          return mqlQuery.getXML();
        }
      } catch (Throwable e) { // PMSFormulaException e) {
        e.printStackTrace();
      }

    } else {
      //throw error
    }
    return null;
  }

}
