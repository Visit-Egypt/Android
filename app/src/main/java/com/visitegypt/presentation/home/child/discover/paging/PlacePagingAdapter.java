package com.visitegypt.presentation.home.child.discover.paging;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.presentation.detail.DetailActivity;

public class PlacePagingAdapter extends PagingDataAdapter<Place, PlacePagingAdapter.PlacePagingHolder> {
    private static final String TAG = "Discover Place Adapter";
    public static String CHOSEN_PLACE_ID = "placeId";
    private static Context context;

    public static void setContext(Context context) {
        PlacePagingAdapter.context = context;
    }

    public PlacePagingAdapter(@NonNull DiffUtil.ItemCallback<Place> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public PlacePagingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlacePagingHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.places_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlacePagingHolder holder, int position) {
        Place place = getItem(position);

        try {
            int reviewsSize = place.getReviews().size();

            if (place.getReviews() != null) {
                holder.txtCommentsCount.setText("" + reviewsSize);
            }

            float averageReview = 0;
            for (Review review : place.getReviews()) {
                averageReview += review.getRating() / reviewsSize;
            }
            holder.txtRating.setText(String.format("%0.2f", averageReview));
        } catch (Exception ignored) {

        }

        if (place.getTitle() != null)
            holder.txtPlaceName.setText(place.getTitle());
        if (place.getCity() != null)
            holder.txtCityName.setText(place.getCity());
        if (place.getDefaultImage() != null && !place.getDefaultImage().isEmpty()) {
            Log.d("TAG", "default image found for: " + place.getTitle());
            Picasso.get().load(place.getDefaultImage()).into(holder.imgPlace);
        } else if (place.getImageUrls() != null && !place.getImageUrls().isEmpty()) {
            Log.d("TAG", "default image not found! loading first image instead for: " + place.getTitle());
            Picasso.get().load(place.getImageUrls().get(0)).into(holder.imgPlace);
        } else {
            Log.d("TAG", "no images found for: " + place.getTitle());
        }
        holder.itemView.setOnClickListener(view -> {

                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(CHOSEN_PLACE_ID, place.getId());
                    context.startActivity(intent);
        });

    }

    public static class PlacePagingHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPlace;
        private final TextView txtCityName, txtPlaceName, txtCommentsCount, txtRating;

        public PlacePagingHolder(@NonNull View itemView) {
            super(itemView);

            txtPlaceName = itemView.findViewById(R.id.txtPlaceCardNewTitle);
            imgPlace = itemView.findViewById(R.id.imgPlaceCardNew);
            txtCityName = itemView.findViewById(R.id.txtPlaceCardNewCity);
            txtCommentsCount = itemView.findViewById(R.id.txtPlaceCardComment);
            txtRating = itemView.findViewById(R.id.txtPlaceCardReviewsNumber);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

}
