package com.visitegypt.presentation.home.child.discover.youMayLike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.presentation.home.child.discover.DiscoverChildViewModel;
import com.visitegypt.presentation.home.child.discover.RecommendationPlaceAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class YouMayLike extends AppCompatActivity {
    private RecyclerView youMayLikeRecyclerView;
    private RecommendationPlaceAdapter placesYouMayLikeAdapter;
    private List<Place> placesYouMayLike;
    private DiscoverChildViewModel discoverChildViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_may_like);
        initViews();
        initViewModel();
        getRecommendedPlaces();

    }
    private void initViewModel() {
        discoverChildViewModel = new ViewModelProvider(this).get(DiscoverChildViewModel.class);
    }

    private void initViews() {

        // init Recommendation Places Recycler View
        placesYouMayLike = new ArrayList<>();
        placesYouMayLikeAdapter = new RecommendationPlaceAdapter(placesYouMayLike, this,1);
        youMayLikeRecyclerView = findViewById(R.id.youMayLikeRecyclerView);
        youMayLikeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        youMayLikeRecyclerView.setAdapter(placesYouMayLikeAdapter);

    }
    private void getRecommendedPlaces()
    {
        discoverChildViewModel.getRecommendedPlaces();
        discoverChildViewModel.recommendationPlacesMutableLiveData.observe(this, place -> {
            if (place != null) {
                placesYouMayLike.addAll(place.getUserLikePlaces());
                placesYouMayLikeAdapter.updatePlacesList(placesYouMayLike);
                placesYouMayLike.addAll(place.getPeopleLikePlaces());
                placesYouMayLikeAdapter.updatePlacesList(placesYouMayLike);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}