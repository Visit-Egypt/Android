package com.visitegypt.domain.repository;

import com.visitegypt.domain.model.User;

import io.reactivex.rxjava3.core.Single;

public interface UserRepository {
    Single<User> getUser(String userId);

    void deleteUser(String userId);

    void registerUser(User user);

    void loginUser(User user);
}
