package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.model.response.PlacePageResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


public interface RetrofitService {
    /******************************************************
     * this Retrofit services for user
     * login
     * register
     * refresh token
     * @param user
     * @return
     */

    @POST("api/user/login")
    public Single<User> loginUser(@Body User user);

    @POST("api/user/register")
    public Single<User> registerUser(@Body User user);

    @POST("api/user/refresh")
    public Single<User> refreshUserToken(@Body User user);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/user")
    public Single<User> getUser(@Query("user_id") String userId, @Query("user_email") String email, @Header("Authorization") String auth);

    @PUT("api/user/:id")
    public void updateUser(@Body int id);

    @DELETE("api/user/:id")
    public void deleteUser(int id);

    /*******************************************************************/
    @GET("api/place/{id}")
    public Single<Place> getPlaceById(@Path("id") String id);

    @GET("api/place")
    public Single<PlacePageResponse> getAllPlaces();

    @GET("api/item")
    public Single<ItemPageResponse> getAllItems(@QueryMap Map<String, String> placeId);

    @POST("api/place/review/{placeId}")
    public Single<Void> submitReview(@Path("placeId") String placeId, @Header("Authorization") String token, @Body Review review);
    /******************************************************
     * this Retrofit services for Posts
     * Get Post
     * Get place posts
     * Post add new post
     * Post add  Like to post
     *
     * @return
     */
    @GET("api/post/place/{post_id}")
    public Single<Post> getPost(@Path("post_id") String postId);
    @GET("api/post/{place_id}")
    public Single<List<Post>> getPlacePosts(@Path("place_id") String placeId);
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("api/post")
    public Single<Post> addNewPost(@Header("Authorization") String token,@Body Post post);
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("api/post/like/{post_id}")
    public Single<Void> addLike(@Header("Authorization") String token,@Path("post_id") String postId);
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("api/post/{post_id}")
    public Single<Post> updatePost(@Header("Authorization") String token,@Path("post_id") String postId,Post post);
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("api/post/{post_id}")
    public Single<Void> deletePost(@Header("Authorization") String token,@Path("post_id") String postId);
    @DELETE("api/post/like/{post_id}")
    public Single<Void> unLike(@Header("Authorization") String token,@Path("post_id") String postId);
    @GET("api/post/user/{user_id}")
    public Single<List<Post>> getPostsByUser(@Path("user_id") String userId);


}
