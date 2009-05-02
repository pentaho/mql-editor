package org.pentaho.commons.metadata.mqleditor.editor.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlModel;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.ui.xul.XulEventSourceAdapter;

/**
 *
 * Main state model for the Mql Editor dialog
 * 
 */
public class Workspace extends XulEventSourceAdapter implements MqlQuery {

  private UIModel model;
  private UIDomain selectedDomain;
  private List<UIDomain> domains;
  private List<UICategory> categories;
  private UICategory selectedCategory;
  private UIOrder selectedOrder;
  
  private UIColumn selectedColumn;
  
  private Columns selectedColumns = new Columns();
  private Conditions conditions = new Conditions();
  private Orders orders = new Orders();
  private String queryStr;

  private List<String> availableFilters = new ArrayList<String>();
  
  public Workspace(){
    setupListeners();
  }
  
 
  /*
   * Adopt all values of bean version of Query as UI-enabled "Workspace". 
   */
  public void wrap(Query thinWorkspace){

    // TODO mlowery need to validate incoming (deserialized) model by making sure that objects still exist on the server

    Domain domain = thinWorkspace.getDomain();
    if (domain != null) {
      for (UIDomain uiDomain : domains) {
        if (uiDomain.getName().equals(domain.getName())) {
          setSelectedDomain(uiDomain);
        }
      }
    }
    
    Model model = thinWorkspace.getModel();
    if (model != null) {
      for (UIModel uiModel : getSelectedDomain().getModels()) {
        if (uiModel.getId().equals(model.getId())) {
          setSelectedModel(uiModel);
        }
      }
    }

    
    if (thinWorkspace.getColumns() != null) {
      for( Column col : thinWorkspace.getColumns()){
        selectedColumns.add(UIColumn.wrap(col));
      }
    }
    if (thinWorkspace.getOrders() != null) {
      for( Order order : thinWorkspace.getOrders()){
        orders.add(UIOrder.wrap(order));
      }
    }
    if (thinWorkspace.getConditions() != null) {
      for( Condition condition : thinWorkspace.getConditions()){
        conditions.add(UICondition.wrap(condition));
      }
    }
  }
  
  public void clear(){
    this.setOrders(new Orders());
    this.setSelectedColumns(new Columns());
    this.setConditions(new Conditions());
    setupListeners();
  }
  
  public void setupListeners(){
    selectedColumns.addPropertyChangeListener("children", new PropertyChangeListener(){
      public void propertyChange(PropertyChangeEvent evt) {
        Workspace.this.firePropertyChange("selectedColumns", null, getSelectedColumns());
      }
    });
    conditions.addPropertyChangeListener("children", new PropertyChangeListener(){
      public void propertyChange(PropertyChangeEvent evt) {
        Workspace.this.firePropertyChange("conditions", null, getConditions());
      }
    });
    orders.addPropertyChangeListener("children", new PropertyChangeListener(){
      public void propertyChange(PropertyChangeEvent evt) {
        Workspace.this.firePropertyChange("orders", null, getOrders());
      }
    });
  }
  
  public void setSelectedModel(UIModel m){
    UIModel prevVal = this.model;
    this.model = m;

    this.firePropertyChange("selectedModel", prevVal, this.model);
    this.firePropertyChange("categories", null, getCategories());
  }
  
  public UIModel getSelectedModel(){
    return model;
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
    
    return (this.model != null) ? this.model.getChildren() : null;
  }

  public void setCategories(List<UICategory> categories) {
    this.categories = categories;
    this.firePropertyChange("categories", null, getCategories());
  }

  public UICategory getSelectedCategory() {
  
    return selectedCategory;
  }

  public void setSelectedCategory(UICategory selectedCategory) {
  
    this.selectedCategory = selectedCategory;
    this.firePropertyChange("selectedCategory", null, this.selectedCategory);
    this.firePropertyChange("columns", null, getColumns());
    
  }

