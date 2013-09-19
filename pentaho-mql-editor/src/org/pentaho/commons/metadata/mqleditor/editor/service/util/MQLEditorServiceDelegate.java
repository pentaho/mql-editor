/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.commons.metadata.mqleditor.editor.service.util;

import org.apache.commons.lang.NotImplementedException;
import org.pentaho.commons.metadata.mqleditor.*;
import org.pentaho.commons.metadata.mqleditor.beans.*;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.utils.ModelSerializer;
import org.pentaho.commons.metadata.mqleditor.utils.ModelUtil;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.metadata.model.concept.types.AggregationType;
import org.pentaho.metadata.model.concept.types.DataType;
import org.pentaho.metadata.query.model.Constraint;
import org.pentaho.metadata.query.model.Parameter;
import org.pentaho.metadata.query.model.util.QueryXmlHelper;
import org.pentaho.metadata.repository.IMetadataDomainRepository;
import org.pentaho.pms.factory.CwmSchemaFactoryInterface;
import org.pentaho.pms.mql.*;
import org.pentaho.pms.schema.BusinessCategory;
import org.pentaho.pms.schema.BusinessColumn;
import org.pentaho.pms.schema.BusinessModel;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.pms.schema.concept.types.aggregation.AggregationSettings;
import org.pentaho.pms.schema.concept.types.datatype.DataTypeSettings;
import org.pentaho.pms.util.UniqueList;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * This delegate class provides the majority of functionality needed by an implementation of the MQLEditor Service.
 * If you wish to use this file as a starting point for your implementation you'll need to provide a CWM isntance and
 * CWMSchemaFactory
 *
 * This delegate is used in the debug services provided in the base application.
 *
 */

public class MQLEditorServiceDelegate {

  private String locale = Locale.getDefault().toString();

  private List<MqlDomain> domains = new ArrayList<MqlDomain>();
  private Set<String> domainNames = new TreeSet<String>();

  private CwmSchemaFactoryInterface factory;

  private IMetadataDomainRepository domainRepository;
  
  private final ConditionFormatter conditionFormatter = new ConditionFormatter(new OperatorFormatter());

  /**
   * Keeps track of where a particular model came from.
   */
  private Map<String, SchemaMeta> modelIdToSchemaMetaMap = new HashMap<String, SchemaMeta>();
  
  public MQLEditorServiceDelegate(IMetadataDomainRepository domainRepository) {
    this.domainRepository = domainRepository;

    for (String id : domainRepository.getDomainIds()) {
      if (!domainNames.contains(id)) {
        // add the domain
        addThinDomain(id);
      }
    }
  }


  public MQLEditorServiceDelegate() {
  }

  protected String getLocale() {
    return locale;
  }
  
  public List<MqlDomain> refreshMetadataDomains() {
    domains.clear();
    domainNames.clear();
    if(domainRepository != null){
      for (String id : domainRepository.getDomainIds()) {
        if (!domainNames.contains(id)) {
          // add the domain
          addThinDomain(id);
        }
      }
    }
    return domains;
  }


  public void addThinDomain(String id) {
    org.pentaho.metadata.model.Domain thinDomain = domainRepository.getDomain(id);
    try {
      Domain domain = new Domain();
      domain.setName(thinDomain.getId());
      for (LogicalModel model : thinDomain.getLogicalModels()) {
        Model myModel = createModel(model);
        domain.getModels().add(myModel);
      }
      domains.add(domain);
      domainNames.add(domain.getName());
    } catch (Exception e) {
      e.printStackTrace();
      // log error
    }

  }

  public String[][] getPreviewData(MqlQuery query, int page, int limit) {
    throw new NotImplementedException("Preview not implemented in the Deligate");
  }

  private Model createModel(BusinessModel m) {
    Model model = new Model();
    model.setName(m.getName(getLocale()));
    model.setId(m.getId());

    UniqueList<BusinessCategory> cats = m.getRootCategory().getBusinessCategories();
    for (BusinessCategory cat : cats) {
      model.getCategories().add(createCategory(m, cat));
    }

    return model;
  }

