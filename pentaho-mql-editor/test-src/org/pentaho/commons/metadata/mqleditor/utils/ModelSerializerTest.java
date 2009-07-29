package org.pentaho.commons.metadata.mqleditor.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.MqlOrder.Type;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;

/**
 * Unit test for {@link ModelSerializer}.
 * 
 * @author mlowery
 */
public class ModelSerializerTest {

  private Query mqlQuery;

  @Before
  public void setUp() throws Exception {
    mqlQuery = new Query();

    Domain domain = new Domain();
    domain.setId("mydomain");
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
    cond.setOperator(Operator.EQUAL);
    cond.setDefaultValue("default");
    cond.setValue("myvalue1");
    conditions.add(cond);

    Condition cond2 = new Condition();
    cond2.setColumn(column);
    cond2.setCombinationType(CombinationType.OR);
    cond2.setOperator(Operator.EQUAL);
    cond2.setValue("myvalue2");
    conditions.add(cond2);

    Condition cond3 = new Condition();
    cond3.setParameterized(true);
    cond3.setColumn(column);
    cond3.setCombinationType(CombinationType.OR);
    cond3.setOperator(Operator.EQUAL);
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

    defaultParameterMap.put("myparameter", "myvalue3");
    mqlQuery.setDefaultParameterMap(defaultParameterMap);
  }

  @After
  public void tearDown() throws Exception {
    mqlQuery = null;
  }

  @Test
  public void testSerializeAndDeSerialize() {
    
    String serialized = ModelSerializer.serialize(mqlQuery);
    System.out.println(serialized);
    
    MqlQuery deserialized = ModelSerializer.deSerialize(serialized);
    //MqlQuery deserialized = ModelSerializer.deSerialize("{\"MQLQuery\":{\"cols\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":\"\"}]},\"conditions\":[{\"org.pentaho.commons.metadata.mqleditor.beans.Condition\":[{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":\"\"},\"operator\":[\"EQUAL\"],\"value\":\"myvalue1\",\"comboType\":\"OR\",\"parameterized\":false},{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":\"\"},\"operator\":[\"EQUAL\"],\"value\":\"myvalue2\",\"comboType\":\"OR\",\"parameterized\":false},{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":\"\"},\"operator\":[\"EQUAL\"],\"value\":\"myparameter\",\"comboType\":\"OR\",\"parameterized\":true}]}],\"orders\":{\"org.pentaho.commons.metadata.mqleditor.beans.Order\":[{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":\"\"},\"orderType\":[\"ASC\"]}]},\"domain\":{\"id\":\"mydomain\",\"name\":\"mydomain\",\"models\":{\"org.pentaho.commons.metadata.mqleditor.beans.Model\":[{\"categories\":{\"org.pentaho.commons.metadata.mqleditor.beans.Category\":[{\"id\":\"mycategory\",\"name\":\"mycategory\",\"columns\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":\"\"}]}}]},\"id\":[\"mymodel\"],\"name\":\"mymodel\"}]}},\"model\":{\"categories\":{\"org.pentaho.commons.metadata.mqleditor.beans.Category\":[{\"id\":\"mycategory\",\"name\":\"mycategory\",\"columns\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":\"\"}]}}]},\"id\":[\"mymodel\"],\"name\":\"mymodel\"}}}");
    

    
    // spot check fields since Query doesn't implement equals!!!
    assertEquals(mqlQuery.getDomain().getName(), deserialized.getDomain().getName());
    assertEquals(mqlQuery.getModel().getName(), deserialized.getModel().getName());
    assertEquals(mqlQuery.getColumns().get(0).getId(), deserialized.getColumns().get(0).getId());
    assertEquals(mqlQuery.getOrders().get(0).getColumn().getName(), deserialized.getOrders().get(0).getColumn()
        .getName());
    assertEquals(mqlQuery.getConditions().get(0).getColumn().getId(), deserialized.getConditions().get(0).getColumn()
        .getId());
    assertEquals(mqlQuery.getConditions().get(0).isParameterized(), deserialized.getConditions().get(0)
        .isParameterized());
    assertEquals(mqlQuery.getConditions().get(0).getDefaultValue(), deserialized.getConditions().get(0)
        .getDefaultValue());

    assertEquals(mqlQuery.getConditions().get(0).getCombinationType(), deserialized.getConditions().get(0).getCombinationType());
    
    assertNotNull(mqlQuery.getDefaultParameterMap().get("myparameter"));
    assertEquals(mqlQuery.getDefaultParameterMap().get("myparameter"), deserialized.getDefaultParameterMap().get("myparameter"));
  }

}
