package com.visitegypt.presentation.place;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetAllBadgesUseCase;
import com.visitegypt.domain.usecase.GetBadgesOfUserUseCase;
import com.visitegypt.domain.usecase.GetPlacesOfCityUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlacesViewModel extends ViewModel {

    private static final String TAG = "Places View Model";

    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    MutableLiveData<List<Badge>> badgesMutableLiveData = new MutableLiveData<>();

    private GetPlacesOfCityUseCase getPlacesOfCityUseCase;
    private GetAllBadgesUseCase getAllBadgesUseCase;
    private GetBadgesOfUserUseCase getBadgesOfUserUseCase;

    @Inject
    public PlacesViewModel(GetPlacesOfCityUseCase getPlacesOfCityUseCase, GetBadgesOfUserUseCase getBadgesOfUserUseCase, GetAllBadgesUseCase getAllBadgesUseCase) {
        this.getPlacesOfCityUseCase = getPlacesOfCityUseCase;
        this.getAllBadgesUseCase = getAllBadgesUseCase;
        this.getBadgesOfUserUseCase = getBadgesOfUserUseCase;

    }

    public void getPlacesInCity(String cityName) {
        getPlacesOfCityUseCase.setCityName(cityName);
        getPlacesOfCityUseCase.execute(getPlacesOfCity -> {
            Log.d(TAG, "getPlacesInCity: done");
            placesMutableLiveData.setValue(getPlacesOfCity.getPlaces());
        }, throwable -> Log.e(TAG, "places retrieve error: " + throwable.getMessage()));

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