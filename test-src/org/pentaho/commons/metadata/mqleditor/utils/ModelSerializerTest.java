package org.pentaho.commons.metadata.mqleditor.utils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.MqlOrder.Type;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessTable;
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

    BusinessTable table = new BusinessTable();
    table.setId("mytable");
    table.setName("mytable");
    column.setTable(table);

    columns.add(column);
    cat.setBusinessColumns(columns);

    categories.add(cat);
    model.setCategories(categories);

    models.add(model);
    domain.setModels(models);

    mqlQuery.setDomain(domain);

    mqlQuery.setModel(model);

    mqlQuery.setCols(columns);

    List<Condition> conditions = new ArrayList<Condition>();
    Condition cond = new Condition();
    cond.setColumn(column);
    cond.setCombinationType(CombinationType.OR);
    cond.setOperator(Operator.EQUAL);
    cond.setValue("myvalue1");
    conditions.add(cond);

    Condition cond2 = new Condition();
    cond2.setColumn(column);
    cond2.setCombinationType(CombinationType.OR);
    cond2.setOperator(Operator.EQUAL);
    cond2.setValue("myvalue2");
    conditions.add(cond2);

    mqlQuery.setConditions(conditions);

    List<Order> orders = new ArrayList<Order>();
    Order order = new Order();
    order.setColumn(column);
    order.setOrderType(Type.ASC);
    orders.add(order);

    mqlQuery.setOrders(orders);

  }

  @After
  public void tearDown() throws Exception {
    mqlQuery = null;
  }

  @Test
  public void testSerializeAndDeSerialize() {
    String serialized = ModelSerializer.serialize(mqlQuery);
    MqlQuery deserialized = ModelSerializer.deSerialize(serialized);
    // spot check fields since Query doesn't implement equals!!!
    assertEquals(mqlQuery.getDomain().getName(), deserialized.getDomain().getName());
    assertEquals(mqlQuery.getColumns().get(0).getId(), deserialized.getColumns().get(0).getId());
    assertEquals(mqlQuery.getOrders().get(0).getColumn().getName(), deserialized.getOrders().get(0).getColumn()
        .getName());
    assertEquals(mqlQuery.getConditions().get(0).getColumn().getId(), deserialized.getConditions().get(0).getColumn()
        .getId());

  }

}
