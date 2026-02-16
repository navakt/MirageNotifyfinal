package com.miragenotify.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages app preferences and settings
 */
public class PreferenceManager {
    
    private static final String PREFS_NAME = "mirage_notify_prefs";
    private static final String KEY_SERVICE_ENABLED = "service_enabled";
    private static final String KEY_ONBOARDING_COMPLETE = "onboarding_complete";
    private static final String KEY_DARK_MODE = "dark_mode";
    
    private final SharedPreferences preferences;
    
    public PreferenceManager(Context context) {
        this.preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public boolean isServiceEnabled() {
        return preferences.getBoolean(KEY_SERVICE_ENABLED, true);
    }
    
    public void setServiceEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply();
    }
    
    public boolean isOnboardingComplete() {
        return preferences.getBoolean(KEY_ONBOARDING_COMPLETE, false);
    }
    
    public void setOnboardingComplete(boolean complete) {
        preferences.edit().putBoolean(KEY_ONBOARDING_COMPLETE, complete).apply();
    }
    
    public boolean isDarkMode() {
        return preferences.getBoolean(KEY_DARK_MODE, false);
    }
    
    public void setDarkMode(boolean darkMode) {
        preferences.edit().putBoolean(KEY_DARK_MODE, darkMode).apply();
    }
}
