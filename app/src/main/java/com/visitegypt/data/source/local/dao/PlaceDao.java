package com.visitegypt.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.visitegypt.domain.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlaceDao {
    // TODO https://developer.android.com/training/data-storage/room

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<Place> places);

    @Query("SELECT * FROM place_table")
    Single<List<Place>> getALLCachedPlaces();

    @Query("SELECT * FROM place_table WHERE title=(:title)")
    Single<Place> getPlaceByTitle(String title);

    @Query("SELECT * FROM place_table WHERE id = (:placeId)")
    Single<Place> getPlaceById(String placeId);

    @Query("SELECT * FROM place_table LIMIT (:numberOfPlaces)")
    Single<List<Place>> getCachedPlaces(int numberOfPlaces);

}
