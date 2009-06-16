package org.pentaho.gwt.widgets.client.controls;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.gwt.widgets.client.utils.ElementUtils;
import org.pentaho.gwt.widgets.client.utils.Rectangle;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class ColorPicker extends Image{

  private List<ColorPickerListener> listeners  = new ArrayList<ColorPickerListener>();
  private ColorPickerDialog picker = new ColorPickerDialog();
  private String selectedColor = "#FFF";
  
  public ColorPicker(){
    super(GWT.getModuleBaseURL()+"style/images/color_picker_frame.png");
    this.getElement().getStyle().setProperty("backgroundColor", "#fff");
    this.getElement().getStyle().setProperty("cursor", "pointer");

    this.sinkEvents(Event.MOUSEEVENTS);
  }
  
  public String getColor(){
    return this.selectedColor;
  }
  
  
  public void setColor(String hex){
    this.selectedColor = hex;
    this.getElement().getStyle().setProperty("backgroundColor", hex);
    
    for(ColorPickerListener listener : listeners){
      listener.colorPicked(this);
    }
  }
  
  public void showPicker(){
    picker.center();
  }
  
  public void addColorPickerListener(ColorPickerListener listener){
    listeners.add(listener);
  }
  
  @Override
  public void onBrowserEvent(Event event) {
    
    switch(event.getKeyCode()){
    case Event.ONDBLCLICK:
    case Event.ONCLICK:
      
      Rectangle rect = ElementUtils.getSize(this.getElement());
      picker.setPopupPosition(DOM.getAbsoluteLeft(this.getElement()), DOM.getAbsoluteTop(this.getElement()) + rect.height + 2);
      picker.show();
      break;
    case Event.ONMOUSEOVER:
    case Event.ONMOUSEOUT:
      break;
    default:
      break;
    }
  }
  
  
  private class ColorPickerDialog extends PopupPanel{
    
    private String[] colors = new String[]{
        "#000",
        "#993300",
        "#333300",
        "#003300",
        "#003366",
        "#000080",
        "#333399",
        "#333333",
        
        "#800000",
        "#ff6600",
        "#808000",
        "#008000",
        "#008080",
        "#0000ff",
        "#666699",
        "#808080",

        "#ff0000",
        "#ff9900",
        "#99cc00",
        "#339966",
        "#33cccc",
        "#3366ff",
        "#800080",
        "#969696",

        "#ff00ff",
        "#ffcc00",
        "#ffff00",
        "#00ff00",
        "#00ffff",
        "#00ccff",
        "#993366",
        "#c0c0c0",
        
        "#ff99cc",
        "#ffcc99",
        "#ffff99",
        "#ccffcc",
        "#ccffff",
        "#99ccff",
        "#cc99ff",
        "#FFF"
    };
    
    public ColorPickerDialog(){
      super(true);
      this.setStyleName("color-picker-popup");
      
      FlexTable table = new FlexTable();
      table.setCellPadding(0);
      table.setCellSpacing(2);
      
      
      for(int i=0, row = 0; i< colors.length; i++, row++){
        for(int y=0; y< 7 && i < colors.length; y++, i++){
          table.setWidget(row, y, new ColorBox(this, colors[i]));
        }
      }
      SimplePanel panel = new SimplePanel();
      panel.getElement().getStyle().setProperty("padding", "3px");
      panel.add(table);
      
      this.add(panel);
    }
    
  }
  
  private class ColorBox extends SimplePanel{
    String color;
    ColorPickerDialog dialog;
    public ColorBox(ColorPickerDialog dialog, String color){
      this.dialog = dialog;
      this.color = color;
      this.setStyleName("color-swatch");
      SimplePanel panel = new SimplePanel();

      panel.getElement().getStyle().setProperty("border", "1px solid #aaa");
      panel.getElement().getStyle().setProperty("backgroundColor", color);
      add(panel);
      panel.setStyleName("color-swatch-center");
      this.sinkEvents(Event.MOUSEEVENTS);
    }

    @Override
    public void onBrowserEvent(Event event) {
      
      switch(event.getKeyCode()){
      case Event.ONDBLCLICK:
      case Event.ONCLICK:
        ColorPicker.this.setColor(color);
        dialog.hide();
        break;
      case Event.ONMOUSEOVER:
      case Event.ONMOUSEOUT:
        break;
      default:
        break;
      }
    }
    
    
  }
}
