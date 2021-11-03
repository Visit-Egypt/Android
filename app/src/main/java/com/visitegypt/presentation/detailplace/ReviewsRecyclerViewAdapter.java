package com.visitegypt.presentation.detailplace;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Review;

import java.util.List;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "Reviews Adapter";

    private List<Review> reviewsArrayList;
    private Context context;

    public ReviewsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review currentReview = reviewsArrayList.get(position);
        holder.reviewVisitorNameTextView.setText(currentReview.getFirstName());
        holder.reviewTextView.setText(currentReview.getReview());
        holder.reviewRatingBar.setRating(currentReview.getRating());
    }

    @Override
    public int getItemCount() {
        if (reviewsArrayList == null)
            return 0;
        return reviewsArrayList.size();
    }

    public void setReviewsArrayList(List<Review> reviewsArrayList) {
        this.reviewsArrayList = reviewsArrayList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView reviewVisitorNameTextView, reviewTextView;
        private RatingBar reviewRatingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewVisitorNameTextView = itemView.findViewById(R.id.reviewCardVisitorNameTextView);
            reviewTextView = itemView.findViewById(R.id.reviewCardTextView);
            reviewRatingBar = itemView.findViewById(R.id.reviewCardRatingBar);
        }
    }
}
