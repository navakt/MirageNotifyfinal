package com.miragenotify.ui.logs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.miragenotify.R;
import com.miragenotify.adapter.LogAdapter;
import com.miragenotify.viewmodel.LogViewModel;

public class LogsFragment extends Fragment {
    
    private LogViewModel viewModel;
    private LogAdapter adapter;
    private RecyclerView recyclerView;
    private View emptyState;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_logs, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(LogViewModel.class);
        
        recyclerView = view.findViewById(R.id.recycler_logs);
        emptyState = view.findViewById(R.id.empty_state_logs);
        
        adapter = new LogAdapter(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        
        viewModel.getAllLogs().observe(getViewLifecycleOwner(), logs -> {
            adapter.setLogs(logs);
            if (logs == null || logs.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        
        view.findViewById(R.id.btn_clear_logs).setOnClickListener(v -> {
            viewModel.deleteAll();
        });
    }
}
