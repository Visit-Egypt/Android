package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.User;
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
}