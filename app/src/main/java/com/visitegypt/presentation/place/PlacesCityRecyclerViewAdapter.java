package com.visitegypt.presentation.place;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.presentation.gamification.GamificationActivity;

import java.util.List;

public class PlacesCityRecyclerViewAdapter extends RecyclerView.Adapter<com.visitegypt.presentation.place.PlacesCityRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "Places Adapter";
    public static String CHOSEN_PLACE_ID = "placeId";
    private List<Place> placeList;
    private Context context;

    public PlacesCityRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public com.visitegypt.presentation.place.PlacesCityRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_place_card, parent, false);
        return new com.visitegypt.presentation.place.PlacesCityRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Place currentPlace = placeList.get(position);
        Log.d(TAG, "onBindViewHolder: " + currentPlace.getDefaultImage());

        if (currentPlace.getImageUrls() != null)
            if (currentPlace.getImageUrls().get(0) != null) {
                if (!currentPlace.getImageUrls().get(0).isEmpty()) {
                    Glide.with(holder.itemView)
                            .load(currentPlace.getImageUrls().get(0))
                            .fitCenter()
                            .into(holder.placeInCityImageView);
                    Log.d(TAG, "image: " + currentPlace.getImageUrls().get(0).toString());

                } else {
                }
            } else {
            }
        if (placeList.get(position).getPlaceActivities() != null) {
            int progress = placeList.get(position).getProgress();
            int maxProgress = placeList.get(position).getMaxProgress();
            int remaining = maxProgress - progress;

            holder.placeInCityRemainingProgressProgressIndicator.setVisibility(View.VISIBLE);

            if (remaining == 0) {
                holder.placesInCityRemainingTextView.setText("Complete");
                holder.placeInCityRemainingProgressProgressIndicator.setProgress(holder.placeInCityRemainingProgressProgressIndicator.getMax(), true);
            } else if (remaining == 1) {
                holder.placesInCityRemainingTextView.setText("1 remaining activity");
                holder.placeInCityRemainingProgressProgressIndicator.setMax(maxProgress);
                holder.placeInCityRemainingProgressProgressIndicator.setProgress(progress, true);
            } else {
                holder.placesInCityRemainingTextView.setText(placeList.get(position).getPlaceActivities().size() + " activities");
                holder.placeInCityRemainingProgressProgressIndicator.setMax(maxProgress);
                holder.placeInCityRemainingProgressProgressIndicator.setProgress(progress, true);
            }
        } else {
            holder.placeInCityRemainingProgressProgressIndicator.setVisibility(View.GONE);
        }
        holder.placeInCityTextView.setText(currentPlace.getTitle());
//        holder.placeInCityRemainingProgressTextView.setProgress(placeList.get(position).getProgress());
//        holder.placesInCityRemainingTextView.setText(placeList.get(position).getProgress() + " remaining activities");
//        holder.itemTitleTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });


    }

    @Override
    public int getItemCount() {
        if (placeList == null)
            return 0;
        return placeList.size();
    }

    public void setplaceList(List<Place> placeList) {
        this.placeList = placeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView placesInCityRemainingTextView, placeInCityTextView;
        private LinearProgressIndicator placeInCityRemainingProgressProgressIndicator;
        private ImageView placeInCityImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeInCityImageView = itemView.findViewById(R.id.placeInCityImageView);
            placesInCityRemainingTextView = itemView.findViewById(R.id.placesInCityRemainingTextView);
            placeInCityTextView = itemView.findViewById(R.id.placeInCityTextView);
            placeInCityRemainingProgressProgressIndicator = itemView.findViewById(R.id.placeInCityRemainingProgressIndicator);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, GamificationActivity.class);
                intent.putExtra(CHOSEN_PLACE_ID, placeList.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });

        }
    }
}