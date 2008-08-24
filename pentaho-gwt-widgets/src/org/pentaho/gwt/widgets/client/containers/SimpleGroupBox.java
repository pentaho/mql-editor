package org.pentaho.gwt.widgets.client.containers;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Steven Barkdull
 *
 */
public class SimpleGroupBox extends ComplexPanel {

  private Element legend;

  public SimpleGroupBox(String caption) {

    Element fieldSet = Document.get().createFieldSetElement();
    legend = Document.get().createLegendElement();
    fieldSet.appendChild(legend);
    setElement(fieldSet);
    setCaption(caption);
    setStyleName( "simpleGroupBox" ); //$NON-NLS-1$
  }

  public String getCaption() {
    return this.legend.getInnerText();
  }

  public void setCaption(String caption) {
    this.legend.setInnerText(caption);
  }

  @Override
  public void add(Widget w) {
    super.add(w, getElement());
  }
}