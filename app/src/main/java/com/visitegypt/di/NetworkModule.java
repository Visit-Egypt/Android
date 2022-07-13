package com.visitegypt.di;

import static com.visitegypt.utils.Constants.BASE_URL;
import static com.visitegypt.utils.Constants.S3_URL;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_ACCESS_TOKEN;
import static com.visitegypt.utils.Constants.SHARED_PREF_USER_REFRESH_TOKEN;
import static com.visitegypt.utils.Constants.WEATHER_API;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;


import androidx.annotation.NonNull;

import com.visitegypt.data.repository.BadgesRepositoryImp;
import com.visitegypt.data.repository.ChatbotRepositoryImp;
import com.visitegypt.data.repository.ItemRepositoryImp;
import com.visitegypt.data.repository.NotificationRepositoryImp;
import com.visitegypt.data.repository.PlaceRepositoryImp;
import com.visitegypt.data.repository.PostRepositoryImp;
import com.visitegypt.data.repository.TagRepositoryImp;
import com.visitegypt.data.repository.UploadToS3Imp;
import com.visitegypt.data.repository.UserRepositoryImp;
import com.visitegypt.data.repository.WeatherUtilRepositoryImp;
import com.visitegypt.data.source.local.dao.PlaceDao;
import com.visitegypt.data.source.local.dao.PlacePageResponseDao;
import com.visitegypt.data.source.local.dao.TagDao;
import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.data.source.remote.RetrofitServiceUpload;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.repository.BadgesRepository;
import com.visitegypt.domain.repository.CallBack;
import com.visitegypt.domain.repository.ChatbotRepository;
import com.visitegypt.domain.repository.ItemRepository;
import com.visitegypt.domain.repository.NotificationRepository;
import com.visitegypt.domain.repository.PlaceRepository;
import com.visitegypt.domain.repository.PostsRepository;
import com.visitegypt.domain.repository.TagRepository;
import com.visitegypt.domain.repository.UploadToS3Repository;
import com.visitegypt.domain.repository.UserRepository;
import com.visitegypt.domain.repository.WeatherUtilRepository;
import com.visitegypt.domain.usecase.GetRefreshTokenUseCase;
import com.visitegypt.utils.JWT;
import com.visitegypt.utils.error.NoConnectivityException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import io.reactivex.rxjava3.core.Single;
import okhttp3.Cache;
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
public class NetworkModule implements CallBack {
    private String token;
    private String newToken;
    private boolean flag = true;
    private static final String TAG = "NetworkModule";

    public NetworkModule() {

    }

