# Mirage Notify - Complete Project Index

## 📁 Quick Navigation Guide

### 🚀 Start Here
1. **README.md** - Project overview and features
2. **QUICK_START.md** - Get running in 5 minutes
3. **PROJECT_SUMMARY.md** - What's included and status

### 📖 Documentation
| File | Purpose | When to Read |
|------|---------|--------------|
| README.md | Overview, features, setup | First read |
| QUICK_START.md | Usage guide, tutorials | After building |
| IMPLEMENTATION_GUIDE.md | Technical details | When coding |
| PROJECT_SUMMARY.md | Deliverables checklist | Reference |
| INDEX.md | This file | Navigation |

### 💻 Source Code Locations

#### Core Service
```
app/src/main/java/com/miragenotify/service/
├── NotificationInterceptorService.java  ⭐ Main notification logic
└── BootReceiver.java                    Auto-start after boot
```

#### Database Layer
```
app/src/main/java/com/miragenotify/database/
├── AppDatabase.java                     Database singleton
├── NotificationRuleDao.java             Rules database access
└── NotificationLogDao.java              Logs database access
```

#### Data Models
```
app/src/main/java/com/miragenotify/model/
├── NotificationRule.java                Rule entity
└── NotificationLog.java                 Log entity
```

#### ViewModels (MVVM)
```
app/src/main/java/com/miragenotify/viewmodel/
├── RuleViewModel.java                   Rules business logic
└── LogViewModel.java                    Logs business logic
```

#### UI Layer
```
app/src/main/java/com/miragenotify/ui/
├── MainActivity.java                    ⭐ Entry point
├── home/HomeFragment.java               Dashboard
├── rules/RulesFragment.java             Rule management
├── logs/LogsFragment.java               Log viewer
└── settings/SettingsFragment.java       Settings
```

#### Adapters
```
app/src/main/java/com/miragenotify/adapter/
├── RuleAdapter.java                     Rules RecyclerView
└── LogAdapter.java                      Logs RecyclerView
```

#### Utilities
```
app/src/main/java/com/miragenotify/utils/
├── PreferenceManager.java               Settings storage
└── NotificationHelper.java              Helper functions
```

### 🎨 Resources

#### Layouts
```
app/src/main/res/layout/
├── activity_main.xml                    ⭐ Main activity layout
├── fragment_home.xml                    Home screen
├── fragment_rules.xml                   Rules screen
├── fragment_logs.xml                    Logs screen
├── fragment_settings.xml                Settings screen
├── item_rule.xml                        Rule list item
└── item_log.xml                         Log list item
```

#### Values (Light Mode)
```
app/src/main/res/values/
├── colors.xml                           Light mode colors
├── strings.xml                          ⭐ All text resources
└── themes.xml                           Light theme styling
```

#### Values (Dark Mode)
```
app/src/main/res/values-night/
├── colors.xml                           Dark mode colors
└── themes.xml                           Dark theme styling
```

#### Drawables
```
app/src/main/res/drawable/
├── ic_home.xml                          Home icon
├── ic_rule.xml                          Rule icon
├── ic_logs.xml                          Logs icon
├── ic_settings.xml                      Settings icon
├── ic_add.xml                           Add FAB icon
├── ic_notification.xml                  Notification icon
├── circle_shape.xml                     Status indicator
└── badge_modified.xml                   Modified badge
```

#### Menus
```
app/src/main/res/menu/
└── bottom_nav_menu.xml                  Bottom navigation
```

#### XML Config
```
app/src/main/res/xml/
├── backup_rules.xml                     Backup config
└── data_extraction_rules.xml            Data extraction
```

### ⚙️ Configuration Files

#### Gradle
```
Root Level:
├── build.gradle                         Root build config
├── settings.gradle                      Project settings
└── gradle.properties                    Gradle properties

App Level:
└── app/build.gradle                     ⭐ App dependencies
```

#### Android
```
app/src/main/
├── AndroidManifest.xml                  ⭐ App manifest
└── proguard-rules.pro                   ProGuard config
```

## 🔍 Key Files by Purpose

### Must Read for Understanding
1. `NotificationInterceptorService.java` - Core logic
2. `MainActivity.java` - App structure
3. `AppDatabase.java` - Data persistence
4. `AndroidManifest.xml` - Permissions & components

### Must Read for UI
1. `activity_main.xml` - Main layout
2. `fragment_home.xml` - Dashboard
3. `colors.xml` - Color scheme
4. `themes.xml` - Material styling

### Must Read for Data
1. `NotificationRule.java` - Rule structure
2. `NotificationLog.java` - Log structure
3. `NotificationRuleDao.java` - Database operations

## 🎯 File Usage by Task

### Adding a New Feature
1. Start with ViewModel (business logic)
2. Update Database (if needed)
3. Create Fragment UI
4. Add layout XML
5. Update navigation

### Modifying UI
1. Edit layout XML files
2. Update colors.xml for new colors
3. Modify Fragment Java code
4. Test in both light/dark modes

### Changing Logic
1. Edit Service class
2. Update ViewModel if needed
3. Modify database entities/DAOs
4. Test thoroughly

### Adding Resources
1. Strings → `strings.xml`
2. Colors → `colors.xml` (both light & dark)
3. Icons → `drawable/` folder
4. Layouts → `layout/` folder

## 📊 File Statistics

### Source Code
- Java Files: 19
- XML Files: 24
- Gradle Files: 3
- Markdown Docs: 5

