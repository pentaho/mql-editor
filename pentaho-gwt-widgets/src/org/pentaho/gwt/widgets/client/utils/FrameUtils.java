package org.pentaho.gwt.widgets.client.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Frame;

/**
 * Contains methods to show/hide iframes if they contain PDFs (<embeds>). This is primarily of interest to Firefox browsers 
 * who's Acrobat Plug-in does not work well with overlapping HTML elements.
 */
public class FrameUtils {

  private static Map<Frame,FrameTimer> timers = new HashMap<Frame, FrameTimer>();
  
  
  /**
   * Private method that does the actual hiding or showing of frames if an embed is in.
   * 
   * @param frame
   * @param visible
   */
  private static native void toggleEmbedVisibility(Element frame, boolean visible)/*-{
    
    try{
      var doc = (frame.contentWindow.document || frame.contentDocument);
      
      if(doc == null)
      {
        //IE you're ok anyway
        return;
      }
      
      var embeds = doc.getElementsByTagName("embed");
      if(embeds.length > 0){
        if(visible){
          if(frame.style.display == "none"){   //don't do anything unless you need to
            frame.style.display = "" ;         //Show frame
            
            // have to reload the frame, as the plug-in doesn't re-render when visibility is returned!
            frame.contentWindow.location.href = frame.contentWindow.location.href;   
          }
        } else {
          frame.style.display = "none" ;  //hide frame
        }
      } else {
        var iframes = doc.getElementsByTagName("iframe");
        if(iframes.length > 0){ // iframe has it's own iframes
          //recurse with child iframe
          for(var i=0; i<iframes.length; i++){
            @org.pentaho.gwt.widgets.client.utils.FrameUtils::toggleEmbedVisibility(Lcom/google/gwt/dom/client/Element;Z)(iframes[i], visible);      
          }
        }
      }
    } catch(e) {
      // Cross-site scripting error in all likelihood
    }
  }-*/;
  
  /**
   * If the frame contains an <embed> schedule it for showing/hiding
   * 
   * @param frame
   * @param visible
   */
  public static void setEmbedVisibility(Frame frame, boolean visible){
    if(timers.containsKey(frame)){      //Already Scheduled
      FrameTimer t = timers.get(frame);
      if(t.visible == visible){         //dupe call, ignore
        return;
      } else {                          //timer exists but the visibility call has changed
        t.cancel();
        t.visible = visible;
        t.schedule(200);
      }
    } else {                            //Not currently in the operations queue
      FrameTimer t = new FrameTimer(frame, visible);
      timers.put(frame, t);
      t.schedule(200);
    }
  }

  /**
   * Loops through all iframe object on the document. Used when reference to Frame is not available. 
   * 
   * @param visible
   */
  public static void toggleEmbedVisibility(boolean visible){
    Element[] frames = new Element[0];
    try {
      frames = ElementUtils.getElementsByTagName("iframe"); //$NON-NLS-1$
    } catch (ClassCastException cce) {
      // ignore class cast exceptions in here, they are happening in hosted mode for Elements
    }
    for(Element ele : frames){
      Frame f = null;
      
      //Attempt to find a previously GWT-wrapped frame instance in our timer collection
      Object[] tmap = timers.entrySet().toArray();
      for(int i=0; i<tmap.length; i++){
        @SuppressWarnings("unchecked")
        Map.Entry<Frame, FrameTimer> t = (Map.Entry<Frame, FrameTimer>) tmap[i];
        if(t.getKey().getElement() == ele){ 
          //found an already wrapped instance
          f = t.getKey();
        }
      }
      
      if(f == null){
        f = Frame.wrap(ele);
      }
      setEmbedVisibility(f, visible);
    }
  }

  /**
   * This Timer adds a buffer to show/hide operations so other code has a chance of "canceling" it.
   */
  private static class FrameTimer extends Timer{
    Frame frame;
    boolean visible;
    
    public FrameTimer(Frame frame, boolean visible){
      this.frame = frame;
      this.visible = visible;
    }
    
    public void run(){
      toggleEmbedVisibility(frame.getElement(), visible);
      timers.remove(frame);
    }
  }
}

  