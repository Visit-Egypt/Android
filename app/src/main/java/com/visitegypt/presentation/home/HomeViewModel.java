package com.visitegypt.presentation.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.data.repository.PlaceRepositoryImp;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetPlacesUseCase;

import java.util.List;

public class HomeViewModel extends ViewModel {
    private static final String TAG = "Home View Model";
    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    private PlaceRepositoryImp placeRepository;
    private GetPlacesUseCase getPlacesUseCase = new GetPlacesUseCase(placeRepository);

    public void getAllPlaces() {
        List<Place> places;
        getPlacesUseCase.execute(
                o -> {
                    placesMutableLiveData.setValue((List<Place>) o);
                    Log.d(TAG, "Getting places success");
                },
                o -> {
                    placesMutableLiveData.setValue(null);
                    Log.e(TAG, "Failed to get places");
                });
    }
}
