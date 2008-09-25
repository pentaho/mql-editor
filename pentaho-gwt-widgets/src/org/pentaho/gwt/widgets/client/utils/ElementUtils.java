package org.pentaho.gwt.widgets.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;
import com.google.gwt.user.client.ui.Widget;

public class ElementUtils {

  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  public static final int TOP = 0;
  public static final int BOTTOM = 1;
  
  
  public static native void blur(Element e)/*-{
   e.blur();
  }-*/;

  public static void removeScrollingFromSplitPane(Widget panel){
    if(!panel.isAttached()){
      //throw new IllegalStateException("Operation not allowed while element not on DOM");
    }
    
    if((panel instanceof HorizontalSplitPanel || panel instanceof VerticalSplitPanel) == false){
      throw new IllegalArgumentException("Widget not expected SplitPane type");
    }
    
    if(panel instanceof HorizontalSplitPanel){
      HorizontalSplitPanel hp = (HorizontalSplitPanel) panel;
      removeScrollingFromUpTo(hp.getLeftWidget().getElement(), hp.getElement());
      removeScrollingFromUpTo(hp.getRightWidget().getElement(), hp.getElement());
    } else {
      VerticalSplitPanel vp = (VerticalSplitPanel) panel;
      removeScrollingFromUpTo(vp.getTopWidget().getElement(), vp.getElement());
      removeScrollingFromUpTo(vp.getBottomWidget().getElement(), vp.getElement());
    }
    
  }
  
  public static void removeScrollingFromUpTo(Element bottom, Element top){
    
    Element ele = bottom;
    while(ele != top && ele.getParentElement() != null){
      ele.getStyle().setProperty("overflow", "visible");
      ele.getStyle().setProperty("overflowX", "visible");
      ele.getStyle().setProperty("overflowY", "visible");
      ele = ele.getParentElement();
    }
  }
  
  private static void killAutoScrolling(Element ele){
    ele.getStyle().setProperty("overflow", "visible");
    if(ele.hasChildNodes()){
      
      NodeList<Node> nodes = ele.getChildNodes();
      for(int i=0; i<nodes.getLength(); i++){
        Node n = nodes.getItem(i);
        if(n instanceof Element){
          killAutoScrolling((Element) n);
        }
      }
    }
  }
  

  public static native void preventTextSelection(Element ele) /*-{
    ele.onselectstart=function() {return false};
  }-*/;

  

  public static native void toggleEmbedVisibilityJS(boolean visible)/*-{
    alert("toggle function called with "+visible);
    var embeds = $doc.getElementsByTagName("embed");
    for(var i=0; i<embeds.length; i++){
      //embeds[i].style.display = (visible)? "" : "none";
    }
    
    var iframes = $doc.getElementsByTagName("iframe");
    for(var i=0; i<iframes.length; i++){
    var doc = (iframes[i].contentWindow.document || iframes[i].contentDocument);
    
    if(doc == null)
    {
      alert("doc null");
      continue;
    }
      var embeds = doc.getElementsByTagName("embed");
      for(var y=0; y<embeds.length; y++){
        if(visible){
        alert("showing iframe");
          iframes[i].style.display = "" ;
          iframes[i].contentWindow.location.href = iframes[i].contentWindow.location.href
        } else {
        alert("hiding iframe");
          iframes[i].style.display = "none" ;
        }
      }
      
    }
    
  }-*/;

  
  public static void toggleEmbedVisibility(boolean visible){
//    NodeList<Element> embeds = RootPanel.getBodyElement().getElementsByTagName("embed");
//    
//    
//    NodeList<Element> iframes = RootPanel.getBodyElement().getElementsByTagName("iframe");
//    
//    for(int i=0; i<iframes.getLength(); i++){
//      Element ele = iframes.getItem(i);
//      embeds = Frame.wrap(ele).getElement().getElementsByTagName("embed");
//      if(embeds.getLength() > 0){
//        ele.getStyle().setProperty("display", (visible)?"" : "none");
//      }
//    }
  }
  
}


  