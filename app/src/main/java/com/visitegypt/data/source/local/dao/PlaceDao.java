package com.visitegypt.data.source.local.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.response.PlacePageResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlaceDao {
    // TODO https://developer.android.com/training/data-storage/room

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    Completable insertAll(List<Place> places);

    @Query("SELECT * FROM place_table")
    Single<List<Place>> getALLCachedPlaces();

    @Query("SELECT * FROM place_table WHERE title=(:title)")
    Single<Place> getPlaceByTitle(String title);

    @Query("SELECT * FROM place_table WHERE id = (:placeId)")
    Single<Place> getPlaceById(String placeId);

    @Query("SELECT * FROM place_table LIMIT (:numberOfPlaces)")
    Single<List<Place>> getCachedPlaces(int numberOfPlaces);

    @Query("DELETE FROM place_table")
    @Transaction
    Completable clearAll();

    @Query("SELECT COUNT(*) FROM place_table")
    public abstract int getCount();

}
