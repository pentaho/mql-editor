/*
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
 * Copyright (c) 2009 Pentaho Corporation.  All rights reserved.
 */
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
import org.pentaho.ui.xul.stereotype.Bindable;

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
  private List<UIColumn> selectedColumns;
  
  
  private UIColumns selections = new UIColumns();
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
        UIColumn c = findAndCloneColumn(col);
        if(c != null){
          selections.add(c);
        } else {
          System.out.println("could not find column");
        }
      }
    }
    
    if (thinWorkspace.getOrders() != null) {
      for( MqlOrder order : thinWorkspace.getOrders()){
        UIOrder ord = new UIOrder(order);
        ord.setColumn(findAndCloneColumn(order.getColumn()));
        orders.add(ord);
      }
    }
    if (thinWorkspace.getConditions() != null) {
      for( Condition condition : thinWorkspace.getConditions()){
        UICondition cond = new UICondition(condition);
        cond.setColumn(findAndCloneColumn(condition.getColumn()));
        conditions.add(cond);
      }
    }
  }
  
  @Bindable
  public void clear(){
    removeListeners();
    this.selectedColumn = null;
    this.selectedColumns = null;
    this.setOrders(new UIOrders());
    this.setSelections(new UIColumns());
    this.setConditions(new UIConditions());
    setupListeners();
  }
  
  private PropertyChangeListener columnListener = new PropertyChangeListener(){
    public void propertyChange(PropertyChangeEvent evt) {
      Workspace.this.firePropertyChange("selections", null, getSelections());
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
    selections.addPropertyChangeListener("children", columnListener);
    conditions.addPropertyChangeListener("children", conditionListener);
    orders.addPropertyChangeListener("children", orderListener);
  }

  public void removeListeners(){
    selections.removePropertyChangeListener(columnListener);
    conditions.removePropertyChangeListener(conditionListener);
    orders.removePropertyChangeListener(orderListener);
  }
  
  
  private UIColumn findAndCloneColumn(MqlColumn col){
    for(UICategory cat : this.selectedModel.getCategories()){
      for(UIColumn c : cat.getBusinessColumns()){
        if(c.getId().equals(col.getId())){
          UIColumn column = (UIColumn)c.clone();
          column.setSelectedAggType(col.getSelectedAggType());
          return column;
        }
      }
    }
    return null;
  }
  
  @Bindable
  public void setSelectedModel(UIModel m){
    UIModel prevVal = this.selectedModel;
    this.selectedModel = m;
    this.clear();
    this.firePropertyChange("selectedModel", prevVal, this.selectedModel);
    this.firePropertyChange("categories", null, getCategories());
  }
  
  @Bindable
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
  
  public List<UIColumn> getColumnsByPos(int[] positions){
    List<UIColumn> cols = new ArrayList<UIColumn>();
    for(int pos : positions){
      cols.add(getColumnByPos(pos));
    }
    return cols;
  }
  
  
  @Bindable
  public List<UICategory> getCategories() {
    
    return (this.selectedModel != null) ? this.selectedModel.getChildren() : null;
  }

  @Bindable
  public void setCategories(List<UICategory> categories) {
    this.categories = categories;
    this.firePropertyChange("categories", null, getCategories());
  }

  @Bindable
  public UICategory getSelectedCategory() {
    return selectedCategory;
  }

  @Bindable
  public void setSelectedCategory(UICategory selectedCategory) {
  
    List<UIColumn> prevColumns = getColumns();
    UICategory oldCat = this.selectedCategory;
    this.selectedCategory = selectedCategory;
    this.firePropertyChange("selectedCategory", oldCat, this.selectedCategory);
    this.firePropertyChange("columns", prevColumns, getColumns());
    
  }

  @Bindable
  public void setSelectedOrder(UIOrder selectedOrder) {
  
    UIOrder prevOrder = this.selectedOrder;
    this.selectedOrder = selectedOrder;
    this.firePropertyChange("selectedOrder", prevOrder, this.selectedOrder);
    
  }
  
  public List<UIColumn> getColumns(){
    return (this.selectedCategory != null) ? this.selectedCategory.getChildren() : null;
  }

  @Bindable
  public UIColumn getSelectedColumn() {
  
    return selectedColumn;
  }

  @Bindable
  public void setSelectedColumn(UIColumn selectedColumn) {
  
    UIColumn prevColumn = this.selectedColumn;
    this.selectedColumn = selectedColumn;
    this.firePropertyChange("selectedColumn", prevColumn, getSelectedColumn());
  }
  
  public void addColumn(UIColumn col){
    selections.add(col);
    
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
  
  
  @Bindable
  public UIColumns getSelections() {
  
    return selections;
  }

  @Bindable
  public void setSelections(UIColumns columns) {
    this.selections = columns;
    this.firePropertyChange("selections", null, getSelections());
  }

  @Bindable
  public void setSelectedColumns(List<UIColumn> selectedColumns) {
    List<UIColumn> prevSelected = this.selectedColumns;
    
    this.selectedColumns = selectedColumns;
    this.firePropertyChange("selectedColumns", prevSelected , selectedColumns);
  }
  
  @Bindable
  public List<UIColumn> getSelectedColumns() {
    return this.selectedColumns;
  }
  
  

  @Bindable
  public UIConditions getConditions() {
    return conditions;
  }

  @Bindable
  public void setConditions(UIConditions conditions) {
    this.conditions = conditions;
    this.firePropertyChange("conditions", null, getConditions());
  }

  @Bindable
  public UIOrders getOrders() {
    return orders;
  }

  @Bindable
  public void setOrders(UIOrders orders) {
    this.orders = orders;
    this.firePropertyChange("orders", null, getOrders());
  }

  @Bindable
  public MqlModel getModel() {
    return this.selectedModel;   
  }
  
  @Bindable
  protected void setModel(UIModel model){
    this.selectedModel = model;
  }

  @Bindable
  public UIDomain getSelectedDomain() {
    return selectedDomain;
  }

  @Bindable
  public void setSelectedDomain(UIDomain selectedDomain){
    UIDomain prevDomain = this.selectedDomain;
    this.selectedDomain = selectedDomain;
    this.clear();
    this.firePropertyChange("selectedDomain", prevDomain, selectedDomain);
  }

  @Bindable
  public MqlDomain getDomain(){
    return this.selectedDomain;
  }
  
 
  public MqlQuery getMqlQuery(){
    UIQuery query = new UIQuery();
    
    List<UIColumn> cols = new ArrayList<UIColumn>();
    List<UIOrder> orders = new ArrayList<UIOrder>();
    List<UICondition> conditions = new ArrayList<UICondition>();
    
    for(UIColumn col : this.selections){
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

  @Bindable
  public List<UIDomain> getDomains() {
    return domains;
  }

  @Bindable
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

  @Bindable
  public void setSelectedModelId(String modelId) {
    for (UIModel uiModel : selectedDomain.getModels()) {
      if (uiModel.getId().equals(modelId)) {
        setSelectedModel(uiModel);
        break;
      }
    }
  }

  @Bindable
  public void setSelectedDomainId(String domainId) {
    for (UIDomain uiDomain : domains) {
      if (uiDomain.getName().equals(domainId)) {
        setSelectedDomain(uiDomain);
        break;
      }
    }
  }
  
}

  