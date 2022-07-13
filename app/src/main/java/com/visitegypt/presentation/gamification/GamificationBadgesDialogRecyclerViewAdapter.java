package com.visitegypt.presentation.gamification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.visitegypt.R;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.utils.GamificationRules;

import java.util.ArrayList;

public class GamificationBadgesDialogRecyclerViewAdapter extends RecyclerView.Adapter<GamificationBadgesDialogRecyclerViewAdapter.ViewHolder> {

    private ArrayList<BadgeTask> badgeTasks;

    public GamificationBadgesDialogRecyclerViewAdapter(ArrayList<BadgeTask> badgeTasks) {
        this.badgeTasks = badgeTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_badge_task_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BadgeTask badgeTask = badgeTasks.get(position);
        holder.titleTextView.setText(badgeTask.getTaskTitle());
        if (badgeTask.getMaxProgress() != badgeTask.getProgress())
            holder.descriptionTextView.setText(badgeTask.getProgress() + "/" + badgeTask.getMaxProgress() + " remaining");
        else {
            holder.descriptionTextView.setText("Completed");
            holder.circleProgressbar.setForegroundProgressColor(holder.circleProgressbar.getResources().getColor(R.color.camel));
        }

        holder.circleProgressbar.setProgress(0);
        holder.circleProgressbar.setMaxProgress(badgeTask.getMaxProgress());
        holder.circleProgressbar.setProgressWithAnimation(badgeTask.getProgress());
//
//        if (badgeTask.getImageUrl() != null && !badgeTask.getImageUrl().isEmpty()) {
//            Target target = new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                    holder.circleProgressbar.setBackground(new BitmapDrawable(holder.circleProgressbar.getResources(), bitmap));
//                }
//
//                @Override
//                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable placeHolderDrawable) {
//                }
//            };
//
//            Picasso.get().load(badgeTask.getImageUrl()).into(target);
//        } else {
//    }
        holder.circleProgressbar.setBackground(GamificationRules.getBadgeTaskDrawable(holder.circleProgressbar.getContext(), badgeTask.getTaskTitle()));

    }

    @Override
    public int getItemCount() {
        if (badgeTasks == null)
            return 0;
        return badgeTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleProgressbar circleProgressbar;
        private MaterialTextView titleTextView, descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleProgressbar = itemView.findViewById(R.id.dialogCardTaskCircleBar);
            titleTextView = itemView.findViewById(R.id.dialogCardTaskTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.dialogTaskDescriptionTextView);
        }
    }
}
