package com.visitegypt.domain.usecase;

import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.usecase.base.SingleUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class SubmitReviewUseCase extends SingleUseCase<List<Review>> {

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
    protected Single<List<Review>> buildSingleUseCase() {
        return placeRepository.submitReview(placeId, review);
    }
}
