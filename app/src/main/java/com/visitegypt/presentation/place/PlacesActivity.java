package com.visitegypt.presentation.place;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.jackandphantom.circularprogressbar.CircleProgressbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Place;

import java.util.ArrayList;
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
    private MaterialTextView placeNamePlacesActivityTextView, cityXPlacesActivityTextView;
    private LinearProgressIndicator cityRemainingProgressPlacesActivityProgressIndicator;
    private CircleProgressbar cityBadgePlacesActivityProgressBar;
    private ArrayList<Badge> badges;


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
        placesPlacesActivityRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        placeNamePlacesActivityTextView = findViewById(R.id.cityTitlePlacesActivityTextView);
        placeNamePlacesActivityTextView.setText(cityName);

        cityRemainingProgressPlacesActivityProgressIndicator = findViewById(R.id.cityRemainingProgressPlacesActivityProgressIndicator);
        cityBadgePlacesActivityProgressBar = findViewById(R.id.cityBadgePlacesActivityProgressBar);
        cityXPlacesActivityTextView = findViewById(R.id.cityXPlacesActivityTextView);

        badges = new ArrayList<>();

    }

    private void initViewModel(String cityName) {
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);
        placesViewModel.getPlacesInCity(cityName);

        placesViewModel.placesMutableLiveData.observe(this, new Observer<List<Place>>() {
            @Override
            public void onChanged(List<Place> placeActivities) {
                Log.d(TAG, " getting places to recycler vieww");
                placesCityRecyclerViewAdapter.setplaceList(placeActivities);

            }
        });

        placesViewModel.getBadges();
        placesViewModel.badgesMutableLiveData.observe(this, badges -> {
            this.badges = (ArrayList<Badge>) badges;

        });
        for (int i = 0; i < badges.size(); i++) {
            if (badges.get(i).getCity() == cityName) {
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        cityBadgePlacesActivityProgressBar.setBackground(new BitmapDrawable(cityBadgePlacesActivityProgressBar.getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.get().load(badges.get(i).getImageUrl()).into(target);
            }
        }


    }
}