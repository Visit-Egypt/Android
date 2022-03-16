package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.Message;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.PostPage;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.model.response.PlacePageResponse;

import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.model.response.UploadedFilesResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


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
    public Single<User> refreshUserToken(@Body Token token);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/user")
    public Single<User> getUser(@Query("user_id") String userId, @Query("user_email") String email);

    @PUT("api/user/{user_id}")
    public Single<User> updateUser(@Path("user_id") String userId, @Body UserUpdateRequest user);

    @POST("api/user/logout/{user_id}")
    public Single<String> logOut(@Path("user_id") String userId);

    @GET("api/user/{id}/upload-photo")
    public Single<UploadResponse> getPreSigendUrl(@Path("id") String id, @Query("content_type") String contentType);
//
//    @Multipart
//    @POST
//    Single<UploadedFilesResponse> uploadToS3(
//            @Url String url,
//            @Part("acl") RequestBody acl,
//            @Part("Content-Type") RequestBody content_type,
//            @Part("key") RequestBody key,
//            @Part("AWSAccessKeyId") RequestBody aws,
//            @Part("policy") RequestBody policy,
//            @Part("signature") RequestBody signature,
//            @Part MultipartBody.Part file);

    /*******************************************************************/
    @GET("api/place/{id}")
    public Single<Place> getPlaceById(@Path("id") String id);

    @GET("api/place")
    public Single<PlacePageResponse> getAllPlaces();

    @GET("api/item")
    public Single<ItemPageResponse> getAllItems(@Query("filters") String queryMap);

    @POST("api/place/review/{place_id}")
    public Single<List<Review>> submitReview(@Path("place_id") String placeId, @Body Review review);

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
    public Single<Post> addNewPost(@Body Post post);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @POST("api/post/like/{post_id}")
    public Single<Void> addLike(@Path("post_id") String postId);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @PUT("api/post/{post_id}")
    public Single<Post> updatePost(@Path("post_id") String postId, Post post);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @DELETE("api/post/{post_id}")
    public Single<Void> deletePost(@Path("post_id") String postId);

    @DELETE("api/post/like/{post_id}")
    public Single<Void> unLike(@Path("post_id") String postId);

    @GET("api/post/user/{user_id}")
    public Single<PostPage> getPostsByUser(@Path("user_id") String userId);


    //Chatbot

    @POST("/api/chatbot")
    public Single<Message> chatbotReceiveRequest(@Body Message message);

}
