package com.visitegypt.presentation.gamification;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;
import com.visitegypt.R;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.presentation.chatbot.ChatbotActivity;

import java.util.ArrayList;

public class GamificationCardRecyclerViewAdapter extends RecyclerView.Adapter<GamificationCardRecyclerViewAdapter.GamificationRecyclerViewViewHolder> {
    private ArrayList<PlaceActivity> placeActivities;
    private boolean expanded;

    public GamificationCardRecyclerViewAdapter(ArrayList<PlaceActivity> placeActivities) {
        this.placeActivities = placeActivities;
    }

    @NonNull
    @Override
    public GamificationRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamification_activity_card, parent, false);
        return new GamificationRecyclerViewViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GamificationRecyclerViewViewHolder holder, int position) {
        PlaceActivity placeActivity = placeActivities.get(position);
        holder.activityTitleTextView.setText(placeActivity.getTitle());
        holder.activityDescriptionTextView.setText(placeActivity.getDescription());
        holder.checkBox.setChecked(placeActivity.isFinished());

        switch (placeActivity.getType()) {
            case PlaceActivity.VISIT_LOCATION:
                holder.button.setText("Confirm Location");
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                break;
            case PlaceActivity.POST_STORY:
                holder.button.setText("Post a story");
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ACTIVITY_POST_STORY));
                    }
                });
                break;
            case PlaceActivity.POST_POST:
                holder.button.setText("Post a Post");
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ACTIVITY_POST_POST));
                    }
                });
                break;
            case PlaceActivity.ASK_CHAT_BOT:
                holder.button.setText("Head to Chat Bot");
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ChatbotActivity.class));
                    }
                });
                break;
            case PlaceActivity.POST_REVIEW:
                holder.button.setText("Post an honest Review");
                break;
            case PlaceActivity.GENERAL:
                holder.button.setVisibility(View.GONE);
            default:
                break;
        }
        this.expanded = placeActivity.isExpanded();
        holder.expandableLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return placeActivities.size();
    }

    class GamificationRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView activityTitleTextView, activityDescriptionTextView;
        private MaterialCheckBox checkBox;
        private LinearLayout expandableLayout;
        private MaterialButton button;


        public GamificationRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            activityTitleTextView = itemView.findViewById(R.id.gamificationActivityCardTitleTextView);
            activityDescriptionTextView = itemView.findViewById(R.id.gamificationActivityCardDescriptionTextView);
            checkBox = itemView.findViewById(R.id.gamificationActivityCardCheckBox);
            button = itemView.findViewById(R.id.gamificationActivityCardButton);

            expandableLayout = itemView.findViewById(R.id.gamificationActivityCardExpandableLayout);

            itemView.setOnClickListener(view -> {
                placeActivities.get(getAdapterPosition()).setExpanded(!placeActivities.get(getAdapterPosition()).isExpanded());
                notifyItemChanged(getAdapterPosition());
            });
        }
    }
}
