package com.visitegypt.di;

import android.app.Application;

import androidx.room.Room;

import com.visitegypt.data.source.local.AppDatabase;
import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.data.source.local.dao.TagDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(Application application) {
        return Room.databaseBuilder(
                        application,
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    public static TagDao provideTagDao(AppDatabase appDatabase) {
        return appDatabase.tagDao();
    }

    @Provides
    @Singleton
    public static PlaceDao providePlaceDao(AppDatabase appDatabase) {
        return appDatabase.placeDao();
    }

}
