package com.visitegypt.presentation.place;


import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.usecase.GetAllBadgesUseCase;
import com.visitegypt.domain.usecase.GetBadgesOfUserUseCase;
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

    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    MutableLiveData<List<Badge>> badgesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<PlaceActivity>> userPlaceActivitiesMutableLiveData = new MutableLiveData<>();

    private GetPlacesOfCityUseCase getPlacesOfCityUseCase;
    private GetAllBadgesUseCase getAllBadgesUseCase;
    private GetBadgesOfUserUseCase getBadgesOfUserUseCase;
    @Inject
    SharedPreferences sharedPreferences;
    private GetUserPlaceActivityUseCase getUserPlaceActivityUseCase;

    @Inject
    public PlacesViewModel(GetPlacesOfCityUseCase getPlacesOfCityUseCase,
                           GetBadgesOfUserUseCase getBadgesOfUserUseCase,
                           GetAllBadgesUseCase getAllBadgesUseCase,
                           GetUserPlaceActivityUseCase getUserPlaceActivityUseCase) {
        this.getPlacesOfCityUseCase = getPlacesOfCityUseCase;
        this.getAllBadgesUseCase = getAllBadgesUseCase;
        this.getBadgesOfUserUseCase = getBadgesOfUserUseCase;

        this.getUserPlaceActivityUseCase = getUserPlaceActivityUseCase;
    }

    public void getPlacesInCity(String cityName) {
        getPlacesOfCityUseCase.setCityName(cityName);
        List<Place> filteredPlaces = new ArrayList<>();
        getPlacesOfCityUseCase.execute(getPlacesOfCity -> {
            Log.d(TAG, "getPlacesInCity: done");
            List<Place> places = getPlacesOfCity.getPlaces();

//            for (Place place : places) {
//                if (place.getPlaceActivities() != null){
//                    if (!place.getPlaceActivities().isEmpty()){
//                        filteredPlaces.add(place);
//                    }
//                }
//            }

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

    public void getBadges() {
        getAllBadgesUseCase.execute(badgeResponse -> {
            badgesMutableLiveData.setValue(badgeResponse.getBadges());
        }, throwable -> Log.e(TAG, "error retrieving badges: " + throwable.getMessage()));

        getBadgesOfUserUseCase.execute(badges -> {
            badgesMutableLiveData.setValue(badges);
        }, throwable -> {
            Log.e(TAG, "error getting user badges: " + throwable.getMessage());
        });
    }

}