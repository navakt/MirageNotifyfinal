# Mirage Notify - Implementation Guide

## 🔍 Critical Implementation Details

### 1. NotificationListenerService Deep Dive

#### Service Lifecycle
```java
onCreate() → createNotificationChannel() → startForeground()
↓
onNotificationPosted() → processNotification() → applyRules()
↓
logNotification() → postModifiedNotification()
```

#### Key Methods Explained

**onNotificationPosted(StatusBarNotification sbn)**
- Called automatically when any app posts a notification
- Runs on main thread - must offload heavy work
- Receives StatusBarNotification object containing all notification data

**processNotification(StatusBarNotification sbn)**
```java
// Extract data from notification extras
Bundle extras = notification.extras;
String title = extras.getCharSequence(Notification.EXTRA_TITLE).toString();
String content = extras.getCharSequence(Notification.EXTRA_TEXT).toString();
```

**Important Notification Extras:**
- `EXTRA_TITLE`: Notification title
- `EXTRA_TEXT`: Main content text
- `EXTRA_SUB_TEXT`: Subtext (often sender name)
- `EXTRA_BIG_TEXT`: Expanded notification text
- `EXTRA_INFO_TEXT`: Additional info text

#### Foreground Service Requirement
Android 8.0+ requires notification listeners to run as foreground services for reliability:
```java
private static final int FOREGROUND_NOTIFICATION_ID = 1001;
startForeground(FOREGROUND_NOTIFICATION_ID, createForegroundNotification());
```

### 2. Room Database Implementation

#### Type Converters
Enum types need custom converters:
```java
@TypeConverter
public static NotificationRule.ModificationType toModificationType(String value) {
    return NotificationRule.ModificationType.valueOf(value);
}
```

#### Database Singleton Pattern
```java
private static volatile AppDatabase INSTANCE;

public static AppDatabase getInstance(Context context) {
    if (INSTANCE == null) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(...)
                    .fallbackToDestructiveMigration()
                    .build();
            }
        }
    }
    return INSTANCE;
}
```

**Important**: All database operations must run on background threads!

### 3. Permission Handling

#### Notification Access Permission
This is a special permission that requires manual user action:

```java
// Check if permission is granted
ComponentName cn = new ComponentName(context, NotificationInterceptorService.class);
String flat = Settings.Secure.getString(
    context.getContentResolver(),
    "enabled_notification_listeners"
);
boolean granted = flat != null && flat.contains(cn.flattenToString());

// Open settings if not granted
Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
context.startActivity(intent);
```

#### Runtime Permissions (Android 13+)
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### 4. Notification Modification Logic

#### Safe Text Modification
```java
private String modifyText(String text, NotificationRule rule) {
    if (text == null) return text; // Null safety
    
    switch (rule.getModificationType()) {
        case REPLACE_TEXT:
            String search = rule.getSearchText();
            String replacement = rule.getReplacementText();
            if (search != null && !search.isEmpty()) {
                return text.replace(search, replacement != null ? replacement : "");
            }
            break;
        // ... other types
    }
    return text;
}
```

#### Preserving Original Notification
```java
// CRITICAL: Copy all important properties
NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
    .setContentTitle(modifiedTitle)
    .setContentText(modifiedContent)
    .setSmallIcon(originalNotification.getSmallIcon()) // Same icon
    .setPriority(originalNotification.priority)        // Same priority
    .setAutoCancel(true);

// Preserve tap action
if (originalNotification.contentIntent != null) {
    builder.setContentIntent(originalNotification.contentIntent);
}
```

### 5. Background Execution Best Practices

#### ExecutorService Usage
```java
private ExecutorService executorService = Executors.newSingleThreadExecutor();

executorService.execute(() -> {
    // Database operations
    // Heavy processing
});

@Override
public void onDestroy() {
    super.onDestroy();
    if (executorService != null) {
        executorService.shutdown();
    }
}
```

#### Why Single Thread Executor?
- Ensures sequential processing of notifications
- Prevents race conditions in database
- Simpler than managing thread pool

### 6. MVVM Architecture Implementation

#### ViewModel Pattern
```java
public class RuleViewModel extends AndroidViewModel {
    private final AppDatabase database;
    private final ExecutorService executorService;
    private final LiveData<List<NotificationRule>> allRules;
    
    public RuleViewModel(Application application) {
        super(application);
        database = AppDatabase.getInstance(application);
        executorService = Executors.newSingleThreadExecutor();
        allRules = database.notificationRuleDao().getAllRules();
    }
    
    public void insert(NotificationRule rule) {
        executorService.execute(() -> database.notificationRuleDao().insert(rule));
    }
}
```