  private Model createModel(LogicalModel m) {
    Model model = new Model();
    model.setName(m.getName(getLocale()));
    model.setId(m.getId());
    for (org.pentaho.metadata.model.Category cat : m.getCategories()) {
      model.getCategories().add(createCategory(m, cat));
    }

    return model;

  }

  private Category createCategory(BusinessModel m, BusinessCategory c) {
    Category cat = new Category();
    cat.setName(c.getName(getLocale()));
    cat.setId(c.getId());
    UniqueList<org.pentaho.pms.schema.BusinessColumn> cols = c.getBusinessColumns();
    for (org.pentaho.pms.schema.BusinessColumn col : cols) {
      cat.getBusinessColumns().add(createColumn(m, col));
    }

    return cat;
  }

  private Category createCategory(LogicalModel m, org.pentaho.metadata.model.Category c) {
    Category cat = new Category();
    cat.setName(c.getName(getLocale()));
    cat.setId(c.getId());
    for (LogicalColumn col : c.getLogicalColumns()) {
      boolean isHidden = (col.getProperty("hidden") != null) ? (Boolean) col.getProperty("hidden") : false;
      if(!isHidden) {	
    	  cat.getBusinessColumns().add(createColumn(m, col));
      }
    }
    return cat;
  }

  private Column createColumn(BusinessModel m, org.pentaho.pms.schema.BusinessColumn c) {
    Column col = new Column();
    col.setName(c.getName(getLocale()));
    col.setId(c.getId());

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
    List<AggregationSettings> possibleAggs = c.getAggregationList();

    for (AggregationSettings agg : possibleAggs) {
      col.getAggTypes().add(getAggType(agg.getType()));
    }

    // There might be a default agg, but no agg list. If so, add it to the list.
    AggType defaultAggType = getAggType(c.getAggregationType().getType());
    if (col.getAggTypes().contains(defaultAggType) == false) {
      col.getAggTypes().add(defaultAggType);
    }

    col.setDefaultAggType(defaultAggType);
    col.setSelectedAggType(defaultAggType);

    return col;
  }

  private Column createColumn(LogicalModel m, LogicalColumn c) {
    Column col = new Column();
    col.setName(c.getName(getLocale()));
    col.setId(c.getId());

    ColumnType ourType = null;
    if (c.getDataType() != null) {
      int type = c.getDataType().getType();
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
    }
    col.setType(ourType);
    List<AggregationType> possibleAggs = c.getAggregationList();
    if (possibleAggs != null) {
      for (AggregationType agg : possibleAggs) {
        col.getAggTypes().add(getAggType(agg.ordinal()));
      }
    }

    // There might be a default agg, but no agg list. If so, add it to the list.

    AggType defaultAggType = null;
    if (c.getAggregationType() != null) {
      defaultAggType = getAggType(c.getAggregationType().ordinal());
    } else {
      defaultAggType = AggType.NONE;
    }
    if (col.getAggTypes().contains(defaultAggType) == false) {
      col.getAggTypes().add(defaultAggType);
    }
    col.setDefaultAggType(defaultAggType);
    col.setSelectedAggType(defaultAggType);

    return col;
  }

  private AggType getAggType(int type) {
    switch (type) {
      case AggregationSettings.TYPE_AGGREGATION_COUNT:
        return AggType.COUNT;
      case AggregationSettings.TYPE_AGGREGATION_COUNT_DISTINCT:
        return AggType.COUNT_DISTINCT;
      case AggregationSettings.TYPE_AGGREGATION_AVERAGE:
        return AggType.AVERAGE;
      case AggregationSettings.TYPE_AGGREGATION_MAXIMUM:
        return AggType.MAX;
      case AggregationSettings.TYPE_AGGREGATION_MINIMUM:
        return AggType.MIN;
      case AggregationSettings.TYPE_AGGREGATION_SUM:
        return AggType.SUM;
      default:
        return AggType.NONE;
    }
  }

