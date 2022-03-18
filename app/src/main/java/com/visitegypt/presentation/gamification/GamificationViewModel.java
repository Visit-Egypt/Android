package com.visitegypt.presentation.gamification;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class GamificationViewModel extends ViewModel {
    private static final String TAG = "Gamification ViewModel";
    SharedPreferences sharedPreferences;
    GetPlaceDetailUseCase getPlaceDetailUseCase;
    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    private String placeId;
    private GetItemsUseCase getItemsUseCase;

    @Inject
    public GamificationViewModel(SharedPreferences sharedPreferences, GetPlaceDetailUseCase getPlaceDetailUseCase, GetItemsUseCase getItemsUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.sharedPreferences = sharedPreferences;
    }


    public void getPlaceDetail() throws Exception {
        if (placeId.isEmpty()) throw new Exception("you must use setPlaceId first");
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(place -> placesMutableLiveData.setValue(place), throwable -> {
        });
    }

//    public void getPlace(String placeId) {
//        getPlaceDetailUseCase.setPlaceId(placeId);
//        getPlaceDetailUseCase.execute(
//                place -> {
//                    placesMutableLiveData.setValue(place);
//                }
//                , throwable -> {
//                    Log.e(TAG, "place detail retrieve failed", throwable);
//                }
//        );
//    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void getItemsByPlaceId(String placeId) {
        getItemsUseCase.setPlaceId(placeId);
        getItemsUseCase.execute(itemPageResponse -> {
                    itemMutableLiveData.setValue(itemPageResponse.getItems());
                }
                ,
                throwable -> {
                    Log.e(TAG, "error retrieving items: " + throwable.getMessage());
                });
    }
}
