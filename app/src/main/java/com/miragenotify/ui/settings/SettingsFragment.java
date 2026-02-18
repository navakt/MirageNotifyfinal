package com.miragenotify.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.miragenotify.R;
import com.miragenotify.utils.NotificationHelper;
import com.miragenotify.utils.PreferenceManager;
import com.miragenotify.viewmodel.LogViewModel;
import com.miragenotify.viewmodel.RuleViewModel;

public class SettingsFragment extends Fragment {
    
    private PreferenceManager preferenceManager;
    private RuleViewModel ruleViewModel;
    private LogViewModel logViewModel;
    
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
        ruleViewModel = new ViewModelProvider(this).get(RuleViewModel.class);
        logViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        
        SwitchMaterial switchService = view.findViewById(R.id.switch_service_status);
        SwitchMaterial switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        TextView tvPermissionStatus = view.findViewById(R.id.tv_permission_status);
        
        // Service Switch
        switchService.setChecked(preferenceManager.isServiceEnabled());
        switchService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setServiceEnabled(isChecked);
        });
        
        // Dark Mode Switch
        switchDarkMode.setChecked(preferenceManager.isDarkMode());
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setDarkMode(isChecked);
            AppCompatDelegate.setDefaultNightMode(
                isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        });
        
        // Permission Status and Click
        updatePermissionStatus(tvPermissionStatus);
        view.findViewById(R.id.layout_notification_access).setOnClickListener(v -> {
            NotificationHelper.openNotificationAccessSettings(requireContext());
        });
        
        // Clear All Data
        view.findViewById(R.id.layout_clear_data).setOnClickListener(v -> {
            showClearDataConfirmation();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Re-check permission status when returning from settings
        TextView tvPermissionStatus = getView().findViewById(R.id.tv_permission_status);
        if (tvPermissionStatus != null) {
            updatePermissionStatus(tvPermissionStatus);
        }
    }

    private void updatePermissionStatus(TextView tv) {
        if (NotificationHelper.isNotificationAccessGranted(requireContext())) {
            tv.setText("Permission Granted");
            tv.setTextColor(getResources().getColor(R.color.success, null));
        } else {
            tv.setText("Permission Required - Tap to grant");
            tv.setTextColor(getResources().getColor(R.color.error, null));
        }
    }

    private void showClearDataConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_clear_data_title)
                .setMessage(R.string.dialog_clear_data_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    ruleViewModel.deleteAll();
                    logViewModel.deleteAll();
                    Toast.makeText(getContext(), R.string.data_cleared, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
