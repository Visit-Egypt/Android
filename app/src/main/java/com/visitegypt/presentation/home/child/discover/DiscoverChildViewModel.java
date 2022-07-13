package com.visitegypt.presentation.home.child.discover;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.RecommendationPlaces;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.usecase.CachePlacesUseCase;
import com.visitegypt.domain.usecase.GetCachedPlacesCount;
import com.visitegypt.domain.usecase.GetCachedPlacesUseCase;
import com.visitegypt.domain.usecase.GetPlacesUseCase;
import com.visitegypt.domain.usecase.GetRecommendedPlacesUseCase;
import com.visitegypt.utils.error.Error;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DiscoverChildViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final String TAG = "Home View Model";
    MutableLiveData<List<Place>> placesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<RecommendationPlaces> recommendationPlacesMutableLiveData = new MutableLiveData<>();
    private GetPlacesUseCase getPlacesUseCase;
    private CachePlacesUseCase cachePlacesUseCase;
    private GetCachedPlacesUseCase getCachedPlacesUseCase;
    private GetCachedPlacesCount getCachedPlacesCount;
    private GetRecommendedPlacesUseCase getRecommendedPlacesUseCase;

    /***********************************************************/

    @Inject
    public DiscoverChildViewModel(GetPlacesUseCase getPlacesUseCase,
                                  CachePlacesUseCase cachePlacesUseCase,
                                  GetCachedPlacesCount getCachedPlacesCount,
                                  GetRecommendedPlacesUseCase getRecommendedPlacesUseCase,
                                  GetCachedPlacesUseCase getCachedPlacesUseCase) {
        this.getPlacesUseCase = getPlacesUseCase;
        this.cachePlacesUseCase = cachePlacesUseCase;
        this.getCachedPlacesUseCase = getCachedPlacesUseCase;
        this.getCachedPlacesCount = getCachedPlacesCount;
        this.getRecommendedPlacesUseCase = getRecommendedPlacesUseCase;
    }

    public void getAllPlaces() {
        getPlacesUseCase.execute(placePageResponse -> {
            placesMutableLiveData.setValue(placePageResponse.getPlaces());
            cachePlaces(placePageResponse);
        }, throwable -> {
            Log.e(TAG, "places retrieve error: " + throwable.getMessage());
            Error error = new Error();
            String errorMsg = error.errorType(throwable);
            Log.d(TAG, "error is:" + errorMsg);
        });
    }

    public void cachePlaces(PlacePageResponse placePageResponse) {
        cachePlacesUseCase.setPlaces(placePageResponse);
        cachePlacesUseCase.execute(() -> {

        }, throwable -> {
            Log.e(TAG, "places cache error: " + throwable.getMessage());
            Error error = new Error();
            String errorMsg = error.errorType(throwable);
            Log.d(TAG, "error is:" + errorMsg);
        });
    }

    public void getCachedPlaces() {
        getCachedPlacesCount.execute(count -> {
            if (count != 0) {
                getCachedPlacesUseCase.execute(places -> {
                    placesMutableLiveData.setValue(places.getPlaces());
                }, throwable -> {
                    Log.e(TAG, "places retrieve error: " + throwable.getMessage());
                    Error error = new Error();
                    String errorMsg = error.errorType(throwable);
                    Log.d(TAG, "error is:" + errorMsg);
                });
            } else {
                getAllPlaces();
            }


        }, throwable -> {
            Log.e(TAG, "places retrieve error: " + throwable.getMessage());
            Error error = new Error();
            String errorMsg = error.errorType(throwable);
            Log.d(TAG, "error is:" + errorMsg);
        });
    }

    public void getRecommendedPlaces() {
        getRecommendedPlacesUseCase.execute(recommendationPlaces -> {
            recommendationPlacesMutableLiveData.setValue(recommendationPlaces);

        }, throwable -> {
            Log.e(TAG, "places retrieve error: " + throwable.getMessage());
            Error error = new Error();
            String errorMsg = error.errorType(throwable);
            Log.d(TAG, "error is:" + errorMsg);
        });

    }
}