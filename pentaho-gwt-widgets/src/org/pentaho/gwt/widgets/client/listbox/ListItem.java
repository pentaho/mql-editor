package org.pentaho.gwt.widgets.client.listbox;

import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Baker
 * Date: Mar 9, 2009
 * Time: 11:17:00 AM
 */
public interface ListItem<T> {

  /**
   * Returns the widget representation of this ListItem.
   *
   * CAUTION: Because this may be represented in a dropdown and in it's popup, getWidget() will be
   * called more than once. As a single widget can't be two places on the DOM, this method must return
   * a clone every time.
   *
   * @return Cloned widget representation
   */
  Widget getWidget();
  Widget getWidgetForDropdown();
  T getValue();
  void setValue(T t);

  void onHoverEnter();
  void onHoverExit();
  void onSelect();
  void onDeselect();
  void setListBox(CustomListBox listbox);
  void setStylePrimaryName(String style);
  
}
