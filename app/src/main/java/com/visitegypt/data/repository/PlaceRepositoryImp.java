package com.visitegypt.data.repository;

import com.visitegypt.data.source.local.dao.PlacePageResponseDao;
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
    private PlacePageResponseDao placePageResponseDao;

    public PlaceRepositoryImp(RetrofitService retrofitService, PlacePageResponseDao placeDao) {
        this.retrofitService = retrofitService;
        this.placePageResponseDao = placeDao;
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
        String cityQuery = "{\"translations.en.city\":\"" + cityName + "\"}";
        return retrofitService.getPlacesOfCity(cityQuery);
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
    public Completable cachingPlaces(PlacePageResponse placePageResponse) {
        return placePageResponseDao.insertAll(placePageResponse);
    }

    @Override
    public Single<PlacePageResponse> getALLCachedPlaces() {
        return placePageResponseDao.getFavPlaces();
    }

    @Override
    public Single<Integer> getCountOfPlaces() {
        return placePageResponseDao.getCountOfPlaces();
    }

}
