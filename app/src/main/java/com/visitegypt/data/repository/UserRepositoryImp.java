package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.ConfirmUploadModel;
import com.visitegypt.domain.model.Token;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.UserUpdateRequest;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UserRepository;

import javax.inject.Inject;

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
    public Single<User> getUser(String userId, String email) {
        return retrofitService.getUser(userId, email);
    }

    @Override
    public Single<String> logOut(String userId) {
        return retrofitService.logOut(userId);
    }

    @Override
    public Single<User> updateUser(String userId, UserUpdateRequest user) {
        return retrofitService.updateUser(userId, user);
    }

    @Override
    public Single<UploadResponse> getPreSigendUrl(String userId, String contentType) {
        return retrofitService.getPreSigendUrl(userId, contentType);
    }

    @Override
    public Call<String> confirmUpload(ConfirmUploadModel confirmUploadModel) {
        return retrofitService.confirmUpload(confirmUploadModel);
    }


}
