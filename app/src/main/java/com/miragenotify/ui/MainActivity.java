package com.miragenotify.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.miragenotify.R;
import com.miragenotify.ui.home.HomeFragment;
import com.miragenotify.ui.logs.LogsFragment;
import com.miragenotify.ui.rules.RulesFragment;
import com.miragenotify.ui.settings.SettingsFragment;

/**
 * Main activity that hosts all fragments
 */
public class MainActivity extends AppCompatActivity {
    
    private BottomNavigationView bottomNavigation;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Setup bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(this::onNavigationItemSelected);
        
        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }
    }
    
    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_home) {
            fragment = new HomeFragment();
            setTitle(R.string.home_title);
        } else if (itemId == R.id.nav_rules) {
            fragment = new RulesFragment();
            setTitle(R.string.rules_title);
        } else if (itemId == R.id.nav_logs) {
            fragment = new LogsFragment();
            setTitle(R.string.logs_title);
        } else if (itemId == R.id.nav_settings) {
            fragment = new SettingsFragment();
            setTitle(R.string.settings_title);
        }
        
        return fragment != null && loadFragment(fragment);
    }
    
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
