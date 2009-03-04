package org.pentaho.commons.metadata.mqleditor.utils;

import org.pentaho.commons.metadata.mqleditor.IQuery;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;


public class ModelSerializer {
  private static XStream xstreamWriter = new XStream(new JettisonMappedXmlDriver());
  private static XStream xstreamReader = new XStream(new JettisonMappedXmlDriver());
  static{
    xstreamWriter.setMode(XStream.NO_REFERENCES);
    xstreamReader.setMode(XStream.NO_REFERENCES);
    
  }
  public static String serialize(IQuery model){
    return xstreamWriter.toXML(model);
  }
  
  public static IQuery deSerialize(String input){
    return (IQuery) xstreamReader.fromXML(input);
  }
}
