package com.visitegypt.presentation.home.child.discover.allPlaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.visitegypt.R;
import com.visitegypt.presentation.home.child.discover.paging.PlacePagingAdapter;
import com.visitegypt.presentation.home.child.discover.paging.PlaceserComparator;
import com.visitegypt.presentation.paging.CustomLoadStateAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DiscoverChildAllPlaces extends Fragment {

    private DiscoverChildAllPlacesViewModel discoverChildAllPlacesViewModel;
    private RecyclerView placesToVisitRecyclerView;
    private PlacePagingAdapter placePagingAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View root;

    public static DiscoverChildAllPlaces newInstance() {
        return new DiscoverChildAllPlaces();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        container.removeAllViews();
        root = inflater.inflate(R.layout.fragment_discover_child_all_places, container, false);

        initViews();
        initPaging();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        discoverChildAllPlacesViewModel = new ViewModelProvider(this).get(DiscoverChildAllPlacesViewModel.class);
        // TODO: Use the ViewModel
        discoverChildAllPlacesViewModel.init();
        getAllPlaces();
    }

    private void initViews() {
        placesToVisitRecyclerView = root.findViewById(R.id.placesToVisitRecyclerView);
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {

            Toast.makeText(getContext(), "Refreshing", Toast.LENGTH_LONG).show();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void initPaging() {

        placePagingAdapter = new PlacePagingAdapter(new PlaceserComparator());
        placePagingAdapter.setContext(getContext());
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

}