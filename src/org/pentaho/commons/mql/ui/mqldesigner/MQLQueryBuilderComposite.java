package org.pentaho.commons.mql.ui.mqldesigner;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.pentaho.pms.core.CWM;
import org.pentaho.pms.core.exception.CWMException;
import org.pentaho.pms.factory.CwmSchemaFactory;
import org.pentaho.pms.mql.MQLQuery;
import org.pentaho.pms.schema.BusinessCategory;
import org.pentaho.pms.schema.BusinessColumn;
import org.pentaho.pms.schema.BusinessModel;
import org.pentaho.pms.schema.OrderBy;
import org.pentaho.pms.schema.SchemaMeta;
import org.pentaho.pms.schema.WhereCondition;

import be.ibridge.kettle.core.list.UniqueList;

public class MQLQueryBuilderComposite extends Composite implements SelectionListener {

  private static Image MOVE_TO_ICON = null;
  private static Image MOVE_UP_ICON = null;
  private static Image MOVE_DOWN_ICON = null;
  private static Image REMOVE_ACTION_ICON = null;
  static final String LOCALE = Locale.getDefault().toString();
  
  MQLColumnsTable mqlDetailsTable;
  MQLOrderTable mqlOrderTable;
  MQLConditionsTable mqlFiltersTable;
  BusinessTablesTree businessTablesTree;
  ArrayList businessModels = new ArrayList();
  Combo viewCombo;
  SchemaMeta schemaMeta;
  
