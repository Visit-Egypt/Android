package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.Badge;
import com.visitegypt.domain.model.BadgeTask;
import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.ConfirmUploadResponse;
import com.visitegypt.domain.model.FullBadge;
import com.visitegypt.domain.model.FullExplore;
import com.visitegypt.domain.model.FullPlaceActivity;
import com.visitegypt.domain.model.PlaceActivity;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.TripMateRequest;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.XPUpdate;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;

public class UserRepositoryImp implements UserRepository {

    private RetrofitService retrofitService;

    @Inject
    public UserRepositoryImp(RetrofitService retrofitService) {
        this.retrofitService = retrofitService;
    }

    @Override
    public Single<User> registerUser(User user) {
        return retrofitService.registerUser(user);
    }

    @Override
    public Single<User> loginUser(User user) {
        return retrofitService.loginUser(user);
    }

    @Override
    public Single<User> refreshUserToken(Token token) {
        return retrofitService.refreshUserToken(token);
    }

    @Override
    public Single<User> googleRegisterUser(Token token) {
        return retrofitService.googleRegisterUser(token);
    }

    @Override
    public Single<User> getUser(String userId, String email) {
        return retrofitService.getUser(userId);
    }

    @Override
    public Single<User> googleLoginUserToken(Token token) {
        return retrofitService.googleLoginUserToken(token);
    }


    @Override
    public Single<User> getUser(String userId) {
        return retrofitService.getUser(userId);
    }

    @Override
    public Single<HashMap<String, String>> follow(String userId) {
        return retrofitService.follow(userId);
    }

    @Override
    public Single<HashMap<String, String>> unFollow(String userId) {
        return retrofitService.unfollow(userId);
    }

    @Override
    public Single<User> requestTripMate(String userId, TripMateRequest requestMateBody) {
        return retrofitService.requestTripMate(userId, requestMateBody);
    }

    @Override
    public Single<User> approveTripMateRequest(String requestId) {
        return retrofitService.approveTripMateRequest(requestId);
    }

    @Override
    public Completable addInterests(HashSet<String> interests) {
        List<String> userInterests = new ArrayList<>();
        userInterests.addAll(interests);
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("pref_list", userInterests);
        return retrofitService.addInterests(map);
    }

    @Override
    public Completable deleteInterests(HashSet<String> interests) {
        List<String> userInterests = new ArrayList<>();
        userInterests.addAll(interests);
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("pref_list", userInterests);
        return retrofitService.deleteInterests(map);
    }

    @Override
    public Single<String> logOut(String userId) {
        return retrofitService.logOut(userId);
    }

    @Override
    public Single<String> forgotPassword(String email) {
        return retrofitService.forgotPassword(email);
    }

    @Override
    public Single<User> updateUser(String userId, UserUpdateRequest user) {
        return retrofitService.updateUser(userId, user);
    }

    @Override
    public Single<UploadResponse> getPreSignedUrl(String userId, String contentType) {
        return retrofitService.getPreSigendUrl(userId, contentType);
    }
    @Override
    public Single<UploadResponse> getUserPhotoAR(String userId, String contentType) {
        return retrofitService.getUserPhotoAR(userId, contentType);
    }

    @Override
    public Call<ConfirmUploadResponse> confirmUpload(ConfirmUploadModel confirmUploadModel) {
        return retrofitService.confirmUpload(confirmUploadModel);
    }

    @Override
    public Observable<List<BadgeTask>> updateUserBadgeTaskProgress(BadgeTask badgeTask) {
        return retrofitService.updateUserBadgeTaskProgress(badgeTask);
    }

    @Override
    public Single<List<Badge>> getUserBadges(String userId) {
        return retrofitService.getUserBadges(userId);
    }

    @Override
    public Single<List<Badge>> updateUserBadge(String badgeId, Badge badge) {
        return retrofitService.updateUserBadge(badgeId, badge);
    }

    @Override
    public Single<List<PlaceActivity>> getUserPlaceActivity(String userId) {
        return retrofitService.getUserPlaceActivity(userId);
    }

    @Override
    public Single<List<PlaceActivity>> updateUserPlaceActivity(String activityId, PlaceActivity placeActivity) {
        return retrofitService.updateUserPlaceActivity(activityId, placeActivity);
    }

    @Override
    public Single<XPUpdate> updateExploreUserActivity(String placeId, String exploreId) {
        return retrofitService.updateExploreUserActivity(placeId, exploreId);
    }

    @Override
    public Single<XPUpdate> updateChatBotArtifactUserActivity(String placeId) {
        return retrofitService.updateChatBotArtifactUserActivity(placeId);
    }

    @Override
    public Single<XPUpdate> updateChatBotPlaceUserActivity(String placeId) {
        return retrofitService.updateChatBotPlaceUserActivity(placeId);
    }

    @Override
    public Single<XPUpdate> updateVisitPlaceUserActivity(String placeId) {
        return retrofitService.updateVisitPlaceUserActivity(placeId);
    }

    @Override
    public Single<XPUpdate> updateUserReviewPlaceActivity(String placeId) {
        return retrofitService.updateUserReviewPlaceActivity(placeId);
    }

    @Override
    public Single<XPUpdate> updatePostUserActivity(String placeId) {
        return retrofitService.updatePostUserActivity(placeId);
    }

    @Override
    public Single<List<FullPlaceActivity>> getUserFullPlaceActivitiesDetail(String userId, String placeId) {
        return retrofitService.getUserFullPlaceActivitiesDetail(userId, placeId);
    }

    @Override
    public Single<List<FullPlaceActivity>> getUserFullPlaceActivityDetail(String userId) {
        return retrofitService.getUserFullPlaceActivityDetail(userId);
    }

    @Override
    public Single<List<FullExplore>> getUserFullExploreDetail(String userId) {
        return retrofitService.getUserFullExploreDetail(userId);
    }

    @Override
    public Single<List<FullBadge>> getUserFullBadgesDetail(String userId) {
        return retrofitService.getUserFullBadgesDetail(userId);
    }

    @Override
    public Single<List<FullPlaceActivity>> getFullActivities(String userId) {
        return retrofitService.getUserFullActivitiesDetail(userId);
    }

    public Completable updateUserInterests(HashSet<String> newInterest, HashSet<String> removedInterest) {
        Completable newInterestRqe = addInterests(newInterest);
        Completable deleteInterestRqe = deleteInterests(removedInterest);
        Completable request = Completable.mergeArray(newInterestRqe, deleteInterestRqe);
        return request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
