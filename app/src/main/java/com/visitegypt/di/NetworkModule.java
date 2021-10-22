package com.visitegypt.di;

import static com.visitegypt.utils.Constants.BASE_URL;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlacePageResponse;
import com.visitegypt.domain.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkModule {
    // TODO https://github.com/ZahraHeydari/Android-Clean-Architecture-MVVM-Hilt-RX/blob/master/app/src/main/java/com/android/artgallery/di/NetworkModule.kt
    private static NetworkModule INSTANCE;

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }

    private RetrofitService retrofitService;

    public NetworkModule() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        retrofitService = retrofit.create(RetrofitService.class);
    }

    public static NetworkModule getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkModule();
        }
        return INSTANCE;
    }

    public Single<User> loginUser(User user) {
        return retrofitService.loginUser(user);
    }
    public Single<User> registerUser(User user) {
        return retrofitService.registerUser(user);
    }

    public Single<PlacePageResponse> getAllPlaces() {
        return retrofitService.getAllPlaces();
    }


    public Call<Place> getPlace(String id) {
        return retrofitService.getPlace(id);
    }
}
