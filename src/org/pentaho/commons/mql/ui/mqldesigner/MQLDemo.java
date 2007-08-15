package org.pentaho.commons.mql.ui.mqldesigner;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

public class MQLDemo {
  public static void main(String args[]) {
    CWMStartup.loadCWMInstance("resources/metadata/repository.properties", "resources/metadata/PentahoCWM.xml"); //$NON-NLS-1$ //$NON-NLS-2$
    CWMStartup.loadMetadata("resources/metadata.xmi", "resources"); //$NON-NLS-1$ //$NON-NLS-2$
    Shell shell = new Shell();
    MQLQueryBuilderDialog dialog = new MQLQueryBuilderDialog(shell);
    if (dialog.open() == Window.OK) {
      try {
        System.out.println("mqlQuery.getXML() = \n" + dialog.getMqlQuery().getXML()); //$NON-NLS-1$
        System.out.println("\nmqlQuery.getQuery() = \n" + dialog.getMqlQuery().getQuery(true)); //$NON-NLS-1$
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
