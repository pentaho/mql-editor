package org.pentaho.gwt.widgets.client.filechooser;

import java.io.Serializable;
import java.util.List;

public class RepositoryFileTree implements Serializable{
  RepositoryFile file;

  List<RepositoryFileTree> children;

  public RepositoryFile getFile() {
    return file;
  }

  public void setFile(RepositoryFile file) {
    this.file = file;
  }

  public List<RepositoryFileTree> getChildren() {
    return children;
  }

  public void setChildren(List<RepositoryFileTree> children) {
    this.children = children;
  }
}
