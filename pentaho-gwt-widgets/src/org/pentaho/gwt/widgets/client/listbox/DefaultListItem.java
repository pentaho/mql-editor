package org.pentaho.gwt.widgets.client.listbox;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import org.pentaho.gwt.widgets.client.utils.ElementUtils;

/**
 * 
 * User: Nick Baker
 * Date: Mar 9, 2009
 * Time: 11:28:45 AM
 */
public class DefaultListItem  implements ListItem {

  private String text = "";
  private CustomListBox listBox;
  private Widget widget;
  private Widget dropWidget;
  private Image img;
  private Widget extraWidget;
  private String baseStyleName = "default-list";

  public DefaultListItem(){

  }

  public DefaultListItem(String str){
    this();
    this.text = str;
    createWidgets();
  }

  public DefaultListItem(String str, Image img){
    this();
    this.text = str;
    this.img = img;
    createWidgets();
  }

  public DefaultListItem(String str, Widget widget){
    this();
    this.text = str;
    this.extraWidget = widget;
    createWidgets();
  }

  public void setStylePrimaryName(String style){
    baseStyleName = style;
    dropWidget.setStylePrimaryName(style+"-item");
    widget.setStylePrimaryName(style+"-item");

  }

  /**
   * There are two widgets that need to be maintaned. One that shows in the drop-down when not opened, and
   * another that shows in the drop-down popup itself.
   */
  private void createWidgets(){

    HorizontalPanel hbox = new WrapperPanel(baseStyleName+"-item");
    formatWidget(hbox);
    widget = hbox;

    hbox = new HorizontalPanel();
    formatWidget(hbox);
    dropWidget = hbox;

  }

  private void formatWidget(HorizontalPanel panel){
    panel.sinkEvents(Event.MOUSEEVENTS);

    if(img != null){
      Image i = new Image(img.getUrl());
      panel.add(i);
      panel.setCellVerticalAlignment(i, HasVerticalAlignment.ALIGN_MIDDLE);
      i.getElement().getStyle().setProperty("marginRight","2px");
    } else if(extraWidget != null){
      Element ele = DOM.clone(extraWidget.getElement(), true);
      Widget w = new WrapperWidget(ele);
      panel.add(w);
      panel.setCellVerticalAlignment(w, HasVerticalAlignment.ALIGN_MIDDLE);
      w.getElement().getStyle().setProperty("marginRight","2px");
    }

    Label label = new Label(text);
    label.getElement().getStyle().setProperty("cursor","pointer");
    label.setWidth("100%");
    panel.add(label);
    panel.setCellWidth(label, "100%");
    panel.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);

    ElementUtils.preventTextSelection(panel.getElement());

    label.setStylePrimaryName("custom-list-item");
    panel.setWidth("100%");
  }

  public Widget getWidgetForDropdown() {
    return dropWidget;
  }

  public Widget getWidget() {
    return widget;
  }


  public Object getValue() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public void setValue(Object o) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void onHoverEnter() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void onHoverExit() {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  public void onSelect() {
    dropWidget.addStyleDependentName("selected");
    widget.addStyleDependentName("selected");
  }

  public void onDeselect() {
    dropWidget.removeStyleDependentName("selected");
    widget.removeStyleDependentName("selected");
  }

  private class WrapperPanel extends HorizontalPanel {

    public WrapperPanel(String styleName){
      this.sinkEvents(Event.MOUSEEVENTS);
      if(styleName == null){
        styleName = "default-list-item";
      }
      this.setStylePrimaryName(styleName);
    }

    @Override
      public void onBrowserEvent(Event event) {
        int code = event.getTypeInt();
        switch(code){
          case Event.ONMOUSEOVER:
            this.addStyleDependentName("hover");
            break;
          case Event.ONMOUSEOUT:
            this.removeStyleDependentName("hover");
            break;
          case Event.ONMOUSEUP:
            listBox.setSelectedItem(DefaultListItem.this);
            this.removeStyleDependentName("hover");
        }
      }
    }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public CustomListBox getListBox() {
    return listBox;
  }

  public void setListBox(CustomListBox listBox) {
    this.listBox = listBox;
  }

  private static class WrapperWidget extends Widget{
    public WrapperWidget(Element ele){
      this.setElement(ele);
    }
  }
}
