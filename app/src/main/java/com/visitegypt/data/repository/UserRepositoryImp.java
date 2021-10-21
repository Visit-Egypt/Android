package com.visitegypt.data.repository;

import com.visitegypt.di.NetworkModule;
import com.visitegypt.domain.model.User;
import com.visitegypt.domain.repository.UserRepository;

import retrofit2.Call;

public class UserRepositoryImp implements UserRepository {
    @Override
    public Call<User> loginUser(User user) {
        return NetworkModule.getINSTANCE().loginUser(user);
    }
}
