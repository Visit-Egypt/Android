package com.visitegypt.presentation.detailplace;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.functions.Consumer;

@HiltViewModel
public class DetailViewModel extends ViewModel {
    private static final String TAG = "Detail View Model";
    MutableLiveData<Place> mutableLiveData = new MutableLiveData<>();
    private GetPlaceDetailUseCase getPlaceDetailUseCase;

    @Inject
    public DetailViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
    }

    public void getPlace(String placeId) {
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(new Consumer<Place>() {
            @Override
            public void accept(Place place) throws Throwable {
                Log.d(TAG, "Place details retrieved!");
                mutableLiveData.setValue(place);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {
                Log.e(TAG, "place detail retrieve failed", throwable);
            }
        });
    }
}
