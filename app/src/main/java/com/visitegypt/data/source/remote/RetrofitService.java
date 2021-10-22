package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlacePageResponse;
import com.visitegypt.domain.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


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


    // TODO finish place when the backend finishes
    @GET("api/place/:id")
    public Place getPlaceById(int id);

    @GET("api/place")
    public Single<PlacePageResponse> getAllPlaces();

    @GET("api/place/{place_id}")
    public Call<Place> getPlace(@Path("place_id") String id);

}
