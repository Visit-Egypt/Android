package com.visitegypt.presentation.place;

import static com.visitegypt.utils.GeneralUtils.LiveDataUtil.observeOnce;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.visitegypt.R;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.FullPlaceActivity;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.presentation.gamification.BadgesSliderViewAdapter;
import com.visitegypt.utils.GamificationRules;

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
    private RecyclerView placesPlacesActivityRecyclerView, cityBadgesPlacesActivityRecyclerView;
    private MaterialTextView placeNamePlacesActivityTextView, nextLevelXPTextView,
            remainingXpTextView, remainingActivitiesTextView;
    private LinearProgressIndicator cityRemainingProgressPlacesActivityProgressIndicator;
    private ArrayList<Badge> badges, cityBadges;
    private BadgesSliderViewAdapter cityBadgesRecyclerViewAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayoutCompat fullLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        cityName = getIntent().getStringExtra("city_name");

        initViews();
        startShimmer();
//        initViewModel(cityName);
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
        cityBadgesRecyclerViewAdapter = new BadgesSliderViewAdapter(cityBadges, this);
        cityBadgesPlacesActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        cityBadgesPlacesActivityRecyclerView.setAdapter(cityBadgesRecyclerViewAdapter);

        fullLayout = findViewById(R.id.activityPlacesLinearLayout);
        shimmerFrameLayout = findViewById(R.id.activityPlacesShimmerFrameLayout);
    }

    private void removeShimmer() {
        shimmerFrameLayout.stopShimmerAnimation();
        shimmerFrameLayout.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
    }

    private void startShimmer() {
        shimmerFrameLayout.startShimmerAnimation();
        fullLayout.setVisibility(View.GONE);
    }

    private void initViewModel(String cityName) {
        placesViewModel = new ViewModelProvider(this).get(PlacesViewModel.class);

        placesViewModel.setCityName(cityName);
        placesViewModel.getUserPlaceActivities();
        placesViewModel.getPlacesInCity();
        placesViewModel.getFullPlaceActivities();

        observeOnce(placesViewModel.placesMutableLiveData, (Observer<List<Place>>) places -> {
            Log.d(TAG, " getting places to recycler view");
            int totalPlaces = 0;

            Log.d(TAG, "initViewModel: " + places.size());
            for (Place place : places) {
                if (place.getExplores() != null || place.getPlaceActivities() != null) {
                    if (place.getExplores() != null && !place.isExploresAdded()) {
                        place.getPlaceActivities().addAll(place.getExplores());
                        place.setExploresAdded(true);
                    }

                    totalPlaces++;
                }
            }

            int finalTotalPlaces = totalPlaces;
            placesViewModel.fullPlaceActivitiesMutableLiveData.observe(this, fullPlaceActivities -> {
                if (fullPlaceActivities != null) {
                    for (FullPlaceActivity fullPlaceActivity : fullPlaceActivities) {
                        for (Place place : places) {
                            if (place.getPlaceActivities() != null)
                                for (PlaceActivity placeActivity : place.getAllTypesOfActivities()) {
//                                            Log.d(TAG, "initViewModel all activities: " + new Gson().toJson(place.getAllTypesOfActivities()));
                                    if (placeActivity.getId().equals(fullPlaceActivity.getId())) {
                                        placeActivity.setProgress(fullPlaceActivity.getProgress());
                                        placeActivity.setFinished(fullPlaceActivity.isFinished());
                                    }
                                }
                        }
                    }
                }
                int ownedPlaces = 0;
                for (Place place : places) {
                    if (place.isOwned()) {
                        ownedPlaces += 1;
                    }
//                            if (place.getPlaceActivities() != null){
//                                totalPlaces++;
//                            }
                }
                remainingActivitiesTextView.setText(ownedPlaces + "/" + finalTotalPlaces + " places complete");
                cityRemainingProgressPlacesActivityProgressIndicator.setMax(finalTotalPlaces);
                cityRemainingProgressPlacesActivityProgressIndicator.setProgress(ownedPlaces);
                Log.d(TAG, "initViewModel: sharqia" + new Gson().toJson(places));
                placesCityRecyclerViewAdapter.setplaceList(places);
                removeShimmer();
            });
        });
        initBadges();
        //initActivities();
    }


    private void initBadges() {
        cityBadges.clear();
        placesViewModel.getCityFullBadges();
        placesViewModel.setCityName(cityName);
        placesViewModel.getAllCityBadges();

        placesViewModel.badgesMutableLiveData.observe(this, allBadges -> {
            Log.d(TAG, "initBadges: " + new Gson().toJson(allBadges));
            observeOnce(placesViewModel.fullBadgesMutableLiveData, fullBadges -> {
                Log.d(TAG, "initBadges: full badges: " + new Gson().toJson(fullBadges));
                ArrayList<Badge> badgeList = new ArrayList<>();
                if (fullBadges != null)
                    for (FullBadge fullBadge : fullBadges) {
                        Log.d(TAG, "initBadges: converting fullBadge to badge: " + fullBadge.getBadge().getTitle());
                        badgeList.add(GamificationRules.fullBadgeToBadge(fullBadge));
                    }

                GamificationRules.mergeTwoBadges(allBadges, badgeList);
                cityBadgesRecyclerViewAdapter.setBadges((ArrayList<Badge>) allBadges);
            });
        });
    }

//    private void initActivities() {
//        placesViewModel.getFullPlaceActivities();
//        observeOnce(placesViewModel.fullPlaceActivitiesMutableLiveData, fullPlaceActivities -> {
//
//        });
//    }
}