  private AggregationType getAggregationType(AggType type) {
    if (type == null) {
      return null;
    }
    switch (type) {
      case COUNT:
        return AggregationType.COUNT;
      case COUNT_DISTINCT:
        return AggregationType.COUNT_DISTINCT;
      case AVERAGE:
        return AggregationType.AVERAGE;
      case MAX:
        return AggregationType.MAXIMUM;
      case MIN:
        return AggregationType.MINIMUM;
      case SUM:
        return AggregationType.SUM;
      default:
        return AggregationType.NONE;
    }
  }

  private AggregationSettings getAggregationSettings(AggType type) {
    if (type == null) {
      return AggregationSettings.NONE;
    }
    switch (type) {
      case COUNT:
        return AggregationSettings.COUNT;
      case COUNT_DISTINCT:
        return AggregationSettings.COUNT_DISTINCT;
      case AVERAGE:
        return AggregationSettings.AVERAGE;
      case MAX:
        return AggregationSettings.MAXIMUM;
      case MIN:
        return AggregationSettings.MINIMUM;
      case SUM:
        return AggregationSettings.SUM;
      default:
        return AggregationSettings.NONE;
    }
  }

  public List<MqlDomain> getMetadataDomains() {
    return domains;
  }

  public MqlDomain getDomainByName(String name) {
	  
    // refresh the metadata domains for every request,
    // we should not be caching this unless its flushable
    // and per user (security issues, etc)
    
    refreshMetadataDomains();
    
    if (!name.endsWith(".xmi")) {
      return matchLegacyDomainName(name);
    }
    for (MqlDomain domain : domains) {
      if (domain.getName().equals(name)) {
        return domain;
      }
    }

    return null;
  }

  private MqlDomain matchLegacyDomainName(String name) {
    for (MqlDomain domain : domains) {
      if (domain.getName().contains(name)) {
        return domain;
      }
    }
    return null;
  }

  private org.pentaho.pms.schema.BusinessColumn[] getColumns(BusinessModel model, List<? extends MqlColumn> thincols) {
    org.pentaho.pms.schema.BusinessColumn[] cols = new org.pentaho.pms.schema.BusinessColumn[thincols.size()];

    int i = 0;
    for (MqlColumn thincol : thincols) {
      UniqueList list = model.getAllBusinessColumns();
      for (Object col : list.getList()) {
        if (((BusinessColumn) col).getId().equals(thincol.getId())) {
          cols[i] = (org.pentaho.pms.schema.BusinessColumn) col;
          cols[i].setAggregationType(getAggregationSettings(thincol.getSelectedAggType()));
          i++;
        }
      }
    }
    return cols;
  }

  private org.pentaho.pms.schema.BusinessColumn getColumn(BusinessModel model, MqlColumn thinCol) {
    UniqueList list = model.getAllBusinessColumns();
    for (Object col : list.getList()) {
      org.pentaho.pms.schema.BusinessColumn bCol = (org.pentaho.pms.schema.BusinessColumn) col;
      if (bCol.getId().equals(thinCol.getId())) {
        
        AggType aggType = thinCol.getSelectedAggType();
        if (aggType == null && thinCol.getDefaultAggType() != null) {
          aggType = thinCol.getDefaultAggType();
        }
        bCol.setAggregationType(getAggregationSettings(aggType));
        return (org.pentaho.pms.schema.BusinessColumn) bCol;
      }
    }
    return null;
  }

  private MQLWhereConditionModel[] getConditions(BusinessModel model, List<? extends MqlCondition> thinConditions) {
    MQLWhereConditionModel[] conditions = new MQLWhereConditionModel[thinConditions.size()];
    int i = 0;
    for (MqlCondition thinCondition : thinConditions) {
      org.pentaho.pms.schema.BusinessColumn col = getColumn(model, thinCondition.getColumn());
      MQLWhereConditionModel where = new MQLWhereConditionModel(
          thinCondition.getCombinationType() == null ? "" : thinCondition.getCombinationType().toString(), //$NON-NLS-1$
          col, conditionFormatter.getCondition(thinCondition, "[" + col.toString() + "]"));
      conditions[i++] = where;
    }
    return conditions;
  }

