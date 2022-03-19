package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.PlacePageResponse;

import java.util.List;
import java.util.ListResourceBundle;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;

public interface PlaceRepository {
    Single<PlacePageResponse> getAllPlaces();

    Single<Place> getPlaceById(String placeId);

    Single<List<Review>> submitReview(String placeId, Review review);

    Single<PlacePageResponse> getPlacesOfCity(String cityName);
    Single<List<String>> getAllCities();
}
