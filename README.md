# Mirage Notify - Android Notification Interceptor

A privacy-focused Android application that intercepts and modifies system notifications in real-time based on user-defined rules, without altering the source application.

## 📱 Features

- **Real-time Notification Interception**: Listens to all notifications using NotificationListenerService
- **Content Modification**: Modify notification titles, content, and sender names
- **Rule-based Customization**: Create custom rules to target specific apps
- **Background Service**: Runs seamlessly in the background with foreground service
- **On-device Processing**: 100% privacy-focused - no cloud, all processing happens locally
- **Notification Logging**: Complete history of original and modified notifications
- **Material Design 3**: Beautiful UI with light and dark mode support

## 🏗️ Architecture

### Tech Stack
- **Language**: Java
- **UI**: XML Layouts
- **Database**: Room (SQLite)
- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **Min SDK**: API 26 (Android 8.0)
- **Target SDK**: API 34 (Android 14)

### Project Structure
```
com.miragenotify/
├── adapter/           # RecyclerView adapters
│   ├── RuleAdapter
│   └── LogAdapter
├── database/          # Room database components
│   ├── AppDatabase
│   ├── NotificationRuleDao
│   └── NotificationLogDao
├── model/             # Data models
│   ├── NotificationRule
│   └── NotificationLog
├── service/           # Background services
│   ├── NotificationInterceptorService
│   └── BootReceiver
├── ui/                # UI components
│   ├── MainActivity
│   ├── home/         # Home fragment
│   ├── rules/        # Rules management
│   ├── logs/         # Notification logs
│   └── settings/     # App settings
├── utils/             # Utility classes
│   ├── PreferenceManager
│   └── NotificationHelper
└── viewmodel/         # ViewModels
    ├── RuleViewModel
    └── LogViewModel
```

## 🔑 Key Components

### 1. NotificationInterceptorService
The core service that:
- Extends `NotificationListenerService`
- Intercepts all system notifications
- Applies user-defined rules
- Cancels original and posts modified notifications
- Logs all notification events
- Runs as foreground service for reliability

### 2. Database Layer (Room)
**Entities:**
- `NotificationRule`: Stores modification rules
- `NotificationLog`: Stores notification history

**DAOs:**
- Provides methods for CRUD operations
- LiveData support for reactive UI updates

### 3. MVVM Architecture
- **ViewModels**: Handle business logic and data operations
- **LiveData**: Observable data holders for UI updates
- **Repository Pattern**: Clean separation of concerns

## 🎨 UI Design

### Color Scheme
**Light Mode:**
- Primary: Light Blue (#03A9F4)
- Background: White (#FFFFFF)
- Accent: Soft Blue (#00BCD4)

**Dark Mode:**
- Primary: Dark Blue (#1976D2)
- Background: Near-black (#121212)
- Accent: Muted Blue (#0097A7)

### Screens
1. **Home**: Service status, statistics, quick actions
2. **Rules**: Manage notification modification rules
3. **Logs**: View notification history (original vs modified)
4. **Settings**: App configuration and permissions

## 🚀 Setup & Installation

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK API 26+

### Build Instructions
1. Clone/Download the project
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device/emulator

### Required Permissions
The app requires the following permissions:
- `BIND_NOTIFICATION_LISTENER_SERVICE`: To read notifications
- `POST_NOTIFICATIONS`: To post modified notifications (Android 13+)
- `RECEIVE_BOOT_COMPLETED`: To restart service after reboot
- `FOREGROUND_SERVICE`: To run background service reliably

## 📖 How It Works

### Notification Modification Flow
1. **Interception**: Service receives notification via `onNotificationPosted()`
2. **Rule Matching**: Checks database for applicable rules based on package name
3. **Modification**: Applies text replacement, masking, or sender renaming
4. **Logging**: Stores original and modified content in database
5. **Re-posting**: Cancels original notification and posts modified version
6. **Preservation**: Maintains original notification actions and intents

### Rule Types
- **REPLACE_TEXT**: Replace specific text with custom text
- **MASK_TEXT**: Hide sensitive content with asterisks
- **RENAME_SENDER**: Change the sender/app name
- **CUSTOM**: Custom modification logic

## 🔒 Privacy & Security

- **100% On-device**: No network requests, no cloud processing
- **Local Storage**: All data stored in local SQLite database
- **No Analytics**: No tracking or data collection
- **Open Architecture**: Code is transparent and auditable

## ⚙️ Configuration

### Adding a Rule
1. Navigate to Rules screen
2. Tap FAB (+ button)
3. Configure:
   - Rule name
   - Target app package
   - Modification type
   - Search/replacement text
   - Fields to modify (title/content/sender)
4. Save rule

### Service Management
- Toggle service on/off from Home or Settings
- Service runs as foreground notification
- Auto-starts on device boot (if enabled)

## 🐛 Known Limitations

1. **Notification Channels**: Some apps use multiple channels - modifications apply to all
2. **Grouped Notifications**: Bundle notifications may behave unexpectedly
3. **System Notifications**: Cannot modify Android system notifications
4. **Battery Optimization**: May be killed by aggressive battery savers

## 🔧 Troubleshooting

### Service Not Working
1. Check notification access permission is granted
2. Disable battery optimization for the app
3. Ensure service is enabled in settings
4. Restart device if needed

### Rules Not Applying
1. Verify rule is enabled
2. Check package name is correct
3. Ensure search text matches notification content
4. Check notification log to verify interception

## 📋 Future Enhancements

- [ ] Pattern matching with regex support
- [ ] Time-based rule activation
- [ ] Import/Export rules
- [ ] Advanced filtering options
- [ ] Notification scheduling
- [ ] Multi-language support
- [ ] Rule templates
- [ ] Statistics dashboard

## 📄 License

This project is provided as-is for educational purposes.

## 🤝 Contributing

This is a demonstration project. Feel free to fork and modify for your needs.

## ⚠️ Disclaimer

This app modifies system notifications and requires sensitive permissions. Use responsibly and only for legitimate privacy purposes. The developers are not responsible for misuse.

---

**Built with ❤️ using Android Studio**
