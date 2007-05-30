package org.pentaho.commons.mql.ui.mqldesigner;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

public class MQLDemo {
  public static void main(String args[]) {
    CWMStartup.loadCWMInstance("resources/metadata/repository.properties", "resources/metadata/PentahoCWM.xml");
    CWMStartup.loadMetadata("resources/metadata.xmi", "resources");
    Shell shell = new Shell();
    MQLQueryBuilderDialog dialog = new MQLQueryBuilderDialog(shell);
    if (dialog.open() == Window.OK) {
      try {
        System.out.println("mqlQuery.getXML() = " + dialog.getMqlQuery().getXML());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
