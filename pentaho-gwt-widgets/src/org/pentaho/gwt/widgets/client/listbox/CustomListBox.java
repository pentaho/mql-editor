package org.pentaho.gwt.widgets.client.listbox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import org.pentaho.gwt.widgets.client.utils.ElementUtils;
import org.pentaho.gwt.widgets.client.utils.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * ComplexListBox is a List-style widget can contain custom list-items made (images + text, text + checkboxes)
 * This list is displayed as a drop-down style component by default. If the visibleRowCount property is set higher
 * than 1 (default), the list is rendered as a multi-line list box.
 *
 * <P>Usage:
 *
 * <p>
 *
 * <pre>
 * ComplexListBox list = new ComplexListBox();
 *
 *  list.addItem("Alberta");
 *  list.addItem("Atlanta");
 *  list.addItem("San Francisco");
 *  list.addItem(new DefaultListItem("Testing", new Image("16x16sample.png")));
 *  list.addItem(new DefaultListItem("Testing 2", new CheckBox()));
 *
 *  list.setVisibleRowCount(6); // turns representation from drop-down to list
 *
 *  list.addChangeListener(new ChangeListener(){
 *    public void onChange(Widget widget) {
 *      System.out.println(""+list.getSelectedIdex());
 *    }
 *  });
 *</pre>
 *
 * User: NBaker
 * Date: Mar 9, 2009
 * Time: 11:01:57 AM
 * 
 */
public class CustomListBox extends HorizontalPanel implements ChangeListener, PopupListener, MouseListener, FocusListener, KeyboardListener{
  private List<ListItem> items = new ArrayList<ListItem>();
  private int selectedIndex = -1;
  private DropDownArrow arrow = new DropDownArrow();
  private int visible = 1;
  private int maxDropVisible = 15;
  private boolean editable = false;
  private VerticalPanel listPanel = new VerticalPanel();
  private ScrollPanel listScrollPanel = new ScrollPanel();

  // Members for drop-down style
  private Grid dropGrid = new Grid(1,2);
  private boolean popupShowing = false;
  private DropPopupPanel popup = new DropPopupPanel();
  private PopupList popupVbox = new PopupList();
  private FocusPanel fPanel = new FocusPanel();
  private ScrollPanel popupScrollPanel = new ScrollPanel();


  private List<ChangeListener> listeners = new ArrayList<ChangeListener>();
  private final int spacing = 2;
  private int maxHeight, maxWidth, averageHeight; //height and width of largest ListItem
  private String primaryStyleName;
  private String height, width;
  private String popupHeight, popupWidth;


  public CustomListBox(){

    dropGrid.getColumnFormatter().setWidth(0, "100%");
    dropGrid.setWidget(0,1, arrow);
    dropGrid.setCellPadding(0);
    dropGrid.setCellSpacing(spacing);
    updateUI();

    // Add List Panel to it's scrollPanel
    listScrollPanel.add(listPanel);
    listScrollPanel.setHeight("100%");
    listScrollPanel.setWidth("100%");
    listScrollPanel.getElement().getStyle().setProperty("overflowX","hidden");
    //listScrollPanel.getElement().getStyle().setProperty("padding",spacing+"px");
    listPanel.setSpacing(spacing);
    listPanel.setWidth("100%");

    //default to drop-down
    fPanel.add(dropGrid);
    fPanel.setHeight("100%");
    super.add(fPanel);

    popup.addPopupListener(this);
    popupScrollPanel.add(popupVbox);
    popupScrollPanel.getElement().getStyle().setProperty("overflowX","hidden");
    popupVbox.setWidth("100%");
    popupVbox.setSpacing(spacing);
    popup.add(popupScrollPanel);

    fPanel.addMouseListener(this);
    fPanel.addFocusListener(this);
    fPanel.addKeyboardListener(this);

    setStyleName("custom-list");


    setTdStyles(this.getElement());
    setTdStyles(listPanel.getElement());

  }

  private native void setTdStyles(Element ele)/*-{
  var tds = ele.getElementsByTagName("td");
    for( var i=0; i< tds.length; i++){
      var td = tds[i];
      if(!td.style){
        td.className = "customListBoxTdFix";
      } else {
        td.style.padding = "0px";
        td.style.border = "none";
      }
    }
  }-*/;

