package com.visitegypt.data.repository;

import com.visitegypt.data.source.remote.RetrofitService;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;

import io.reactivex.rxjava3.core.Single;

public class UserRepositoryImp implements UserRepository {
    private RetrofitService retrofitService;

    @Override
    public Single<User> getUser(String userId) {
        return retrofitService.getUserById(userId);
    }

    @Override
    public void deleteUser(String userId) {
        retrofitService.deleteUser(userId);
    }

    @Override
    public void registerUser(User user) {
        retrofitService.registerUser(user);
    }

    @Override
    public void loginUser(User user) {
        retrofitService.loginUser(user);
    }
}
