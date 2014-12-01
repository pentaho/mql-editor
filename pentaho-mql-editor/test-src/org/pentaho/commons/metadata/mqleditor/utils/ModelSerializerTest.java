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

package org.pentaho.commons.metadata.mqleditor.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlOrder.Type;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.Category;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.platform.util.JSONComparitor;


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
    column.setPersistent( true );

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
    cond.setCombinationType(null);
    cond.setOperator(Operator.EQUAL);
    cond.setDefaultValue("default");
    cond.setValue("myvalue1");
    conditions.add(cond);

    Condition cond2 = new Condition();
    cond2.setColumn(column);
    cond2.setCombinationType(CombinationType.OR);
    cond2.setOperator(Operator.IN);
    cond2.setValue("myvalue2|\"my value2\"|\"my;value 2\"|my;value2"); //$NON-NLS-1$
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

    Map<String, Object> defaultParameterMap = new HashMap<String, Object>();

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

		JSONObject jsoRef = null;
		JSONObject jso = null;
		String jsonRef = "{\"MQLQuery\":{\"cols\":[{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":[\"\"],\"persistent\":true}}],\"conditions\":[{\"org.pentaho.commons.metadata.mqleditor.beans.Condition\":[{\"condition\":{\"@combinationType\":\"\",\"@defaultValue\":\"default\",\"@operator\":\"=\",\"@selectedAggType\":\"\",\"@value\":\"myvalue1\",\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":[\"\"],\"persistent\":true}}},{\"condition\":{\"@combinationType\":\"OR\",\"@defaultValue\":\"null\",\"@operator\":\"in\",\"@selectedAggType\":\"\",\"@value\":\"myvalue2|\\\"my value2\\\"|\\\"my;value 2\\\"|my;value2\",\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":[\"\"],\"persistent\":true}}},{\"condition\":{\"@combinationType\":\"OR\",\"@defaultValue\":\"null\",\"@operator\":\"=\",\"@selectedAggType\":\"\",\"@value\":\"myparameter\",\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":[\"\"],\"persistent\":true}}}]}],\"orders\":[{\"org.pentaho.commons.metadata.mqleditor.beans.Order\":{\"column\":{\"id\":\"mycolumn\",\"name\":\"mycolumn\",\"type\":\"TEXT\",\"aggTypes\":[\"\"],\"persistent\":true},\"orderType\":\"ASC\"}}],\"limit\":-1,\"domain\":{\"@id\":\"mydomain\",\"@name\":\"mydomain\"},\"model\":{\"@id\":\"mymodel\",\"@name\":\"mymodel\"},\"defaultParameterMap\":[{\"entry\":{\"string\":[\"myparameter\",\"myvalue3\"]}}]}}";
		     
		try {
			boolean isEqual = JSONComparitor.jsonEqual(jsonRef, serialized, null);
			assertTrue(isEqual);
		} catch (JSONException e) {
			Assert.fail("Invalid JSON format");
		}
		
    MqlQuery deserialized = ModelSerializer.deSerialize(serialized);
        
    // spot check fields since Query doesn't implement equals!!!
    assertEquals(mqlQuery.getDomain().getName(), deserialized.getDomain().getName());
    assertEquals(mqlQuery.getModel().getName(), deserialized.getModel().getName());
    assertEquals(mqlQuery.getColumns().get(0).getId(), deserialized.getColumns().get(0).getId());
    assertEquals(mqlQuery.getColumns().get(0).isPersistent(), deserialized.getColumns().get(0).isPersistent());
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
