package com.visitegypt.di;

import static com.visitegypt.utils.Constants.BASE_URL;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.visitegypt.data.repository.ChatbotRepositoryImp;
import com.visitegypt.data.repository.ItemRepositoryImp;
import com.visitegypt.data.repository.PlaceRepositoryImp;
import com.visitegypt.data.repository.PostRepositoryImp;
import com.visitegypt.data.repository.UserRepositoryImp;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.repository.ChatbotRepository;
import com.visitegypt.domain.repository.ItemRepository;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.repository.UserRepository;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
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
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder httpClient, HttpLoggingInterceptor logging, SharedPreferences sharedPreferences) {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                final String token = sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        });
        return httpClient.build();
    }

    @Provides
    @Singleton
    public RetrofitService getRetrofitService(Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    public ItemRepository provideItemRepository(RetrofitService retrofitService) {
        return new ItemRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    public PostsRepository providePostRepository(RetrofitService retrofitService) {
        return new PostRepositoryImp(retrofitService);
    }
    @Provides
    @Singleton
    public ChatbotRepository provideChatbotRepository(RetrofitService retrofitService) {
        return new ChatbotRepositoryImp(retrofitService);
    }
}
