package com.visitegypt.di;

import static com.visitegypt.utils.Constants.WEB_SOCKET_URL;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@InstallIn(SingletonComponent.class)
@Module
public class WebSocketModule {
    public WebSocketModule() {
    }

    @Provides
    @Singleton
    public Request provideRequest() {
        return new Request.Builder().url(WEB_SOCKET_URL).build();
    }

//    @Provides
//    @Singleton
//    public OkHttpClient provideOkHttpClient() {
//        return new OkHttpClient();
//    }


}
