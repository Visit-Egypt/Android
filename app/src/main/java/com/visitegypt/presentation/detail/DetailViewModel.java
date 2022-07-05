package com.visitegypt.presentation.detail;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.visitegypt.domain.model.Item;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.usecase.GetItemPagingUseCase;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.GetUserPlaceActivityUseCase;
import com.visitegypt.utils.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class DetailViewModel extends ViewModel {
    private static final String TAG = "Detail View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Item>> itemMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<PlaceActivity>> userPlaceActivitiesMutableLiveData = new MutableLiveData<>();
    Flowable<PagingData<Item>> flowable;
    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private GetItemPagingUseCase getItemPagingUseCase;
    private GetUserPlaceActivityUseCase getUserPlaceActivityUseCase;
    private SharedPreferences sharedPreferences;

    @Inject
    public DetailViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase,
                           GetItemPagingUseCase GetItemPagingUseCase,
                           GetUserPlaceActivityUseCase getUserPlaceActivityUseCase,
                           SharedPreferences sharedPreferences
    ) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.getItemPagingUseCase = GetItemPagingUseCase;
        this.getUserPlaceActivityUseCase = getUserPlaceActivityUseCase;
        this.sharedPreferences = sharedPreferences;
    }

    //This function is used to get the details of place by passing place ID
    public void getPlace(String placeId) {
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(
                place -> {
                    placesMutableLiveData.setValue(place);
                }, throwable -> {
                    Log.e(TAG, "place detail retrieve failed", throwable);
                }
        );
    }

    //This function is used to get place items
    public void getItemsByPlaceId(String placeId) {
        getItemPagingUseCase.setPlaceId(placeId);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<Integer, Item> pager = new Pager(
                // Create new paging config
                new PagingConfig(10, //  Count of items in one page
                        10, //  Number of items to prefetch
                        false, // Enable placeholders for data which is not yet loaded
                        10, // initialLoadSize - Count of items to be loaded initially
                        10 * 499),// maxSize - Count of total items to be shown in recyclerview
                () -> getItemPagingUseCase); // set paging source
        flowable = PagingRx.getFlowable(pager);
        PagingRx.cachedIn(flowable, viewModelScope);
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