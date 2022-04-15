package com.visitegypt.di;

import android.app.Application;

import androidx.room.Room;

import com.visitegypt.data.source.local.AppDatabase;
import com.visitegypt.data.source.local.dao.PlaceDao;

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
        ).allowMainThreadQueries().build();
    }

    @Provides
    public PlaceDao providePlacesDao(AppDatabase appDatabase) {
        return appDatabase.userDao();
    }
}
