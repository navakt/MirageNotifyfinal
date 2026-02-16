package com.miragenotify.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.miragenotify.R;
import com.miragenotify.utils.NotificationHelper;
import com.miragenotify.utils.PreferenceManager;
import com.miragenotify.viewmodel.LogViewModel;
import com.miragenotify.viewmodel.RuleViewModel;

/**
 * Home screen showing service status and statistics
 */
public class HomeFragment extends Fragment {
    
    private PreferenceManager preferenceManager;
    private RuleViewModel ruleViewModel;
    private LogViewModel logViewModel;
    
    private TextView tvStatus;
    private TextView tvInterceptedCount;
    private TextView tvModifiedCount;
    private TextView tvRulesCount;
    private SwitchMaterial switchService;
    private View statusIndicator;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        preferenceManager = new PreferenceManager(requireContext());
        ruleViewModel = new ViewModelProvider(this).get(RuleViewModel.class);
        logViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        
        // Initialize views
        tvStatus = view.findViewById(R.id.tv_status);
        tvInterceptedCount = view.findViewById(R.id.tv_intercepted_count);
        tvModifiedCount = view.findViewById(R.id.tv_modified_count);
        tvRulesCount = view.findViewById(R.id.tv_rules_count);
        switchService = view.findViewById(R.id.switch_service);
        statusIndicator = view.findViewById(R.id.status_indicator);
        
        // Setup switch
        switchService.setChecked(preferenceManager.isServiceEnabled());
        switchService.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferenceManager.setServiceEnabled(isChecked);
            updateServiceStatus();
        });
        
        view.findViewById(R.id.btn_manage_rules).setOnClickListener(v -> {
            // Navigate to rules fragment
            requireActivity().findViewById(R.id.bottom_navigation).setSelected(false);
        });
        
        // Observe data
        observeData();
        updateServiceStatus();
    }
    
    private void observeData() {
        logViewModel.getLogCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvInterceptedCount.setText(String.valueOf(count));
            }
        });
        
        logViewModel.getModifiedCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvModifiedCount.setText(String.valueOf(count));
            }
        });
        
        ruleViewModel.getRuleCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                tvRulesCount.setText(count + " rules active");
            }
        });
    }
    
    private void updateServiceStatus() {
        boolean isEnabled = preferenceManager.isServiceEnabled();
        boolean hasPermission = NotificationHelper.isNotificationAccessGranted(requireContext());
        
        if (isEnabled && hasPermission) {
            tvStatus.setText(R.string.status_active);
            statusIndicator.setBackgroundTintList(
                getResources().getColorStateList(R.color.success, null));
        } else {
            tvStatus.setText(R.string.status_inactive);
            statusIndicator.setBackgroundTintList(
                getResources().getColorStateList(R.color.error, null));
        }
    }
}
