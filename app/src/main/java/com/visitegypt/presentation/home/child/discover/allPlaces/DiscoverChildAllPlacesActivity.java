package com.visitegypt.presentation.home.child.discover.allPlaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.Toast;

import com.visitegypt.R;
import com.visitegypt.presentation.home.child.discover.paging.PlacePagingAdapter;
import com.visitegypt.presentation.home.child.discover.paging.PlaceserComparator;
import com.visitegypt.presentation.paging.CustomLoadStateAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DiscoverChildAllPlacesActivity extends AppCompatActivity {
    private DiscoverChildAllPlacesViewModel discoverChildAllPlacesViewModel;
    private RecyclerView placesToVisitRecyclerView;
    private PlacePagingAdapter placePagingAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_child_all_places);
        initViews();
        discoverChildAllPlacesViewModel = new ViewModelProvider(this).get(DiscoverChildAllPlacesViewModel.class);
        discoverChildAllPlacesViewModel.init();
        initPaging();
        getAllPlaces();
    }

    private void initViews() {
        placesToVisitRecyclerView = findViewById(R.id.placesToVisitRecyclerView);
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            Toast.makeText(this, "Refreshing", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void initPaging() {

        placePagingAdapter = new PlacePagingAdapter(new PlaceserComparator());
        placePagingAdapter.setContext(this);
        CustomLoadStateAdapter customLoadStateAdapter = new CustomLoadStateAdapter(view -> {
            placePagingAdapter.retry();
        });
        placesToVisitRecyclerView.setAdapter(placePagingAdapter.withLoadStateFooter(customLoadStateAdapter));
    }

    private void getAllPlaces() {
        discoverChildAllPlacesViewModel.flowable.subscribe(placePagingData -> {
            this.placePagingAdapter.submitData(getLifecycle(), placePagingData);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}