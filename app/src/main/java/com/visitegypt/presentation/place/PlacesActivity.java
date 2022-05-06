package com.visitegypt.presentation.place;

import android.content.SharedPreferences;
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
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Place;
import com.visitegypt.utils.GamificationRules;
import com.visitegypt.utils.GeneralUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    private RecyclerView placesPlacesActivityRecyclerView, cityBadgesPlacesActivityRecyclerView;
    private MaterialTextView placeNamePlacesActivityTextView, nextLevelXPTextView,
            remainingXpTextView, remainingActivitiesTextView;
    private LinearProgressIndicator cityRemainingProgressPlacesActivityProgressIndicator;
    private ArrayList<Badge> badges, cityBadges;
    private CityBadgesRecyclerViewAdapter cityBadgesRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        cityName = getIntent().getStringExtra("city_name");

        initViews();
        initViewModel(cityName);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        remainingActivitiesTextView = findViewById(R.id.remainingActivitiesPlacesActivityTextView);
        remainingXpTextView = findViewById(R.id.cityRemainingXPlacesActivityTextView);
        nextLevelXPTextView = findViewById(R.id.nextLevelRemainingXPTextViewPlacesActivity);
        cityRemainingProgressPlacesActivityProgressIndicator = findViewById(R.id.cityRemainingProgressPlacesActivityProgressIndicator);

        badges = new ArrayList<>();
        cityBadges = new ArrayList<>();

        cityBadgesPlacesActivityRecyclerView = findViewById(R.id.cityBadgesPlacesActivityRecyclerView);
        cityBadgesRecyclerViewAdapter = new CityBadgesRecyclerViewAdapter(cityBadges, this);
        cityBadgesPlacesActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cityBadgesPlacesActivityRecyclerView.setAdapter(cityBadgesRecyclerViewAdapter);

    }

    private void initViewModel(String cityName) {
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);


        placesViewModel.getUserPlaceActivities();
        placesViewModel.getPlacesInCity(cityName);
        placesViewModel.placesMutableLiveData.observe(this, (Observer<List<Place>>) places -> {
            Log.d(TAG, " getting places to recycler view");
            AtomicInteger progress = new AtomicInteger();
            AtomicInteger maxProgress = new AtomicInteger();

            AtomicInteger ownedPlaces = new AtomicInteger();
            AtomicInteger totalPlaces = new AtomicInteger();

            placesCityRecyclerViewAdapter.setplaceList(places);
            GeneralUtils.LiveDataUtil.observeOnce(placesViewModel.userPlaceActivitiesMutableLiveData,
                    placeActivities -> {
                        if (placeActivities != null)
                            for (Place place : places) {
                                if (place.getPlaceActivities() != null)
                                    GamificationRules.mergeTwoPlaceActivities(place.getPlaceActivities(),
                                            placeActivities);
                            }
                        for (Place place : places) {
                            progress.addAndGet(place.getProgress());
                            maxProgress.addAndGet(place.getMaxProgress());
                            Log.d(TAG, "initViewModel: place progress: " + place.getProgress());
                            Log.d(TAG, "initViewModel: place max progress: " + place.getMaxProgress());
                            if (progress.get() == place.getMaxProgress() && (place.getProgress() != 0)) {
                                ownedPlaces.getAndIncrement();
                                Log.d(TAG, "initViewModel: incremented owned places: " + ownedPlaces.get());
                            }
                            if (place.getMaxProgress() != 0) {
                                totalPlaces.getAndIncrement();
                                Log.d(TAG, "initViewModel: incremented total places: " + totalPlaces.get());
                            }
                        }

                        if (ownedPlaces.get() == 1) {
                            remainingXpTextView.setText(MessageFormat.format("{0} place completed", 1));
                        } else {
                            remainingXpTextView.setText(MessageFormat.format("{0} places completed", ownedPlaces.get()));
                        }

                        if (totalPlaces.get() - ownedPlaces.get() == 1) {
                            nextLevelXPTextView.setText(MessageFormat.format("{0} places remaining", totalPlaces.get() - ownedPlaces.get()));
                        } else if (totalPlaces.get() - ownedPlaces.get() == 0) {
                            nextLevelXPTextView.setText("no places remaining");

                        } else {
                            nextLevelXPTextView.setText(MessageFormat.format("{0} places remaining", totalPlaces.get() - ownedPlaces.get()));
                        }

                        cityRemainingProgressPlacesActivityProgressIndicator.setMax(totalPlaces.get());
                        cityRemainingProgressPlacesActivityProgressIndicator.setProgress(ownedPlaces.get(), true);


                        Log.d(TAG, "initViewModel: max progress of city: " + maxProgress.get());
                        Log.d(TAG, "initViewModel: progress of city: " + progress.get());

                        int remaining = maxProgress.get() - progress.get();

                        if (remaining == 0) {
                            remainingActivitiesTextView.setText("City complete");
                        } else if (remaining == 1) {
                            remainingActivitiesTextView.setText("1 activity remaining");
                        } else {
                            remainingActivitiesTextView.setText(maxProgress.get() - progress.get() + " activities remaining");
                        }
                    });


        });

        placesViewModel.getBadges();
        placesViewModel.badgesMutableLiveData.observe(this, badges -> {
            this.badges = (ArrayList<Badge>) badges;
            for (int i = 0; i < badges.size(); i++) {
                if (badges.get(i).getCity() != null)
                    if (badges.get(i).getCity().equals(cityName)) {
                        cityBadges.add(badges.get(i));
                    }
            }
            cityBadgesRecyclerViewAdapter.setBadges(cityBadges);
        });
    }
}