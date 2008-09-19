package org.pentaho.gwt.widgets.client.splitpanel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.VerticalSplitPanel;

public class SplitPanelUtil {

  // these guys must operate on populated scrollpanels (content added)
  public static void setHorizontalSplitPanelScrolling(HorizontalSplitPanel hsplit, boolean enableLeft, boolean enableRight) {
    Element splitElement = hsplit.getElement();
    Element leftElement = hsplit.getLeftWidget().getElement();
    while (leftElement != splitElement && leftElement != null) {
      leftElement = leftElement.getParentElement();
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) leftElement, "overflowX", enableLeft ? "auto" : "hidden");
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) leftElement, "overflowY", enableLeft ? "auto" : "hidden");
    }
    Element rightElement = hsplit.getRightWidget().getElement();
    while (rightElement != splitElement && rightElement != null) {
      rightElement = rightElement.getParentElement();
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) rightElement, "overflowX", enableRight ? "auto" : "hidden");
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) rightElement, "overflowY", enableRight ? "auto" : "hidden");
    }
  }

  // these guys must operate on populated scrollpanels (content added)
  public static void setVerticalSplitPanelScrolling(VerticalSplitPanel vsplit, boolean enableTop, boolean enableBottom) {
    Element splitElement = vsplit.getElement();
    Element topElement = vsplit.getTopWidget().getElement();
    while (topElement != splitElement && topElement != null) {
      topElement = topElement.getParentElement();
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) topElement, "overflowX", enableTop ? "auto" : "hidden");
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) topElement, "overflowY", enableTop ? "auto" : "hidden");
    }
    Element bottomElement = vsplit.getBottomWidget().getElement();
    while (bottomElement != splitElement && bottomElement != null) {
      bottomElement = bottomElement.getParentElement();
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) bottomElement, "overflowX", enableBottom ? "auto" : "hidden");
      DOM.setStyleAttribute((com.google.gwt.user.client.Element) bottomElement, "overflowY", enableBottom ? "auto" : "hidden");
    }
  }

}