  /**
   * Only {@link: DefaultListItem} and Strings (as labels) may be passed into this Widget
   */
  @Override
  public void add(Widget child) {
    throw new UnsupportedOperationException(
        "This panel does not support no-arg add()");
  }
  
  /**
   * Convenience method to support the more conventional method of child attachment
   * @param listItem
   */
  public void add(ListItem listItem){
    this.addItem(listItem);
  }

  /**
   * Removes the passed in ListItem
   *
   * @param listItem item to remove
   */
  public void remove(ListItem listItem){
    this.items.remove(listItem);
    setSelectedIndex(0);

    updateUI();
  }

  /**
   * Removes all items from the list.
   */
  public void removeAll(){
    this.items.clear();
    this.selectedIndex = -1;
    updateUI();
  }

  /**
   * Removes all items from the list.
   */
  public void clear(){
    removeAll();
  }
  
  /**
   * Convenience method to support the more conventional method of child attachment
   * @param label
   */
  public void add(String label){
    this.addItem(label);
  }

  /**
   * Adds the given ListItem to the list control.
   *
   * @param item ListItem
   */
  public void addItem(ListItem item){
    items.add(item);
    item.setListBox(this);

    // If first one added, set selectedIndex to 0        
    if(items.size() == 1){
      setSelectedIndex(0);
    }
    updateUI();

  }

  /**
   * Convenience method creates a {@link: DefaultListItem} with the given text and adds it to the list control
   *
   * @param label
   */
  public void addItem(String label){
    DefaultListItem item = new DefaultListItem(label);
    items.add(item);
    item.setListBox(this);

    // If first one added, set selectedIndex to 0
    if(items.size() == 1){
      setSelectedIndex(0);
    }
    updateUI();
    
  }


  /**
   * Returns a list of current ListItems.
   * 
   * @return List of ListItems
   */
  public List<ListItem> getItems(){
    return items;
  }

  /**
   * Sets the number of items to be displayed at once in the lsit control. If set to 1 (default) the list is rendered
   * as a drop-down
   *
   * @param visibleCount number of rows to be visible.
   */
  public void setVisibleRowCount(int visibleCount){
    int prevCount = visible;
    this.visible = visibleCount;

    if(visible > 1 && prevCount == 1){
      // switched from drop-down to list
      fPanel.remove(dropGrid);
      fPanel.add(listScrollPanel);
    } else if(visible == 1 && prevCount > 1){
      // switched from list to drop-down
      fPanel.remove(listScrollPanel);
      fPanel.add(dropGrid);
    }
    updateUI();
  }

  /**
   * Returns the number of rows visible in the list
   * @return number of visible rows.
   */
  public int getVisibleRowCount(){
    return visible;
  }

  private void updateUI(){
    if(visible > 1){
      updateList();
    } else {
      updateDropDown();
    }
  }

  /**
   * Returns the number of rows to be displayed in the drop-down popup.
   *
   * @return number of visible popup items.
   */
  public int getMaxDropVisible() {
    return maxDropVisible;
  }

  /**
   * Sets the number of items to be visible in the drop-down popup. If set lower than the number of items
   * a scroll-bar with provide access to hidden items.
   *
   * @param maxDropVisible number of items visible in popup.
   */
  public void setMaxDropVisible(int maxDropVisible) {
    this.maxDropVisible = maxDropVisible;

    // Update the popup to respect this value
    if(maxHeight > 0){ //Items already added
      System.out.println("Max heihgt : "+this.maxDropVisible * maxHeight);
      this.popupHeight = this.maxDropVisible * maxHeight + "px";
    }
  }

