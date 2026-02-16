package com.miragenotify.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a logged notification (original and modified)
 */
@Entity(tableName = "notification_logs")
public class NotificationLog {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String packageName;
    private String appName;
    
    // Original notification data
    private String originalTitle;
    private String originalContent;
    private String originalSender;
    
    // Modified notification data
    private String modifiedTitle;
    private String modifiedContent;
    private String modifiedSender;
    
    private boolean wasModified;
    private long ruleId; // ID of the rule that was applied (0 if none)
    private long timestamp;

    public NotificationLog() {
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }

    public String getOriginalSender() {
        return originalSender;
    }

    public void setOriginalSender(String originalSender) {
        this.originalSender = originalSender;
    }

    public String getModifiedTitle() {
        return modifiedTitle;
    }

    public void setModifiedTitle(String modifiedTitle) {
        this.modifiedTitle = modifiedTitle;
    }

    public String getModifiedContent() {
        return modifiedContent;
    }

    public void setModifiedContent(String modifiedContent) {
        this.modifiedContent = modifiedContent;
    }

    public String getModifiedSender() {
        return modifiedSender;
    }

    public void setModifiedSender(String modifiedSender) {
        this.modifiedSender = modifiedSender;
    }

    public boolean isWasModified() {
        return wasModified;
    }

    public void setWasModified(boolean wasModified) {
        this.wasModified = wasModified;
    }

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
