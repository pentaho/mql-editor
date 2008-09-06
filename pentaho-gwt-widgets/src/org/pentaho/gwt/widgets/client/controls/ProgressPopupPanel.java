package org.pentaho.gwt.widgets.client.controls;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProgressPopupPanel extends PopupPanel {

  private Label label;
  private int width = 150;
  private int height = 100;
  
  public ProgressPopupPanel() {
    super( false /*autohide*/, true /*modal*/);
    init();
  }
  
  private void init() {
    this.setStyleName( "progressPopupPanel" );
    setPixelSize( width, height );
    
    label = new Label();
    label.setStyleName( "progressPopupPanel.label" );
    
    VerticalPanel vp = new VerticalPanel();
    vp.add( label );

    vp.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE );
    vp.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_CENTER ); 
//    vp.setCellHeight( label, "100%" );
//    vp.setCellWidth( label, "100%" );
    add( vp );
  }
  
  public void setLabelText( String text ) {
    label.setText( text );
  }
}
