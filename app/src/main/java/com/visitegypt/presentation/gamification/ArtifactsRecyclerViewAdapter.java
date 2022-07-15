package com.visitegypt.presentation.gamification;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Explore;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ArtifactsRecyclerViewAdapter extends RecyclerView.Adapter<com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Artifacts Adapter in Gamification ";
    private List<Explore> exploreArrayList;

    private Context context;

    public ArtifactsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamification_item_card, parent, false);
        return new com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder holder, int position) {

        Explore currentExplore = exploreArrayList.get(position);
        holder.itemTitleTextView.setText(currentExplore.getTitle());

        holder.itemTitleTextView.setOnClickListener(view -> {
            showHints(view, currentExplore);
        });
        holder.itemImage.setOnClickListener(view -> {
            showHints(view, currentExplore);
        });
        if (currentExplore.getImageUrl() != null) {
            if (!currentExplore.getImageUrl().isEmpty()) {
                Log.d(TAG, "found image for item: " + currentExplore.getImageUrl());
                Glide.with(holder.itemView)
                        .load(currentExplore.getImageUrl())
                        .fitCenter()
                        .into(holder.itemImage);
            } else {
            }
        } else {
            holder.itemImage.setVisibility(View.GONE);
        }
        if (exploreArrayList.get(position).getProgress() == 1) {
            holder.materialCardView.setStrokeColor(holder.materialCardView.getResources().getColor(R.color.camel));
        }

    }

    @Override
    public int getItemCount() {
        if (exploreArrayList == null)
            return 0;
        return exploreArrayList.size();
    }

    public void setExploreArrayList(List<Explore> exploreArrayList) {
        this.exploreArrayList = exploreArrayList;
        notifyDataSetChanged();
    }

    public void showHints(View view, Explore currentExplore) {
        showExploreDialog(currentExplore);
    }

    private void showExploreDialog(Explore explore) {
        Dialog dialog = new Dialog(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_ar_explore, null, false);

        MaterialTextView materialTextView = v.findViewById(R.id.gamificationHintDialogTitle);
        materialTextView.setText(explore.getTitle());

//        materialTextView.setText(explore.getTitle());

        ImageView zoomableImageView = v.findViewById(R.id.gamificationHintDialogZoomableImageView);
        if (explore.getImageUrl() != null) {
            if (!explore.getImageUrl().isEmpty()) {
                Picasso.get().load(explore.getImageUrl()).into(zoomableImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(zoomableImageView);
                        photoViewAttacher.update();
                        ProgressBar progressBar = v.findViewById(R.id.gamificationHintDialogProgressBar);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        }

        if (explore.getHints() != null) {
            GamificationHintRecyclerViewAdapter adapter = new GamificationHintRecyclerViewAdapter(explore.getHints());
            RecyclerView recyclerView = v.findViewById(R.id.gamificationHintDialogRecyclerView);
            recyclerView.setAdapter(adapter);
        }

        dialog.setContentView(v);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;
        private MaterialTextView itemTitleTextView;
        private MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemGamificationCardImageView);
            itemTitleTextView = itemView.findViewById(R.id.itemGamificationCardTitleTextView);
            materialCardView = itemView.findViewById(R.id.cardExploreMaterialCardView);
        }
    }
}