  private List<OrderBy> getOrders(BusinessModel model, List<? extends MqlOrder> thinOrders) {
    List<OrderBy> ord = new ArrayList<OrderBy>();

    for (MqlOrder thinOrder : thinOrders) {
      Selection selection = new Selection(getColumn(model, thinOrder.getColumn()));
      ord.add(new OrderBy(selection, (thinOrder.getOrderType() == MqlOrder.Type.ASC)));
    }
    return ord;
  }

  public String saveQuery(MqlQuery query) {
    if (query.getColumns().isEmpty()) {
      // UI allowed user to create a query without columns
      throw new RuntimeException("query is not valid without columns"); //$NON-NLS-1$
    }
    
    if (domainRepository != null) {
      org.pentaho.metadata.model.Domain thinDomain = domainRepository.getDomain(query.getDomain().getName());
      if (thinDomain != null) {
        org.pentaho.metadata.query.model.Query queryModel = convertQueryModel(thinDomain, query);
        return new QueryXmlHelper().toXML(queryModel);
      }
    }

    MQLQuery fatQ = convertModel(query);
    if (fatQ != null) {
      return fatQ.getXML();
    } else {
      return "";
    }

  }

  private org.pentaho.metadata.model.Category findCategory(LogicalModel model, MqlColumn col) {
    for (org.pentaho.metadata.model.Category category : model.getCategories()) {
      for (LogicalColumn lcol : category.getLogicalColumns()) {
        if (lcol.getId().equals(col.getId())) {
          return category;
        }
      }
    }
    return null;
  }
  
  /*
   * a call to this method assumes that we are using the new thin metadata model.
   * 
   */
  public org.pentaho.metadata.query.model.Query convertQueryModel(MqlQuery query) {
    org.pentaho.metadata.model.Domain thinDomain = domainRepository.getDomain(query.getDomain().getName());
    return convertQueryModel(thinDomain, query);
  }
  
  private DataType getDataType(ColumnType type){
    switch(type){
      case BOOLEAN:
        return DataType.BOOLEAN;
      case DATE:
        // As we are crafting an open formula function to handle dates, the parameter data type needs to be a String.
        // This will eventually be handled by the Metadata layer.
        return DataType.STRING;
        // return DataType.DATE;
      case FLOAT:
      case NUMERIC:
        return DataType.NUMERIC;
      case TEXT:
        return DataType.STRING;
      default:
        return DataType.UNKNOWN;
        
    }
  }
  
  private org.pentaho.metadata.query.model.Query convertQueryModel(org.pentaho.metadata.model.Domain thinDomain, MqlQuery query) {
    LogicalModel model = thinDomain.findLogicalModel(query.getModel().getId());
    if (model != null) {
      org.pentaho.metadata.query.model.Query queryObject = new org.pentaho.metadata.query.model.Query(thinDomain, model);
      try {
        if (query.getColumns().size() > 0) {
          for (MqlColumn col : query.getColumns()) {
            
            org.pentaho.metadata.model.Category view = findCategory(model, col);

            if (view == null) {
              // log an error
              System.err.println("could not find category for column " + col.getId());
              return null;
            }
            LogicalColumn column = view.findLogicalColumn(col.getId());
            if (column == null) {
              System.err.println("could not find column : " + col.getId());
              return null;
            }
            queryObject.getSelections().add(
                new org.pentaho.metadata.query.model.Selection(view, column, getAggregationType(col.getSelectedAggType()))
              );
          }

          for (MqlCondition condition : query.getConditions()) {
            org.pentaho.metadata.model.Category view = findCategory(model, condition.getColumn());
            AggregationType type = getAggregationType(condition.getSelectedAggType());
            String field = "[";
            field += view.getId() +"." + condition.getColumn().getId();
            if (type != null) {
              field += "." + type.toString();
            }
            field += "]";
            
            if(condition.isParameterized()){
              queryObject.getParameters().add(new Parameter(condition.getValue().replaceAll("[\\{\\}]", ""), getDataType(condition.getColumn().getType()), condition.getDefaultValue()));
            }
            queryObject.getConstraints().add(
                new Constraint(getComboType(condition.getCombinationType()), conditionFormatter.getCondition(condition, field))
              );
          }

          for (MqlOrder order : query.getOrders()) {
            org.pentaho.metadata.model.Category view = findCategory(model, order.getColumn());
            LogicalColumn column = view.findLogicalColumn(order.getColumn().getId());
            if (view == null || column == null) {
              // log an error
              return null;
            }
            queryObject.getOrders().add(new org.pentaho.metadata.query.model.Order(
                new org.pentaho.metadata.query.model.Selection(view, column, getAggregationType(order.getSelectedAggType())), 
                getOrderType(order.getOrderType()))
              );
          }
          
          queryObject.setLimit(query.getLimit());
          
          return queryObject;
        }
      } catch (Throwable e) { // PMSFormulaException e) {
        e.printStackTrace();
      }

    } else {
      //throw error
    }
    return null;
  }
  
