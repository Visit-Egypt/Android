package com.visitegypt.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.domain.model.Place;

@Database(entities = {Place.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    // REF https://developer.android.com/training/data-storage/room
    public static final String DATABASE_NAME = "PlacesDB.db";

    public abstract PlaceDao userDao();
}
