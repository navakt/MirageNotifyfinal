package com.miragenotify.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.miragenotify.R;
import com.miragenotify.database.AppDatabase;
import com.miragenotify.model.NotificationLog;
import com.miragenotify.model.NotificationRule;
import com.miragenotify.utils.PreferenceManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Core service that intercepts and modifies notifications.
 * It deletes the original notification and posts a modified one through this app.
 */
public class NotificationInterceptorService extends NotificationListenerService {
    
    private static final String TAG = "NotificationInterceptor";
    private static final String FOREGROUND_CHANNEL_ID = "mirage_notify_service";
    private static final String MODIFIED_CHANNEL_ID = "modified_notifications";
    private static final int FOREGROUND_NOTIFICATION_ID = 1001;
    
    private AppDatabase database;
    private ExecutorService executorService;
    private PreferenceManager preferenceManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        
        database = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        preferenceManager = new PreferenceManager(this);
        
        createNotificationChannels();
        
        Notification notification = createForegroundNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startForeground(FOREGROUND_NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
        } else {
            startForeground(FOREGROUND_NOTIFICATION_ID, notification);
        }
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn == null || sbn.getPackageName() == null || sbn.getPackageName().equals(getPackageName())) {
            return;
        }
        
        if (preferenceManager == null || !preferenceManager.isServiceEnabled()) {
            return;
        }
        
        // Prevent infinite loops by checking for our custom flag
        if (sbn.getNotification().extras.getBoolean("mirage_modified", false)) {
            return;
        }
        
        executorService.execute(() -> processNotification(sbn));
    }
    
    private void processNotification(StatusBarNotification sbn) {
        try {
            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            
            String packageName = sbn.getPackageName();
            CharSequence titleCs = extras.getCharSequence(Notification.EXTRA_TITLE);
            CharSequence textCs = extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence subTextCs = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);

            String originalTitle = titleCs != null ? titleCs.toString() : "";
            String originalContent = textCs != null ? textCs.toString() : "";
            String originalSender = subTextCs != null ? subTextCs.toString() : "";
            
            String appName = getAppName(packageName);
            List<NotificationRule> rules = database.notificationRuleDao().getEnabledRulesForPackage(packageName);
            
            String modifiedTitle = originalTitle;
            String modifiedContent = originalContent;
            String modifiedSender = originalSender;
            boolean wasModified = false;
            long appliedRuleId = 0;
            
            for (NotificationRule rule : rules) {
                if (applyRule(rule, modifiedTitle, modifiedContent, modifiedSender)) {
                    wasModified = true;
                    appliedRuleId = rule.getId();
                    
                    if (rule.isModifyTitle()) modifiedTitle = modifyText(modifiedTitle, rule);
                    if (rule.isModifyContent()) modifiedContent = modifyText(modifiedContent, rule);
                    if (rule.isModifySender()) modifiedSender = modifyText(modifiedSender, rule);
                    break;
                }
            }
            
            logNotification(packageName, appName, originalTitle, originalContent, originalSender,
                    modifiedTitle, modifiedContent, modifiedSender, wasModified, appliedRuleId);
            
            if (wasModified) {
                // 1. Delete (cancel) the original message
                cancelNotification(sbn.getKey());
                
                // 2. Push the modified message through our app
                postModifiedNotification(sbn, modifiedTitle, modifiedContent, modifiedSender);
                Log.d(TAG, "Replaced original notification from " + packageName + " with modified version.");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error processing notification", e);
        }
    }
    
    private boolean applyRule(NotificationRule rule, String title, String content, String sender) {
        String searchText = rule.getSearchText();
        if (searchText == null || searchText.isEmpty()) return true;
        return (title != null && title.contains(searchText)) ||
               (content != null && content.contains(searchText)) ||
               (sender != null && sender.contains(searchText));
    }
    
    private String modifyText(String text, NotificationRule rule) {
        if (text == null) return "";
        switch (rule.getModificationType()) {
            case REPLACE_TEXT:
                String search = rule.getSearchText();
                if (search != null && !search.isEmpty()) {
                    return text.replace(search, rule.getReplacementText() != null ? rule.getReplacementText() : "");
                }
                return text;
            case MASK_TEXT:
                String searchMask = rule.getSearchText();
                if (searchMask != null && !searchMask.isEmpty()) {
                    String masked = searchMask.replaceAll(".", "*");
                    return text.replace(searchMask, masked);
                }
                return text;
            case RENAME_SENDER:
                return rule.getReplacementText() != null ? rule.getReplacementText() : text;
            default:
                return text;
        }
    }
    
    private void postModifiedNotification(StatusBarNotification sbn, String title, String content, String sender) {
        try {
            Notification original = sbn.getNotification();
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            
            Bundle extras = new Bundle(original.extras);
            extras.putBoolean("mirage_modified", true);

            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new Notification.Builder(this, MODIFIED_CHANNEL_ID);
            } else {
                builder = new Notification.Builder(this);
            }
            
            // Mirror the original app's icon but add our app's identity through the channel
            try {
                Drawable appIcon = getPackageManager().getApplicationIcon(sbn.getPackageName());
                builder.setLargeIcon(drawableToBitmap(appIcon));
            } catch (Exception ignored) {}

            builder.setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.drawable.ic_notification) // Pushing "through our app"
                    .setPriority(Notification.PRIORITY_MAX) 
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .addExtras(extras);
            
            if (sender != null && !sender.isEmpty()) builder.setSubText(sender);
            
            // Preserve the original notification's PendingIntent for seamless interaction
            if (original.contentIntent != null) {
                builder.setContentIntent(original.contentIntent);
            }
            
            String tag = "Mirage_" + sbn.getPackageName();
            if (notificationManager != null) {
                notificationManager.notify(tag, sbn.getId(), builder.build());
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error posting modified notification", e);
        }
    }
    
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), 
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager == null) return;

            NotificationChannel serviceChannel = new NotificationChannel(
                    FOREGROUND_CHANNEL_ID, "Mirage Service Status", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(serviceChannel);

            NotificationChannel modifiedChannel = new NotificationChannel(
                    MODIFIED_CHANNEL_ID, "Privacy Modifications", NotificationManager.IMPORTANCE_HIGH);
            modifiedChannel.setDescription("Notifications modified by Mirage Notify for privacy");
            manager.createNotificationChannel(modifiedChannel);
        }
    }
    
    private Notification createForegroundNotification() {
        Notification.Builder builder = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                new Notification.Builder(this, FOREGROUND_CHANNEL_ID) : new Notification.Builder(this);
        
        return builder.setContentTitle("Mirage Notify Active")
                .setContentText("Monitoring notifications for privacy")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(Notification.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }

    private void logNotification(String packageName, String appName, String originalTitle, String originalContent, 
                                String originalSender, String modifiedTitle, String modifiedContent, 
                                String modifiedSender, boolean wasModified, long ruleId) {
        NotificationLog log = new NotificationLog();
        log.setPackageName(packageName);
        log.setAppName(appName);
        log.setOriginalTitle(originalTitle);
        log.setOriginalContent(originalContent);
        log.setOriginalSender(originalSender);
        log.setModifiedTitle(modifiedTitle);
        log.setModifiedContent(modifiedContent);
        log.setModifiedSender(modifiedSender);
        log.setWasModified(wasModified);
        log.setRuleId(ruleId);
        log.setTimestamp(System.currentTimeMillis());
        database.notificationLogDao().insert(log);
    }
    
    private String getAppName(String packageName) {
        try {
            return getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(packageName, 0)).toString();
        } catch (Exception e) {
            return packageName;
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) executorService.shutdown();
    }
}