  private org.pentaho.metadata.query.model.Order.Type getOrderType(MqlOrder.Type type) {
    return org.pentaho.metadata.query.model.Order.Type.values()[type.ordinal()];
  }

  private org.pentaho.metadata.query.model.CombinationType getComboType(CombinationType type) {
    if (type == null) {
      return null;
    }
    return org.pentaho.metadata.query.model.CombinationType.values()[type.ordinal()];
  }

  public MqlQuery convertModelToThin(MQLQuery fatQ) {
    Query query = new Query();

    // currently only called by the PME-editor in which case there's only one domain.
    BusinessModel model = null;
    for (BusinessModel m : modelIdToSchemaMetaMap.get(fatQ.getModel().getId()).getBusinessModels()) {
      if (m.getId() == fatQ.getModel().getId()) {
        model = m;
      }
    }

    for (MqlModel m : domains.get(0).getModels()) {
      if (m.getId().equals(fatQ.getModel().getId())) {
        query.setModel((Model) m);
        query.setDomain((Domain) domains.get(0));
      }
    }

    List<Column> cols = new ArrayList<Column>();
    for (Selection sel : fatQ.getSelections()) {
      Column col = createColumn(model, sel.getBusinessColumn());
      cols.add(col);
    }
    query.setColumns(cols);

    List<Condition> conditions = new ArrayList<Condition>();
    for (WhereCondition w : fatQ.getConstraints()) {
      Pattern p = Pattern.compile("\\[([^\\]]*)\\.([^\\]]*)\\] (.*)"); //$NON-NLS-1$
      Matcher m = p.matcher(w.getCondition());
      if (m.find()) {
        String cat = m.group(1);
        String col = m.group(2);

        UniqueList list = model.getAllBusinessColumns();
        BusinessColumn fatcol = null;
        for (Object c : list.getList()) {
          if (((BusinessColumn) c).getId().equals(col)) {
            fatcol = (org.pentaho.pms.schema.BusinessColumn) c;

          }
        }
        Column c = createColumn(model, fatcol);
        String condition = m.group(3);
        String operator = w.getOperator();

        Condition cond = new Condition();
        cond.setColumn(c);
        cond.setValue(condition);
        cond.setOperator(Operator.parse(operator));
        conditions.add(cond);
      } else {
        // log error?
      }
    }

    query.setConditions(conditions);

    List<Order> orders = new ArrayList<Order>();
    for (OrderBy ord : fatQ.getOrder()) {
      Column col = createColumn(model, ord.getSelection().getBusinessColumn());
      if (ord.getSelection().getAggregationType() != null) {
        col.setDefaultAggType(this.getAggType(ord.getSelection().getAggregationType().getType()));
      }
      Order o = new Order();
      o.setColumn(col);
      o.setOrderType(ord.isAscending() ? MqlOrder.Type.ASC : MqlOrder.Type.DESC);
      orders.add(o);
    }

    query.setOrders(orders);
    
    query.setLimit(fatQ.getLimit());

    return query;
  }

