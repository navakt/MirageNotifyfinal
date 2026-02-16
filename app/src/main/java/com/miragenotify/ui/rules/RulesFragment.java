package com.miragenotify.ui.rules;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.miragenotify.R;
import com.miragenotify.adapter.RuleAdapter;
import com.miragenotify.model.NotificationRule;
import com.miragenotify.viewmodel.RuleViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RulesFragment extends Fragment implements RuleAdapter.OnRuleClickListener {
    
    private RuleViewModel viewModel;
    private RuleAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyState;
    private final List<AppEntry> installedApps = new ArrayList<>();
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rules, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(RuleViewModel.class);
        
        recyclerView = view.findViewById(R.id.recycler_rules);
        emptyState = view.findViewById(R.id.empty_state);
        FloatingActionButton fabAdd = view.findViewById(R.id.fab_add_rule);
        
        adapter = new RuleAdapter(requireContext(), viewModel);
        adapter.setOnRuleClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        
        viewModel.getAllRules().observe(getViewLifecycleOwner(), rules -> {
            adapter.setRules(rules);
            if (rules == null || rules.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        
        fabAdd.setOnClickListener(v -> showRuleEditorDialog(null));
        
        // Load apps in background or once during creation
        loadInstalledApps();
    }

    private void loadInstalledApps() {
        PackageManager pm = requireContext().getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        installedApps.clear();

        for (ApplicationInfo app : apps) {
            // Filter to show user-installed apps and updated system apps (like WhatsApp, Telegram, etc.)
            boolean isSystemApp = (app.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            boolean isUpdatedSystemApp = (app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0;
            
            if (!isSystemApp || isUpdatedSystemApp) {
                String label = pm.getApplicationLabel(app).toString();
                installedApps.add(new AppEntry(label, app.packageName));
            }
        }
        Collections.sort(installedApps);
    }

    @Override
    public void onEditClick(NotificationRule rule) {
        showRuleEditorDialog(rule);
    }

    @Override
    public void onDeleteClick(NotificationRule rule) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_delete_rule_title)
                .setMessage(R.string.dialog_delete_rule_message)
                .setPositiveButton(R.string.delete, (dialog, which) -> viewModel.delete(rule))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void showRuleEditorDialog(@Nullable NotificationRule ruleToEdit) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_rule_editor, null);
        
        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        EditText etName = dialogView.findViewById(R.id.et_rule_name);
        Spinner spinnerApps = dialogView.findViewById(R.id.spinner_apps);
        RadioGroup rgType = dialogView.findViewById(R.id.rg_modification_type);
        EditText etSearch = dialogView.findViewById(R.id.et_search_text);
        EditText etReplacement = dialogView.findViewById(R.id.et_replacement_text);
        CheckBox cbTitle = dialogView.findViewById(R.id.cb_title);
        CheckBox cbContent = dialogView.findViewById(R.id.cb_content);
        CheckBox cbSender = dialogView.findViewById(R.id.cb_sender);

        // Setup Spinner
        ArrayAdapter<AppEntry> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                installedApps
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerApps.setAdapter(spinnerAdapter);

        if (ruleToEdit != null) {
            tvTitle.setText(R.string.edit_rule);
            etName.setText(ruleToEdit.getRuleName());
            etSearch.setText(ruleToEdit.getSearchText());
            etReplacement.setText(ruleToEdit.getReplacementText());
            cbTitle.setChecked(ruleToEdit.isModifyTitle());
            cbContent.setChecked(ruleToEdit.isModifyContent());
            cbSender.setChecked(ruleToEdit.isModifySender());

            // Set spinner selection based on existing rule's package name
            for (int i = 0; i < installedApps.size(); i++) {
                if (installedApps.get(i).packageName.equals(ruleToEdit.getTargetPackageName())) {
                    spinnerApps.setSelection(i);
                    break;
                }
            }

            switch (ruleToEdit.getModificationType()) {
                case REPLACE_TEXT: rgType.check(R.id.rb_replace); break;
                case MASK_TEXT: rgType.check(R.id.rb_mask); break;
                case RENAME_SENDER: rgType.check(R.id.rb_rename); break;
            }
        } else {
            rgType.check(R.id.rb_replace);
            cbContent.setChecked(true);
        }

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton(R.string.save_rule, (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    AppEntry selectedApp = (AppEntry) spinnerApps.getSelectedItem();
                    
                    if (name.isEmpty() || selectedApp == null) {
                        Toast.makeText(getContext(), "Rule name and target app are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    NotificationRule rule = ruleToEdit != null ? ruleToEdit : new NotificationRule();
                    rule.setRuleName(name);
                    rule.setTargetPackageName(selectedApp.packageName);
                    rule.setSearchText(etSearch.getText().toString());
                    rule.setReplacementText(etReplacement.getText().toString());
                    rule.setModifyTitle(cbTitle.isChecked());
                    rule.setModifyContent(cbContent.isChecked());
                    rule.setModifySender(cbSender.isChecked());

                    int checkedId = rgType.getCheckedRadioButtonId();
                    if (checkedId == R.id.rb_replace) rule.setModificationType(NotificationRule.ModificationType.REPLACE_TEXT);
                    else if (checkedId == R.id.rb_mask) rule.setModificationType(NotificationRule.ModificationType.MASK_TEXT);
                    else if (checkedId == R.id.rb_rename) rule.setModificationType(NotificationRule.ModificationType.RENAME_SENDER);

                    if (ruleToEdit != null) viewModel.update(rule);
                    else viewModel.insert(rule);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * Helper class to store app label and package name for the spinner
     */
    private static class AppEntry implements Comparable<AppEntry> {
        final String label;
        final String packageName;

        AppEntry(String label, String packageName) {
            this.label = label;
            this.packageName = packageName;
        }

        @NonNull
        @Override
        public String toString() {
            return label; // This is what the spinner displays
        }

        @Override
        public int compareTo(AppEntry other) {
            return label.compareToIgnoreCase(other.label);
        }
    }
}
