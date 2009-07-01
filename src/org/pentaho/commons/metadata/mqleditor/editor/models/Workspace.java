package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlModel;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.utils.ModelUtil;
import org.pentaho.ui.xul.XulEventSourceAdapter;

/**
 *
 * Main state model for the Mql Editor dialog
 * 
 */
public class Workspace extends XulEventSourceAdapter implements MqlQuery {

  private UIModel selectedModel;
  private UIDomain selectedDomain;
  private List<UIDomain> domains;
  private List<UICategory> categories;
  private UICategory selectedCategory;
  private UIOrder selectedOrder;
  
  private UIColumn selectedColumn;
  
  private UIColumns selectedColumns = new UIColumns();
  private UIConditions conditions = new UIConditions();
  private UIOrders orders = new UIOrders();
  private String queryStr;
  
  public Workspace(){
    setupListeners();
  }
  
 
  /*
   * Adopt all values of bean version of Query as UI-enabled "Workspace". 
   */
  public void wrap(Query thinWorkspace) {

    // TODO mlowery need to validate incoming (deserialized) model by making sure that objects still exist on the server

    MqlDomain domain = thinWorkspace.getDomain();
    if (domain == null) {
      return;
    }
    
    for (UIDomain uiDomain : domains) {
      if (uiDomain.getName().equals(domain.getName())) {
        setSelectedDomain(uiDomain);
        break;
      }
    }
    
    if (selectedDomain == null) {
      return;
    }
    
    MqlModel model = thinWorkspace.getModel();
    if (model == null) {
      return;
    }
    
    for (UIModel uiModel : selectedDomain.getModels()) {
      if (uiModel.getId().equals(model.getId())) {
        setSelectedModel(uiModel);
        break;
      }
    }
    
    if (selectedModel == null) {
      return;
    }
    
    if (thinWorkspace.getColumns() != null) {
      for( MqlColumn col : thinWorkspace.getColumns()){
        UIColumn c = findColumn(col);
        if(c != null){
          selectedColumns.add(c);
        } else {
          System.out.println("could not find column");
        }
      }
    }
    
    if (thinWorkspace.getOrders() != null) {
      for( MqlOrder order : thinWorkspace.getOrders()){
        UIOrder ord = new UIOrder(order);
        ord.setColumn(findColumn(order.getColumn()));
        orders.add(ord);
      }
    }
    if (thinWorkspace.getConditions() != null) {
      for( Condition condition : thinWorkspace.getConditions()){
        UICondition cond = new UICondition(condition);
        cond.setColumn(findColumn(condition.getColumn()));
        conditions.add(cond);
      }
    }
  }
  
  public void clear(){
    removeListeners();
    this.setOrders(new UIOrders());
    this.setSelectedColumns(new UIColumns());
    this.setConditions(new UIConditions());
    setupListeners();
  }
  
  private PropertyChangeListener columnListener = new PropertyChangeListener(){
    public void propertyChange(PropertyChangeEvent evt) {
      Workspace.this.firePropertyChange("selectedColumns", null, getSelectedColumns());
    }
  };
    
  private PropertyChangeListener conditionListener =  new PropertyChangeListener(){
    public void propertyChange(PropertyChangeEvent evt) {
      Workspace.this.firePropertyChange("conditions", null, getConditions());
    }
  };
  
  private PropertyChangeListener orderListener = new PropertyChangeListener(){
    public void propertyChange(PropertyChangeEvent evt) {
      Workspace.this.firePropertyChange("orders", null, getOrders());
    }
  };
  
  public void setupListeners(){
    selectedColumns.addPropertyChangeListener("children", columnListener);
    conditions.addPropertyChangeListener("children", conditionListener);
    orders.addPropertyChangeListener("children", orderListener);
  }

  public void removeListeners(){
    selectedColumns.removePropertyChangeListener(columnListener);
    conditions.removePropertyChangeListener(conditionListener);
    orders.removePropertyChangeListener(orderListener);
  }
  
  
  private UIColumn findColumn(MqlColumn col){
    for(UICategory cat : this.selectedModel.getCategories()){
      for(UIColumn c : cat.getBusinessColumns()){
        if(c.getId().equals(col.getId())){
          return c;
        }
      }
    }
    return null;
  }
  
  public void setSelectedModel(UIModel m){
    UIModel prevVal = this.selectedModel;
    this.selectedModel = m;
    this.clear();
    this.firePropertyChange("selectedModel", prevVal, this.selectedModel);
    this.firePropertyChange("categories", null, getCategories());
  }
  
  public UIModel getSelectedModel(){
    return selectedModel;
  }
  
  public UIColumn getColumnByPos(int pos){
    if(pos < 0 || getSelectedModel() == null){
      return null;
    }
    List<UICategory> children = getSelectedModel().getChildren();
    int curpos = 0;
    for(UICategory child : children){
      if(child.getChildren().size() + 1 +curpos > pos){
        //within this node
        int posInNode = pos - curpos - 1;
        if(posInNode < 0){
          //node was selected
          return null;
        }
        return child.getChildren().get(posInNode);
      } else {
        curpos += child.getChildren().size() + 1;
      }
    }
    return null;
  }
  
  
  public List<UICategory> getCategories() {
    
    return (this.selectedModel != null) ? this.selectedModel.getChildren() : null;
  }

