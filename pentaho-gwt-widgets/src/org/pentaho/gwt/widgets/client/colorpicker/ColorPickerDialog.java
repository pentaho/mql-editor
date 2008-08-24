package org.pentaho.gwt.widgets.client.colorpicker;

import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;

public class ColorPickerDialog extends PromptDialogBox {

  public ColorPickerDialog(String startHex) {
    super("Color Chooser", new ColorPicker(), "OK", "Cancel", false, true);
    ColorPicker colorPicker = (ColorPicker) getContent();
    try {
      colorPicker.setHex(startHex);
    } catch (Exception e) {
    }
  }

  public String getHexColor() {
    ColorPicker colorPicker = (ColorPicker) getContent();
    return colorPicker.getHexColor();
  }

}
