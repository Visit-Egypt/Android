package com.visitegypt.presentation.review;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.presentation.detail.DetailViewModel;
import com.visitegypt.presentation.detail.ReviewsRecyclerViewAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ReviewActivity extends AppCompatActivity {
    private static final String TAG = "Review Activity";
    @Inject
    public SharedPreferences sharedPreferences;
    private DetailViewModel detailViewModel;
    private ReviewViewModel reviewViewModel;
    private ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    private RecyclerView itemsRecyclerView, reviewsRecyclerView;
    private String placeId, placeIdToDetail;
    private TextView noReviewsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        placeId = getIntent().getStringExtra("place_id");
        placeIdToDetail = placeId;
        initViews();
        initViewModel(placeId);
    }

    private void initViews() {
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView);
        reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(this);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsRecyclerView.setAdapter(reviewsRecyclerViewAdapter);
        noReviewsTextView = findViewById(R.id.noReviewsTextView);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        reviewViewModel.reviewMutableLiveData.observe(ReviewActivity.this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewsRecyclerViewAdapter.setReviewsArrayList(reviews);
            }
        });
    }

    private void initViewModel(String placeId) {
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        reviewViewModel.getPlace(placeId);
        reviewViewModel.placesMutableLiveData.observe(this, new Observer<Place>() {
            @Override
            public void onChanged(Place place) {
                if (place.getReviews() != null & !place.getReviews().isEmpty()) {
                    noReviewsTextView.setVisibility(View.GONE);
                    Log.d(TAG, "reviews: " + place.getReviews().toString());
                    reviewsRecyclerViewAdapter.setReviewsArrayList(place.getReviews());
                    Log.d(TAG, "reviews available");
                } else {
                    Log.d(TAG, "no reviews available ");
                }
            }
        });
    }

}