package com.miragenotify.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.miragenotify.R;
import com.miragenotify.utils.NotificationHelper;
import com.miragenotify.utils.PreferenceManager;

public class SettingsFragment extends Fragment {
    
    private PreferenceManager preferenceManager;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        preferenceManager = new PreferenceManager(requireContext());
        
        SwitchMaterial switchService = view.findViewById(R.id.switch_service_status);
        SwitchMaterial switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        
        switchService.setChecked(preferenceManager.isServiceEnabled());
        switchService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setServiceEnabled(isChecked);
        });
        
        switchDarkMode.setChecked(preferenceManager.isDarkMode());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setDarkMode(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
        
        view.findViewById(R.id.layout_notification_access).setOnClickListener(v -> {
            NotificationHelper.openNotificationAccessSettings(requireContext());
        });
        
        view.findViewById(R.id.layout_clear_data).setOnClickListener(v -> {
            // Show confirmation dialog
        });
    }
}
