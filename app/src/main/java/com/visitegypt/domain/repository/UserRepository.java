package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.ConfirmUploadResponse;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.response.UploadResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Path;

public interface UserRepository {
    /**
     * void getUser();
     * <p>
     * void deleteUser(User user);
     * <p>
     * <p>
     * /
     *
     * @return
     */
    Single<User> registerUser(User user);

    Single<String> forgotPassword(String email);

    Single<User> loginUser(User user);

    Single<User> refreshUserToken(Token token);

    Single<User> getUser(String userId);

    Single<HashMap<String, String>> follow(String userId);

    Single<HashMap<String, String>> unFollow(String userId);

    Single<User> requestTripMate(String userId, TripMateRequest requestMateBody);

    public Single<User> approveTripMateRequest(String requestId);

    public Completable addInterests(HashSet<String> interests);

    public Completable deleteInterests(HashSet<String> interests);
    public Completable updateUserInterests(HashSet<String> newInterest , HashSet<String> removedInterest );
    Single<User> googleLoginUserToken(Token token);

    Single<User> googleRegisterUser(Token token);

    Single<User> getUser(String userId, String email);

    Single<String> logOut(String userId);

    Single<User> updateUser(String userId, UserUpdateRequest user);

    Single<UploadResponse> getPreSignedUrl(String userId, String contentType);

    Call<ConfirmUploadResponse> confirmUpload(ConfirmUploadModel confirmUploadModel);

    Observable<List<BadgeTask>> updateUserBadgeTaskProgress(BadgeTask badgeTask);

    Single<List<Badge>> getUserBadges(String userId);

    Single<List<Badge>> updateUserBadge(String badgeID, Badge badge);

    Single<List<PlaceActivity>> getUserPlaceActivity(String userId);

    Single<List<PlaceActivity>> updateUserPlaceActivity(String activityId, PlaceActivity placeActivity);

}
