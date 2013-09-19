/*!
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
*/

package org.pentaho.commons.metadata.mqleditor.editor.controllers;

import org.pentaho.commons.metadata.mqleditor.editor.models.Workspace;
import org.pentaho.ui.xul.binding.Binding;
import org.pentaho.ui.xul.binding.BindingConvertor;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.containers.XulTree;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;
import org.pentaho.ui.xul.stereotype.Bindable;

public class ConditionsController extends AbstractXulEventHandler{

  private Workspace workspace;
  private XulTree conditionsTree;
  private BindingFactory bf;
  private XulTree conditionTree;
  
  @Bindable
  public void init(){
    conditionsTree = (XulTree) document.getElementById("conditionsTree");
    
    bf.createBinding(workspace, "conditions", conditionsTree, "elements");
    
    conditionTree = (XulTree) document.getElementById("conditionsTree");

    BindingConvertor<int[], Boolean> buttonConvertor = new BindingConvertor<int[], Boolean>(){

      @Override
      public Boolean sourceToTarget(int[] value) {
        return (value == null || value.length ==0);
      }

      @Override
      public int[] targetToSource(Boolean value) {return null;}
        
    };
    
    bf.setBindingType(Binding.Type.ONE_WAY);
    bf.createBinding(conditionTree,"selectedRows", "conditionUp", "disabled", buttonConvertor);
    bf.createBinding(conditionTree,"selectedRows", "conditionDown", "disabled", buttonConvertor);
    bf.createBinding(conditionTree,"selectedRows", "conditionRemove", "disabled", buttonConvertor);
  }

  public int getSelectedIndex() {
    int[] rows = this.conditionTree.getAbsoluteSelectedRows();
    return (rows != null && rows.length == 0) ? -1 : rows[0];
  }

  @Bindable
  public void moveUp(){
    try{
      int prevIndex = getSelectedIndex();
      workspace.getConditions().moveChildUp(getSelectedIndex());
      
      conditionTree.clearSelection();
      conditionTree.setSelectedRows(new int[]{prevIndex -1});
    } catch(IllegalArgumentException e){
      //out of bounds
    }
  }

  @Bindable
  public void moveDown(){
    int prevIndex = getSelectedIndex();
    try{
      workspace.getConditions().moveChildDown(getSelectedIndex());
      
      conditionTree.clearSelection();
      conditionTree.setSelectedRows(new int[]{prevIndex+1});
    } catch(IllegalArgumentException e){
      //out of bounds
    }
  }
  
  @Bindable
  public void remove(){
    if(getSelectedIndex() < 0){
      return;
    }
    workspace.getConditions().remove(getSelectedIndex());
    conditionTree.clearSelection();
  }

  
  public void setBindingFactory(BindingFactory bf){
    this.bf = bf;
  }
  
  public void setWorkspace(Workspace workspace) {
    this.workspace = workspace;
  }
  
  public String getName(){
    return "conditionsController";
    
  }
  
}

  