  private TextBox editableTextBox;
  private SimplePanel selectedItemWrapper = new SimplePanel();
  private void updateSelectedDropWidget(){
    Widget selectedWidget = new Label(""); //Default to show in case of empty sets?
    if(editable == false){ // only show their widget if editable is false
      if(selectedIndex >= 0){
        selectedWidget = items.get(selectedIndex).getWidgetForDropdown();
      } else if(items.size() > 0){
        selectedWidget = items.get(0).getWidgetForDropdown();
      }
    } else {
      editableTextBox = new TextBox(){
        @Override
        public void onBrowserEvent(Event event) {
          int code = event.getKeyCode();

          switch(DOM.eventGetType(event)){
            case Event.ONKEYUP:
              onChange(editableTextBox);
              break;
          }
          event.cancelBubble(true);
        }
      };
      
      if(selectedIndex >= 0){
        editableTextBox.setText(items.get(selectedIndex).getValue().toString());
      } else if(items.size() > 0){
        editableTextBox.setText(items.get(0).getValue().toString());
      }
      editableTextBox.setWidth("100%");
      editableTextBox.sinkEvents(Event.KEYEVENTS);
      editableTextBox.sinkEvents(Event.MOUSEEVENTS);
      selectedWidget = editableTextBox;

    }
    this.setTdStyles(selectedWidget.getElement());
    selectedItemWrapper.getElement().getStyle().setProperty("overflow", "hidden");
    selectedItemWrapper.clear();
    selectedItemWrapper.add(selectedWidget);
    dropGrid.setWidget(0,0, selectedItemWrapper);
  }

  /**
   * Called by updateUI when the list is not a drop-down (visible row count > 1)
   */
  private void updateList(){
    popupVbox.clear();
    maxHeight = 0;
    maxWidth = 0;

    //actually going to average up the heights
    for(ListItem li : this.items){
      Widget w = li.getWidget();

      Rectangle rect = ElementUtils.getSize(w.getElement());
      // we only care about this if the user hasn't specified a height.
      if(height == null){
        maxHeight += rect.height;
      }
      maxWidth = Math.max(maxWidth,rect.width);

      // Add it to the dropdown
      listPanel.add(w);
      listPanel.setCellWidth(w, "100%");
    }
    if(height == null){
      maxHeight = Math.round(maxHeight / this.items.size());
    }

    // we only care about this if the user has specified a visible row count and no heihgt
    if(height == null){
      this.listScrollPanel.setHeight((this.visible * (maxHeight + spacing)) + "px");
    }
    if(width == null){
      this.fPanel.setWidth(maxWidth + 40 + "px"); //20 is scrollbar space
    }

  }

  /**
   * Called by updateUI when the list is a drop-down (visible row count = 1)
   */
  private void updateDropDown(){

    // Update Shown selection in grid
    updateSelectedDropWidget();

    // Update popup panel,
    // Calculate the size of the largest list item.
    popupVbox.clear();
    maxWidth = 0;
    averageHeight = 0; // Actually used to set the width of the arrow
    popupHeight = null;
    for(ListItem li : this.items){
      Widget w = li.getWidget();

      Rectangle rect = ElementUtils.getSize(w.getElement());
      maxWidth = Math.max(maxWidth,rect.width);
      maxHeight = Math.max(maxHeight,rect.height);
      averageHeight += rect.height;

      // Add it to the dropdown
      popupVbox.add(w);
      popupVbox.setCellWidth(w, "100%");
    }

    // Average the height of the items
    if(items.size() > 0){
      averageHeight = Math.round(averageHeight / items.size());
    }


    // Set the size of the drop-down based on the largest list item
    if(width == null){
      //TODO: move "10" to a static member  
      dropGrid.setWidth(maxWidth + (spacing*6) + maxHeight + 10 + "px"); //adding a little more room with the 10 
      this.popupWidth = maxWidth + (spacing*6) + maxHeight + 10 + "px";
    } else if(width.equals("100%")){
      dropGrid.setWidth("100%");
      this.popupWidth = maxWidth + (spacing*6) + maxHeight + 10 + "px";
    } else {
      dropGrid.setWidth("100%");
      int w = Integer.parseInt(this.width.replace("px",""));
      selectedItemWrapper.setWidth( (w - (averageHeight + (this.spacing*2))) + "px" );
    }

    // Store the the size of the popup to respect MaxDropVisible now that we know the item height
    // This cannot be set here as the popup is not visible :(

    if(maxDropVisible > 0){
      // (Lesser of maxDropVisible or items size) * (Average item height + spacing value) 
      this.popupHeight = (Math.min(this.maxDropVisible, this.items.size()) * (averageHeight + (this.spacing * 2) )) + "px";
    }
  }

