package com.visitegypt.presentation.detail;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.usecase.GetItemsUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.GetUserPlaceActivityUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DetailViewModel extends ViewModel {
    private static final String TAG = "Detail View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<PlaceActivity>> userPlaceActivitiesMutableLiveData = new MutableLiveData<>();

    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private GetItemsUseCase getItemsUseCase;
    @Inject
    SharedPreferences sharedPreferences;
    private GetUserPlaceActivityUseCase getUserPlaceActivityUseCase;

    @Inject
    public DetailViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase,
                           GetItemsUseCase getItemsUseCase,
                           GetUserPlaceActivityUseCase getUserPlaceActivityUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemsUseCase = getItemsUseCase;
        this.getUserPlaceActivityUseCase = getUserPlaceActivityUseCase;
    }

    //This function is used to get the details of place by passing place ID
    public void getPlace(String placeId) {
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(
                place -> {
                    placesMutableLiveData.setValue(place);
                }
                , throwable -> {
                    Log.e(TAG, "place detail retrieve failed", throwable);
                }
        );
    }

    //This function is used to get place items
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

    public void getUserPlaceActivity() {
        String userId = sharedPreferences.getString(Constants.SHARED_PREF_USER_ID, "");
        if (userId.isEmpty()) {
            Log.e(TAG, "getUserPlaceActivity: user not found");
        }
        getUserPlaceActivityUseCase.setUserId(userId);
        getUserPlaceActivityUseCase.execute(placeActivities -> {
            Log.d(TAG, "getUserPlaceActivity: setting placeActivities to live data");
            userPlaceActivitiesMutableLiveData.setValue(placeActivities);
        }, throwable -> {
            userPlaceActivitiesMutableLiveData.setValue(null);
            Log.e(TAG, "getUserPlaceActivity: failed to get place activities for the user: " + throwable.getMessage());
        });
    }

}