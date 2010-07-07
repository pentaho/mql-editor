  /*
   * This program is free software; you can redistribute it and/or modify it under the 
   * terms of the GNU General Public License, version 2 as published by the Free Software 
   * Foundation.
   *
   * You should have received a copy of the GNU General Public License along with this 
   * program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html 
   * or from the Free Software Foundation, Inc., 
   * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   *
   * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
   * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
   * See the GNU General Public License for more details.
   */

package org.pentaho.gwt.widgets.client.filechooser;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * JAXB-safe version of {@code RepositoryFile}. ({@code RepositoryFile} has no zero-arg constructor and no public 
 * mutators.)
 * 
 * @see RepositoryFileAdapter
 * 
 * @author mlowery
 */

public class RepositoryFile implements Serializable {

    public RepositoryFile() {
      super();
    }

    protected String name;
    protected String id;
    protected Date createdDate;
    protected Date lastModifiedDate;
    protected boolean folder;
    protected String path;
    protected boolean hidden;
    protected boolean versioned;
    protected String versionId;
    protected boolean locked;
    protected String lockOwner;
    protected String lockMessage;
    protected Date lockDate;
    protected String owner;
    protected int ownerType = -1;
    protected String title;
    protected String description;  
    protected String locale;
    protected String originalParentFolderPath;
    protected String originalParentFolderId;
    protected Date deletedDate;
    protected List<LocalizedStringMapEntry> titleMapEntries;
    protected List<LocalizedStringMapEntry> descriptionMapEntries;
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getId() {
      return id;
    }
    public void setId(String id) {
      this.id = id;
    }
    public Date getCreatedDate() {
      return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
    }
    public Date getLastModifiedDate() {
      return lastModifiedDate;
    }
    public void setLastModifiedDate(Date lastModifiedDate) {
      this.lastModifiedDate = lastModifiedDate;
    }
    public boolean isFolder() {
      return folder;
    }
    public void setFolder(boolean folder) {
      this.folder = folder;
    }
    public String getPath() {
      return path;
    }
    public void setPath(String path) {
      this.path = path;
    }
    public boolean isHidden() {
      return hidden;
    }
    public void setHidden(boolean hidden) {
      this.hidden = hidden;
    }
    public boolean isVersioned() {
      return versioned;
    }
    public void setVersioned(boolean versioned) {
      this.versioned = versioned;
    }
    public String getVersionId() {
      return versionId;
    }
    public void setVersionId(String versionId) {
      this.versionId = versionId;
    }
    public boolean isLocked() {
      return locked;
    }
    public void setLocked(boolean locked) {
      this.locked = locked;
    }
    public String getLockOwner() {
      return lockOwner;
    }
    public void setLockOwner(String lockOwner) {
      this.lockOwner = lockOwner;
    }
    public String getLockMessage() {
      return lockMessage;
    }
    public void setLockMessage(String lockMessage) {
      this.lockMessage = lockMessage;
    }
    public Date getLockDate() {
      return lockDate;
    }
    public void setLockDate(Date lockDate) {
      this.lockDate = lockDate;
    }
    public String getOwner() {
      return owner;
    }
    public void setOwner(String owner) {
      this.owner = owner;
    }
    public int getOwnerType() {
      return ownerType;
    }
    public void setOwnerType(int ownerType) {
      this.ownerType = ownerType;
    }
    public String getTitle() {
      return title;
    }
    public void setTitle(String title) {
      this.title = title;
    }
    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public String getLocale() {
      return locale;
    }
    public void setLocale(String locale) {
      this.locale = locale;
    }
    public String getOriginalParentFolderPath() {
      return originalParentFolderPath;
    }
    public void setOriginalParentFolderPath(String originalParentFolderPath) {
      this.originalParentFolderPath = originalParentFolderPath;
    }
    public String getOriginalParentFolderId() {
      return originalParentFolderId;
    }
    public void setOriginalParentFolderId(String originalParentFolderId) {
      this.originalParentFolderId = originalParentFolderId;
    }
    public Date getDeletedDate() {
      return deletedDate;
    }
    public void setDeletedDate(Date deletedDate) {
      this.deletedDate = deletedDate;
    }
    public List<LocalizedStringMapEntry> getTitleMapEntries() {
      return titleMapEntries;
    }
    public void setTitleMapEntries(List<LocalizedStringMapEntry> titleMapEntries) {
      this.titleMapEntries = titleMapEntries;
    }
    public List<LocalizedStringMapEntry> getDescriptionMapEntries() {
      return descriptionMapEntries;
    }
    public void setDescriptionMapEntries(List<LocalizedStringMapEntry> descriptionMapEntries) {
      this.descriptionMapEntries = descriptionMapEntries;
    }
}
