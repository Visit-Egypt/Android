package com.visitegypt.presentation.tripmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.visitegypt.R;

import java.util.List;
import java.util.LongSummaryStatistics;

public class HashtagChipAdapter extends RecyclerView.Adapter<HashtagChipAdapter.HashingChipViewHolder> {
    private List<String> labelesList;

    public HashtagChipAdapter(List<String> labelesList) {
        this.labelesList = labelesList;
    }

    //    private HashtagListener  listener;
    @NonNull
    @Override
    public HashingChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HashingChipViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hashtag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HashingChipViewHolder holder, int position) {
        if (!labelesList.isEmpty())
        {
            holder.bind(labelesList.get(position));
        }

    }
    public void updateLabelesList(List<String> labelesList)
    {
        this.labelesList = labelesList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        int size = labelesList.size();
        return  size != 0 ?  size: 0;
    }

    public class HashingChipViewHolder extends RecyclerView.ViewHolder {
       private final Chip chip ;
        public HashingChipViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.textHashtag);
        }
        public void bind(String filter)
        {
            chip.setText(filter);
        }
    }
}
