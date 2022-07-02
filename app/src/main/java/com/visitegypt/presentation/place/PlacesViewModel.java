package com.visitegypt.presentation.place;


import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.FullPlaceActivity;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.usecase.GetAllBadgesUseCase;
import com.visitegypt.domain.usecase.GetBadgesOfUserUseCase;
import com.visitegypt.domain.usecase.GetFullActivitiesUseCase;
import com.visitegypt.domain.usecase.GetFullBadgeUseCase;
import com.visitegypt.domain.usecase.GetPlacesOfCityUseCase;
import com.visitegypt.domain.usecase.GetUserPlaceActivityUseCase;
import com.visitegypt.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlacesViewModel extends ViewModel {

    private static final String TAG = "Places View Model";

    MutableLiveData<List<FullBadge>> fullBadgesMutableLiveData = new MutableLiveData<>();

    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    MutableLiveData<List<Badge>> badgesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<PlaceActivity>> userPlaceActivitiesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<FullPlaceActivity>> fullPlaceActivitiesMutableLiveData = new MutableLiveData<>();
    private SharedPreferences sharedPreferences;

    private GetPlacesOfCityUseCase getPlacesOfCityUseCase;
    private GetAllBadgesUseCase getAllBadgesUseCase;
    private GetBadgesOfUserUseCase getBadgesOfUserUseCase;
    private GetFullBadgeUseCase getFullBadgeUseCase;
    private GetUserPlaceActivityUseCase getUserPlaceActivityUseCase;
    private GetFullActivitiesUseCase getFullActivitiesUseCase;

    private String cityName;

    @Inject
    public PlacesViewModel(GetPlacesOfCityUseCase getPlacesOfCityUseCase,
                           GetBadgesOfUserUseCase getBadgesOfUserUseCase,
                           GetAllBadgesUseCase getAllBadgesUseCase,
                           GetUserPlaceActivityUseCase getUserPlaceActivityUseCase,
                           GetFullBadgeUseCase getFullBadgeUseCase,
                           GetFullActivitiesUseCase getFullActivitiesUseCase,
                           SharedPreferences sharedPreferences) {
        this.getPlacesOfCityUseCase = getPlacesOfCityUseCase;
        this.getAllBadgesUseCase = getAllBadgesUseCase;
        this.getBadgesOfUserUseCase = getBadgesOfUserUseCase;
        this.getUserPlaceActivityUseCase = getUserPlaceActivityUseCase;
        this.getFullBadgeUseCase = getFullBadgeUseCase;
        this.getFullActivitiesUseCase = getFullActivitiesUseCase;
        this.sharedPreferences = sharedPreferences;
    }

    public void getPlacesInCity(String cityName) {
        getPlacesOfCityUseCase.setCityName(cityName);
        List<Place> filteredPlaces = new ArrayList<>();
        getPlacesOfCityUseCase.execute(getPlacesOfCity -> {
            Log.d(TAG, "getPlacesInCity: done");
            List<Place> places = getPlacesOfCity.getPlaces();
            placesMutableLiveData.setValue(getPlacesOfCity.getPlaces());
        }, throwable -> Log.e(TAG, "places retrieve error: " + throwable.getMessage()));

    }

    public void getUserPlaceActivities() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        getUserPlaceActivityUseCase.setUserId(userId);
        getUserPlaceActivityUseCase.execute(placeActivities -> {
            userPlaceActivitiesMutableLiveData.setValue(placeActivities);
        }, throwable -> {
            userPlaceActivitiesMutableLiveData.setValue(null);
            Log.e(TAG, "getUserPlaceActivities: " + throwable.getMessage());
        });
    }

    public void getAllBadges() {
        getAllBadgesUseCase.execute(badgeResponse -> {
            badgesMutableLiveData.setValue(badgeResponse.getBadges());
        }, throwable -> Log.e(TAG, "error retrieving badges: " + throwable.getMessage()));
    }

    public void getCityFullBadges() {
        getFullBadgeUseCase.execute(fullBadges -> {
            fullBadges.removeIf(fullBadge -> !fullBadge.getBadge().getCity().equals(cityName));
            fullBadgesMutableLiveData.setValue(fullBadges);
        }, throwable -> {
            fullBadgesMutableLiveData.setValue(null);
            Log.e(TAG, "getFullBadges: ", throwable);
        });
    }

    public void getFullPlaceActivities() {
        getFullActivitiesUseCase.execute(fullPlaceActivities -> {
            fullPlaceActivitiesMutableLiveData.setValue(fullPlaceActivities);
        }, throwable -> {
            fullPlaceActivitiesMutableLiveData.setValue(null);
            Log.e(TAG, "getFullPlaceActivities: ", throwable);
        });
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}