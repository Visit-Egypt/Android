package com.visitegypt.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.visitegypt.domain.model.Place;

import java.util.List;

@Dao
public interface PlaceDao {
    // TODO https://developer.android.com/training/data-storage/room

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Place place);

    @Query("SELECT * FROM Place")
    List<Place> getALLPlaces();

    @Query("SELECT * FROM Place WHERE title=(:title)")
    Place getPlaceByTitle(String title);
}
