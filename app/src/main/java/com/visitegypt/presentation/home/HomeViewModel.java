package com.visitegypt.presentation.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetPlacesUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private static final String TAG = "Home View Model";
    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    private GetPlacesUseCase getPlacesUseCase;

    @Inject
    public HomeViewModel(GetPlacesUseCase getPlacesUseCase) {
        this.getPlacesUseCase = getPlacesUseCase;
    }

    public void getAllPlaces() {
        getPlacesUseCase.execute(placePageResponse -> {
            Log.d(TAG, "places retrieved");
            placesMutableLiveData.setValue(placePageResponse.getPlaces());
        }, throwable -> Log.e(TAG, "places retrieve error: " + throwable.getMessage()));
    }
}
