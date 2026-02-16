package com.miragenotify.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a notification modification rule
 */
@Entity(tableName = "notification_rules")
public class NotificationRule {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String ruleName;
    private String targetPackageName; // Package name of the app to target
    private boolean isEnabled;
    private ModificationType modificationType;
    
    // Modification parameters
    private String searchText; // Text to search for
    private String replacementText; // Text to replace with
    private boolean modifyTitle;
    private boolean modifyContent;
    private boolean modifySender;
    
    private long createdAt;
    private long updatedAt;

    public NotificationRule() {
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.isEnabled = true;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getTargetPackageName() {
        return targetPackageName;
    }

    public void setTargetPackageName(String targetPackageName) {
        this.targetPackageName = targetPackageName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public ModificationType getModificationType() {
        return modificationType;
    }

    public void setModificationType(ModificationType modificationType) {
        this.modificationType = modificationType;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getReplacementText() {
        return replacementText;
    }

    public void setReplacementText(String replacementText) {
        this.replacementText = replacementText;
    }

    public boolean isModifyTitle() {
        return modifyTitle;
    }

    public void setModifyTitle(boolean modifyTitle) {
        this.modifyTitle = modifyTitle;
    }

    public boolean isModifyContent() {
        return modifyContent;
    }

    public void setModifyContent(boolean modifyContent) {
        this.modifyContent = modifyContent;
    }

    public boolean isModifySender() {
        return modifySender;
    }

    public void setModifySender(boolean modifySender) {
        this.modifySender = modifySender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Types of modifications that can be applied
     */
    public enum ModificationType {
        REPLACE_TEXT,    // Replace specific text
        MASK_TEXT,       // Hide/mask sensitive content
        RENAME_SENDER,   // Change sender name
        CUSTOM          // Custom modification logic
    }
}