  public void setSelectedOrder(UIOrder selectedOrder) {
  
    this.selectedOrder = selectedOrder;
    this.firePropertyChange("selectedOrder", null, this.selectedOrder);
    this.firePropertyChange("orders", null, getOrders());
    
  }
  
  public List<UIColumn> getColumns(){
    return (this.selectedCategory != null) ? this.selectedCategory.getChildren() : null;
  }

  public UIColumn getSelectedColumn() {
  
    return selectedColumn;
  }

  public void setSelectedColumn(UIColumn selectedColumn) {
  
    this.selectedColumn = selectedColumn;
    this.firePropertyChange("selectedColumn", null, getSelectedColumn());
  }
  
  public void addColumn(UIColumn col){
    if(selectedColumns.contains(col)){
      return;
    }
    selectedColumns.add(col);
    
  }

  public void addCondition(UIColumn col){
    
    UICondition condition = new UICondition();
    condition.setColumn(col);
    // Give it a list of the fitlers the MQL Editor was told are available in the outside application
    condition.setAvailableFilters(this.availableFilters);

    conditions.add(condition);
    
  }

  public void addOrder(UIColumn col){
    
    UIOrder order = new UIOrder();
    order.setColumn(col);
    orders.add(order);
    
  }
  
  
  public Columns getSelectedColumns() {
  
    return selectedColumns;
  }

  public void setSelectedColumns(Columns columns) {
    this.selectedColumns = columns;
    this.firePropertyChange("selectedColumns", null, getSelectedColumns());
  }

  public void setSelectedColumns(List<UIColumn> selectedColumns) {
    this.selectedColumns = new Columns(selectedColumns);
    this.firePropertyChange("selectedColumns", null, getSelectedColumns());
  }

  public Conditions getConditions() {
    return conditions;
  }

  public void setConditions(Conditions conditions) {
    this.conditions = conditions;
    this.firePropertyChange("conditions", null, getConditions());
  }

  public Orders getOrders() {
    return orders;
  }

  public void setOrders(Orders orders) {
    this.orders = orders;
    this.firePropertyChange("orders", null, getOrders());
  }

  public MqlModel getModel() {
    return this.model;   
  }
  
  protected void setModel(UIModel model){
    this.model = model;
  }

  public UIDomain getSelectedDomain() {
    return selectedDomain;
  }

  public void setSelectedDomain(UIDomain selectedDomain){
    UIDomain prevDomain = this.selectedDomain;
    this.selectedDomain = selectedDomain;

    this.firePropertyChange("selectedDomain", prevDomain, selectedDomain);
  }

  public MqlDomain getDomain(){
    return this.selectedDomain;
  }
  
 
  public Query getMqlQuery(){
    Query query = new Query();
    query.setColumns(this.selectedColumns.getBeanCollection());
    query.setConditions(this.conditions.getBeanCollection());
    query.setOrders(orders.getBeanCollection());
    query.setMqlStr(this.getMqlStr());
    query.setDomain(this.selectedDomain.getBean());
    query.setModel(this.model.getBean());


    Map<String, String> params = new HashMap<String, String>();
    for(UICondition c : this.conditions){
      if(c.isParameterized()){
        params.put(c.getValue().replaceAll("[\\{\\}]", ""), c.getDefaultValue());
      }
    }
    query.setDefaultParameterMap(params);

    return query;
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
    this.domains = domains;
    this.firePropertyChange("domains", null, domains); //$NON-NLS-1$
  }


  public Map<String, String> getDefaultParameterMap() {
    // TODO mlowery not sure what goes here
    throw new UnsupportedOperationException();
  }
  

  public void setAvailableFilters(List<String> availableFilters) {
    // Must conform to \{\w*\}$

    List<String> newParams = new ArrayList<String>();
    for(String param : availableFilters){
      if(param.matches("\\{[\\w*]$\\}")){
        newParams.add(param);
      } else {
        newParams.add("{"+param+"}");
      }
    }
    this.availableFilters = newParams;
  }

  public List<String> getAvailableFilters() {
    return availableFilters;
  }
  
}

  