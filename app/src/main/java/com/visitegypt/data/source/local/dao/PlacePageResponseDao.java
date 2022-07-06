package com.visitegypt.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.response.PlacePageResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlacePageResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(PlacePageResponse placePageResponse);

    @Query("SELECT * FROM place_page_response WHERE currentPage = (:currentPage)")
    Single<PlacePageResponse> getALLCachedPlaces(int currentPage);
    @Query("SELECT * FROM place_page_response WHERE currentPage = 1")
    Single<PlacePageResponse> getFavPlaces();
    @Query("SELECT COUNT(*) FROM place_page_response")
    Single<Integer> getCountOfPlaces();


}
