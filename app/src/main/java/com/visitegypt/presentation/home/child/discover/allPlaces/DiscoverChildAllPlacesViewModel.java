package com.visitegypt.presentation.home.child.discover.allPlaces;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.usecase.GetAllPlacesPagingUseCase;
import com.visitegypt.domain.usecase.GetAllPlacesPagingWithRoom;
import com.visitegypt.domain.usecase.GetPlacesUseCase;

import javax.inject.Inject;
import com.visitegypt.utils.error.Error;
import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

@HiltViewModel
public class DiscoverChildAllPlacesViewModel extends ViewModel {

    // TODO: Implement the ViewModel
    private static final String TAG = "Home View Model";
    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    private GetPlacesUseCase getPlacesUseCase;
    private GetAllPlacesPagingUseCase getAllPlacesPagingUseCase;
    private GetAllPlacesPagingWithRoom getAllPlacesPagingWithRoom;
    Flowable<PagingData<Place>> flowable;


    @Inject
    public DiscoverChildAllPlacesViewModel(GetPlacesUseCase getPlacesUseCase,
                                           GetAllPlacesPagingUseCase getAllPlacesPagingUseCase,
                                             GetAllPlacesPagingWithRoom getAllPlacesPagingWithRoom

    ) {

        this.getPlacesUseCase = getPlacesUseCase;
        this.getAllPlacesPagingUseCase = getAllPlacesPagingUseCase;
        this.getAllPlacesPagingWithRoom = getAllPlacesPagingWithRoom;
    }

    public void getAllPlaces() {
        getPlacesUseCase.execute(placePageResponse -> {
            Log.d(TAG, "places retrieved");
            placesMutableLiveData.setValue(placePageResponse.getPlaces());
        }, throwable -> {
            Log.e(TAG, "places retrieve error: " + throwable.getMessage());
            Error error = new Error();
            String errorMsg = error.errorType(throwable);
            Log.d(TAG, "error is:" + errorMsg);
        });

    }

    public void init() {
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        @SuppressLint("UnsafeOptInUsageError") Pager<Integer, Place> pager = new Pager(
                // Create new paging config
                new PagingConfig(15, //  Count of items in one page
                        15, //  Number of items to prefetch
                        false, // Enable placeholders for data which is not yet loaded
                        15, // initialLoadSize - Count of items to be loaded initially
                        15 * 499),// maxSize - Count of total items to be shown in recyclerview
                null,
                () -> getAllPlacesPagingWithRoom); // set paging source
        flowable = PagingRx.getFlowable(pager);
        PagingRx.cachedIn(flowable, viewModelScope);
    }

    public void getAllCachedPlaces() {
        getAllPlacesPagingUseCase.getAllCachedPlaces().subscribe((places, throwable) -> {

        });
    }

}