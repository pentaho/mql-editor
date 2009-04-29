package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import java.util.List;

import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.commons.metadata.mqleditor.editor.service.MQLEditorService;
import org.pentaho.ui.xul.XulComponent;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulServiceCallback;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.components.XulTreeCell;
import org.pentaho.ui.xul.components.XulTreeCol;
import org.pentaho.ui.xul.containers.XulDialog;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.containers.XulTreeItem;
import org.pentaho.ui.xul.containers.XulTreeRow;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

public class PreviewController extends AbstractXulEventHandler {

  private Workspace workspace;

  private BindingFactory bf;

  private XulTree previewTree;
  
  private XulDialog previewDialog;

  private MQLEditorService service;

  private String query;

  private int page = 1;

  private String[][] previewData;
  
  private int previewLimit = 10;
  
  public void init() {
    previewTree = (XulTree) document.getElementById("previewTree");
    previewDialog = (XulDialog) document.getElementById("previewDialog");
    bf.createBinding(document.getElementById("previewLimit"), "value", this, "previewLimit", new BindingConvertor<String, Integer>(){

      @Override
      public Integer sourceToTarget(String value) {
        return Integer.parseInt(value);
      }

      @Override
      public String targetToSource(Integer value) {
        return ""+value;  
      }
      
    });
  }

  public void setBindingFactory(BindingFactory bf) {
    this.bf = bf;
  }

  public void setWorkspace(Workspace workspace) {
    this.workspace = workspace;
  }

  public String getName() {
    return "previewController";

  }

  public MQLEditorService getService() {

    return service;
  }

  public void setService(MQLEditorService service) {

    this.service = service;
  }

  public void showPreview() {
    service.getPreviewData(workspace.getMqlQuery(), page, previewLimit, new XulServiceCallback<String[][]>() {

      public void error(String message, Throwable error) {
        System.out.println(message);
        error.printStackTrace();
        setPreviewData(new String[][]{});
        openDialog();
      }

      public void success(String[][] retVal) {
        setPreviewData(retVal);
        openDialog();
      }
    });
  }

  private void openDialog() {
    previewDialog.show();
  }

  public String[][] getPreviewData() {
    return previewData;
  }
  
  public void upateQuery(){

    service.getPreviewData(workspace.getMqlQuery(), page, previewLimit, new XulServiceCallback<String[][]>() {

      public void error(String message, Throwable error) {
        System.out.println(message);
        error.printStackTrace();
        setPreviewData(new String[][]{});
      }

      public void success(String[][] retVal) {
        setPreviewData(retVal);
      }
    });
  }

  public void setPreviewData(String[][] previewData) {
    this.previewData = previewData;
    if(previewData.length == 0){
      previewTree.setElements(null);
      return;
    }
    
    
    // Adjust number of columns as needed.
    int colCount = previewData[0].length;
    int curTreeColCount = previewTree.getColumns().getColumnCount();
    try{
      if(colCount > curTreeColCount){ // Add new Columns
        for(int i = (colCount - curTreeColCount); i > 0; i--){
          previewTree.getColumns().addColumn( (XulTreeCol) document.createElement("treecol"));
        }
      } else if (colCount < curTreeColCount){ // Remove un-needed exiting columns
        List<XulComponent> cols = previewTree.getColumns().getChildNodes();
        
        for(int i = (curTreeColCount - colCount); i < cols.size(); i++){
          previewTree.getColumns().removeChild(cols.get(i));
        }
      }
    } catch (XulException e){
      // TODO: add logging!!
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
    previewTree.getRootChildren().removeAll();
    try{
      for(int i=0; i < previewData.length; i++){
        XulTreeItem item = (XulTreeItem) document.createElement("treeitem");
        XulTreeRow row = (XulTreeRow) document.createElement("treerow");
        
        String[] r = previewData[i];
        for(int y=0; y<r.length; y++){
          XulTreeCell cell = (XulTreeCell) document.createElement("treecell");
          cell.setLabel( (r[y] != null)? r[y] : "");
          row.addCell(cell);
        }
  
        item.addChild(row);
        previewTree.getRootChildren().addChild(item);
      }
      previewTree.update();
    } catch(XulException e){
      // TODO: add logging
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    
  }
  
  public void closeDialog(){
    previewDialog.hide();
  }

  public int getPreviewLimit() {
  
    return previewLimit;
  }

  public void setPreviewLimit(int previewLimit) {
  
    this.previewLimit = previewLimit;
  }
  
  
}
