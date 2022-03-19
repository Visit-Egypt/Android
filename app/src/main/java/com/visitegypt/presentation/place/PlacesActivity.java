package com.visitegypt.presentation.place;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.domain.model.Place;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlacesActivity extends AppCompatActivity {
    private static final String TAG = "place Activity";
    @Inject
    public SharedPreferences sharedPreferences;
    private PlacesViewModel placesViewModel;
    private String cityName;
    private PlacesCityRecyclerViewAdapter placesCityRecyclerViewAdapter;
    private RecyclerView placesPlacesActivityRecyclerView;
    private TextView placeNamePlacesActivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        cityName = getIntent().getStringExtra("city_name");

        initViews();
        initViewModel(cityName);
    }

    private void initViews() {
        placesPlacesActivityRecyclerView = findViewById(R.id.placesPlacesActivityRecyclerView);
        placesCityRecyclerViewAdapter = new PlacesCityRecyclerViewAdapter(this);
        placesPlacesActivityRecyclerView.setLayoutManager(new LinearLayoutManager(PlacesActivity.this, LinearLayoutManager.VERTICAL, true));
        placesPlacesActivityRecyclerView.setAdapter(placesCityRecyclerViewAdapter);
        placeNamePlacesActivityTextView = findViewById(R.id.placeNamePlacesActivityTextView);
        placeNamePlacesActivityTextView.setText(cityName);
    }

    private void initViewModel(String cityName) {
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        placesViewModel.getPlacesInCity(cityName);
        Log.d(TAG, "initViewModel: placess done ");

        placesViewModel.placesMutableLiveData.observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> placeActivities) {
                Log.d(TAG, " getting places to recycler vieww");
                placesCityRecyclerViewAdapter.setplaceList(placeActivities);


            }
        });


    }
}