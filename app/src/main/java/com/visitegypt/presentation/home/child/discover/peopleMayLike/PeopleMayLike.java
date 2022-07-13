package com.visitegypt.presentation.home.child.discover.peopleMayLike;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.presentation.home.child.discover.DiscoverChildViewModel;
import com.visitegypt.presentation.home.child.discover.RecommendationPlaceAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PeopleMayLike extends AppCompatActivity {
    private RecyclerView peopleMayLikeRecyclerView;
    private RecommendationPlaceAdapter placesPeopleMayLikeAdapter;
    private List<Place> placesPeopleMayLike;
    private DiscoverChildViewModel discoverChildViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_may_like);
        initViews();
        initViewModel();
        getRecommendedPlaces();
    }

    private void initViews() {

        // init Recommendation Places Recycler View
        placesPeopleMayLike = new ArrayList<>();
        placesPeopleMayLikeAdapter = new RecommendationPlaceAdapter(placesPeopleMayLike, this, 1);
        peopleMayLikeRecyclerView = findViewById(R.id.peopleMayLikeRecyclerView);
        peopleMayLikeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        peopleMayLikeRecyclerView.setAdapter(placesPeopleMayLikeAdapter);

    }

    public void initViewModel() {
        discoverChildViewModel = new ViewModelProvider(this).get(DiscoverChildViewModel.class);
    }

    private void getRecommendedPlaces() {
        discoverChildViewModel.getRecommendedPlaces();
        discoverChildViewModel.recommendationPlacesMutableLiveData.observe(this, place -> {
            if (place != null) {
                placesPeopleMayLike.addAll(place.getPeopleLikePlaces());
                placesPeopleMayLikeAdapter.updatePlacesList(placesPeopleMayLike);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}