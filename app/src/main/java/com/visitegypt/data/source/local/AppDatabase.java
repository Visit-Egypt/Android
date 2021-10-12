package com.visitegypt.data.source.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.domain.model.Converters.ImageUrlsConverter;
import com.visitegypt.domain.model.Place;

@Database(entities = {Place.class}, version = 1)
@TypeConverters({ImageUrlsConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    // TODO https://developer.android.com/training/data-storage/room
    public abstract PlaceDao userDao();
}
