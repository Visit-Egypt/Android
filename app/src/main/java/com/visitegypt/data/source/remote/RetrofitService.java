package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitService {

    @POST("api/user/register")
    public void registerUser(User user);

    @PUT("api/user/login")
    public void loginUser(User user);

    @GET("api/user/:userId")
    public Single<User> getUserById(String userId);

    @PUT("api/user/:userId")
    public void updateUser(int userId);

    @DELETE("api/user/:userId")
    public void deleteUser(String userId);


    // TODO finish place when the backend finishes
    @GET("api/place/:id")
    public Single<Place> getPlaceById(String id);

    @GET("api/places")
    public Single<List<Place>> getPlaces();
}
