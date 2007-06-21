package org.pentaho.commons.mql.ui.mqldesigner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Section;

public class WidgetFactory {

  public static Button createButton(Composite parent, String text, int style) {
    Button button = new Button(parent, style);
    if (text != null)
      button.setText(text);
    adapt(button);
    return button;
  }
  
  public static Combo createCombo(Composite parent, int style) {
    Combo combo = new Combo(parent, style);
    adapt(combo);
    return combo;
  }
  
  public static Text createText(Composite parent, String value, int style) {
    Text text = new Text(parent, style);
    if (value != null) {
      text.setText(value);
    }
    adapt(text);
    return text;
  }
  
  public static Text createText(Composite parent, String value) {
    return createText(parent, value, SWT.SINGLE | SWT.BORDER);
  }
  
  public static Table createTable(Composite parent, int style) {
    Table table = new Table(parent, style);
    adapt(table);
    return table;
  }
  
  public static Label createLabel(Composite parent, String text, int style) {
    Label label = new Label(parent, style);
    if (text != null) {
      label.setText(text);
    }
    adapt(label);
    return label;
  }
  
  public static Label createLabel(Composite parent, String text) {
    return createLabel(parent, text, SWT.NONE);
  }
  
  public static Composite createComposite(Composite parent, int style) {
    Composite composite = new Composite(parent, style);
    adapt(composite);
    return composite;
  }
  
  public static Composite createComposite(Composite parent) {
    return createComposite(parent, SWT.NULL);
  }
  
  public static void adapt(Control control) {
  }
  
  public static void adapt(Composite composite) {
  }
  
  public static Tree createTree(Composite parent, int style) {
    Tree tree = new Tree(parent, style);
    adapt(tree);
    return tree;
  }
  
  public static Section createSection(Composite parent, int sectionStyle) {
    Section section = new Section(parent, sectionStyle);
    section.setMenu(parent.getMenu());
    adapt(section);
    return section;
  }
  
  public static Label createSeparator(final Composite parent) {
    Label separator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
    if (parent instanceof Section)
      ((Section) parent).setSeparatorControl(separator);
    return separator;
  }
}
