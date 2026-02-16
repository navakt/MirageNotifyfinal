# Add project specific ProGuard rules here.

# Keep Room database classes
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Keep model classes
-keep class com.miragenotify.model.** { *; }

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

# Keep Service
-keep class com.miragenotify.service.** { *; }
