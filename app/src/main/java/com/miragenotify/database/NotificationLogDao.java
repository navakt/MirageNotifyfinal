package com.miragenotify.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.miragenotify.model.NotificationLog;

import java.util.List;

/**
 * Data Access Object for NotificationLog
 */
@Dao
public interface NotificationLogDao {
    
    @Insert
    long insert(NotificationLog log);
    
    @Delete
    void delete(NotificationLog log);
    
    @Query("SELECT * FROM notification_logs ORDER BY timestamp DESC LIMIT 500")
    LiveData<List<NotificationLog>> getAllLogs();
    
    @Query("SELECT * FROM notification_logs WHERE packageName = :packageName ORDER BY timestamp DESC")
    LiveData<List<NotificationLog>> getLogsForPackage(String packageName);
    
    @Query("SELECT * FROM notification_logs WHERE wasModified = 1 ORDER BY timestamp DESC")
    LiveData<List<NotificationLog>> getModifiedLogs();
    
    @Query("DELETE FROM notification_logs WHERE timestamp < :timestamp")
    void deleteOlderThan(long timestamp);
    
    @Query("DELETE FROM notification_logs")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM notification_logs")
    LiveData<Integer> getLogCount();
    
    @Query("SELECT COUNT(*) FROM notification_logs WHERE wasModified = 1")
    LiveData<Integer> getModifiedCount();
}
