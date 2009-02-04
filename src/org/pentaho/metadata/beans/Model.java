package org.pentaho.metadata.beans;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.metadata.IModel;

public class Model implements IModel<Category> {

  private List<Category> categories = new ArrayList<Category>();

  private String id, name;

  public List<Category> getCategories() {
    return categories;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

}
