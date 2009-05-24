package org.pentaho.commons.metadata.mqleditor.editor.service.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.pentaho.commons.metadata.mqleditor.ColumnType;
import org.pentaho.commons.metadata.mqleditor.CombinationType;
import org.pentaho.commons.metadata.mqleditor.DatabaseColumnType;
import org.pentaho.commons.metadata.mqleditor.IConnection;
import org.pentaho.commons.metadata.mqleditor.IDatasource;
import org.pentaho.commons.metadata.mqleditor.MqlBusinessTable;
import org.pentaho.commons.metadata.mqleditor.MqlCategory;
import org.pentaho.commons.metadata.mqleditor.MqlColumn;
import org.pentaho.commons.metadata.mqleditor.MqlCondition;
import org.pentaho.commons.metadata.mqleditor.MqlDomain;
import org.pentaho.commons.metadata.mqleditor.MqlModel;
import org.pentaho.commons.metadata.mqleditor.MqlOrder;
import org.pentaho.commons.metadata.mqleditor.MqlQuery;
import org.pentaho.commons.metadata.mqleditor.Operator;
import org.pentaho.commons.metadata.mqleditor.beans.BogoPojo;
import org.pentaho.commons.metadata.mqleditor.beans.BusinessData;
import org.pentaho.commons.metadata.mqleditor.beans.Connection;
import org.pentaho.commons.metadata.mqleditor.beans.Datasource;
import org.pentaho.commons.metadata.mqleditor.editor.service.DatasourceServiceException;
import org.pentaho.commons.metadata.mqleditor.editor.service.impl.DatasourceServiceDelegate;
import org.pentaho.commons.metadata.mqleditor.utils.SerializedResultSet;
import org.pentaho.metadata.model.Category;
import org.pentaho.metadata.model.Domain;
import org.pentaho.metadata.model.IPhysicalColumn;
import org.pentaho.metadata.model.LogicalColumn;
import org.pentaho.metadata.model.LogicalModel;
import org.pentaho.metadata.model.SqlPhysicalColumn;
import org.pentaho.metadata.model.SqlPhysicalModel;
import org.pentaho.metadata.model.SqlPhysicalTable;
import org.pentaho.metadata.model.concept.Concept;
import org.pentaho.metadata.model.concept.types.AggregationType;
import org.pentaho.metadata.model.concept.types.DataType;
import org.pentaho.metadata.model.concept.types.LocalizedString;
import org.pentaho.metadata.model.concept.types.TargetColumnType;
import org.pentaho.metadata.model.concept.types.TargetTableType;
import org.pentaho.pms.schema.v3.envelope.Envelope;
import org.pentaho.pms.schema.v3.model.Attribute;
import org.pentaho.pms.schema.v3.model.Column;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

public class DatasourceGwtServlet extends RemoteServiceServlet implements DatasourceGwtService {

  DatasourceServiceDelegate SERVICE;

  public DatasourceGwtServlet() {
    SERVICE = new DatasourceServiceDelegate();
  }
  public Boolean addDatasource(IDatasource datasource) {
    return SERVICE.addDatasource(datasource);
  }

  public Boolean deleteDatasource(IDatasource datasource) {
    return SERVICE.deleteDatasource(datasource);
  }

  public Boolean deleteDatasource(String name) {
    return SERVICE.deleteDatasource(name);
  }


  public SerializedResultSet doPreview(IConnection connection, String query, String previewLimit)
      throws DatasourceServiceException {
    return SERVICE.doPreview(connection, query, previewLimit);
  }

  public SerializedResultSet doPreview(IDatasource datasource) throws DatasourceServiceException {
    return SERVICE.doPreview(datasource);
  }

  public IDatasource getDatasourceByName(String name) {
    return SERVICE.getDatasourceByName(name);
  }

  public List<IDatasource> getDatasources() {
    return SERVICE.getDatasources();
  }

  public Boolean updateDatasource(IDatasource datasource) {
    return SERVICE.updateDatasource(datasource);
  }

  public BusinessData generateModel(String modelName, IConnection connection, String query, String previewLimit)
      throws DatasourceServiceException {
    return SERVICE.generateModel(modelName, connection, query, previewLimit);
  }

  public Boolean saveModel(String modelName, IConnection connection, String query, Boolean overwrite)
  throws DatasourceServiceException {
      return SERVICE.saveModel(modelName, connection, query, overwrite);
  }
  public Boolean saveModel(BusinessData businessData, Boolean overwrite) throws DatasourceServiceException {
    return SERVICE.saveModel(businessData, overwrite);
  }
  protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest arg0, String arg1, String arg2) {
    return new SerializationPolicy(){

      List<Class<?>> classes = new ArrayList<Class<?>>();
      {
        classes.add(Exception.class);
        classes.add(Integer.class);
        classes.add(Number.class);
        classes.add(Boolean.class);
        classes.add(RuntimeException.class);
        classes.add(String.class);
        classes.add(Throwable.class);
        classes.add(ArrayList.class);
        classes.add(HashMap.class);
        classes.add(LinkedHashMap.class);
        classes.add(LinkedList.class);
        classes.add(Stack.class);
        classes.add(Vector.class);
        classes.add(MqlDomain.class);
        classes.add(MqlColumn.class);
        classes.add(MqlCondition.class);
        classes.add(MqlOrder.class);
        classes.add(ColumnType.class);
        classes.add(CombinationType.class);
        classes.add(MqlBusinessTable.class);
        classes.add(MqlCategory.class);
        classes.add(MqlModel.class);
        classes.add(MqlQuery.class);
        classes.add(Operator.class);
        classes.add(DatabaseColumnType.class);
        classes.add(Connection.class);
        classes.add(Datasource.class);
        classes.add(MqlOrder.class);
        classes.add(Column.class);
        classes.add(Attribute.class);
        classes.add(Envelope.class);
        classes.add(BusinessData.class);
        classes.add(SerializedResultSet.class);
        classes.add(DataType.class);
        classes.add(Domain.class);
        classes.add(IPhysicalColumn.class);
        classes.add(LogicalModel.class);
        classes.add(Category.class);
        classes.add(LogicalColumn.class);
        classes.add(SqlPhysicalColumn.class);
        classes.add(SqlPhysicalModel.class);
        classes.add(SqlPhysicalTable.class);
        classes.add(Concept.class);
        classes.add(AggregationType.class);
        classes.add(DataType.class);
        classes.add(TargetColumnType.class);
        classes.add(TargetTableType.class);
        classes.add(LocalizedString.class);
      }
      @Override
      public boolean shouldDeserializeFields(Class<?> clazz) {
        return classes.contains(clazz);
      }

      @Override
      public boolean shouldSerializeFields(Class<?> clazz) {

        return classes.contains(clazz);
          
      }

      @Override
      public void validateDeserialize(Class<?> arg0) throws SerializationException {
        
          
      }

      @Override
      public void validateSerialize(Class<?> arg0) throws SerializationException {
        
          
      }
      
    };
}
  
  public BogoPojo gwtWorkaround(BogoPojo pojo) {
    return pojo;
  }
}