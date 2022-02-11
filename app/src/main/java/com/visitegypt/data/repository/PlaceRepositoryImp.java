package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleSource;


public class PlaceRepositoryImp implements PlaceRepository {
    private static final String TAG = "Place Repository Impl";
    private RetrofitService retrofitService;

    public PlaceRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<PlacePageResponse> getAllPlaces() {
        return retrofitService.getAllPlaces();
    }

    @Override
    public Single<Place> getPlaceById(String placeId) {
        return retrofitService.getPlaceById(placeId);
    }

    @Override
    public Single<List<Review>> submitReview(String placeId, Review review) {
        return retrofitService.submitReview(placeId, review);
    }
}
