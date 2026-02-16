package com.miragenotify.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.miragenotify.database.AppDatabase;
import com.miragenotify.model.NotificationLog;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel for managing notification logs
 */
public class LogViewModel extends AndroidViewModel {
    
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final LiveData<List<NotificationLog>> allLogs;
    private final LiveData<Integer> logCount;
    private final LiveData<Integer> modifiedCount;
    
    public LogViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        executorService = Executors.newSingleThreadExecutor();
        allLogs = database.notificationLogDao().getAllLogs();
        logCount = database.notificationLogDao().getLogCount();
        modifiedCount = database.notificationLogDao().getModifiedCount();
    }
    
    public LiveData<List<NotificationLog>> getAllLogs() {
        return allLogs;
    }
    
    public LiveData<Integer> getLogCount() {
        return logCount;
    }
    
    public LiveData<Integer> getModifiedCount() {
        return modifiedCount;
    }
    
    public void delete(NotificationLog log) {
        executorService.execute(() -> database.notificationLogDao().delete(log));
    }
    
    public void deleteAll() {
        executorService.execute(() -> database.notificationLogDao().deleteAll());
    }
    
    public void deleteOlderThan(long timestamp) {
        executorService.execute(() -> database.notificationLogDao().deleteOlderThan(timestamp));
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
