package org.pentaho.metadata.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.pentaho.metadata.editor.models.Columns;
import org.pentaho.metadata.editor.models.Conditions;
import org.pentaho.metadata.editor.models.Orders;
import org.pentaho.metadata.editor.models.UIBusinessColumn;
import org.pentaho.metadata.editor.models.UICategory;
import org.pentaho.metadata.editor.models.UICondition;
import org.pentaho.metadata.editor.models.UIDomain;
import org.pentaho.metadata.editor.models.UIModel;
import org.pentaho.metadata.editor.models.UIOrder;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class Workspace extends XulEventSourceAdapter{

  private UIModel model;
  private UIDomain domain;
  private List<UICategory> categories;
  private UICategory selectedCategory;
  private UIOrder selectedOrder;
  
  private UIBusinessColumn selectedColumn;
  
  private Columns selectedColumns = new Columns();
  private Conditions conditions = new Conditions();
  private Orders orders = new Orders();
  private String query;
  
  public Workspace(){
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
    this.model = m;

    this.firePropertyChange("model", null, this.selectedCategory);
    this.firePropertyChange("categories", null, getCategories());
  }
  
  public UIModel getSelectedModel(){
    return model;
  }
  
  public UIBusinessColumn getColumnByPos(int pos){
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

  public UIDomain getDomain() {
  
    return domain;
  }

  public void setDomain(UIDomain domain) {
  
    this.domain = domain;
    this.firePropertyChange("domain", null, domain);
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
  
  public List<UIBusinessColumn> getColumns(){
    return (this.selectedCategory != null) ? this.selectedCategory.getChildren() : null;
  }

  public UIBusinessColumn getSelectedColumn() {
  
    return selectedColumn;
  }

  public void setSelectedColumn(UIBusinessColumn selectedColumn) {
  
    this.selectedColumn = selectedColumn;
    this.firePropertyChange("selectedColumn", null, getSelectedColumn());
  }
  
  public void addColumn(UIBusinessColumn col){
    if(selectedColumns.contains(col)){
      return;
    }
    selectedColumns.add(col);
    this.firePropertyChange("selectedColumns", null, getSelectedColumns());
    
  }

  public void addCondition(UIBusinessColumn col){
    
    UICondition condition = new UICondition();
    condition.setColumn(col);
    conditions.add(condition);
    this.firePropertyChange("conditions", null, getConditions());
    
  }

  public void addOrder(UIBusinessColumn col){
    
    UIOrder order = new UIOrder();
    order.setColumn(col);
    orders.add(order);
    this.firePropertyChange("orders", null, getOrders());
    
  }
  
  
  public Columns getSelectedColumns() {
  
    return selectedColumns;
  }

  public void setSelectedColumns(List<UIBusinessColumn> selectedColumns) {
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

  public void setMqlQuery(String query) {
    this.query = query;
  }
  
  public String getMqlQuery(){
    return this.query;
  }
}

  