  public void setCategories(List<UICategory> categories) {
    this.categories = categories;
    this.firePropertyChange("categories", null, getCategories());
  }

  public UICategory getSelectedCategory() {
  
    return selectedCategory;
  }

  public void setSelectedCategory(UICategory selectedCategory) {
  
    List<UIColumn> prevColumns = getColumns();
    UICategory oldCat = this.selectedCategory;
    this.selectedCategory = selectedCategory;
    this.firePropertyChange("selectedCategory", oldCat, this.selectedCategory);
    this.firePropertyChange("columns", prevColumns, getColumns());
    
  }

  public void setSelectedOrder(UIOrder selectedOrder) {
  
    UIOrder prevOrder = this.selectedOrder;
    this.selectedOrder = selectedOrder;
    this.firePropertyChange("selectedOrder", prevOrder, this.selectedOrder);
    
  }
  
  public List<UIColumn> getColumns(){
    return (this.selectedCategory != null) ? this.selectedCategory.getChildren() : null;
  }

  public UIColumn getSelectedColumn() {
  
    return selectedColumn;
  }

  public void setSelectedColumn(UIColumn selectedColumn) {
  
    UIColumn prevColumn = this.selectedColumn;
    this.selectedColumn = selectedColumn;
    this.firePropertyChange("selectedColumn", prevColumn, getSelectedColumn());
  }
  
  public void addColumn(UIColumn col){
    selectedColumns.add(col);
    
  }

  public void addCondition(UIColumn col){
    
    UICondition condition = new UICondition();
    condition.setColumn(col);

    conditions.add(condition);
  }

  public void addOrder(UIColumn col){
    
    UIOrder order = new UIOrder();
    order.setColumn(col);
    orders.add(order);
    
  }
  
  
  public UIColumns getSelectedColumns() {
  
    return selectedColumns;
  }

  public void setSelectedColumns(UIColumns columns) {
    this.selectedColumns = columns;
    this.firePropertyChange("selectedColumns", null, getSelectedColumns());
  }

  public void setSelectedColumns(List<UIColumn> selectedColumns) {
    this.selectedColumns = new UIColumns(selectedColumns);
    this.firePropertyChange("selectedColumns", null, getSelectedColumns());
  }

  public UIConditions getConditions() {
    return conditions;
  }

  public void setConditions(UIConditions conditions) {
    this.conditions = conditions;
    this.firePropertyChange("conditions", null, getConditions());
  }

  public UIOrders getOrders() {
    return orders;
  }

  public void setOrders(UIOrders orders) {
    this.orders = orders;
    this.firePropertyChange("orders", null, getOrders());
  }

  public MqlModel getModel() {
    return this.selectedModel;   
  }
  
  protected void setModel(UIModel model){
    this.selectedModel = model;
  }

  public UIDomain getSelectedDomain() {
    return selectedDomain;
  }

  public void setSelectedDomain(UIDomain selectedDomain){
    UIDomain prevDomain = this.selectedDomain;
    this.selectedDomain = selectedDomain;
    this.clear();
    this.firePropertyChange("selectedDomain", prevDomain, selectedDomain);
  }

  public MqlDomain getDomain(){
    return this.selectedDomain;
  }
  
 
  public MqlQuery getMqlQuery(){
    UIQuery query = new UIQuery();
    
    List<UIColumn> cols = new ArrayList<UIColumn>();
    List<UIOrder> orders = new ArrayList<UIOrder>();
    List<UICondition> conditions = new ArrayList<UICondition>();
    
    for(UIColumn col : this.selectedColumns){
      cols.add(col);
    }

    for(UIOrder order : this.orders){
      orders.add(order);
    }

    for(UICondition condition : this.conditions){
      conditions.add(condition);
    }
    
    query.setColumns(cols);
    query.setOrders(orders);
    query.setConditions(conditions);
    
    query.setMqlStr(this.getMqlStr());
    query.setDomain(this.selectedDomain);
    query.setModel(this.selectedModel);

    return ModelUtil.convertUIModelToBean(query);
  }
  

  
  public String getMqlStr() {
    return queryStr;
  }
  
  public void setMqlStr(String query){
    this.queryStr = query;
  }

  public List<UIDomain> getDomains() {
    return domains;
  }

  public void setDomains(List<UIDomain> domains) {
    List<UIDomain> oldDomains = this.domains;
    this.domains = domains;
    if(oldDomains != domains){
      this.firePropertyChange("domains", null, domains); //$NON-NLS-1$
    }
  }
  
  public void addDomain(UIDomain domain) {
    this.domains.add(domain);
    this.firePropertyChange("domains", null, domains);
  }

  public Map<String, String> getDefaultParameterMap() {
    // TODO mlowery not sure what goes here
    throw new UnsupportedOperationException();
  }

  public void setSelectedModelId(String modelId) {
    for (UIModel uiModel : selectedDomain.getModels()) {
      if (uiModel.getId().equals(modelId)) {
        setSelectedModel(uiModel);
        break;
      }
    }
  }

  public void setSelectedDomainId(String domainId) {
    for (UIDomain uiDomain : domains) {
      if (uiDomain.getName().equals(domainId)) {
        setSelectedDomain(uiDomain);
        break;
      }
    }
  }
  
}

  