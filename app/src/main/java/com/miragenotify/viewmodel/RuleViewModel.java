package com.miragenotify.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.miragenotify.database.AppDatabase;
import com.miragenotify.model.NotificationRule;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel for managing notification rules
 */
public class RuleViewModel extends AndroidViewModel {
    
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final LiveData<List<NotificationRule>> allRules;
    private final LiveData<Integer> ruleCount;
    
    public RuleViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        executorService = Executors.newSingleThreadExecutor();
        allRules = database.notificationRuleDao().getAllRules();
        ruleCount = database.notificationRuleDao().getRuleCount();
    }
    
    public LiveData<List<NotificationRule>> getAllRules() {
        return allRules;
    }
    
    public LiveData<Integer> getRuleCount() {
        return ruleCount;
    }
    
    public void insert(NotificationRule rule) {
        executorService.execute(() -> database.notificationRuleDao().insert(rule));
    }
    
    public void update(NotificationRule rule) {
        rule.setUpdatedAt(System.currentTimeMillis());
        executorService.execute(() -> database.notificationRuleDao().update(rule));
    }
    
    public void delete(NotificationRule rule) {
        executorService.execute(() -> database.notificationRuleDao().delete(rule));
    }
    
    public void deleteAll() {
        executorService.execute(() -> database.notificationRuleDao().deleteAll());
    }
    
    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
