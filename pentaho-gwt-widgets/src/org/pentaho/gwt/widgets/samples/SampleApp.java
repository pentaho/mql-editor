package org.pentaho.gwt.widgets.samples;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.*;
import org.pentaho.gwt.widgets.client.listbox.CustomListBox;
import org.pentaho.gwt.widgets.client.listbox.DefaultListItem;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Baker
 * Date: Mar 9, 2009
 * Time: 12:54:29 PM
 */
public class SampleApp implements EntryPoint {
  public void onModuleLoad() {

    final CustomListBox list = new CustomListBox();

    list.addItem("Alberta");
    list.addItem("Atlanta");
    list.addItem("San Francisco");
    list.addItem("Alberta");
    list.addItem("Atlanta");
    list.addItem("San Francisco");
    list.addItem("Alberta");
    list.addItem("Atlanta");
    list.addItem("San Francisco");
    list.addItem("Alberta");
    list.addItem("Atlanta");
    list.addItem("San Francisco");
    list.add("Alberta");
    list.add("Atlanta");
    list.add("San Francisco");
    list.add("Alberta");
    list.add("Atlanta");
    list.add("San Francisco");
    list.addItem(new DefaultListItem("Testing", new Image("16x16sample.png")));
    list.add(new DefaultListItem("Testing 2", new CheckBox()));

//    list.setVisibleRowCount(6);

    list.addChangeListener(new ChangeListener(){
      public void onChange(Widget widget) {
        System.out.println(""+list.getSelectedIdex());
      }
    });


    list.setWidth("100%");
    list.setHeight("100%");

    RootPanel.get().add(new Label("Combo: "));
    RootPanel.get().add(list);


    final CustomListBox list2 = new CustomListBox();

    list2.addItem("Alberta");
    list2.addItem("Atlanta");
    list2.addItem("San Francisco");
    list2.addItem("Alberta");
    list2.addItem("Atlanta");
    list2.addItem("San Francisco");
    list2.addItem("Alberta");
    list2.addItem("Atlanta");
    list2.addItem("San Francisco");
    list2.addItem("Alberta");
    list2.addItem("Atlanta");
    list2.addItem("San Francisco");
    list2.add("Alberta");
    list2.add("Atlanta");
    list2.add("San Francisco");
    list2.add("Alberta");
    list2.add("Atlanta");
    list2.add("San Francisco");
    list2.add(new DefaultListItem("Testing", new Image("16x16sample.png")));
    list2.addItem(new DefaultListItem("Testing 2", new CheckBox()));


    RootPanel.get().add(new Label(""));
    RootPanel.get().add(new Label("Combo2: "));
    RootPanel.get().add(list2);

    
    CustomListBox list3 = new CustomListBox();

    list3.add(new DefaultListItem("Testing", new Image("16x16sample.png")));
    list3.addItem(new DefaultListItem("Testing 2", new CheckBox()));  
    RootPanel.get().add(new Label(""));
    RootPanel.get().add(new Label("Combo3: "));
    RootPanel.get().add(list3);
  }
}