  public MQLQueryBuilderComposite(Composite parent, int style) {
    super(parent, style);
    if (MOVE_TO_ICON == null) {
      MOVE_TO_ICON = loadImageResource("icons/e_forward.gif");
    }
    if (MOVE_UP_ICON == null) {
      MOVE_UP_ICON = loadImageResource("icons/move_up.gif");
    }
    if (MOVE_DOWN_ICON == null) {
      MOVE_DOWN_ICON = loadImageResource("icons/move_down.gif");
    }
    if (REMOVE_ACTION_ICON == null) {
      REMOVE_ACTION_ICON = loadImageResource("icons/delete.gif");
    }
    setLayout(new GridLayout(3, false));
    
    GridData gridData = new GridData();
    gridData.horizontalSpan = 3;
    WidgetFactory.createLabel(this, Messages.getString("MQLColumnSelectorComposite.BUSINESS_VIEW")).setLayoutData(gridData); //$NON-NLS-1$
    viewCombo = WidgetFactory.createCombo(this, SWT.NONE);
    gridData = new GridData();
    gridData.horizontalAlignment = SWT.FILL;
    viewCombo.setLayoutData(gridData);
    viewCombo.addSelectionListener(this);
    
    String[] domains = null;
    ArrayList items = new ArrayList();
    BusinessModel businessModelToSelect = null;
    try {
      domains = CWM.getDomainNames();
      if (domains.length > 0) {
        CWM cwm = CWM.getInstance(domains[0], false);
        CwmSchemaFactory cwmSchemaFactory = new CwmSchemaFactory();
        schemaMeta = cwmSchemaFactory.getSchemaMeta(cwm);
        schemaMeta.setActiveLocale("en_US"); //$NON-NLS-1$
        UniqueList uniqueList = schemaMeta.getBusinessModels();
        if (uniqueList != null) {
          for (Iterator iter = uniqueList.iterator(); iter.hasNext();) {
            BusinessModel businessModel = (BusinessModel)iter.next();
            businessModels.add(businessModel);
            items.add(businessModel.getDisplayName(LOCALE));
          }
        }
      }
    } catch (CWMException e) {
      e.printStackTrace();
    }
    viewCombo.setItems((String[])items.toArray(new String[0]));
    
    gridData = new GridData();
    gridData.horizontalSpan = 2;
    WidgetFactory.createLabel(this, "").setLayoutData(gridData); //$NON-NLS-1$
    
    WidgetFactory.createLabel(this, Messages.getString("MQLColumnSelectorComposite.AVAILABLE_ITEMS")); //$NON-NLS-1$
    
    ToolBar toolBar = new ToolBar(this, SWT.FLAT);
    ToolItem toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_TO_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_TO_DETAILS"));  //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {

      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        moveSelectedColumnsToDetails();
      }
    });
    
    gridData = new GridData();
    gridData.verticalSpan = 2;
    gridData.verticalAlignment = SWT.CENTER;
    toolBar.setLayoutData(gridData);
  
    Composite composite = WidgetFactory.createComposite(this);
    GridLayout gridLayout = new GridLayout(2, false);
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    composite.setLayout(gridLayout);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    composite.setLayoutData(gridData);
    
    gridData = new GridData();
    gridData.verticalAlignment = SWT.END;
    WidgetFactory.createLabel(composite, Messages.getString("MQLColumnSelectorComposite.SELECTED_ITEMS")).setLayoutData(gridData); //$NON-NLS-1$
    
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = GridData.END;
    createDetailsToolbar(composite).setLayoutData(gridData);
        
    businessTablesTree = new BusinessTablesTree(this, SWT.BORDER | SWT.MULTI);
    gridData = new GridData(GridData.FILL_VERTICAL);
    gridData.verticalSpan = 5;
    gridData.widthHint = 200;
    businessTablesTree.getControl().setLayoutData(gridData);
    
    mqlDetailsTable = new MQLColumnsTable(this);
    gridData = new GridData(GridData.FILL_BOTH);
    mqlDetailsTable.setLayoutData(gridData);
    
    toolBar = new ToolBar(this, SWT.FLAT);
    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_TO_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_TO_FILTERS"));  //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {

      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        moveSelectedColumnsToFilters();
      }
    });
    gridData = new GridData();
    gridData.verticalSpan = 2;
    gridData.verticalAlignment = SWT.CENTER;
    toolBar.setLayoutData(gridData);
    
    composite = WidgetFactory.createComposite(this);
    gridLayout = new GridLayout(2, false);
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    composite.setLayout(gridLayout);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    composite.setLayoutData(gridData);
    
    WidgetFactory.createLabel(composite, Messages.getString("MQLColumnSelectorComposite.FILTERS")); //$NON-NLS-1$
    
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = GridData.END;
    createFiltersToolbar(composite).setLayoutData(gridData);
    
    mqlFiltersTable = new MQLConditionsTable(this);
    gridData = new GridData(GridData.FILL_BOTH);
    mqlFiltersTable.setLayoutData(gridData);
      
    toolBar = new ToolBar(this, SWT.FLAT);
    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_TO_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_TO_ORDER_BY"));  //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {

      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        moveSelectedColumnsToSortOrder();
      }
    });
    gridData = new GridData();
    gridData.verticalSpan = 2;
    gridData.verticalAlignment = SWT.CENTER;
    toolBar.setLayoutData(gridData);
    
    composite = WidgetFactory.createComposite(this);
    gridLayout = new GridLayout(2, false);
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    composite.setLayout(gridLayout);
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    composite.setLayoutData(gridData);
    
    WidgetFactory.createLabel(composite, Messages.getString("MQLColumnSelectorComposite.ORDER_BY")); //$NON-NLS-1$
    
    gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = GridData.END;
    createSortToolbar(composite).setLayoutData(gridData);
    
    mqlOrderTable = new MQLOrderTable(this);
    gridData = new GridData(GridData.FILL_BOTH);
    mqlOrderTable.setLayoutData(gridData);
    
    if (businessModels.size() == 1) {
      setSelectedBusinessModel((BusinessModel)businessModels.get(0));
    }

  }
  
  protected void moveSelectedColumnsToDetails() {
    BusinessColumn[] businessColumns = businessTablesTree.getSelectedBusinessColumns();
    for (int i = 0; i < businessColumns.length; i++) {
      mqlDetailsTable.add(businessColumns[i]);
    }
  }

  protected void moveSelectedColumnsToFilters() {
    BusinessColumn[] businessColumns = businessTablesTree.getSelectedBusinessColumns();
    for (int i = 0; i < businessColumns.length; i++) {
      MQLWhereConditionModel whereCondition = null;
      if (mqlFiltersTable.getConditions().length > 0) {
        whereCondition = new MQLWhereConditionModel("AND", businessColumns[i], "=\" \""); //$NON-NLS-1$ //$NON-NLS-2$
      } else {
        whereCondition = new MQLWhereConditionModel(null, businessColumns[i], "=\" \""); //$NON-NLS-1$
      }
      mqlFiltersTable.add(whereCondition);
    }
  }

  protected void moveSelectedColumnsToSortOrder() {
    BusinessColumn[] businessColumns = businessTablesTree.getSelectedBusinessColumns();
    for (int i = 0; i < businessColumns.length; i++) {
      OrderBy orderBy = new OrderBy(businessColumns[i]);
      mqlOrderTable.add(orderBy);
    }
  }
  
  public Composite createAvailItemsComposite(Composite parent) {
    Composite composite = WidgetFactory.createComposite(parent);
    composite.setLayout(new GridLayout());
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    return composite;
  }
    
  public void setSelectedBusinessModel(BusinessModel businessModel) {
    if ((businessTablesTree.getInput() == null) || !businessTablesTree.getInput().equals(businessModel)) {
      viewCombo.clearSelection();
      if (businessModel != null) {
        for (int i = 0; i < businessModels.size(); i++) {
          if (businessModel.equals(businessModels.get(i))) {
            viewCombo.select(i);
            break;
          }
        }
      }
      businessTablesTree.setInput(businessModel);
      mqlDetailsTable.clear();
      mqlFiltersTable.clear();
      mqlOrderTable.clear();
    }      
  }
  
  protected ToolBar createFiltersToolbar(Composite parent) {
    ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
    ToolItem toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_DOWN_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_DOWN")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        int[] rows = mqlFiltersTable.getRowSelection();
        if ((rows.length == 1) && (rows[0] != mqlFiltersTable.getModel().getRowCount() - 1)) {
          mqlFiltersTable.move(rows[0], rows[0] + 1);
          mqlFiltersTable.setSelection(0, rows[0] + 1, true);
        }
      }
    });

    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_UP_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_UP")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        int[] rows = mqlFiltersTable.getRowSelection();
        if ((rows.length == 1) && (rows[0] != mqlFiltersTable.getModel().getFixedHeaderRowCount())) {
          mqlFiltersTable.move(rows[0], rows[0] - 1);
          mqlFiltersTable.setSelection(0, rows[0] - 1, true);
        }
      }
    });
    
    toolItem = new ToolItem(toolBar, SWT.SEPARATOR);

    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(REMOVE_ACTION_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.REMOVE")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        mqlFiltersTable.removeSelectedRows();
      }
    });
    return toolBar;
  }
  
  protected ToolBar createSortToolbar(Composite parent) {
    ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
    ToolItem toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_DOWN_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_DOWN")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        int[] rows = mqlOrderTable.getRowSelection();
        if ((rows.length == 1) && (rows[0] != mqlOrderTable.getModel().getRowCount() - 1)) {
          mqlOrderTable.move(rows[0], rows[0] + 1);
          mqlOrderTable.setSelection(0, rows[0] + 1, true);
        }
      }
    });

    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_UP_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_UP")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        int[] rows = mqlOrderTable.getRowSelection();
        if ((rows.length == 1) && (rows[0] != mqlOrderTable.getModel().getFixedHeaderRowCount())) {
          mqlOrderTable.move(rows[0], rows[0] - 1);
          mqlOrderTable.setSelection(0, rows[0] - 1, true);
        }
      }
    });
    
    new ToolItem(toolBar, SWT.SEPARATOR);

    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(REMOVE_ACTION_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.REMOVE")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        mqlOrderTable.removeSelectedRows();
      }
    });
    return toolBar;
  }
  
  protected ToolBar createDetailsToolbar(Composite parent) {
    ToolBar toolBar = new ToolBar(parent, SWT.FLAT);
    ToolItem toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_DOWN_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_DOWN")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        int[] rows = mqlDetailsTable.getRowSelection();
        if ((rows.length == 1) && (rows[0] != mqlDetailsTable.getModel().getRowCount() - 1)) {
          mqlDetailsTable.move(rows[0], rows[0] + 1);
          mqlDetailsTable.setSelection(0, rows[0] + 1, true);
        }
      }
    });

    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(MOVE_UP_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.MOVE_UP")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        int[] rows = mqlDetailsTable.getRowSelection();
        if ((rows.length == 1) && (rows[0] != mqlDetailsTable.getModel().getFixedHeaderRowCount())) {
          mqlDetailsTable.move(rows[0], rows[0] - 1);
          mqlDetailsTable.setSelection(0, rows[0] - 1, true);
        }
      }
    });
    
    new ToolItem(toolBar, SWT.SEPARATOR);

    toolItem = new ToolItem(toolBar, SWT.NULL);
    toolItem.setImage(REMOVE_ACTION_ICON);
    toolItem.setToolTipText(Messages.getString("MQLColumnSelectorComposite.REMOVE")); //$NON-NLS-1$
    toolItem.addSelectionListener(new SelectionListener() {
      public void widgetDefaultSelected(SelectionEvent e) {
      }

      public void widgetSelected(SelectionEvent e) {
        mqlDetailsTable.removeSelectedRows();
      }
    });
    return toolBar;
  }
  
  public BusinessColumn[] getDetailColumns() {
    return mqlDetailsTable.getBusinessColumns();
  }
  
  public void setDetailColumns(BusinessColumn[] businessColumns) {
    mqlDetailsTable.setBusinessColumns(businessColumns);
  }
  
  public MQLWhereConditionModel[] getConditions() {
    return mqlFiltersTable.getConditions();
  }
  
  public void setConditions(MQLWhereConditionModel[] conditions) {
    mqlFiltersTable.setConditions(conditions);
  }
  
  public OrderBy[] getOrderBy() {
    return mqlOrderTable.getOrderBy();
  }
  
  public void setOrderBy(OrderBy[] orderBy) {
    mqlOrderTable.setOrderBy(orderBy);
  }

  public void widgetDefaultSelected(SelectionEvent e) {
    // TODO Auto-generated method stub
    
  }

  public void widgetSelected(SelectionEvent e) {
    int index = viewCombo.getSelectionIndex();
    if (index >= 0) {
      viewCombo.removeSelectionListener(this);
      setSelectedBusinessModel((BusinessModel)businessModels.get(index));
      viewCombo.addSelectionListener(this);
    }   
  }
  
  public MQLQuery getMqlQuery() {
    MQLQuery mqlQuery = null;
    try {

      BusinessColumn[] businessColumns = getDetailColumns();
      if (businessColumns.length > 0) {
        BusinessModel businessModel = (BusinessModel)businessTablesTree.getInput();
        mqlQuery = new MQLQuery(schemaMeta, businessModel, "en_US"); //$NON-NLS-1$
        mqlQuery.setSelections(Arrays.asList(businessColumns));
        MQLWhereConditionModel wherelist[] = getConditions();
        ArrayList constraints = new ArrayList();
        BusinessCategory rootCat = businessModel.getRootCategory();

        for (int i = 0; i < wherelist.length; i++) {
          BusinessCategory businessCategory = rootCat.findBusinessCategoryForBusinessColumn(wherelist[i].getField());
          
          constraints.add(
              new WhereCondition(businessModel, wherelist[i].getOperator(),
                  "[" + businessCategory.getId() + "." + //$NON-NLS-1$ //$NON-NLS-2$
                  wherelist[i].getField().getId() +"] " + wherelist[i].getCondition()) //$NON-NLS-1$
          );
        }
        mqlQuery.setConstraints(constraints);
        mqlQuery.setOrder(Arrays.asList(getOrderBy()));
    
      }
    } catch (Throwable e) { // PMSFormulaException e) {
      e.printStackTrace();
    }
    return mqlQuery;
  }
  
  
  
  public void setMqlQuery(MQLQuery mqlQuery) {
    setSelectedBusinessModel(mqlQuery.getModel());
    if (viewCombo.getSelectionIndex() != -1) {
      List businessColumns = mqlQuery.getSelections();
      setDetailColumns((BusinessColumn[])businessColumns.toArray(new BusinessColumn[0]));
      List constraints = mqlQuery.getConstraints();
      // convert over to where conditions
      MQLWhereConditionModel whereConditions[] = new MQLWhereConditionModel[constraints.size()];
      for (int i = 0; i < constraints.size(); i++) {
        WhereCondition cond = (WhereCondition)constraints.get(i);
        
        whereConditions[i] = new MQLWhereConditionModel(mqlQuery.getModel(), cond);
      }
      
      setConditions(whereConditions);
      List orderBy = mqlQuery.getOrder();
      setOrderBy((OrderBy[])orderBy.toArray(new OrderBy[0]));
    }    
  }
  
  private Image loadImageResource(String name) {
    try {
      Image ret = null;
      InputStream is = BusinessTablesTree.class.getResourceAsStream(name);
      if (is != null) {
        ret = new Image(getDisplay(),is);
        is.close();
      }
      return ret;
    } catch (Exception e1) {
      return null;
    }
  }
}