### Lines of Code (Approximate)
- Java: ~2,500 lines
- XML: ~1,500 lines
- Documentation: ~1,000 lines
- Total: ~5,000 lines

## 🗂️ Complete File Tree

```
MirageNotify/
│
├── 📄 README.md
├── 📄 QUICK_START.md
├── 📄 IMPLEMENTATION_GUIDE.md
├── 📄 PROJECT_SUMMARY.md
├── 📄 INDEX.md (this file)
│
├── 📄 build.gradle
├── 📄 settings.gradle
├── 📄 gradle.properties
│
└── app/
    ├── 📄 build.gradle
    ├── 📄 proguard-rules.pro
    │
    └── src/main/
        ├── 📄 AndroidManifest.xml
        │
        ├── java/com/miragenotify/
        │   ├── 📄 MirageNotifyApplication.java
        │   │
        │   ├── adapter/
        │   │   ├── 📄 RuleAdapter.java
        │   │   └── 📄 LogAdapter.java
        │   │
        │   ├── database/
        │   │   ├── 📄 AppDatabase.java
        │   │   ├── 📄 NotificationRuleDao.java
        │   │   └── 📄 NotificationLogDao.java
        │   │
        │   ├── model/
        │   │   ├── 📄 NotificationRule.java
        │   │   └── 📄 NotificationLog.java
        │   │
        │   ├── service/
        │   │   ├── 📄 NotificationInterceptorService.java
        │   │   └── 📄 BootReceiver.java
        │   │
        │   ├── ui/
        │   │   ├── 📄 MainActivity.java
        │   │   ├── home/
        │   │   │   └── 📄 HomeFragment.java
        │   │   ├── rules/
        │   │   │   └── 📄 RulesFragment.java
        │   │   ├── logs/
        │   │   │   └── 📄 LogsFragment.java
        │   │   └── settings/
        │   │       └── 📄 SettingsFragment.java
        │   │
        │   ├── utils/
        │   │   ├── 📄 PreferenceManager.java
        │   │   └── 📄 NotificationHelper.java
        │   │
        │   └── viewmodel/
        │       ├── 📄 RuleViewModel.java
        │       └── 📄 LogViewModel.java
        │
        └── res/
            ├── drawable/
            │   ├── 📄 ic_home.xml
            │   ├── 📄 ic_rule.xml
            │   ├── 📄 ic_logs.xml
            │   ├── 📄 ic_settings.xml
            │   ├── 📄 ic_add.xml
            │   ├── 📄 ic_notification.xml
            │   ├── 📄 circle_shape.xml
            │   └── 📄 badge_modified.xml
            │
            ├── layout/
            │   ├── 📄 activity_main.xml
            │   ├── 📄 fragment_home.xml
            │   ├── 📄 fragment_rules.xml
            │   ├── 📄 fragment_logs.xml
            │   ├── 📄 fragment_settings.xml
            │   ├── 📄 item_rule.xml
            │   └── 📄 item_log.xml
            │
            ├── menu/
            │   └── 📄 bottom_nav_menu.xml
            │
            ├── values/
            │   ├── 📄 colors.xml
            │   ├── 📄 strings.xml
            │   └── 📄 themes.xml
            │
            ├── values-night/
            │   ├── 📄 colors.xml
            │   └── 📄 themes.xml
            │
            └── xml/
                ├── 📄 backup_rules.xml
                └── 📄 data_extraction_rules.xml
```

## 🎓 Learning Path

### For Beginners
1. Read `QUICK_START.md`
2. Build and run the app
3. Explore UI in `fragment_*.xml`
4. Read `MainActivity.java`

### For Intermediate
1. Read `README.md`
2. Study `NotificationInterceptorService.java`
3. Understand Room database setup
4. Explore MVVM pattern

### For Advanced
1. Read `IMPLEMENTATION_GUIDE.md`
2. Study service lifecycle
3. Optimize database queries
4. Implement new features

## 🔗 Cross-References

### Notification Interception
- Service: `NotificationInterceptorService.java`
- Guide: `IMPLEMENTATION_GUIDE.md` (Section 1)
- Manifest: `AndroidManifest.xml` (Lines 30-38)

### Database Operations
- Setup: `AppDatabase.java`
- Entities: `model/` folder
- DAOs: `database/` folder
- Guide: `IMPLEMENTATION_GUIDE.md` (Section 2)

### UI Implementation
- Fragments: `ui/` folder
- Layouts: `res/layout/` folder
- Adapters: `adapter/` folder
- Colors: `res/values/colors.xml`

### Rule Management
- Entity: `NotificationRule.java`
- DAO: `NotificationRuleDao.java`
- ViewModel: `RuleViewModel.java`
- Fragment: `RulesFragment.java`
- Adapter: `RuleAdapter.java`
- Layout: `item_rule.xml`

## 💡 Quick Tips

### Finding Things
- **Strings**: Search `strings.xml`
- **Colors**: Check `colors.xml` (light/dark)
- **Logic**: Look in ViewModels first
- **UI**: Fragment + Layout pair

### Common Tasks
- Add string: Edit `strings.xml`
- Change color: Edit `colors.xml`
- New screen: Create Fragment + Layout
- Database change: Update Entity + DAO

### Debugging
- Logs: Use Android Studio Logcat
- Filter: Tag "NotificationInterceptor"
- Database: Use Device File Explorer
- UI: Use Layout Inspector

---

**Last Updated**: 2026-02-10
**Project Version**: 1.0.0
**Total Files**: 51 files
**Status**: ✅ Complete and Ready