  /**
   * Used internally to hide/show drop-down popup.
   */
  private void togglePopup(){
    if(popupShowing == false){

//      popupScrollPanel.setWidth(this.getElement().getOffsetWidth() - 8 +"px");

      popup.setPopupPosition(this.getElement().getAbsoluteLeft(), this.getElement().getAbsoluteTop() + this.getElement().getOffsetHeight()+2);
      
      popup.show();

      // Set the size of the popup calculated in updateDropDown().
      if(this.popupHeight != null){
        this.popupScrollPanel.getElement().getStyle().setProperty("height", this.popupHeight);
      }
      
      if(this.popupWidth != null){
        this.popupScrollPanel.getElement().getStyle().setProperty("width", Math.max(this.getElement().getOffsetWidth()-2, this.maxWidth+10)+"px");
      }
      
      scrollSelectedItemIntoView();

      popupShowing = true;
    } else {
      popup.hide();
      fPanel.setFocus(true);
    }
  }

  private void scrollSelectedItemIntoView(){

    // Scroll to view currently selected widget  
    //DOM.scrollIntoView(this.getSelectedItem().getWidget().getElement());
    // Side effect of the previous call scrolls the scrollpanel to the right. Compensate here
    //popupScrollPanel.setHorizontalScrollPosition(0);

    // if the position of the selected item is greater than the height of the scroll area plus it's scroll offset
    if( ((this.selectedIndex + 1) * this.averageHeight) > popupScrollPanel.getOffsetHeight() + popupScrollPanel.getScrollPosition()){
      popupScrollPanel.setScrollPosition( (((this.selectedIndex ) * this.averageHeight) - popupScrollPanel.getOffsetHeight()) + averageHeight );
      return;
    }

    // if the position of the selected item is Less than the scroll offset
    if( ((this.selectedIndex) * this.averageHeight) < popupScrollPanel.getScrollPosition()){
      popupScrollPanel.setScrollPosition( ((this.selectedIndex ) * this.averageHeight));
    }
  }

  /**
   * Selects the given ListItem in the list.
   *
   * @param item ListItem to be selected.
   */
  public void setSelectedItem(ListItem item){
    if(items.contains(item) == false){
      throw new RuntimeException("Item not in collection");
    }
    // Clear previously selected item
    if(selectedIndex > -1){
      items.get(selectedIndex).onDeselect();
    }
    if(visible == 1){ // Drop-down mode
      if(popupShowing) {
        togglePopup();
      }
    }
    setSelectedIndex(items.indexOf(item));
     
  }

  /**
   * Selects the ListItem at the given index (zero-based)
   *
   * @param idx index of ListItem to select
   */
  public void setSelectedIndex(int idx){
    if(idx < 0 || idx > items.size()){
      throw new RuntimeException("Index out of bounds: "+ idx);
    }
    // De-Select the current
    if(selectedIndex > -1){
      items.get(selectedIndex).onDeselect();
    }


    selectedIndex = idx;
    items.get(idx).onSelect();

    if(visible == 1){
      updateSelectedDropWidget();
      scrollSelectedItemIntoView();
    }
    
    for(ChangeListener l : listeners){
      l.onChange(this);
      System.out.println("firing onchange in customlistbox");
    }
  }

  /**
   * Registers a ChangeListener with the list.
   *
   * @param listener ChangeListner
   */
  public void addChangeListener(ChangeListener listener){
    listeners.add(listener);
  }

  /**
   * Removes to given ChangeListener from list.
   *
   * @param listener ChangeListener
   */
  public void removeChangeListener(ChangeListener listener){
    this.listeners.remove(listener);
  }

  /**
   * Returns the selected index of the list (zero-based)
   *
   * @return Integer index
   */
  public int getSelectedIndex(){
    return selectedIndex;
  }

  /**
   * Returns the currently selected item
   *
   * @return currently selected Item
   */
  public ListItem getSelectedItem(){
    if(selectedIndex < 0){
      return null;
    }

    return items.get(selectedIndex);
  }

