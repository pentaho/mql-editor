package org.pentaho.commons.metadata.mqleditor.utils;

import org.pentaho.commons.metadata.mqleditor.MqlQuery;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

import org.pentaho.commons.metadata.mqleditor.beans.Query;


public class ModelSerializer {
  private static XStream xstreamWriter = new XStream(new JettisonMappedXmlDriver());
  static{
    xstreamWriter.setMode(XStream.NO_REFERENCES);

    xstreamWriter.alias("MQLQuery", Query.class); //$NON-NLS-1$
    
  }
  public static String serialize(MqlQuery model){
    return xstreamWriter.toXML(model);
  }
  
  public static MqlQuery deSerialize(String input){
    return (MqlQuery) xstreamWriter.fromXML(input);
  }
}
