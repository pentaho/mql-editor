package org.pentaho.commons.metadata.mqleditor;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.MqlOrder.Type;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumn;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIColumns;
import org.pentaho.commons.metadata.mqleditor.editor.models.UICondition;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIConditions;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIDomain;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIOrder;
import org.pentaho.commons.metadata.mqleditor.editor.models.UIOrders;
import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.CWMStartup;
import org.pentaho.commons.metadata.mqleditor.editor.service.util.MQLEditorServiceCWMDelegate;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.factory.CwmSchemaFactory;

public class WorkspaceTest {

  private Query mqlQuery;
  private MQLEditorServiceCWMDelegate service;
  Workspace workspace;
  
  @Before
  public void setUp() throws Exception {
    mqlQuery = new Query();

    Domain domain = new Domain();
    domain.setName("mydomain");

    List<Model> models = new ArrayList<Model>();
    Model model = new Model();
    model.setId("mymodel");
    model.setName("mymodel");

    List<Category> categories = new ArrayList<Category>();
    Category cat = new Category();
    cat.setId("mycategory");
    cat.setName("mycategory");

    List<Column> columns = new ArrayList<Column>();
    Column column = new Column();
    column.setId("mycolumn");
    column.setName("mycolumn");
    column.setType(ColumnType.TEXT);

    columns.add(column);
    cat.setBusinessColumns(columns);

    categories.add(cat);
    model.setCategories(categories);

    models.add(model);
    domain.setModels(models);

    mqlQuery.setDomain(domain);

    mqlQuery.setModel(model);

    mqlQuery.setColumns(columns);

    List<Condition> conditions = new ArrayList<Condition>();
    Condition cond = new Condition();
    cond.setColumn(column);
    cond.setCombinationType(CombinationType.OR);
    cond.setOperator(Operator.BEGINS_WITH);
    cond.setValue("myvalue1");
    conditions.add(cond);

    Condition cond2 = new Condition();
    cond2.setColumn(column);
    cond2.setCombinationType(CombinationType.OR);
    cond2.setOperator(Operator.CONTAINS);
    cond2.setValue("myvalue2");
    conditions.add(cond2);

    Condition cond3 = new Condition();
    cond3.setParameterized(true);
    cond3.setColumn(column);
    cond3.setCombinationType(CombinationType.OR);
    cond3.setOperator(Operator.EXACTLY_MATCHES);
    
    cond3.setValue("myparameter");
    conditions.add(cond3);

    mqlQuery.setConditions(conditions);

    List<Order> orders = new ArrayList<Order>();
    Order order = new Order();
    order.setColumn(column);
    order.setOrderType(Type.ASC);
    orders.add(order);

    mqlQuery.setOrders(orders);

    Map<String, String> defaultParameterMap = new HashMap<String, String>();
    
    CWMStartup.loadCWMInstance("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/repository.properties", "/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWM cwm = CWMStartup.loadMetadata("/org/pentaho/commons/metadata/mqleditor/sampleMql/metadata_steelwheels.xmi", "/org/pentaho/commons/metadata/mqleditor/sampleMql"); //$NON-NLS-1$ //$NON-NLS-2$

    CwmSchemaFactory factory = new CwmSchemaFactory();
    
    List<CWM> cwms = new ArrayList<CWM>();
    cwms.add(cwm);
    
    service = new MQLEditorServiceCWMDelegate(cwms, factory);

    workspace = new Workspace();
    
    List<UIDomain> uiDomains = new ArrayList<UIDomain>();
    uiDomains.add(new UIDomain(domain));
    
    workspace.setDomains(uiDomains);
    
    
    workspace.wrap(mqlQuery);
    
    
  }

  @After
  public void tearDown() throws Exception {
    mqlQuery = null;
  }


  @Test
  public void testSelectedDomainAndModel() {

    assertEquals(mqlQuery.getDomain().getId(), workspace.getSelectedDomain().getId());
    assertEquals(mqlQuery.getModel().getId(), workspace.getSelectedModel().getId());
    
  }

  @Test
  public void testSelections() {
    
    // Test Conditions
    UIColumns outColumns = workspace.getSelectedColumns();
    
    assertEquals(outColumns.size(), mqlQuery.getColumns().size());
    
    for(int i=0; i<outColumns.size(); i++){
      UIColumn uiCol = outColumns.get(i);
      Column inCol = mqlQuery.getColumns().get(i);
      assertEquals(uiCol.getId(), inCol.getId());
      assertEquals(uiCol.getSelectedAggType(), inCol.getSelectedAggType());
      
      for(int z=0; z< uiCol.getAggTypes().size(); z++){
        assertEquals(uiCol.getAggTypes().get(z), inCol.getAggTypes().get(z));
      }
    }
    
  }
  
  @Test
  public void testWorkspaceConditions() {
    UIConditions outConditions = workspace.getConditions();
    
    assertEquals(outConditions.size(), mqlQuery.getConditions().size());
    
    for(int i=0; i<outConditions.size(); i++){
      UICondition uiCond = outConditions.get(i);
      Condition inCond = mqlQuery.getConditions().get(i);
      assertEquals(uiCond.getColumn().getId(), inCond.getColumn().getId());
      assertEquals(uiCond.getCombinationType(), inCond.getCombinationType());
      
      assertEquals(uiCond.getValue(), inCond.getValue());
      assertEquals(uiCond.getOperator(), inCond.getOperator());
      assertEquals(uiCond.getSelectedAggType(), inCond.getSelectedAggType());
    }
    
  }

  @Test
  public void testWorkspaceOrders() {
    
    UIOrders outOrders = workspace.getOrders();
    
    assertEquals(outOrders.size(), mqlQuery.getOrders().size());
    
    for(int i=0; i<outOrders.size(); i++){
      UIOrder uiOrd = outOrders.get(i);
      Order inOrd = mqlQuery.getOrders().get(i);
      assertEquals(uiOrd.getColumn().getId(), inOrd.getColumn().getId());
      assertEquals(uiOrd.getOrderType(), inOrd.getOrderType());
      assertEquals(uiOrd.getSelectedAggType(), inOrd.getSelectedAggType());
      
    }
    
  }
}
