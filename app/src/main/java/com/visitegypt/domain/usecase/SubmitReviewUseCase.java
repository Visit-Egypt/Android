package com.visitegypt.domain.usecase;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;
import android.util.Log;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class SubmitReviewUseCase extends SingleUseCase<String> {

    private static final String TAG = "submit review usecase";

    private PlaceRepository placeRepository;
    private String placeId;
    private Review review;

    @Inject
    public SubmitReviewUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    protected Single<String> buildSingleUseCase() {
        return placeRepository.submitReview(placeId, review);
    }
}
