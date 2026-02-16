package com.miragenotify.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.miragenotify.utils.PreferenceManager;

/**
 * Receiver that starts the notification service after device boot
 */
public class BootReceiver extends BroadcastReceiver {
    
    private static final String TAG = "BootReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction())) {
            
            Log.d(TAG, "Boot completed, checking if service should start");
            
            PreferenceManager preferenceManager = new PreferenceManager(context);
            
            // Only start if service was previously enabled
            if (preferenceManager.isServiceEnabled()) {
                Log.d(TAG, "Starting notification service after boot");
                // Service will auto-start when enabled via notification listener permission
            }
        }
    }
}
