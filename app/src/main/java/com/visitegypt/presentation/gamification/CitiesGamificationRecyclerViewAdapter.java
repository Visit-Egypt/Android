package com.visitegypt.presentation.gamification;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

public class CitiesGamificationRecyclerViewAdapter extends RecyclerView.Adapter<CitiesGamificationRecyclerViewAdapter.ViewHolder> {

    private String cityName;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private MaterialTextView titleTextView, descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
