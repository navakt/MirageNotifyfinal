package com.miragenotify.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.miragenotify.model.NotificationLog;
import com.miragenotify.model.NotificationRule;

/**
 * Main Room Database for Mirage Notify
 */
@Database(entities = {NotificationRule.class, NotificationLog.class}, version = 1, exportSchema = false)
@TypeConverters({AppDatabase.Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    
    private static volatile AppDatabase INSTANCE;
    
    public abstract NotificationRuleDao notificationRuleDao();
    public abstract NotificationLogDao notificationLogDao();
    
    /**
     * Get database instance (Singleton pattern)
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "mirage_notify_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
    
    /**
     * Type converters for custom types
     */
    public static class Converters {
        
        @TypeConverter
        public static NotificationRule.ModificationType toModificationType(String value) {
            if (value == null) {
                return null;
            }
            return NotificationRule.ModificationType.valueOf(value);
        }
        
        @TypeConverter
        public static String fromModificationType(NotificationRule.ModificationType type) {
            if (type == null) {
                return null;
            }
            return type.name();
        }
    }
}
