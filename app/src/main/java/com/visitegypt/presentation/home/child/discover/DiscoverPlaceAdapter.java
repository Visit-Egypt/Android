package com.visitegypt.presentation.home.child.discover;

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

import java.util.List;

public class DiscoverPlaceAdapter extends RecyclerView.Adapter<DiscoverPlaceAdapter.PlaceViewHolder> {
    private static final String TAG = "Discover Place Adapter";
    public static String CHOSEN_PLACE_ID = "placeId";
    private final int maxSize = 5;
    private final Context context;
    private List<Place> placesList;

    public DiscoverPlaceAdapter(Context context, List<Place> placesList) {
        this.context = context;
        this.placesList = placesList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaceViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.places_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        if (placesList.get(position).getTitle() != null)
            holder.txtPlaceName.setText(placesList.get(position).getTitle());
        if (placesList.get(position).getCity() != null)
            holder.txtCityName.setText(placesList.get(position).getCity());
        if (placesList.get(position).getDefaultImage() != null) {
            Log.d("TAG", "default image found for: " + placesList.get(position).getTitle());
            Picasso.get().load(placesList.get(position).getDefaultImage()).into(holder.imgPlace);
        } else if (placesList.get(position).getImageUrls() != null) {
            Log.d("TAG", "default image not found! loading first image instead for: " + placesList.get(position).getTitle());
            Picasso.get().load(placesList.get(position).getImageUrls().get(0)).into(holder.imgPlace);
        } else {
            Log.d("TAG", "no images found for: " + placesList.get(position).getTitle());
        }
        if (placesList.get(position).getReviews() != null) {
            holder.txtReviewsNumber.setText(placesList.get(position).getReviews().size() + "");
        }

        float rating = 0;
        for (Review review : placesList.get(position).getReviews()) {
            rating += review.getRating();
        }
        if (rating != 0) {
            String ratingText = String.format("%.1f", rating / placesList.get(position).getReviews().size());
            holder.txtRating.setText(ratingText);
        }
    }

    @Override
    public int getItemCount() {
        if (placesList == null || placesList.isEmpty())
            return 0;
        if (placesList.size() < maxSize) {
            return placesList.size();

        } else
           return maxSize;
    }

    public void updatePlacesList(List<Place> placesList) {
        this.placesList = placesList;
        Log.d(TAG, "updatePlacesList: is done");
        notifyDataSetChanged();
    }


    public class PlaceViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPlace;
        private final TextView txtPlaceName, txtCityName, txtReviewsNumber, txtRating;


        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPlaceName = itemView.findViewById(R.id.txtPlaceCardNewTitle);
            imgPlace = itemView.findViewById(R.id.imgPlaceCardNew);
            txtCityName = itemView.findViewById(R.id.txtPlaceCardNewCity);
            txtReviewsNumber = itemView.findViewById(R.id.txtPlaceCardComment);
            txtRating = itemView.findViewById(R.id.txtPlaceCardReviewsNumber);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(CHOSEN_PLACE_ID, placesList.get(getAdapterPosition()).getId());
                context.startActivity(intent);
            });

        }
    }
}