  public MQLQuery convertModel(MqlQuery query) {
    SchemaMeta meta = modelIdToSchemaMetaMap.get(query.getModel().getId());

    // TODO: Name isn't guaranteed to be unique, ID is... This should use ID.
    UniqueList<BusinessModel> models = meta.getBusinessModels();
    BusinessModel realModel = null;
    for (BusinessModel m : models) {
      if (m.getId().equals(query.getModel().getId())) {
        realModel = m;
        break;
      }
    }
    if (realModel != null) {

      MQLQuery mqlQuery = null;
      try {

        org.pentaho.pms.schema.BusinessColumn[] businessColumns = getColumns(realModel, query.getColumns());
        if (businessColumns.length > 0) {
          BusinessModel businessModel = realModel;
          mqlQuery = new MQLQueryImpl(meta, businessModel, null, meta.getActiveLocale());
          List<Selection> selections = new ArrayList<Selection>();
          for (int i = 0; i < businessColumns.length; i++) {
            selections.add(new Selection(businessColumns[i], businessColumns[i].getAggregationType()));
          }

          mqlQuery.setSelections(selections);
          MQLWhereConditionModel wherelist[] = getConditions(realModel, query.getConditions());
          ArrayList<WhereCondition> constraints = new ArrayList<WhereCondition>();
          BusinessCategory rootCat = businessModel.getRootCategory();
          //mqlQuery.setDisableDistinct(!this.distinctSelections.getSelection());
          for (int i = 0; i < wherelist.length; i++) {
            BusinessCategory businessCategory = rootCat.findBusinessCategoryForBusinessColumn(wherelist[i].getField());

            constraints.add(new WhereCondition(businessModel, wherelist[i].getOperator(), wherelist[i].getCondition()) //$NON-NLS-1$
                );
          }
          mqlQuery.setConstraints(constraints);
          mqlQuery.setOrder(getOrders(realModel, query.getOrders()));
          mqlQuery.setLimit(query.getLimit());
          
          return mqlQuery;
        }
      } catch (Throwable e) { // PMSFormulaException e) {
        e.printStackTrace();
      }

    } else {
      //throw error
    }
    return null;
  }

  public String serializeModel(MqlQuery uiQuery) {

    Query query = ModelUtil.convertUIModelToBean(uiQuery);

    return ModelSerializer.serialize(query);
  }

  public MqlQuery deserializeModel(String serializedQuery) {
    return ModelSerializer.deSerialize(serializedQuery);
  }

  private class MQLWhereConditionModel {

    private String operator; // AND

    private org.pentaho.pms.schema.BusinessColumn field; // customer_name

    private String condition; // = 'Casters'

    public MQLWhereConditionModel(String operator, org.pentaho.pms.schema.BusinessColumn field, String condition) {
      this.operator = operator;
      this.field = field;
      this.condition = condition;
    }

    public String getOperator() {
      return operator;
    }

    public org.pentaho.pms.schema.BusinessColumn getField() {
      return field;
    }

    public String getCondition() {
      return condition;
    }

    public void setCondition(String condition) {
      this.condition = condition;
    }

    public void setOperator(String operator) {
      this.operator = operator;
    }

  }

  
  
