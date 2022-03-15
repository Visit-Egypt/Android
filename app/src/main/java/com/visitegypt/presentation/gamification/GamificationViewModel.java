package com.visitegypt.presentation.gamification;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GamificationViewModel extends ViewModel {
    SharedPreferences sharedPreferences;
    GetPlaceDetailUseCase getPlaceDetailUseCase;
    MutableLiveData<Place> placeMutableLiveData = new MutableLiveData<>();
    private String placeId;

    @Inject
    public GamificationViewModel(SharedPreferences sharedPreferences, GetPlaceDetailUseCase getPlaceDetailUseCase) {
        this.sharedPreferences = sharedPreferences;
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
    }


    public void getPlaceDetail() throws Exception {
        if (placeId.isEmpty()) throw new Exception("you must use setPlaceId first");
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(place -> placeMutableLiveData.setValue(place), throwable -> {
        });
    }


    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
