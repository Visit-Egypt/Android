package com.visitegypt.di;

import static com.visitegypt.utils.Constants.BASE_URL;

import com.visitegypt.data.repository.PlaceRepositoryImp;
import com.visitegypt.data.repository.UserRepositoryImp;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.repository.UserRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    private RetrofitService retrofitService;

    public NetworkModule() {

    }

    @Provides
    @Singleton
    public RetrofitService getRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory, RxJava3CallAdapterFactory rxJava3CallAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava3CallAdapterFactory)
                .build();
    }

    @Provides
    @Singleton
    public GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    public RxJava3CallAdapterFactory provideRxJava3CallAdapterFactory() {
        return RxJava3CallAdapterFactory.create();
    }

    @Provides
    @Singleton
    public PlaceRepository providePlaceRepository(RetrofitService retrofitService) {
        return new PlaceRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(RetrofitService retrofitService) {
        return new UserRepositoryImp(retrofitService);
    }

    public Single<User> loginUser(User user) {
        return retrofitService.loginUser(user);
    }

    public Single<User> registerUser(User user) {
        return retrofitService.registerUser(user);
    }
}