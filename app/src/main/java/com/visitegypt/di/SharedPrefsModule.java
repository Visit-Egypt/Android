package com.visitegypt.di;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class SharedPrefsModule {
    private static final String TAG = "SharedPreferences";
    @Singleton
    @Provides
    public SharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        Log.d(TAG, "provideSharedPreferences: Hello from shared pref " );
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