  public MqlQuery convertModelToThin(org.pentaho.metadata.query.model.Query query){
    Query q = new Query();
    
    String domainId = query.getDomain().getId();
    String modelId = query.getLogicalModel().getId();
    
    MqlDomain selectedDomain = null;
    MqlModel selectedModel = null;
    for(MqlDomain d : this.domains){
      if(d.getName().equals(domainId)){
        selectedDomain = d;
        break;
      }
    }
    if(selectedDomain == null){
      throw new IllegalStateException("Could not find domain: "+domainId);
    }
    q.setDomain((Domain) selectedDomain);
    for(MqlModel m : selectedDomain.getModels()){
      if(m.getId().equals(modelId)){
        selectedModel = m;
        break;
      }
    }
    if(selectedModel == null){
      throw new IllegalStateException("Could not find model: "+modelId); 
    }
    q.setModel((Model) selectedModel);
    List<Column> cols = q.getColumns();
    for(org.pentaho.metadata.query.model.Selection sel : query.getSelections()){
      Column c = (Column) convertNewThinColumn(selectedModel, sel.getLogicalColumn().getId());
      c.setSelectedAggType(convertNewThinAggregationType(sel.getAggregationType())); 
      cols.add(c);
    }
    
    for(org.pentaho.metadata.query.model.Constraint constraint : query.getConstraints()){
      FormulaParser fp = new FormulaParser(constraint.getFormula());
      
      Condition cond = fp.getCondition();
      Outter:
      for(MqlCategory cat : selectedModel.getCategories()){
        for(MqlColumn col : cat.getBusinessColumns()){
          if(col.getId().equals(fp.getColID())){
            cond.setColumn((Column) col);
            break Outter;
          }
        }
      }
      
      // PRD-3710
      if(fp.getAggType() != null){
        cond.setSelectedAggType(convertNewThinAggregationType(AggregationType.valueOf(fp.getAggType())));
      }
      
      cond.setCombinationType(CombinationType.valueOf(constraint.getCombinationType().name().toUpperCase()));
      String val = cond.getValue();
      
      // check to see if it was parameterized, if so resolve default and setup the condition properly
      if(val.indexOf("[param:") == 0){
        String paramKey = val.substring(7, val.length() -1);
        cond.setValue("{"+paramKey+"}");
        cond.setParameterized(true);
        for(Parameter p : query.getParameters()){
          if(p.getName().equals(paramKey)){
            // convert arrays back to vertical bar (pipe) delimited string to support multi-valued defaults
            cond.setDefaultValue(getDisplayableDefaultValue(p));
            break;
          }
        }
      }
      q.getConditions().add(cond);
    }
    
    for(org.pentaho.metadata.query.model.Order ord : query.getOrders()){
      Order o = new Order();
      o.setColumn((Column) convertNewThinColumn(selectedModel, ord.getSelection().getLogicalColumn().getId()));
      o.setOrderType(MqlOrder.Type.valueOf(ord.getType().toString().toUpperCase()));
      q.getOrders().add(o);
    }
    
    q.setLimit(query.getLimit());
    return q;
  }
  
  private AggType convertNewThinAggregationType(AggregationType aggregationType) {
    switch(aggregationType){
      case COUNT:
        return AggType.COUNT;
      case COUNT_DISTINCT:
        return AggType.COUNT_DISTINCT;
      case AVERAGE:
        return AggType.AVERAGE;
      case MINIMUM:
        return AggType.MIN;
      case MAXIMUM:
        return AggType.MAX;
      case SUM:
        return AggType.SUM;
      case NONE:
      default:
        return AggType.NONE;
    }
  }


  private MqlColumn convertNewThinColumn(MqlModel selectedModel, String colId){

    for(MqlCategory c : selectedModel.getCategories()){
      for(MqlColumn col : c.getBusinessColumns()){
        if(col.getId().equals(colId)){
          //PRD-3542 - clone the columns to allow for more than one field with different aggregations.
          if (col instanceof Column) {
            return ((Column)col).clone();
          } else {
            return col;
          }
        }
      }
    }
    
    return null;
  }

  private String getDisplayableDefaultValue(Parameter p) {
    if (p == null || p.getDefaultValue() == null) {
      return null;
    } else if (p.getDefaultValue() instanceof Object[]) {
      Object[] values = (Object[])p.getDefaultValue();
      StringBuilder sb = new StringBuilder();
      for (Object value : values) {
        if (sb.length() > 0) {
          sb.append("|");
        }
        if (value.toString().contains("|")) {
          sb.append("\"")
            .append(value)
            .append("\"");
        } else {
          sb.append(value);
        }
      }
      return sb.toString();
    } else {
      return p.getDefaultValue().toString();
    }
  }

}