#### LiveData Observation in Fragment
```java
viewModel.getAllRules().observe(getViewLifecycleOwner(), rules -> {
    // Update UI
    adapter.setRules(rules);
});
```

### 7. Common Pitfalls & Solutions

#### Pitfall 1: Service Killed by System
**Problem**: Service stops working after some time
**Solution**: 
- Use foreground service with notification
- Request battery optimization exemption
- Implement proper restart logic

#### Pitfall 2: Notification Not Cancelled
**Problem**: Both original and modified notifications show
**Solution**:
```java
// Use the notification key to cancel
cancelNotification(sbn.getKey());
```

#### Pitfall 3: Database on Main Thread
**Problem**: App crashes with "Cannot access database on main thread"
**Solution**: Always use ExecutorService or Coroutines

#### Pitfall 4: Memory Leaks
**Problem**: Activity/Fragment context held in background
**Solution**: Use Application context for long-lived operations

### 8. Testing Strategy

#### Unit Testing
```java
@Test
public void testTextReplacement() {
    NotificationRule rule = new NotificationRule();
    rule.setModificationType(ModificationType.REPLACE_TEXT);
    rule.setSearchText("password");
    rule.setReplacementText("****");
    
    String result = modifyText("Your password is 1234", rule);
    assertEquals("Your **** is 1234", result);
}
```

#### Integration Testing
- Test with real notifications from popular apps
- Verify notification actions work after modification
- Test with different Android versions

### 9. Performance Optimization

#### Minimize Database Queries
```java
// Cache enabled rules instead of querying every time
private List<NotificationRule> cachedRules;

private void refreshRulesCache() {
    executorService.execute(() -> {
        cachedRules = database.notificationRuleDao().getEnabledRules();
    });
}
```

#### Limit Log Size
```java
// Automatically delete old logs
public void cleanOldLogs() {
    long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);
    database.notificationLogDao().deleteOlderThan(thirtyDaysAgo);
}
```

### 10. Security Considerations

#### Input Validation
```java
private boolean isValidRule(NotificationRule rule) {
    return rule.getRuleName() != null && !rule.getRuleName().trim().isEmpty()
        && rule.getTargetPackageName() != null && !rule.getTargetPackageName().isEmpty();
}
```

#### Package Name Verification
```java
private boolean isValidPackage(String packageName) {
    try {
        context.getPackageManager().getPackageInfo(packageName, 0);
        return true;
    } catch (PackageManager.NameNotFoundException e) {
        return false;
    }
}
```

## 🚨 Important Notes

### Android Version Compatibility

**Android 8.0+ (API 26)**
- Notification channels required
- Background execution limits
- Foreground service required

**Android 13+ (API 33)**
- Runtime permission for POST_NOTIFICATIONS required
- Stricter background restrictions

### Battery Optimization
Guide users to:
1. Disable battery optimization for your app
2. Add app to "Never sleeping apps" list (Samsung)
3. Enable "Autostart" (Xiaomi, Huawei)

### User Privacy
- Clearly communicate what data is accessed
- Show examples of notifications before enabling
- Provide easy disable/uninstall options
- Never transmit notification data outside device

## 📱 Device-Specific Issues

### Samsung
- May require "Allow notification access" in app settings
- OneUI may have additional restrictions

### Xiaomi/MIUI
- Must enable "Autostart"
- May need to lock app in recent apps

### Huawei/EMUI
- Similar to Xiaomi
- May require "Protected apps" setting

## 🔧 Debugging Tips

### Enable Detailed Logging
```java
private static final String TAG = "NotificationInterceptor";

Log.d(TAG, "Notification from: " + sbn.getPackageName());
Log.d(TAG, "Title: " + title);
Log.d(TAG, "Content: " + content);
```

### Check Service Status
```java
private boolean isServiceRunning() {
    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        if (NotificationInterceptorService.class.getName().equals(service.service.getClassName())) {
            return true;
        }
    }
    return false;
}
```

## 📚 Additional Resources

- [Android NotificationListenerService](https://developer.android.com/reference/android/service/notification/NotificationListenerService)
- [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- [Material Design 3](https://m3.material.io/)
- [Android Background Execution Limits](https://developer.android.com/about/versions/oreo/background)

---

**Remember**: This app handles sensitive user data (notifications). Always prioritize privacy and security in your implementation!
