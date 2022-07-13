package com.visitegypt.presentation.home.child.discover;

import static com.visitegypt.presentation.home.child.discover.DiscoverPlaceAdapter.CHOSEN_PLACE_ID;

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

import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.presentation.detail.DetailActivity;

import java.util.Calendar;
import java.util.List;

public class RecommendationPlaceAdapter extends RecyclerView.Adapter<RecommendationPlaceAdapter.RecommendPlaceViewHolder> {

    private List<Place> placeList;
    private final Context context;
    private final int maxSize = 2;
    private int flag;

    public RecommendationPlaceAdapter(List<Place> placeList, Context context, int flag) {
        this.placeList = placeList;
        this.context = context;
        this.flag = flag;
    }

    @NonNull
    @Override
    public RecommendPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecommendPlaceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommendation_card, parent, false));
    }

    public void updatePlacesList(List<Place> placesList) {
        this.placeList = placesList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendPlaceViewHolder holder, int position) {
        holder.txtPlaceTitle.setText(placeList.get(position).getTitle());
        holder.txtCityTitle.setText(placeList.get(position).getCity());
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 21) {

            holder.txtState.setText("open");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            holder.txtState.setText("closed");
        }
        if (placeList.get(position).getDefaultImage() != null && !placeList.get(position).getDefaultImage().isEmpty()) {
            Log.d("TAG", "default image found for: " + placeList.get(position).getTitle());
            Picasso.get().load(placeList.get(position).getDefaultImage()).into(holder.imgPlace);
        } else if (placeList.get(position).getImageUrls() != null) {
            Log.d("TAG", "default image not found! loading first image instead for: " + placeList.get(position).getTitle());
            Picasso.get().load(placeList.get(position).getImageUrls().get(0)).into(holder.imgPlace);
        } else {
            Log.d("TAG", "no images found for: " + placeList.get(position).getTitle());
        }
        float rating = 0;
        for (Review review : placeList.get(position).getReviews()) {
            rating += review.getRating();
        }
        if (rating != 0) {
            String ratingText = String.format("%.1f", rating / placeList.get(position).getReviews().size());
            holder.txtTotalRate.setText(ratingText);
        }

    }

    @Override
    public int getItemCount() {
        if (flag == 1) {
            if (placeList == null || placeList.isEmpty()) {
                return 0;
            } else {
                return placeList.size();
            }
        } else {
            if (placeList.size() < maxSize) {
                return placeList.size();

            } else
                return maxSize;
        }
    }

    public class RecommendPlaceViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtPlaceTitle;
        private final ImageView imgPlace;
        private final TextView txtCityTitle;
        private final TextView txtState;
        private final TextView txtTotalRate;

        public RecommendPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace = itemView.findViewById(R.id.imgPlaceCard);
            txtCityTitle = itemView.findViewById(R.id.cityTextView);
            txtPlaceTitle = itemView.findViewById(R.id.placeCardNameTextView);
            txtState = itemView.findViewById(R.id.stateTextView);
            txtTotalRate = itemView.findViewById(R.id.txtPlaceCardRating);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(CHOSEN_PLACE_ID, placeList.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });
        }
    }
}
