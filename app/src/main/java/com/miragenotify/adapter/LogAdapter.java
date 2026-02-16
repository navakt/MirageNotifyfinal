package com.miragenotify.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miragenotify.R;
import com.miragenotify.model.NotificationLog;
import com.miragenotify.utils.NotificationHelper;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    
    private final Context context;
    private List<NotificationLog> logs = new ArrayList<>();
    
    public LogAdapter(Context context) {
        this.context = context;
    }
    
    public void setLogs(List<NotificationLog> logs) {
        this.logs = logs != null ? logs : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_log, parent, false);
        return new LogViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        NotificationLog log = logs.get(position);
        
        holder.tvAppName.setText(log.getAppName());
        holder.tvTimestamp.setText(NotificationHelper.getRelativeTime(log.getTimestamp()));
        
        holder.tvOriginalTitle.setText(log.getOriginalTitle());
        holder.tvOriginalContent.setText(log.getOriginalContent());
        
        if (log.isWasModified()) {
            holder.tvModifiedBadge.setVisibility(View.VISIBLE);
            holder.layoutModified.setVisibility(View.VISIBLE);
            holder.tvModifiedTitle.setText(log.getModifiedTitle());
            holder.tvModifiedContent.setText(log.getModifiedContent());
        } else {
            holder.tvModifiedBadge.setVisibility(View.GONE);
            holder.layoutModified.setVisibility(View.GONE);
        }
    }
    
    @Override
    public int getItemCount() {
        return logs.size();
    }
    
    static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppName, tvTimestamp, tvModifiedBadge;
        TextView tvOriginalTitle, tvOriginalContent;
        TextView tvModifiedTitle, tvModifiedContent;
        View layoutModified;
        
        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppName = itemView.findViewById(R.id.tv_app_name);
            tvTimestamp = itemView.findViewById(R.id.tv_timestamp);
            tvModifiedBadge = itemView.findViewById(R.id.tv_modified_badge);
            tvOriginalTitle = itemView.findViewById(R.id.tv_original_title);
            tvOriginalContent = itemView.findViewById(R.id.tv_original_content);
            tvModifiedTitle = itemView.findViewById(R.id.tv_modified_title);
            tvModifiedContent = itemView.findViewById(R.id.tv_modified_content);
            layoutModified = itemView.findViewById(R.id.layout_modified);
        }
    }
}
