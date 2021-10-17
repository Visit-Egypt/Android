package com.visitegypt.presentation.home;

import androidx.lifecycle.MutableLiveData;

import com.visitegypt.data.repository.PlaceRepositoryImp;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetPlacesUseCase;

public class HomeViewModel {
    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    private PlaceRepositoryImp placeRepository;

    public void getPlaces(GetPlacesUseCase getPlacesUseCase) {
        //placesMutableLiveData.setValue();


    }

}
