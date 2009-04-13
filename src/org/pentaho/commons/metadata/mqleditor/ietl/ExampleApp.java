package org.pentaho.commons.metadata.mqleditor.ietl;


import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.connection.IPentahoResultSet;
import org.pentaho.commons.metadata.mqleditor.MqlOrder.Type;
import org.pentaho.commons.metadata.mqleditor.beans.Column;
import org.pentaho.commons.metadata.mqleditor.beans.Model;
import org.pentaho.commons.metadata.mqleditor.beans.Order;
import org.pentaho.commons.metadata.mqleditor.beans.Query;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.JobEntryLoader;
import org.pentaho.di.trans.StepLoader;
import org.pentaho.di.trans.steps.groupby.GroupByMeta;

public class ExampleApp {
  
  public static void init() throws Exception {
    EnvUtil.environmentInit();
    StepLoader.init();
    // JobEntryLoader.init();
  }
  
  public static void main(String args[]) throws Exception {
    
    init();
    
    CsvPhysicalModel model = new CsvPhysicalModel();
    model.setCsvLocation("file://home/gorman/example_output.csv");
    model.setHeaderPresent(true);
    BusinessModelGenerator gen = new BusinessModelGenerator();
    Model bizmodel = gen.generateModel(model);
    System.out.println("BIZMODEL : " + bizmodel);
    
    Query query = new Query();
    
    List<Column> cols = new ArrayList<Column>();
    cols.add(bizmodel.getCategories().get(0).getBusinessColumns().get(3));
    cols.add(bizmodel.getCategories().get(0).getBusinessColumns().get(1));
    bizmodel.getCategories().get(0).getBusinessColumns().get(1).agg = GroupByMeta.TYPE_GROUP_SUM;
    query.setModel(bizmodel);
    query.setCols(cols);
    
    List<Order> orders = new ArrayList<Order>();
    Order order = new Order();
    order.setColumn(bizmodel.getCategories().get(0).getBusinessColumns().get(3));
    order.setOrderType(Type.DESC);
    orders.add(order);
    query.setOrders(orders);

    CsvMqlGenerator gener = new CsvMqlGenerator();
    IPentahoResultSet rs = gener.generate(model, query);
    for (int j = 0; j < rs.getColumnCount(); j++) {
      if (j != 0) {
        System.out.print(",");
      }
      System.out.print(rs.getMetaData().getColumnHeaders()[0][j]);
    }
    System.out.println("");
    for (int i = 0; i < rs.getRowCount(); i++) {
      for (int j = 0; j < rs.getColumnCount(); j++) {
        if (j != 0) {
          System.out.print(",");
        }
        System.out.print(rs.getValueAt(i, j));
      }
      System.out.println("");
    }
    
  }
  
}
