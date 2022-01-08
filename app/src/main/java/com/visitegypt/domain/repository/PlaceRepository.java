package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.PlacePageResponse;

import io.reactivex.rxjava3.core.Single;

public interface PlaceRepository {
    Single<PlacePageResponse> getAllPlaces();

    Single<Place> getPlaceById(String placeId);

    Single<Void> submitReview(String placeId, Review review);
}
