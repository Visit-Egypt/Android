package com.visitegypt.presentation.review;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.SubmitReviewUseCase;
import com.visitegypt.domain.usecase.UpdateReviewActivityUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.HttpException;
import retrofit2.Response;

@HiltViewModel
public class ReviewViewModel extends ViewModel {

    private static final String TAG = "Review View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Review>> reviewMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> mutableLiveDataResponseCode = new MutableLiveData<>();
    public MutableLiveData<Boolean> activityUpdatedMutableLiveData = new MutableLiveData<>();

    private SubmitReviewUseCase submitReviewUseCase;
    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private UpdateReviewActivityUseCase updateReviewActivityUseCase;

    @Inject
    public ReviewViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase,
                           SubmitReviewUseCase submitReviewUseCase,
                           UpdateReviewActivityUseCase updateReviewActivityUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
        this.updateReviewActivityUseCase = updateReviewActivityUseCase;
    }


    public void getPlace(String placeId) {
        getPlaceDetailUseCase.setPlaceId(placeId);
        getPlaceDetailUseCase.execute(
                place -> {
                    placesMutableLiveData.setValue(place);
                }
                , throwable -> {
                    Log.e(TAG, "place detail retrieve failed: " + throwable.getMessage());
                }
        );
    }


    public void submitReview(String placeId, Review review) {
        Log.d(TAG, "submitReview: init");
        submitReviewUseCase.setPlaceId(placeId);
        submitReviewUseCase.setReview(review);
        submitReviewUseCase.execute(reviews -> {
            Log.d(TAG, "submitReview: Review is submitted");
            reviewMutableLiveData.setValue(reviews);
            mutableLiveDataResponseCode.setValue(200);
            updateReviewActivityUseCase.setPlaceId(placeId);
            updateReviewActivityUseCase.execute(unused -> {
                activityUpdatedMutableLiveData.setValue(true);
            }, throwable -> {
                Log.e(TAG, "submitReview: " + throwable.getMessage());
                if (throwable.getMessage() == null) {
                    activityUpdatedMutableLiveData.setValue(true);
                } else {
                    activityUpdatedMutableLiveData.setValue(false);
                }
            });
        }, throwable -> {
            try {
                Log.e(TAG, "submitReview: " + throwable.getMessage());
                Response<?> response = ((HttpException) throwable).response();
                mutableLiveDataResponseCode.setValue(response.code());
            } catch (Exception e) {
                Log.e(TAG, "accept catch: " + e.getMessage());
            }
        });

    }

}
