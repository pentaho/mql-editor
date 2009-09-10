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
package org.pentaho.commons.metadata.mqleditor.utils;

import org.apache.commons.lang.StringUtils;
import org.pentaho.commons.metadata.mqleditor.AggType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.Operator;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Condition;
import org.pentaho.commons.metadata.mqleditor.beans.Domain;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Query;


public class ModelSerializer {
  private static XStream xstreamWriter = new XStream(new JettisonMappedXmlDriver());
  
  static{
    xstreamWriter.setMode(XStream.NO_REFERENCES);
    xstreamWriter.alias("MQLQuery", Query.class); //$NON-NLS-1$
    xstreamWriter.useAttributeFor(CombinationType.class);
    xstreamWriter.registerConverter(new ConditionConverter());
    xstreamWriter.registerConverter(new DomainConverter());
    xstreamWriter.registerConverter(new ModelConverter());
    
  }
  public static String serialize(MqlQuery model){
    Query q = (Query) model;
    if(q.getColumns().size() == 0){
      q.setColumns(null);
    }
    if(q.getConditions().size() == 0){
      q.setConditions(null);
    }
    if(q.getOrders().size() == 0){
      q.setOrders(null);
    }
    
    return xstreamWriter.toXML(model);
  }
  
  public static MqlQuery deSerialize(String input){
    try{
      return (MqlQuery) xstreamWriter.fromXML(input);
    } catch(Exception e){
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return null;
  }
  
  public static void main(String[] args){
    MqlQuery query = ModelSerializer.deSerialize(
        
    "{\"MQLQuery\":{\"cols\":{\"org.pentaho.commons.metadata.mqleditor.beans.Column\":[{\"id\":\"BC_CUSTOMER_W_TER_CUSTOMERNUMBER\",\"name\":\"Customernumber\",\"type\":\"FLOAT\",\"aggTypes\":\"\"},{\"id\":\"BC_ORDERDETAILS_TOTAL\",\"name\":\"Total\",\"aggTypes\":\"\"},{\"id\":\"BC_ORDERS_STATUS\",\"name\":\"Status\",\"type\":\"TEXT\",\"aggTypes\":\"\"}]},\"conditions\":[{\"org.pentaho.commons.metadata.mqleditor.beans.Condition\":[{\"condition\":{\"@combinationType\":\"AND\",\"@defaultValue\":\"\",\"@operator\":\"=\",\"@selectedAggType\":\"\",\"@value\":\"131\",\"column\":{\"id\":\"BC_CUSTOMER_W_TER_CUSTOMERNUMBER\",\"name\":\"Customernumber\",\"type\":\"FLOAT\",\"aggTypes\":\"\"}}},{\"condition\":{\"@combinationType\":\"OR\",\"@defaultValue\":\"\",\"@operator\":\"=\",\"@selectedAggType\":\"\",\"@value\":\"145\",\"column\":{\"id\":\"BC_CUSTOMER_W_TER_CUSTOMERNUMBER\",\"name\":\"Customernumber\",\"type\":\"FLOAT\",\"aggTypes\":\"\"}}}]}],\"domain\":{\"@id\":\"default\",\"@name\":\"steel-wheels\"},\"model\":{\"@id\":\"BV_ORDERS\",\"@name\":\"Orders\"}}}"
        
    
    );
    System.out.println("val: "+query.getConditions().get(0).getValue());
    int i=0;
  }
   
  private static class DummyConverter implements Converter{

    public void marshal(Object arg0, HierarchicalStreamWriter arg1,
        MarshallingContext arg2) {
    }

    public Object unmarshal(HierarchicalStreamReader arg0,
        UnmarshallingContext arg1) {
      return null;
    }

    public boolean canConvert(Class arg0) {
      return true;
    }
    
  }
  
  private static class ConditionConverter implements Converter{
    public boolean canConvert(Class clazz) {
      return clazz.equals(Condition.class);
    }
    
    public void marshal(Object value, HierarchicalStreamWriter writer,
                  MarshallingContext context) {
          Condition condition = (Condition) value;
          
          writer.startNode("condition");
          writer.addAttribute("combinationType", condition.getCombinationType() != null ? condition.getCombinationType().toString() : "");
          writer.addAttribute("defaultValue", condition.getDefaultValue());
          writer.addAttribute("operator", condition.getOperator().toString());
          writer.addAttribute("selectedAggType", condition.getSelectedAggType() != null ? condition.getSelectedAggType().toString() : "");
          writer.addAttribute("value", condition.getValue());
          writer.startNode("column");
          context.convertAnother(condition.getColumn());
          writer.endNode();
          writer.endNode();
    }
    
    public Object unmarshal(HierarchicalStreamReader reader,
                  UnmarshallingContext context) {
          reader.moveDown();
          Condition condition = new Condition();
          String combinationType = reader.getAttribute("combinationType");
          condition.setCombinationType(StringUtils.isNotEmpty(combinationType) ? CombinationType.valueOf(combinationType) : null);
          String aggType = reader.getAttribute("selectedAggType");
          condition.setSelectedAggType(StringUtils.isNotEmpty(aggType) ? AggType.valueOf(aggType) : null);
          condition.setOperator(Operator.parse(reader.getAttribute("operator")));
          condition.setDefaultValue(reader.getAttribute("defaultValue"));
          condition.setValue(reader.getAttribute("value"));
          reader.moveDown();
          Column col = (Column) context.convertAnother(condition, Column.class);
          reader.moveUp();
          condition.setColumn(col);
          reader.moveUp();
          
          return condition;
    }
  }
  
  private static class DomainConverter implements Converter{
    public boolean canConvert(Class clazz) {
      return clazz.equals(Domain.class);
    }
    
    public void marshal(Object value, HierarchicalStreamWriter writer,
                  MarshallingContext context) {
      Domain domain = (Domain) value;
         
      writer.addAttribute("id", domain.getId());
      writer.addAttribute("name", domain.getName());
    }
    
    public Object unmarshal(HierarchicalStreamReader reader,
                  UnmarshallingContext context) {
          Domain domain = new Domain();
          domain.setId(reader.getAttribute("id"));
          domain.setName(reader.getAttribute("name"));
          return domain;
    }
  }
  
  private static class ModelConverter implements Converter{
    public boolean canConvert(Class clazz) {
      return clazz.equals(Model.class);
    }
    
    public void marshal(Object value, HierarchicalStreamWriter writer,
                  MarshallingContext context) {
      Model model = (Model) value;
          
      writer.addAttribute("id", model.getId());
      writer.addAttribute("name", model.getName());
    }
    
    public Object unmarshal(HierarchicalStreamReader reader,
                  UnmarshallingContext context) {
          Model model = new Model();
          model.setId(reader.getAttribute("id"));
          model.setName(reader.getAttribute("name"));
          return model;
    }
  }
  
}
