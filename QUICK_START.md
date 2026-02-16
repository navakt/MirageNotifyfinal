# Mirage Notify - Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Step 1: Build the Project
```bash
# Open in Android Studio
File → Open → Select MirageNotify folder

# Wait for Gradle sync to complete

# Build project
Build → Make Project (Ctrl+F9)
```

### Step 2: Run on Device/Emulator
```bash
# Connect Android device or start emulator
# Click Run (Shift+F10)
```

### Step 3: Grant Permissions
1. App opens to onboarding screen (if implemented)
2. Tap "Grant Permission"
3. Find "Mirage Notify" in the list
4. Toggle ON
5. Confirm with "Allow"
6. Return to app

### Step 4: Create Your First Rule
1. Navigate to "Rules" tab
2. Tap the blue + button
3. Fill in:
   - **Rule Name**: "Hide Passwords"
   - **Target App**: "com.whatsapp" (or any app)
   - **Modification Type**: MASK_TEXT
   - **Search Text**: "password"
   - **Check**: "Modify Content"
4. Tap "Save"

### Step 5: Test It Out
1. Send yourself a test notification containing "password"
2. Check the "Logs" tab to see the modification
3. Original and modified versions will be shown

## 📖 Basic Usage

### Creating Rules

#### Replace Text Rule
- **Use Case**: Replace sensitive words
- **Setup**:
  - Type: REPLACE_TEXT
  - Search: "credit card"
  - Replace: "payment method"

#### Mask Text Rule
- **Use Case**: Hide passwords/codes
- **Setup**:
  - Type: MASK_TEXT
  - Search: "verification code"
  - Result: "*************** ****"

#### Rename Sender Rule
- **Use Case**: Anonymize notifications
- **Setup**:
  - Type: RENAME_SENDER
  - Replace: "Private Message"

### Managing Rules
- **Enable/Disable**: Use the switch on each rule card
- **Edit**: Tap "Edit" button
- **Delete**: Tap "Delete" button (confirms first)

### Viewing Logs
- All notifications appear in "Logs" tab
- Green badge = Modified
- Shows original vs modified side-by-side
- Automatically limited to 500 most recent

### Settings
- **Service Status**: Toggle service on/off
- **Dark Mode**: Switch between light/dark themes
- **Clear Data**: Remove all rules and logs

## 🎯 Common Use Cases

### 1. Privacy on Shared Screens
**Scenario**: Don't want others to see notification content
```
Rule: Hide All Content
Type: REPLACE_TEXT
Target: * (all apps)
Search: (leave empty)
Replace: "New notification"
```

### 2. Filter Work Messages
**Scenario**: Replace colleague names with roles
```
Rule: Anonymize Slack
Type: REPLACE_TEXT
Target: com.slack
Search: "John Doe"
Replace: "Manager"
```

### 3. Hide Verification Codes
**Scenario**: Prevent shoulder-surfing
```
Rule: Mask OTP
Type: MASK_TEXT
Target: com.google.android.gms (SMS)
Search: "code"
Modify: Title + Content
```

## 🔧 Troubleshooting

### Service Not Working
**Check:**
1. ✓ Notification access granted
2. ✓ Service enabled in settings
3. ✓ Battery optimization disabled
4. ✓ App not force-stopped

**Fix:**
```
Settings → Apps → Mirage Notify → Permissions → Notification Access → ON
Settings → Battery → Battery Optimization → Mirage Notify → Don't optimize
```

### Rules Not Applying
**Check:**
1. ✓ Rule is enabled (switch ON)
2. ✓ Package name is correct
3. ✓ Search text matches notification
4. ✓ Correct fields selected (Title/Content/Sender)

**Debug:**
- Check "Logs" tab to verify interception
- Look at original content to verify text

### Notifications Not Intercepted
**Check:**
1. ✓ Not your own app's notifications (app filters itself)
2. ✓ Not system notifications (cannot modify)
3. ✓ Service running (check Settings)

## 📱 App Structure

```
┌─────────────────────────────────────────┐
│  Home                                   │
│  ├─ Service Status (Active/Inactive)   │
│  ├─ Total Intercepted (counter)        │
│  ├─ Total Modified (counter)           │
│  └─ Active Rules (counter)             │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│  Rules                                  │
│  ├─ Rule Card 1                         │
│  │  ├─ Name, Target, Type              │
│  │  ├─ Enable Switch                   │
│  │  └─ Edit / Delete buttons           │
│  ├─ Rule Card 2                         │
│  └─ + FAB (Add Rule)                    │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│  Logs                                   │
│  ├─ Log Card 1                          │
│  │  ├─ App Name, Time                  │
│  │  ├─ Original: Title, Content        │
│  │  └─ Modified: Title, Content        │
│  ├─ Log Card 2                          │
│  └─ Clear All button                    │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│  Settings                               │
│  ├─ Notification Access                 │
│  ├─ Service Status (Switch)            │
│  ├─ Dark Mode (Switch)                 │
│  ├─ Clear All Data                     │
│  └─ About (Version info)               │
└─────────────────────────────────────────┘
```

## 🎨 UI Components Guide

### Material Design Elements Used
- **Cards**: All content in elevated cards
- **FAB**: Floating action button for add actions
- **Switches**: Material switches for toggles
- **Buttons**: Filled and outlined button styles
- **Colors**: Dynamic based on light/dark mode

### Navigation
- **Bottom Navigation**: 4 tabs (Home, Rules, Logs, Settings)
- **Toolbar**: App title at top
- **No Drawer**: Simple bottom nav only

## 💡 Tips & Best Practices

### Performance
- Don't create too many overlapping rules
- Disable unused rules instead of deleting
- Clear old logs periodically

### Privacy
- Rules apply immediately - test with non-sensitive data first
- Review logs to ensure modifications work as expected
- Clear logs if they contain sensitive data

### Rule Creation
- Start with simple rules
- Test one modification at a time
- Use specific search text for accuracy
- Package names are case-sensitive

## 🔐 Security Notes

### What This App Can Do
✅ Read all notifications
✅ Modify notification content
✅ Cancel notifications
✅ Post new notifications

### What This App CANNOT Do
❌ Access notification actions/replies
❌ Access app data directly
❌ Send data to internet (no network permission)
❌ Modify system notifications

### Privacy Guarantee
- **No Internet**: App has no network permission
- **Local Only**: All data stored on device
- **No Analytics**: No tracking or telemetry
- **Open Code**: Implementation is transparent

## 📋 Next Steps

1. **Experiment**: Try different rule types
2. **Customize**: Set up rules for your frequently used apps
3. **Monitor**: Check logs to verify behavior
4. **Optimize**: Disable rules you don't use
5. **Share**: Help others set up their rules

## 🆘 Need Help?

### Common Questions

**Q: Does this work with all apps?**
A: Yes, except system apps and the app itself.

**Q: Will rules survive app updates?**
A: Yes, rules are stored in database.

**Q: Can I backup my rules?**
A: Not yet - future feature planned.

**Q: Does this drain battery?**
A: Minimal impact - runs efficiently in background.

**Q: Is my data safe?**
A: Yes - everything stays on your device.

---

**Happy Notifying! 🎉**
