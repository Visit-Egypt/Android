package com.visitegypt.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.data.source.local.dao.PlacePageResponseDao;
import com.visitegypt.data.source.local.dao.TagDao;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.response.PlacePageResponse;

@Database(entities = {Tag.class , Place.class, PlacePageResponse.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "Visit Egypt";

    public abstract TagDao tagDao();
    public abstract PlaceDao placeDao();
    public abstract PlacePageResponseDao placePageResponseDao();
}
