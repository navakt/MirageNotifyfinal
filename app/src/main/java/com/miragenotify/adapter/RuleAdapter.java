package com.miragenotify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.miragenotify.R;
import com.miragenotify.model.NotificationRule;
import com.miragenotify.viewmodel.RuleViewModel;

import java.util.ArrayList;
import java.util.List;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.RuleViewHolder> {
    
    private final Context context;
    private final RuleViewModel viewModel;
    private List<NotificationRule> rules = new ArrayList<>();
    private OnRuleClickListener listener;

    public interface OnRuleClickListener {
        void onEditClick(NotificationRule rule);
        void onDeleteClick(NotificationRule rule);
    }
    
    public RuleAdapter(Context context, RuleViewModel viewModel) {
        this.context = context;
        this.viewModel = viewModel;
    }
    
    public void setOnRuleClickListener(OnRuleClickListener listener) {
        this.listener = listener;
    }

    public void setRules(List<NotificationRule> rules) {
        this.rules = rules != null ? rules : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rule, parent, false);
        return new RuleViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RuleViewHolder holder, int position) {
        NotificationRule rule = rules.get(position);
        
        holder.tvRuleName.setText(rule.getRuleName());
        holder.tvTargetApp.setText("Target: " + rule.getTargetPackageName());
        holder.tvModificationType.setText("Type: " + rule.getModificationType().name());
        holder.switchEnabled.setChecked(rule.isEnabled());
        
        // Remove listener before setting checked to avoid triggering it
        holder.switchEnabled.setOnCheckedChangeListener(null);
        holder.switchEnabled.setChecked(rule.isEnabled());
        holder.switchEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rule.setEnabled(isChecked);
            viewModel.update(rule);
        });
        
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(rule);
        });
        
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(rule);
        });
    }
    
    @Override
    public int getItemCount() {
        return rules.size();
    }
    
    static class RuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvRuleName, tvTargetApp, tvModificationType;
        SwitchMaterial switchEnabled;
        MaterialButton btnEdit, btnDelete;
        
        public RuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRuleName = itemView.findViewById(R.id.tv_rule_name);
            tvTargetApp = itemView.findViewById(R.id.tv_target_app);
            tvModificationType = itemView.findViewById(R.id.tv_modification_type);
            switchEnabled = itemView.findViewById(R.id.switch_enabled);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
