package org.pentaho.commons.metadata.mqleditor.beans;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.commons.metadata.mqleditor.IDomain;

public class Domain implements IDomain<Model> {

  private String id, name;

  private List<Model> models = new ArrayList<Model>();

  public String getId() {
    return id;
  }

  public List<Model> getModels() {
    return models;
  }

  public String getName() {
    return name;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setModels(List<Model> models) {
    this.models = models;
  }

  public void setName(String name) {
    this.name = name;
  }

}
