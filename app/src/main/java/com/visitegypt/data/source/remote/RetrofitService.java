package com.visitegypt.data.source.remote;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.ConfirmUploadResponse;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.FullExplore;
import com.visitegypt.domain.model.FullPlaceActivity;
import com.visitegypt.domain.model.Message;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Post;
import com.visitegypt.domain.model.PostPage;
import com.visitegypt.domain.model.RecommendationPlaces;
import com.visitegypt.domain.model.Review;
import com.visitegypt.domain.model.Tag;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.WeatherModel;
import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.domain.model.response.BadgeResponse;
import com.visitegypt.domain.model.response.ItemPageResponse;
import com.visitegypt.domain.model.response.PlacePageResponse;
import com.visitegypt.domain.model.response.PostPageResponse;
import com.visitegypt.domain.model.response.UploadResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


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

    @GET("api/user/forgotpassword/{email}")
    public Single<String> forgotPassword(@Path("email") String email);

    @POST("api/user/register")
    public Single<User> registerUser(@Body User user);

    @POST("api/user/register/google")
    public Single<User> googleRegisterUser(@Body Token token);

    @POST("api/user/refresh")
    public Single<User> refreshUserToken(@Body Token token);

    @POST("api/user/login/google")
    public Single<User> googleLoginUserToken(@Body Token token);

    @Headers({"Content-Type: application/json;charset=UTF-8"})
    @GET("api/user")
    public Single<User> getUser(@Query("user_id") String userId);

    @PUT("api/user/{user_id}")
    public Single<User> updateUser(@Path("user_id") String userId, @Body UserUpdateRequest user);

    @POST("api/user/logout/{user_id}")
    public Single<String> logOut(@Path("user_id") String userId);

    @GET("api/user/{id}/upload-photo")
    public Single<UploadResponse> getPreSigendUrl(@Path("id") String id, @Query("content_type") String contentType);

    @POST("api/upload/confirm-upload")
    public Call<ConfirmUploadResponse> confirmUpload(@Body ConfirmUploadModel confirmUploadModel);

    @GET("api/user/{id}/upload-ar")
    public Single<UploadResponse> getUserPhotoAR(@Path("id") String id, @Query("content_type") String contentType);

    //    public Call<ConfirmUploadModel> confirmUpload(@Body ConfirmUploadModel confirmUploadModel);
    //with tasks
    @GET("api/user/badges/{user_id}")
    public Single<List<Badge>> getUserBadges(@Path("user_id") String userId);

    @PUT("api/user/badge/task")
    public Observable<List<BadgeTask>> updateUserBadgeTaskProgress(@Body BadgeTask badgeTask);

    //without Tasks
    @PUT("api/user/badges/{badge_id}")
    public Single<List<Badge>> updateUserBadge(@Path("badge_id") String badgeId, @Body Badge badge);

    @GET("api/user/actvity/{user_id}")
    public Single<List<PlaceActivity>> getUserPlaceActivity(@Path("user_id") String userId);

    @PUT("api/user/actvity/{activity_id}")
    public Single<List<PlaceActivity>> updateUserPlaceActivity(@Path("activity_id") String activityId, @Body PlaceActivity placeActivity);

    //    @GET("api/user/activity/{user_id}")
    @POST("api/user/{user_id}/follow")
    public Single<HashMap<String, String>> follow(@Path("user_id") String userId);

    @POST("api/user/{user_id}/unfollow")
    public Single<HashMap<String, String>> unfollow(@Path("user_id") String userId);

    @POST("api/user/{user_id}/mate")
    public Single<User> requestTripMate(@Path("user_id") String userId, @Body TripMateRequest requestMateBody);

    @POST("api/user/trip-mate-reqs/{req_id}/approve")
    public Single<User> approveTripMateRequest(@Path("req_id") String requestId);

    @POST("api/user/interests")
    public Completable addInterests(@Body HashMap<String, List<String>> interests);

    @POST("api/user/interests/delete")
    public Completable deleteInterests(@Body HashMap<String, List<String>> interests);

    @GET("api/user/recommendation/{user_id}")
    public Single<RecommendationPlaces> getRecommendationPlaces(@Path("user_id") String userId);
    /*******************************************************************/
    @GET("api/place/{id}")
    public Single<Place> getPlaceById(@Path("id") String id);

    @GET("api/place")
    public Single<PlacePageResponse> getAllPlaces();

    @GET("api/place")
    public Single<PlacePageResponse> getPlacesPaging(@Query("page_num") int pageNumber);

    @GET("api/item")
    public Single<ItemPageResponse> getAllItems(@Query("filters") String queryMap, @Query("page_num") int pageNumber, @Query("limit") int limit);

    @POST("api/place/review/{place_id}")
    public Single<List<Review>> submitReview(@Path("place_id") String placeId, @Body Review review);

    @GET("api/place")
    public Single<PlacePageResponse> getPlacesOfCity(@Query("filters") String cityName);

    @GET("api/place/cities/all")
    public Single<List<String>> getAllCities();

    @GET("api/place/cities/all")
    public Single<List<String>> getAllAvailableCities();

    /******************************************************
     * this Retrofit services for Posts
     * Get Post
     * Get place posts
     * Post add new post
     * Post add  Like to post
     * Get User Posts
     * @return
     */

    @GET("api/post")
    public Single<PostPageResponse> getUserPosts(@Query("filters") String queryMap, @Query("page_num") int pageNumber, @Query("limit") int limit);

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

    /************************************************************/
    @GET("api/badge")
    public Single<BadgeResponse> getAllBadges(@Query("limit") int limit);

    @GET("api/badge")
    public Single<BadgeResponse> getBadgesByPlace(@Query("filters") String placeId);

    @GET("/api/place/activityid")
    public Single<List<Place>> getPlacesByPlaceActivitiesId(@Query("id") List<String> placeActivitiesIds);

    /******************************************************************/
    @POST("api/notification/register-device")
    public Single<HashMap<Object, Object>> RegisterDeviceToNotification(@Body HashMap<Object, Object> deviceToken);

    /********************************************************************/
    @GET("api/tag")
    public Single<List<Tag>> getTags();

    @POST("api/tag/users")
    public Single<List<User>> getAllUserTags(@Body HashMap<String, List<String>> tagsId);

    /******* new gamification *********/

    @PUT("api/user/scanobject/{place_Id}/{explore_id}")
    public Single<XPUpdate> updateExploreUserActivity(@Path("place_id") String placeId, @Path("explore_id") String exploreId);

    @PUT("api/user/chatbotartifact/{place_id}")
    public Single<XPUpdate> updateChatBotArtifactUserActivity(@Path("place_id") String placeId);

    @PUT("api/user/chatbotplace/{place_id}")
    public Single<XPUpdate> updateChatBotPlaceUserActivity(@Path("place_id") String placeId);

    @PUT("api/user/visitplace/{place_id}")
    public Single<XPUpdate> updateVisitPlaceUserActivity(@Path("place_id") String placeId);

    @PUT("api/user/reviewplace/{place_id}")
    public Single<XPUpdate> updateUserReviewPlaceActivity(@Path("place_id") String placeId);

    @PUT("api/user/addpost/{place_id}")
    public Single<XPUpdate> updatePostUserActivity(@Path("place_id") String placeId);

    @GET("/api/user/allactvitydetail/{user_id}")
    public Single<List<FullPlaceActivity>> getUserFullPlaceActivitiesDetail(@Path("user_id") String userId,
                                                                            @Query("place_id") String place_id);

    @GET("/api/user/allactvitydetail/{user_id}")
    public Single<List<FullPlaceActivity>> getUserFullActivitiesDetail(@Path("user_id") String userId);

    @GET("/api/user/activitydetail/{user_id}")
    public Single<List<FullPlaceActivity>> getUserFullPlaceActivityDetail(@Path("user_id") String userId);

    @GET("/api/user/exploredetail/{user_id}")
    public Single<List<FullExplore>> getUserFullExploreDetail(@Path("user_id") String userId);

    @GET("/api/user/badgesdetail/{user_id}")
    public Single<List<FullBadge>> getUserFullBadgesDetail(@Path("user_id") String userId);
    /********************************************************************************/
    @GET("data/2.5/weather")
    public Single<WeatherModel> getWeatherByCity(@Query("q") String cityName, @Query("appid") String appId);
    @GET("data/2.5/weather")
    public Single<WeatherModel> getWeatherByLocation(@Query("lat") double lat,@Query("lon") double lon, @Query("appid") String appId);
}
