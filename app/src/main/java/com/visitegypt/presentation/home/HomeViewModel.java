package com.visitegypt.presentation.home;

import androidx.lifecycle.MutableLiveData;

import com.visitegypt.domain.model.Place;

import java.util.List;

public class HomeViewModel  {
    MutableLiveData placesMutableLiveData = new MutableLiveData<List<Place>>();

    public void getPlaces(){
        //placesMutableLiveData.setValue();
    }

}