  @Override
  public void setStylePrimaryName(String s) {
    super.setStylePrimaryName(s);
    this.primaryStyleName = s;

    // This may have came in late. Update ListItems
    for(ListItem item : items){
      item.setStylePrimaryName(s);
    }
  }

  @Override
  protected void onAttach() {
    super.onAttach();
    updateUI();
  }

  @Override
  /**
   * Calling setHeight will implecitly change the list from a drop-down style to a list style.
   */
  public void setHeight(String s) {
    this.height = s;
    // user has specified height, focusPanel needs to be 100%;
    this.fPanel.setHeight(s);
    if(visible == 1){
      this.setVisibleRowCount(15);
    }
    super.setHeight(s);
  }

  @Override
  public void setWidth(String s) {
    fPanel.setWidth(s);
    this.listScrollPanel.setWidth("100%");
    this.width = s;
    this.popupWidth = s;
    super.setWidth(s);
  }

  // ======================================= Listener methods ===================================== //

  public void onPopupClosed(PopupPanel popupPanel, boolean b) {
    this.popupShowing = false;
  }

  public void onMouseDown(Widget widget, int i, int i1) {}

  public void onMouseEnter(Widget widget) {}

  public void onMouseLeave(Widget widget) {}

  public void onMouseMove(Widget widget, int i, int i1) {}

  public void onMouseUp(Widget widget, int i, int i1) {
    if(visible == 1){ //drop-down mode
      this.togglePopup();
    }
  }

  public void onFocus(Widget widget) {
     fPanel.setFocus(true);
  }

  public void onLostFocus(Widget widget) {}

  public void onKeyDown(Widget widget, char c, int i) {}

  public void onKeyPress(Widget widget, char c, int i) {}

  public void onKeyUp(Widget widget, char c, int i) {
    switch(c){
      case 38: // UP

        if(selectedIndex > 0){
          setSelectedIndex(selectedIndex - 1);
        }

        break;
      case 40: // Down
        if(selectedIndex < items.size() -1){
          setSelectedIndex(selectedIndex + 1);
        }
        break;
      case 27: // ESC
      case 13: // Enter
        if(popupShowing){
          togglePopup();
        }
        break;
    }
  }

  // ======================================= Inner Classes ===================================== //

  /**
   * Panel used as a drop-down popup.
   */
  private class DropPopupPanel extends PopupPanel {
    public DropPopupPanel(){
      super(true);
      setStyleName("drop-popup");
    }

    @Override
    public boolean onEventPreview(Event event) {
      if(DOM.isOrHasChild(CustomListBox.this.getElement(), DOM.eventGetTarget(event))){

        return true;
      }
      return super.onEventPreview(event);
    }
  }

  /**
   * Panel contained in the popup
   */
  private class PopupList extends VerticalPanel{

    public PopupList(){
      this.sinkEvents(Event.MOUSEEVENTS);
    }

    @Override
    public void onBrowserEvent(Event event) {
      super.onBrowserEvent(event);
    }
  }

  /**
   * This is the arrow rendered in the drop-down.
   */
  private class DropDownArrow extends SimplePanel{
    public DropDownArrow(){
      Image img = new Image(GWT.getModuleBaseURL() + "arrow.png");

      this.setStylePrimaryName("combo-arrow");
      super.add(img);
      ElementUtils.preventTextSelection(this.getElement());
    }
  }

  /**
   * Setting editable to true allows the user to specify their own value for the combobox.
   *
   * @param editable
   */
  public void setEditable(boolean editable){
    this.editable = editable;
    this.updateUI();
  }

  public boolean isEditable(){
    return this.editable;
  }

  /**
   * Returns the user-entered value in the case of an editable drop-down
   *
   * @return Value user has entered
   */
  public String getValue(){
    if(!editable){
      return null;
    } else {
      return (editableTextBox != null)
        ? editableTextBox.getText()
        : null;
    }
  }

  public void onChange(Widget sender) {
    for(ChangeListener l : listeners){
      l.onChange(this);
    }
  }
}
