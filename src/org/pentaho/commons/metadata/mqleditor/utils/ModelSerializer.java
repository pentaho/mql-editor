package org.pentaho.commons.metadata.mqleditor.utils;

import org.pentaho.commons.metadata.mqleditor.IQuery;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import org.pentaho.commons.metadata.mqleditor.beans.Query;


public class ModelSerializer {
  private static XStream xstreamWriter = new XStream(new JettisonMappedXmlDriver());
  static{
    xstreamWriter.setMode(XStream.NO_REFERENCES);

    xstreamWriter.alias("MQLQuery", Query.class); //$NON-NLS-1$
    
  }
  public static String serialize(IQuery model){
    return xstreamWriter.toXML(model);
  }
  
  public static IQuery deSerialize(String input){
    return (IQuery) xstreamWriter.fromXML(input);
  }
}
