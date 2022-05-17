package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
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
import com.visitegypt.domain.repository.UserRepository;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
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
    public Single<User> googleLoginUserToken(Token token) {
        return retrofitService.googleLoginUserToken(token);
    }


    @Override
    public Single<User> getUser(String userId) {
        return retrofitService.getUser(userId);
    }

    @Override
    public Single<HashMap<String,String>> follow(String userId) {
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

}
