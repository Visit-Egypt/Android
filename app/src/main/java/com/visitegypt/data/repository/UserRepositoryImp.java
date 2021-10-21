package com.visitegypt.data.repository;

import com.visitegypt.di.NetworkModule;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;

public class UserRepositoryImp implements UserRepository {

    @Override
    public Single<User> registerUser(User user) {
        return NetworkModule.getINSTANCE() .registerUser(user);
    }

    @Override
    public Single<User> loginUser(User user) {

        return NetworkModule.getINSTANCE().loginUser(user);
    }
}