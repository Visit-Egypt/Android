package com.visitegypt.presentation.review;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.usecase.GetPlaceDetailUseCase;
import com.visitegypt.domain.usecase.SubmitReviewUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import com.visitegypt.domain.model.Place;
@HiltViewModel
public class ReviewViewModel extends ViewModel {

    private static final String TAG = "Review View Model";

    MutableLiveData<Place> placesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Review>> reviewMutableLiveData = new MutableLiveData<>();

    private SubmitReviewUseCase submitReviewUseCase;
    private GetPlaceDetailUseCase getPlaceDetailUseCase;

    @Inject
    public ReviewViewModel(GetPlaceDetailUseCase getPlaceDetailUseCase,SubmitReviewUseCase submitReviewUseCase) {
        this.getPlaceDetailUseCase = getPlaceDetailUseCase;
        this.submitReviewUseCase = submitReviewUseCase;
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

    public void submitReview(String placeId, Review review) {
        submitReviewUseCase.setPlaceId(placeId);
        submitReviewUseCase.setReview(review);
        submitReviewUseCase.execute(reviews -> {
            Log.d(TAG, "submitReview: Review is submitted");
            reviewMutableLiveData.setValue(reviews);

        }, throwable -> {
            Log.d(TAG, "submitReview: " + throwable.getMessage());
        });

    }

}
