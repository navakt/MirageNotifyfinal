package com.miragenotify.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.miragenotify.model.NotificationRule;

import java.util.List;

/**
 * Data Access Object for NotificationRule
 */
@Dao
public interface NotificationRuleDao {
    
    @Insert
    long insert(NotificationRule rule);
    
    @Update
    void update(NotificationRule rule);
    
    @Delete
    void delete(NotificationRule rule);
    
    @Query("SELECT * FROM notification_rules ORDER BY createdAt DESC")
    LiveData<List<NotificationRule>> getAllRules();
    
    @Query("SELECT * FROM notification_rules WHERE isEnabled = 1")
    List<NotificationRule> getEnabledRules();
    
    @Query("SELECT * FROM notification_rules WHERE targetPackageName = :packageName AND isEnabled = 1")
    List<NotificationRule> getEnabledRulesForPackage(String packageName);
    
    @Query("SELECT * FROM notification_rules WHERE id = :id")
    NotificationRule getRuleById(long id);
    
    @Query("DELETE FROM notification_rules")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM notification_rules")
    LiveData<Integer> getRuleCount();
}
