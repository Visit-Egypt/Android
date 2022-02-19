package com.visitegypt.di;

import static com.visitegypt.utils.Constants.BASE_URL;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_REFRESH_TOKEN;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;

import com.visitegypt.data.repository.ChatbotRepositoryImp;
import com.visitegypt.data.repository.ItemRepositoryImp;
import com.visitegypt.data.repository.PlaceRepositoryImp;
import com.visitegypt.data.repository.PostRepositoryImp;
import com.visitegypt.data.repository.UserRepositoryImp;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.ChatbotRepository;
import com.visitegypt.domain.repository.ItemRepository;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.utils.JWT;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    public NetworkModule() {

    }

    @Provides
    @Singleton
    public OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder();
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor(OkHttpClient.Builder httpClient) {
        return new HttpLoggingInterceptor();
    }


    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder httpClient, HttpLoggingInterceptor logging, SharedPreferences sharedPreferences, @Named("RefreshToken") UserRepository userRepository) {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                String token = sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
                String refreshToken = sharedPreferences.getString(SHARED_PREF_USER_REFRESH_TOKEN, "");
                Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                Response response = chain.proceed(request);
                if (response.code() == 403 || response.code() == 401) {
                    response.close();
                    getNewToken(userRepository, sharedPreferences);
                    token = sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");

                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(newRequest);


                }

                return response;
            }


        });


        return httpClient.build();
    }

    @Provides
    @Singleton
    @Named("Normal")
    public RetrofitService getRetrofitService(@Named("Normal") Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    @Singleton
    @Named("RefreshToken")
    public RetrofitService getRetrofitServiceٌRefreshToken(@Named("RefreshToken") Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    @Singleton
    @Named("Normal")
    public Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory, RxJava3CallAdapterFactory rxJava3CallAdapterFactory, OkHttpClient client) {

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava3CallAdapterFactory)
                .client(client)
                .build();
    }


    @Provides
    @Singleton
    @Named("RefreshToken")
    public Retrofit provideRetrofitRefreshToken(GsonConverterFactory gsonConverterFactory, RxJava3CallAdapterFactory rxJava3CallAdapterFactory) {

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
    public PlaceRepository providePlaceRepository(@Named("Normal") RetrofitService retrofitService) {
        return new PlaceRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    @Named("Normal")
    public UserRepository provideUserRepository(@Named("Normal") RetrofitService retrofitService) {
        return new UserRepositoryImp(retrofitService);
    }


    @Provides
    @Singleton
    @Named("RefreshToken")
    public UserRepository provideUserRepositoryRefreshToken(@Named("RefreshToken") RetrofitService retrofitService) {
        return new UserRepositoryImp(retrofitService);
    }


    @Provides
    @Singleton
    public ItemRepository provideItemRepository(@Named("Normal") RetrofitService retrofitService) {
        return new ItemRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    public PostsRepository providePostRepository(@Named("Normal") RetrofitService retrofitService) {
        return new PostRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    public ChatbotRepository provideChatbotRepository(@Named("Normal") RetrofitService retrofitService) {
        return new ChatbotRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    public JWT provideJwt(SharedPreferences sharedPreferences) {
        String token = sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
        String refreshToken = sharedPreferences.getString(SHARED_PREF_USER_REFRESH_TOKEN, "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new JWT(token, refreshToken);
        }
        return null;
    }


    public void getNewToken(@Named("RefreshToken") UserRepository userRepository, SharedPreferences sharedPreferences) {
        Log.d("TAG", "getNewToken: welcome ");
        String token = sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
        String refreshToken = sharedPreferences.getString(SHARED_PREF_USER_REFRESH_TOKEN, "");
        Single<User> myUser = userRepository.refreshUserToken(new Token(token, refreshToken))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        myUser.subscribe(user -> {
            Log.d("TAG", "getNewToken: HIIIII ");
            sharedPreferences.edit()
                    .putString(SHARED_PREF_USER_ACCESS_TOKEN, user.getAccessToken())
                    .putString(SHARED_PREF_USER_REFRESH_TOKEN, user.getRefreshToken())
                    .apply();
        }, throwable -> {
            Log.d("TAG", "getNewToken: error" + throwable.getMessage());
        });
    }


}
