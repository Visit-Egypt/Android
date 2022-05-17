package com.visitegypt.presentation.tripmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.visitegypt.R;

import java.util.List;

public class FilterChipAdapter extends RecyclerView.Adapter<FilterChipAdapter.FilterChipViewHolder> {
    private List<String> labelesList;

    public FilterChipAdapter(List<String> labelesList) {
        this.labelesList = labelesList;
    }

    @NonNull
    @Override
    public FilterChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilterChipViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hashtag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilterChipViewHolder holder, int position) {
        if (!labelesList.isEmpty()) {
            holder.bind(labelesList.get(position));
        }

    }

    public void updateLabelesList(List<String> labelesList) {
        this.labelesList = labelesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int size = labelesList.size();
        return size != 0 ? size : 0;
    }

    public class FilterChipViewHolder extends RecyclerView.ViewHolder {
        private final Chip chip;

        public FilterChipViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.textHashtag);
        }

        public void bind(String filter) {
            chip.setText(filter);
        }
    }
}
