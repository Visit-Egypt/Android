package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;


public class PlaceRepositoryImp implements PlaceRepository {
    private static final String TAG = "Place Repository Impl";
    private RetrofitService retrofitService;

    public PlaceRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Inject
    public Single<PlacePageResponse> getAllPlaces() {
        return retrofitService.getAllPlaces();
    }

    @Override
    @Inject
    public Single<Place> getPlaceById(String placeId) {
        // return retrofitService.getPlaceById(placeId);
        return null;
    }
}
