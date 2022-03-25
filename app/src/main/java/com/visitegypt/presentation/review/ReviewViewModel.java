package com.visitegypt.presentation.review;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.SubmitReviewUseCase;
import com.visitegypt.domain.usecase.UpdateUserPlaceActivityUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.HttpException;
import retrofit2.Response;

@HiltViewModel
public class ReviewViewModel extends ViewModel {

    private static final String TAG = "Review View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Review>> reviewMutableLiveData = new MutableLiveData<>();

    private SubmitReviewUseCase submitReviewUseCase;
    private GetPlaceDetailUseCase getPlaceDetailUseCase;
    private UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase;
    public MutableLiveData<Integer> mutableLiveDataResponseCode = new MutableLiveData<>();
    private PlaceActivity placeActivity;

    @Inject
    public ReviewViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase,
                           SubmitReviewUseCase submitReviewUseCase,
                           UpdateUserPlaceActivityUseCase updateUserPlaceActivityUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
        this.updateUserPlaceActivityUseCase = updateUserPlaceActivityUseCase;
    }


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

    public void setPlaceActivity(PlaceActivity placeActivity) {
        this.placeActivity = placeActivity;
    }

    public void submitReview(String placeId, Review review) {
        if (placeActivity == null) {
            Log.w(TAG, "submitReview: consider calling setPlaceActivity to update user progress");
        }
        submitReviewUseCase.setPlaceId(placeId);
        submitReviewUseCase.setReview(review);
        submitReviewUseCase.execute(reviews -> {
            Log.d(TAG, "submitReview: Review is submitted");
            reviewMutableLiveData.setValue(reviews);
            if (placeActivity != null) {
                if (placeActivity.getProgress() != placeActivity.getMaxProgress()) {
                    placeActivity.setProgress(placeActivity.getProgress() + 1);
                }
                updateUserPlaceActivityUseCase.setPlaceActivity(placeActivity);
                updateUserPlaceActivityUseCase.execute(placeActivities -> {

                    Log.d(TAG, "submitReview: updated user progress");
                }, throwable -> {
                    Log.d(TAG, "submitReview: "+ throwable.getMessage());
                });
            }
            mutableLiveDataResponseCode.setValue(200);
        }, throwable -> {
            try {
                Log.e(TAG, "getUser: " + throwable.getMessage());
                Response<?> response = ((HttpException) throwable).response();
                mutableLiveDataResponseCode.setValue(response.code());

            } catch (Exception e) {
                Log.e(TAG, "accept catch: " + e.getMessage());
            }
        });

    }

}
