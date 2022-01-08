package com.visitegypt.presentation.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.usecase.GetPlacesUseCase;
import com.visitegypt.domain.usecase.UploadUserPhotoUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private static final String TAG = "Home View Model";
    MutableLiveData placesMutableLiveData = new MutableLiveData<Place>();
    private GetPlacesUseCase getPlacesUseCase;

    /*
    * For Testing Purposes Only
    * */

    MutableLiveData<UploadResponse> uploadResponseMutableLiveData = new MutableLiveData<UploadResponse>();
    private UploadUserPhotoUseCase uploadUserPhotoUseCase;

    @Inject
    public HomeViewModel(GetPlacesUseCase getPlacesUseCase, UploadUserPhotoUseCase uploadUserPhotoUseCase) {
        this.getPlacesUseCase = getPlacesUseCase;
        this.uploadUserPhotoUseCase = uploadUserPhotoUseCase;
    }

    public void getAllPlaces() {
        getPlacesUseCase.execute(placePageResponse -> {
            Log.d(TAG, "places retrieved");
            placesMutableLiveData.setValue(placePageResponse.getPlaces());
        }, throwable -> Log.e(TAG, "places retrieve error: " + throwable.getMessage()));

        uploadUserPhotoUseCase.setContentType("Image/png");
        uploadUserPhotoUseCase.execute(uploadResponse -> {
            uploadResponseMutableLiveData.setValue(uploadResponse);
        }, throwable -> Log.e(TAG, "Error in retrieving upload url: " + throwable.getMessage()));
    }
}
