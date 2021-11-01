package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.model.response.PlacePageResponse;

import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


public interface RetrofitService {

    @POST("api/user/login")
    public Single<User> loginUser(@Body User user);
    @POST("api/user/register")
    public Single<User> registerUser(@Body User user);

    @GET("api/user/:id")
    public User getUserById(int id);

    @PUT("api/user/:id")
    public void updateUser(@Body int id);

    @DELETE("api/user/:id")
    public void deleteUser(int id);

    @GET("api/place/{id}")
    public Single<Place> getPlaceById(@Path("id") String id);

    @GET("api/place")
    public Single<PlacePageResponse> getAllPlaces();

    @GET("api/item")
    public Single<ItemPageResponse> getAllItems(@QueryMap Map<String, String> placeId);


}
