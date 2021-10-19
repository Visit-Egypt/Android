package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.repository.PlaceRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class PlaceRepositoryImp implements PlaceRepository {
    private static final String TAG = "Place Repository Impl";
    private RetrofitService retrofitService;

    @Override
    public Single<List<Place>> getAllPlaces() {
        //return retrofitService.getPlaces();
        return  null;
    }

    @Override
    public Single<Place> getPlaceById(String placeId) {
       // return retrofitService.getPlaceById(placeId);
        return null;
    }
}