    @Provides
    @Singleton
    public OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient
                .Builder()
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
    }

    @Provides
    @Singleton
    public HttpLoggingInterceptor provideHttpLoggingInterceptor(OkHttpClient.Builder httpClient) {


        return new HttpLoggingInterceptor();
    }

    @Provides
    @Singleton
    public ConnectivityManager provideConnectivityManager(Application context) {

        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(OkHttpClient.Builder httpClient,
                                            HttpLoggingInterceptor logging,
                                            SharedPreferences sharedPreferences,
                                            ConnectivityManager connectivityManager,
                                            GetRefreshTokenUseCase userRepository) {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);


        httpClient.addInterceptor(new Interceptor() {
            @NonNull
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                token = sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    Response response = chain.proceed(request);
                    response.cacheResponse();
                    if (response.code() == 403 || response.code() == 401) {
                        response.close();
                        getNewToken(userRepository, sharedPreferences);

                        while (flag) ;
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + newToken)
                                .build();
                        flag = true;
                        return chain.proceed(newRequest);

                    }

                    return response;

                } else
                    throw new NoConnectivityException();
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
    @Named("Weather")
    public RetrofitService getRetrofitServiceٌWeater(@Named("Weather") Retrofit retrofit) {
        return retrofit.create(RetrofitService.class);
    }

    @Provides
    @Singleton
    public RetrofitServiceUpload getRetrofitServiceٌUpload(@Named("Upload") Retrofit retrofit) {
        return retrofit.create(RetrofitServiceUpload.class);
    }

    @Provides
    @Singleton
    @Named("Normal")
    public Retrofit provideRetrofit(GsonConverterFactory gsonConverterFactory, RxJava3CallAdapterFactory rxJava3CallAdapterFactory, OkHttpClient client) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava3CallAdapterFactory)
                .client(client)
                .build();
        Log.d("TAG", " Retrofit provideRetrofit: " + retrofit);
        return retrofit;
    }

    @Provides
    @Singleton
    @Named("Upload")
    public Retrofit provideRetrofitUpload(GsonConverterFactory gsonConverterFactory, RxJava3CallAdapterFactory rxJava3CallAdapterFactory) {
        Log.d("TAG", "provideRetrofitUpload:  " + S3_URL);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(S3_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava3CallAdapterFactory)
                .build();
        Log.d("TAG", " Retrofit provideRetrofitUpload: " + retrofit);
        return retrofit;
    }


    @Provides
    @Singleton
    @Named("RefreshToken")
    public Retrofit provideRetrofitRefreshToken(GsonConverterFactory gsonConverterFactory, RxJava3CallAdapterFactory rxJava3CallAdapterFactory) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava3CallAdapterFactory)
                .build();
        Log.d("TAG", " Retrofit provideRetrofitRefreshToken: " + retrofit);
        return retrofit;
    }
    @Provides
    @Singleton
    @Named("Weather")
    public Retrofit provideRetrofitWeather(GsonConverterFactory gsonConverterFactory, RxJava3CallAdapterFactory rxJava3CallAdapterFactory, OkHttpClient client) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_API)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava3CallAdapterFactory)
                .client(client)
                .build();
        Log.d("TAG", " Retrofit provideRetrofitRefreshToken: " + retrofit);
        return retrofit;
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
    public PlaceRepository providePlaceRepository(@Named("Normal") RetrofitService retrofitService, PlacePageResponseDao placeDao,SharedPreferences sharedPreferences) {
        return new PlaceRepositoryImp(retrofitService, sharedPreferences, placeDao);
    }
    @Provides
    @Singleton
    public WeatherUtilRepository provideWeatherUtilRepository(@Named("Weather") RetrofitService retrofitService) {
        return new WeatherUtilRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    public BadgesRepository provideBadgeRepository(@Named("Normal") RetrofitService retrofitService) {
        return new BadgesRepositoryImp(retrofitService);
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
    public UploadToS3Repository provideUploadToS3Repository(RetrofitServiceUpload retrofitServiceUpload) {

        return new UploadToS3Imp(retrofitServiceUpload);
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

    @Provides
    @Singleton
    public GetRefreshTokenUseCase provideGetRefreshToken(@Named("RefreshToken") UserRepository userRepository, SharedPreferences sharedPreferences) {
        return new GetRefreshTokenUseCase(userRepository, sharedPreferences);
    }

    @Provides
    @Singleton
    public NotificationRepository provideNotificationRepository(@Named("Normal") RetrofitService retrofitService) {
        return new NotificationRepositoryImp(retrofitService);
    }

    @Provides
    @Singleton
    public TagRepository provideTagRepository(@Named("Normal") RetrofitService retrofitService, TagDao tagDao) {
        return new TagRepositoryImp(retrofitService, tagDao);
    }


    private void getNewToken(GetRefreshTokenUseCase getRefreshTokenUseCase, SharedPreferences sharedPreferences) throws IOException {
        Log.d("TAG", "Threads: " + Thread.currentThread());
        token = sharedPreferences.getString(SHARED_PREF_USER_ACCESS_TOKEN, "");
        String refreshToken = sharedPreferences.getString(SHARED_PREF_USER_REFRESH_TOKEN, "");
        getRefreshTokenUseCase.setToken(new Token(token, refreshToken));
        getRefreshTokenUseCase.setCallBack(this::callBack);
        getRefreshTokenUseCase.getNewToken();

    }


    @Override
    public void callBack(String token) {
        Log.d("TAG", "callBack: " + token);
        newToken = token;
        flag = false;
    }
}