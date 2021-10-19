package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitService {

    @POST("api/user/login")
    public Call<User> loginUser(@Body User user);
    @PUT("api/user/register")
    public void registerUser(User user);

    @GET("api/user/:id")
    public User getUserById(int id);

    @PUT("api/user/:id")
    public void updateUser(int id);

    @DELETE("api/user/:id")
    public void deleteUser(int id);


    // TODO finish place when the backend finishes
    @GET("api/place/:id")
    public Place getPlaceById(int id);



}
