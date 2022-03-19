package com.visitegypt.presentation.place;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetPlacesOfCityUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlacesViewModel extends ViewModel {

    private static final String TAG = "Places View Model";

    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    private GetPlacesOfCityUseCase getPlacesOfCityUseCase;

    @Inject
    public PlacesViewModel(GetPlacesOfCityUseCase getPlacesOfCityUseCase) {
        this.getPlacesOfCityUseCase = getPlacesOfCityUseCase;
    }

    public void getPlacesInCity(String cityName) {
        getPlacesOfCityUseCase.setCityName(cityName);
        getPlacesOfCityUseCase.execute(getPlacesOfCity -> {
            Log.d(TAG, "getPlacesInCity: done");
            placesMutableLiveData.setValue(getPlacesOfCity.getPlaces());
        }, throwable -> Log.e(TAG, "places retrieve error: " + throwable.getMessage()));

    }
}