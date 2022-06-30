package com.visitegypt.data.repository;

import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.repository.PlaceRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;


public class PlaceRepositoryImp implements PlaceRepository {
    private static final String TAG = "Place Repository Impl";
    private RetrofitService retrofitService;
    private PlaceDao placeDao;

    public PlaceRepositoryImp(RetrofitService retrofitService, PlaceDao placeDao) {
        this.retrofitService = retrofitService;
        this.placeDao = placeDao;
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

    @Override
    public Single<PlacePageResponse> getPlacesOfCity(String cityName) {
        return retrofitService.getPlacesOfCity(cityName);
    }

    @Override
    public Single<List<String>> getAllCities() {
        return retrofitService.getAllCities();
    }

    @Override
    public Single<List<String>> getAllAvailableCities() {
        return retrofitService.getAllAvailableCities();
    }

    @Override
    public Single<List<Place>> getPlacesByPlaceActivityIds(List<String> placeActivitiesId) {
        return retrofitService.getPlacesByPlaceActivitiesId(placeActivitiesId);
    }

    @Override
    public Single<PlacePageResponse> getPlacesPaging(int pageNumber) {
        return retrofitService.getPlacesPaging(pageNumber);
    }

    @Override
    public Completable cachingPlaces(List<Place> places) {
        return placeDao.insert(places);
    }

    @Override
    public Single<List<Place>> getALLCachedPlaces() {
        return placeDao.getALLCachedPlaces();
    }

    @Override
    public Single<List<Place>> getCachedPlaces(int numberOfPlaces) {
        return placeDao.getCachedPlaces(numberOfPlaces);
    }

}
