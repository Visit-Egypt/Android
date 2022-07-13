package com.visitegypt.data.repository;

import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ID;

import android.content.SharedPreferences;

import com.visitegypt.data.source.local.dao.PlacePageResponseDao;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.RecommendationPlaces;
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
    private SharedPreferences sharedPreferences;

    public PlaceRepositoryImp(RetrofitService retrofitService,
                              SharedPreferences sharedPreferences,
                              PlacePageResponseDao placeDao) {
        this.retrofitService = retrofitService;
        this.placePageResponseDao = placeDao;
        this.sharedPreferences = sharedPreferences;
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
        String cityQuery = "{\"city\":\"" + cityName + "\"}";
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

    @Override
    public Single<RecommendationPlaces> getRecommendationPlaces() {
        return retrofitService.getRecommendationPlaces(sharedPreferences.getString(SHARED_PREF_USER_ID, ""));
    }

}
