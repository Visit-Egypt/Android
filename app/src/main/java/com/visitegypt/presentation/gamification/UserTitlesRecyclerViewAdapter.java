package com.visitegypt.presentation.gamification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.domain.model.UserTitle;

import java.util.ArrayList;

public class UserTitlesRecyclerViewAdapter extends RecyclerView.Adapter<UserTitlesRecyclerViewAdapter.ViewHolder> {
    private ArrayList<UserTitle> userTitles;

    public UserTitlesRecyclerViewAdapter(ArrayList<UserTitle> userTitles) {
        this.userTitles = userTitles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_title, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(userTitles.get(position).getTitle());
        holder.unlockLevelTextView.setText("Unlocked at level: " + userTitles.get(position).getLevel());
        holder.unlocked.setChecked(userTitles.get(position).isPassed());
    }

    @Override
    public int getItemCount() {
        return userTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, unlockLevelTextView;
        private CheckBox unlocked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.userTitleCardTextView);
            unlockLevelTextView = itemView.findViewById(R.id.userTitleCardLevelTextView);
            unlocked = itemView.findViewById(R.id.userTItleCardCheckBox);
        }
    }
}
