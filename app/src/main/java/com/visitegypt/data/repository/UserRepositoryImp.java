package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.model.response.UploadResponse;
import com.visitegypt.domain.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

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
    public Single<User> refreshUserToken(User user) { return retrofitService.refreshUserToken(user);}

    @Override
    public Single<User> getUser(String userId, String email) {
        return retrofitService.getUser(userId,email);
    }

    @Override
    public Single<UploadResponse> uploadUserPhoto(String userId, String contentType){
        return retrofitService.uploadUserPhoto(userId, contentType);
    }
}