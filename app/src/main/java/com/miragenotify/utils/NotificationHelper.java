package com.miragenotify.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;

/**
 * Helper class for notification-related utilities
 */
public class NotificationHelper {
    
    /**
     * Check if notification listener permission is granted
     */
    public static boolean isNotificationAccessGranted(Context context) {
        ComponentName cn = new ComponentName(context, 
                "com.miragenotify.service.NotificationInterceptorService");
        String flat = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");
        return flat != null && flat.contains(cn.flattenToString());
    }
    
    /**
     * Open notification access settings
     */
    public static void openNotificationAccessSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        context.startActivity(intent);
    }
    
    /**
     * Format timestamp to readable date/time
     */
    public static String formatTimestamp(long timestamp) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
                "MMM dd, yyyy hh:mm a", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date(timestamp));
    }
    
    /**
     * Get relative time (e.g., "5 minutes ago")
     */
    public static String getRelativeTime(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (seconds < 60) {
            return "Just now";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (hours < 24) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else if (days < 7) {
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        } else {
            return formatTimestamp(timestamp);
        }
    }
}
